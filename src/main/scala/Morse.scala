import java.io.*
import javax.sound.sampled.*

@main def decode(fileName: String): Unit =
  val codes = ("A._,B_...,C_._.,D_..,E.,F.._.,G__.,H....,I..,J.___,K_._,L._..,M__,N_.,O___,P.__.,Q__._,R._.,S...,T_," +
    "U.._,V..._,W.__,X_.._,Y_.__,Z__..,1.____,2..___,3...__,4...._,5.....,6_....,7__...,8___..,9____.,0_____," +
    ".._._._,  ").split(",").map(c => c.substring(1) -> c.head).toMap
  val inputStream = BufferedInputStream(AudioSystem.getAudioInputStream(File(fileName)))
  val buf = scala.collection.mutable.Buffer[Double]()
  var bytes = inputStream.readNBytes(2)
  while bytes.length == 2 do
    buf += (((bytes(0) & 0xFF) + (bytes(1) << 8)) / 32768d).abs
    bytes = inputStream.readNBytes(2)
  val sounds = (Seq(0d) ++ buf.grouped(64).map(it => it.sum / it.size) ++ Seq(0d)).sliding(2).zipWithIndex.flatMap {
    case (Seq(a, b), index) => if ((a < 0.05) != (b < 0.05)) Some(index) else None
  }.toSeq.grouped(2).map(i => Sound(i.head, i(1))).toSeq
  val sGrouper = Grouper(sounds.map(_.length).sorted, 2)
  val pGrouper = Grouper(sounds.sliding(2).flatMap { case Seq(a, b) => Some(b.start - a.end) }.toSeq.sorted, 3)
  println((sounds.sliding(2).flatMap { case Seq(a, b) =>
    (if (sGrouper.group(a.length) == 0) "." else "_") + Map(0->"", 1->",", 2->", ,")(pGrouper.group(b.start - a.end))
  } ++ (if (sGrouper.group(sounds.last.length) == 0) "." else "_")).mkString.split(",").map(codes).mkString)

case class Sound(start: Long, end: Long) { val length: Long = end - start }

class Grouper(values: Seq[Long], count: Int) {
  private val thresholds = values.zip(values.sliding(2).flatMap { case Seq(a, b) => Some(b - a) })
    .sortBy(-_._2).take(count - 1).map(p => p._1 + p._2 / 2)
  def group(value: Long): Int = thresholds.count(_ < value)
}
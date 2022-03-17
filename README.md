# wav-morse-decoder

Tongue-in-cheek submission to the [Wundernut 11](https://github.com/wunderdogsw/wundernut-vol11) coding challenge.

Takes a wav file (16bit, mono) with a morse code message and decodes it.

## Features
- thirty lines od Scala 3 code
- uses no external libraries
- demonstrates code quality metrics by doing the exact opposite: no docs, inefficient, unclear, unreadable, unmaintainable, non-extensible
- kind of works

## Usage
- install [sbt](https://www.scala-sbt.org/)
- clone repo with: `git clone https://github.com/zstojanovic/wav-morse-decoder.git`
- run with: `sbt "run message.wav"`

## Addendum
It turns out it was silly of me to assume the message would start (or end) with silence, so my original submission fails to decode verification message. I've added a fix for that.

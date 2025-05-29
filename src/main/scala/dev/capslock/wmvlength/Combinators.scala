package dev.capslock.wmvlength

import scodec.codecs.uint32L

object Combinators {
  val uint64LDecoder = for
    l <- uint32L // due to little endian, lower bytes are read fast
    h <- uint32L
  yield (BigInt(h) << 32) | BigInt(l)
}

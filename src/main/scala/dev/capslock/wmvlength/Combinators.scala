package dev.capslock.wmvlength

import scodec.Decoder
import scodec.codecs.uint32L

object Combinators {
  val uint64LDecoder: Decoder[BigInt] = for
    l <- uint32L // due to little endian, lower bytes are read first
    h <- uint32L
  yield (BigInt(h) << 32) | BigInt(l)
}

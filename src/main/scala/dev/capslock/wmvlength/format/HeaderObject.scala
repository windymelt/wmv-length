package dev.capslock.wmvlength
package format

import scodec.bits.{ByteVector, hex}
import scodec.codecs.*
import scodec.*
import Combinators.uint64LDecoder

object HeaderObject {
  case class Header(
      headerSize: BigInt,
      headerCount: Long,
  )
  object Header {
    val headerObjectHeaderDecoder =
      for
        size  <- uint64LDecoder
        count <- uint32L
        _     <- constant(hex"01") // reserved
        _     <- constant(hex"02") // reserved
      yield Header(size, count)
  }
}

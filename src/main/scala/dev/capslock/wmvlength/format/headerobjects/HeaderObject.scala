package dev.capslock.wmvlength
package format.headerobjects

import scodec.bits.{ByteVector, hex}
import scodec.codecs.*
import scodec.*
import Combinators.uint64LDecoder
import scala.concurrent.duration.FiniteDuration

object HeaderObject {
  // Header Object consists of header and value fields

  // Utility to construct Little Endian binary from hex format
  extension (bv: ByteVector) {
    private def toLE: ByteVector = bv.reverse
  }

  private val fileInfoPrembleGuid =
    (hex"8CABDCA1".toLE, hex"A947".toLE, hex"11CF".toLE, hex"8EE400C00C205365")

  private val fileInfoPreamble =
    constant(fileInfoPrembleGuid._1)
      ~> constant(fileInfoPrembleGuid._2)
      ~> constant(fileInfoPrembleGuid._3)
      ~> constant(fileInfoPrembleGuid._4)

  private val filePropertiesDecoder =
    for
      _     <- scodec.codecs.bytes(48) // discard 48 bytes
      nanos <- uint64LDecoder          // duration is 100-nanosecs in 64 bit int
      _     <- uint64LDecoder          // drop send duration
      preroll <-
        uint64LDecoder // preroll (millisec) is for buffering time. if we have preroll, duration has positive offset.
      millisecs = BigDecimal(nanos) * BigDecimal(10).pow(-4) - BigDecimal(
        preroll,
      )
      seconds = millisecs / 1000
    yield HeaderObject.FileProperties(FiniteDuration(seconds.toLong, "seconds"))

  val headerObjectDecoder =
    discriminatorFallback[HeaderObject, HeaderObject](
      Codec(
        Encoder(_ => ???),
        bv =>
          // retrieve size
          uint64LDecoder
            .decode(bv)
            .flatMap(entireSize =>
              val guidSize = 16
              Attempt.successful(
                DecodeResult(
                  HeaderObject.Otherwise,
                  bv.drop((entireSize.value.toLong - guidSize) * 8),
                ),
              ),
            ),
      ),
      discriminated[HeaderObject]
        .by(fileInfoPreamble)
        .typecase(
          (),
          Codec(
            Encoder((fp: HeaderObject) => ???),
            filePropertiesDecoder,
          ),
        ),
    ).map(_.fold(identity, identity))
}

enum HeaderObject {
  case FileProperties(playDuration: FiniteDuration)
  case Otherwise
}

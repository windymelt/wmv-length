package dev.capslock.wmvlength.format

import scodec.codecs.*
import scodec.bits.{ByteVector, hex}

object GlobalHeader {
  // Utility to construct Little Endian binary from hex format
  extension (bv: ByteVector) {
    private def toLE: ByteVector = bv.reverse
  }

  // Preamble is guid. CAVEAT! In windows, guid is LE-LE-LE-BE.
  private val globalPreambleGuid =
    (hex"75B22630".toLE, hex"668E".toLE, hex"11CF".toLE, hex"A6D900AA0062CE6C")

  val globalPreamble =
    constant(globalPreambleGuid._1)
      ~> constant(globalPreambleGuid._2)
      ~> constant(globalPreambleGuid._3)
      ~> constant(globalPreambleGuid._4)
}

package dev.capslock.wmvlength

import scodec.*
import format.GlobalHeader.globalPreamble
import format.HeaderObject.Header.headerObjectHeaderDecoder
import format.headerobjects.HeaderObject
import format.headerobjects.HeaderObject.headerObjectDecoder

object Wmv {

  val wmvDecoder: Decoder[HeaderObject] = for
    _  <- globalPreamble.withContext("preamble (whether WMV or not)")
    _  <- headerObjectHeaderDecoder
    ho <- headerObjectDecoder
  yield ho // we don't need other infos.
}

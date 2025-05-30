package dev.capslock.wmvlength

import dev.capslock.wmvlength.format.headerobjects.HeaderObject
import scodec.bits.BitVector
import scodec.Attempt
import io.circe.*

object Main {
  def main(args: Array[String]): Unit =
    if !args.isDefinedAt(0) then
      throw new Exception("Please specify wmv file as argument")

    val filePath = args(0)
    val fileBodyIS = os.read.inputStream(
      os.pwd / os.SubPath(filePath),
    )

    val bitVector  = BitVector.fromInputStream(fileBodyIS)
    val decodedAsf = Wmv.wmvDecoder.decode(bitVector)

    val resultJson = decodedAsf match
      case Attempt.Failure(cause) =>
        JsonObject(
          "status"  -> Json.fromString("err"),
          "message" -> Json.fromString(cause.messageWithContext),
        )
      case Attempt.Successful(value) =>
        JsonObject(
          "status" -> Json.fromString("ok"),
          "durationSec" -> Json.fromLong(
            value.value
              .asInstanceOf[HeaderObject.FileProperties]
              .playDuration
              .toSeconds,
          ),
        )

    println(resultJson.toJson.noSpaces)
}

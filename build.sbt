scalaVersion := "3.3.3" // A Long Term Support version.

enablePlugins(ScalaNativePlugin)

// set to Debug for compilation details (Info is default)
logLevel := Level.Info

// import to add Scala Native options
import scala.scalanative.build._

// defaults set with common options shown
nativeConfig ~= { c =>
  c.withLTO(LTO.none)     // thin
    .withMode(Mode.debug) // releaseFast
    .withGC(GC.immix)     // commix
}

libraryDependencies ++= Seq(
  "com.lihaoyi" %%% "os-lib"      % "0.11.4",
  "org.scodec"  %%% "scodec-core" % "2.3.2",
  "org.scodec"  %%% "scodec-bits" % "1.2.1",
  "io.circe"    %%% "circe-core"  % "0.14.13",
)

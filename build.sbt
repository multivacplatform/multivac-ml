import sbtassembly.MergeStrategy
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging

enablePlugins(JavaServerAppPackaging)
enablePlugins(JavaAppPackaging)

val sparkVer = "2.3.1"
val corenlpVer = "3.9.2"
val hadoopVer = "2.7.2"
val scalaTestVer = "3.0.0"
val sparknlpVer = "1.7.2"

lazy val commonSettings = Seq(
  name := "multivac-ml",
  organization := "multivacplatform.org",
  version := "1.0.0",
  scalaVersion := "2.11.12",
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  licenses := Seq("AGPL-3.0" -> url("https://opensource.org/licenses/AGPL-3.0"))
)

lazy val analyticsDependencies = Seq(
  "org.apache.spark" %% "spark-core" % sparkVer % "provided" withSources(),
  "org.apache.spark" %% "spark-mllib" %sparkVer
)

lazy val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % scalaTestVer % "test"
)

lazy val utilDependencies = Seq(
  "com.typesafe" % "config" % "1.3.1",
  "com.johnsnowlabs.nlp" %% "spark-nlp" % "1.7.3"
)

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++=
      analyticsDependencies ++
        testDependencies ++
        utilDependencies
  )

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

assemblyExcludedJars in assembly := {
  val cp = (fullClasspath in assembly).value
  cp filter {
    j => {
      j.data.getName.startsWith("spark-core") ||
        j.data.getName.startsWith("spark-sql") ||
        j.data.getName.startsWith("spark-hive") ||
        j.data.getName.startsWith("spark-mllib") ||
        j.data.getName.startsWith("spark-graphx") ||
        j.data.getName.startsWith("spark-yarn") ||
        j.data.getName.startsWith("spark-streaming") ||
        j.data.getName.startsWith("hadoop") ||
        j.data.getName.startsWith("hadoop-client")
    }
  }
}
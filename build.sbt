name := "multivac-ml"

import sbtassembly.MergeStrategy
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging

enablePlugins(JavaServerAppPackaging)
enablePlugins(JavaAppPackaging)

val sparkVer = "2.4.0"
val sparknlpVer = "1.8.0"
val corenlpVer = "3.9.2"
val hadoopVer = "2.7.2"
val scalaTestVer = "3.0.0"
val typeSafeVer = "1.3.1"

// PROJECTS
lazy val root = (project in file("."))
  .settings(commonSettings)
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    demo,
    ml
  )

lazy val demo = project
  .settings(name := "demo")
  .settings(
    commonSettings,
    assemblySettings,
    libraryDependencies ++=
      sparkDependencies_demo ++
        testDependencies ++
        utilDependencies
  )
  .dependsOn(ml)

lazy val ml = project
  .settings(name := "ml")
  .settings(
    commonSettings,
    assemblySettings,
    libraryDependencies ++=
      sparkDependencies_ml ++
        testDependencies ++
        utilDependencies
  )
// DEPENDENCIES
lazy val sparkDependencies_demo = Seq(
  "org.apache.spark" %% "spark-core" % sparkVer % "provided" withSources(),
  "org.apache.spark" %% "spark-mllib" %sparkVer % "provided" withSources()
)

lazy val sparkDependencies_ml = Seq(
  "org.apache.spark" %% "spark-core" % sparkVer,
  "org.apache.spark" %% "spark-mllib" %sparkVer
)

lazy val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % scalaTestVer % "test"
)

lazy val utilDependencies = Seq(
  "com.typesafe" % "config" % typeSafeVer,
  "com.johnsnowlabs.nlp" %% "spark-nlp" % sparknlpVer
)

// SETTINGS
lazy val commonSettings = Seq(
  organization := "multivacplatform.org",
  version := "1.0.0",
  scalaVersion := "2.11.12",
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))
)

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := "multivac-" + name.value + ".jar",
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
  },
  assemblyMergeStrategy in assembly := {
    case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
    case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
    case PathList("org", "apache", xs @ _*) => MergeStrategy.last
    case PathList("com", "google", xs @ _*) => MergeStrategy.last
    case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
    case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
    case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
    case "about.html" => MergeStrategy.rename
    case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
    case "META-INF/mailcap" => MergeStrategy.last
    case "META-INF/mimetypes.default" => MergeStrategy.last
    case "plugin.properties" => MergeStrategy.last
    case "log4j.properties" => MergeStrategy.last
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)

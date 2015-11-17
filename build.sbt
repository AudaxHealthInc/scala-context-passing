
lazy val commonRootSettings = Seq(
  organization := "com.rallyhealth",
  organizationName := "Rally Health",
  scalaVersion := "2.11.7",

  // disable compilation of ScalaDocs, since this always breaks on links and isn't as helpful as source
  sources in(Compile, doc) := Seq.empty,
  // disable publishing empty ScalaDocs
  publishArtifact in (Compile, packageDoc) := false,
  licenses += ("Apache-2.0", url("http://opensource.org/licenses/apache-2.0"))
)

lazy val root = (project in file("."))
  .settings(commonRootSettings)
  .settings(
    name := "context",
    publish := {},
    publishLocal := {}
  )
  .aggregate(core, server, client, example)

lazy val commonProjectSettings = commonRootSettings ++ Seq(
  scalacOptions ++= Seq(
    "-encoding", "UTF-8",
    "-deprecation:false",
    "-feature",
    "-optimize",
    "-unchecked",
    "-Xfatal-warnings",
    "-Ywarn-dead-code"
  )
)

lazy val core = (project in file("core"))
  .settings(commonProjectSettings)
  .settings(
    name := "context-core",
    libraryDependencies ++= Seq(
      "com.rallyhealth" %% "scalacheck-ops" % Versions.scalacheckOps % "test",
      "com.typesafe.play" %% "play-functional" % Versions.play,  // switch to Cats when stable
      "com.typesafe.play" %% "play-ws" % Versions.play,
      "org.scalactic" %% "scalactic" % Versions.scalactic,
      "org.scalacheck" %% "scalacheck" % Versions.scalacheck % "test",
      "org.scalatest" %% "scalatest" % Versions.scalatest % "test"
    )
  )

lazy val client = (project in file("client"))
  .settings(commonProjectSettings)
  .settings(
    name := "context-play-ws",
    libraryDependencies ++= Seq(
      "com.rallyhealth" %% "scalacheck-ops" % Versions.scalacheckOps % "test",
      "com.typesafe.play" %% "play-functional" % Versions.play,  // switch to Cats when stable
      "com.typesafe.play" %% "play-ws" % Versions.play,
      "org.scalactic" %% "scalactic" % Versions.scalactic,
      "org.scalacheck" %% "scalacheck" % Versions.scalacheck % "test",
      "org.mockito" % "mockito-core" % Versions.mockito % "test",
      "org.scalatest" %% "scalatest" % Versions.scalatest % "test"
    )
  ).dependsOn(core)

lazy val server = (project in file("server"))
  .settings(commonProjectSettings)
  .settings(
    name := "context-play-server",
    libraryDependencies ++= Seq(
      "com.rallyhealth" %% "scalacheck-ops" % Versions.scalacheckOps % "test",
      "com.typesafe.play" %% "play" % Versions.play,
      "com.typesafe.play" %% "play-functional" % Versions.play,  // switch to Cats when stable
      "org.scalactic" %% "scalactic" % Versions.scalactic,
      "org.scalacheck" %% "scalacheck" % Versions.scalacheck % "test",
      "org.scalatest" %% "scalatest" % Versions.scalatest % "test"
    )
  ).dependsOn(core)

lazy val example = (project in file("example"))
  .settings(commonProjectSettings)
  .settings(
    name := "example",
    version := "0.0.1",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-functional" % Versions.play,  // switch to Cats when stable
      "com.typesafe.play" %% "play-ws" % Versions.play,
      "org.scalactic" %% "scalactic" % Versions.scalactic
    ),
    // Don't publish this source code example
    publish := {},
    publishLocal := {}
  )
  .enablePlugins(PlayScala)
  .dependsOn(core, client, server)


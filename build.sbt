ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.navneetgupta"
ThisBuild / organizationName := "navneetgupta"
ThisBuild / name             := "expense_manager"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps",
  "-Xfatal-warnings",
  "-Ypartial-unification",
  "-language:higherKinds",
  "-language:implicitConversions"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0-M4")
addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M11" cross CrossVersion.full)

libraryDependencies ++= {
  val catVersion = "1.5.0"
  val doobieVersion = "0.6.0"
  val http4sVersion = "0.19.0"
  Seq(
    "org.scalactic"   %% "scalactic"           % "3.0.5",
    "org.typelevel"   %% "cats-core"           % catVersion,
    "org.tpolecat"    %% "doobie-core"         % doobieVersion,
    "org.tpolecat"    %% "doobie-hikari"       % doobieVersion,
    "org.tpolecat"    %% "doobie-postgres"     % "0.6.0",
    "org.tpolecat"    %% "doobie-specs2"       % doobieVersion % "test", // Specs2 support for typechecking statements.
    "org.tpolecat"    %% "doobie-scalatest"    % doobieVersion % "test",  // ScalaTest support for typechecking statements
  )
}

val circeVersion = "0.10.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-literal"
).map(_ % circeVersion)

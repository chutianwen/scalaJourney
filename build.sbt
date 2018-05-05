import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import sbt.Credentials
import sbt.Keys.{credentials, fork, licenses, publishArtifact, publishTo}

import scalariform.formatter.preferences.DoubleIndentConstructorArguments

//enablePlugins(JavaAppPackaging)

lazy val commonSettings = Seq(
	organization := "com.fractal",
	scalaVersion := "2.11.8",
	resolvers ++= Seq(
		"snapshots"     at "https://oss.sonatype.org/content/repositories/snapshots",
		"staging"       at "https://oss.sonatype.org/content/repositories/staging",
		"releases"      at "https://oss.sonatype.org/content/repositories/releases",
		DefaultMavenRepository
	),
	ScalariformKeys.preferences := ScalariformKeys.preferences.value.setPreference(DoubleIndentConstructorArguments, true)
)

lazy val SparkSandBox = (project in file("."))
	.settings(
		commonSettings
	)
	.settings(
		name := "Research Study -- SparkSandBox",
		libraryDependencies ++= {
			val sparkVersion = sys.props.getOrElse("spark Backend", default = "2.2.0")
			Seq(
				"com.typesafe.akka" %% "akka-actor" % "2.5.7",
				"com.amazonaws" % "aws-java-sdk" % "1.11.46",
				"org.apache.httpcomponents" % "httpclient" % "4.5.4",
				"org.apache.spark" %% "spark-core" % sparkVersion,
				"org.apache.spark" %% "spark-sql" % sparkVersion,
				"com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
				"com.typesafe" % "config" % "1.3.2",
				"org.scalafx" % "scalafx_2.11" % "8.0.102-R11",
				"org.apache.spark" % "spark-mllib_2.11" % "2.2.0",
				"org.apache.spark" % "spark-graphx_2.11" % "2.2.0",
				"org.mongodb.spark" %% "mongo-spark-connector" % "2.2.0",
				"org.typelevel" %% "cats" % "0.7.2"
			)
		}
	)
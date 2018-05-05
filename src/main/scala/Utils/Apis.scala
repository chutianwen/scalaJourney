package Utils

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths, StandardCopyOption}
import java.security.MessageDigest

import org.apache.commons.io.FileUtils
import org.apache.spark.sql.{DataFrame, SaveMode}

import scala.util.Try

/**
  * Common APIs.
  *
  */
object Apis {

  /**
    * Get memory usage of running a Spark program.
    *
    */
  def getMemUsage {
    //"java -XX:+PrintFlagsFinal -version | grep -iE 'HeapSize|PermSize|ThreadStackSize'" !
    // memory info
    val mb = 1024 * 1024
    val runtime = Runtime.getRuntime
    println("** Used Memory:  " + (runtime.totalMemory - runtime.freeMemory) / mb + " MB")
    println("** Free Memory:  " + runtime.freeMemory / mb + " MB")
    println("** Total Memory: " + runtime.totalMemory / mb + " MB")
    println("** Max Memory:   " + runtime.maxMemory / mb + " MB")
  }


  /**
    * Get time usage of running a Spark program.
    *
    */
  def getTimeUsage(startTime: Long) {
    val endTime = System.nanoTime()
    println("** Elapsed time: " + (endTime - startTime) / 1e9 + " s")
  }

  /**
    * Save DataFrame to the output file.
    *
    */
  def saveDataFrame(df: DataFrame, outFile: String, format: String = "csv", header: Boolean = false) {
    var formatString = "com.databricks.spark.csv"
    var delimiter = ","

    format match {
      case "tsv" => delimiter = "\t"
      case "json" => formatString = "org.apache.spark.sql.json"
      case "parquet" => formatString = "org.apache.spark.sql.parquet"
      case "txt" => formatString = "text"
      case "jdbc" => formatString = "jdbc"
      case "orc" => formatString = "orc"
      case _ =>
    }
    df.repartition(1).write.mode(SaveMode.Overwrite).format(formatString).option("header", header)
      .option("delimiter", delimiter).save(outFile + ".temp")

    val sparkOutFile = Files.list(Paths.get(outFile + ".temp")).toArray
      .map(x => x.toString).filter(x => x.endsWith(format)).head
    Files.move(Paths.get(sparkOutFile), Paths.get(outFile), StandardCopyOption.REPLACE_EXISTING)
    FileUtils.deleteDirectory(new File(outFile + ".temp"))
  }


  /**
    *
    * @param ip to generate hash
    * @return Hash
    */

  def md5Hash(ip: String) = {
    MessageDigest.getInstance("MD5").digest(ip.getBytes).map(0xFF & _).map {
      "%02x".format(_)
    }.mkString
  }

  /**
    * Save text data to file.
    *
    */
  def writeFile(filePath: String, content: String): Unit = {
    Try {
      Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8))
    } getOrElse()
  }
}

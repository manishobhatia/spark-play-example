package models

import scalaz._
import Scalaz._
import au.com.bytecode.opencsv.CSVReader
import java.io.FileReader
import scala.collection.JavaConverters._
import org.apache.spark.rdd.RDD
import java.io.StringReader

case class ReturnsAttribute(firmId: String, returnId: String, year: Option[Int], name: String,
  efiled: Option[Boolean], clientName: String, clientId: String, statusId: String, paid: Option[Boolean],
  returnType: String, agency: String, efileItems: String)

object ReturnsAttribute {

  var all: RDD[ReturnsAttribute] = Connection.sc.emptyRDD
  
  def serialize(arr: Array[String]): ReturnsAttribute = {
    ReturnsAttribute(arr(0), arr(1), arr(2).parseInt.toOption, arr(3),
      arr(4).parseBoolean.toOption, arr(5), arr(6), arr(7), arr(8).parseBoolean.toOption,
      arr(9), arr(10), arr(11))
  }
  
   def loadFromSpark(filePath: String, header: Boolean): Unit = {
    val input = Connection.sc.textFile(filePath)
    val result = input.map { line =>
      val reader = new CSVReader(new StringReader(line))
      reader.readNext
    }
    all = result.filter { x => header && (x(0) != "id_firm") }.map(arr => serialize(arr)).persist()

  }

  def findPaidByYear(year: Int): RDD[ReturnsAttribute] = {
    all.filter(_.year == Some(year)).filter(_.paid == Some(true))
  }

  def findEfiledByYear(year: Int): RDD[ReturnsAttribute] = {
    all.filter(_.year == Some(year)).filter(_.efiled == Some(true))
  }

  def findByFirmId(firmId: String): RDD[ReturnsAttribute] = {
    all.filter(_.firmId == firmId)
  }

  def findByFirmIdAndReturnId(firmId: String, returnId: String): Option[ReturnsAttribute] = {
    findByFirmId(firmId).collect.find { ret => ret.returnId == returnId }
  }
}
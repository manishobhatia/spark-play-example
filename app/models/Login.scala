package models

import scalaz._
import Scalaz._
import au.com.bytecode.opencsv.CSVReader
import java.io.FileReader
import scala.collection.JavaConverters._
import org.apache.spark.rdd.RDD
import java.io.StringReader

case class Login(realmId: String, firmId: String)

object Login {
  var all: RDD[Login] = Connection.sc.emptyRDD

  def serialize(arr: Array[String]): Login = {
    Login(arr(0), arr(1))
  }

  def loadFromSpark(filePath: String, header: Boolean): Unit = {
    val sc = Connection.sc
    val input = sc.textFile(filePath)
    val result = input.map { line =>
      val reader = new CSVReader(new StringReader(line))
      reader.readNext
    }
    all = result.filter { x => header && (x(0) != "realmid") }.map(arr => Login.serialize(arr)).persist()

  }

  def findByRealmId(realmId: String): Option[Login] = {
    all.collect().find(_.realmId == realmId)
  }
}
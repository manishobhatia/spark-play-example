package models

import scalaz._
import Scalaz._
import au.com.bytecode.opencsv.CSVReader
import java.io.FileReader
import scala.collection.JavaConverters._

case class BankProduct(firmId: String, returnId: String)

object BankProduct {
  var data: Set[BankProduct] = Set()

  def serialize(arr: Array[String]): BankProduct = {
    BankProduct(arr(0), arr(1))
  }

  def loadFromFile(filePath: String, header: Boolean): Unit = {
    val reader = new CSVReader(new FileReader(filePath))
    if (header) reader.readNext();
    //   var count = 0
    data = reader.readAll().asScala.map { arr =>
      //      count += 1
      //      println(count)
      serialize(arr)
    }.toSet
    reader.close
  }
}
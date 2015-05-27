package models

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object Connection{
    val conf = new SparkConf().setMaster("local").setAppName("My App")
    val sc = new SparkContext(conf)

//  println("hello world")
//  val conf = new SparkConf().setMaster("local").setAppName("My App")
//  val sc = new SparkContext(conf)
//  val input = sc.textFile("/Users/mbhatia/Desktop/prod_data/login.csv")
//  println(input.count())
  //connect
}
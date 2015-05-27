package models

case class ReturnCounts(year: Int, paid: Long, efiled: Long)

object ReturnCounts {
  var counts_14 = count(2014)
  var counts_13 = count(2013)

  def reload = {
    counts_14 = count(2014)
    counts_13 = count(2013)
  }
  
  def count(year: Int): ReturnCounts = {
    ReturnCounts(year, ReturnsAttribute.findPaidByYear(year).count, ReturnsAttribute.findEfiledByYear(year).count)
  }

  def list: Seq[ReturnCounts] = {
    List(counts_14, counts_13)
  }

}
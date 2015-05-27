package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.libs.json.Writes
import models.ReturnCounts
import play.api.libs.json.Json

object Returns extends Controller {

  implicit val writesReturnCounts = Writes[ReturnCounts] {
    case ReturnCounts(year, paid, efiled) => Json.obj(
      "year" -> year,
      "paid" -> paid,
      "efiled" -> efiled)
  }

  def countForAllYears = Action {
    Ok(Json.toJson(ReturnCounts.list))
  }

}
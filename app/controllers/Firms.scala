package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import models.Login
import models.ReturnsAttribute
import play.api.libs.json.Writes
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

object Firms extends Controller {

  implicit val writesReturnsAttribute = Writes[ReturnsAttribute] {
    case ReturnsAttribute(firmId, returnId, year, name, efiled, clientName, clientId, statusId,
      paid, returnType, agency, efileItems) => Json.obj(
      "firmId" -> firmId,
      "returnId" -> returnId,
      "year" -> year,
      "name" -> name,
      "efiled" -> efiled,
      "clientName" -> clientName,
      "clientId" -> clientId,
      "statusId" -> statusId,
      "paid" -> paid,
      "returnType" -> returnType,
      "agency" -> agency,
      "efileItems" -> efileItems)
  }

  def returnsList(realmId: String) = Action {

    Login.findByRealmId(realmId) map { l =>
      Ok(Json.toJson(ReturnsAttribute.findByFirmId(l.firmId).collect))
    } getOrElse { NotFound }
  }

  def returnByFirmAndId(realmId: String, returnId: String) = Action.async { implicit request =>

    Login.findByRealmId(realmId) map { login =>
      val ws = WS.client(play.api.Play.current)
      ws.url("https://ito-ws.intuit.com/datasvc/v1/returns/" + returnId)
        .withHeaders(("Accept" -> "application/json"),
          ("Content-Type" -> "application/json"),
          ("X-Intuit-Itoservice" -> "bank-product-service"),
          ("Cookie" -> ("domain=ito-ws.intuit.com; path=/; ito.firmUuid=" + login.firmId )))
        .get().map { response => Ok(Json.parse(response.body)) }

    } getOrElse { Future.successful(NotFound) }
  }

}
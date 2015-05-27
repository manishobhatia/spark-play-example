import play.api.GlobalSettings
import play.api.Application
import akka.actor.{Actor, Props}
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import models.ReturnsAttribute
import play.api.Logger
import models.ReturnCounts
import models.Login

object Global extends GlobalSettings {
  
  override def onStart(application: Application) {
    
    import scala.concurrent.duration._
    import play.api.Play.current
    
    val actor = Akka.system.actorOf(Props(new LoadDataActor ))
    
    actor ! "ReturnsAttribute"
    actor ! "Login"
    Akka.system.scheduler.schedule(Duration.Zero, Duration(10, MINUTES), actor, "ReturnsAttribute")
    Akka.system.scheduler.schedule(Duration.Zero, Duration(10, MINUTES), actor, "Login")
  }
}

class LoadDataActor extends Actor {
  
  def receive = {
    case "ReturnsAttribute" => {
      Logger.info("Loading Returns Attribute")
      ReturnsAttribute.loadFromSpark("/Users/mbhatia/Desktop/prod_data/returns_attribute.csv", true)
      ReturnCounts.reload
    }
    case "Login" => {
      Logger.info("Loading Login")
      Login.loadFromSpark("/Users/mbhatia/Desktop/prod_data/login.csv", true);
    }
    case _ => Logger.warn("unsupported message type")
  }
}
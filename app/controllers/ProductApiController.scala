package controllers

import javax.inject._

import akka.actor.ActorSystem
import model.ui.ProductUI
import play.api.libs.json.Json
import play.api.mvc._
import services.ProductService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductApiController @Inject()(productService: ProductService,
                                     cc: ControllerComponents,
                                     actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def createProduct = Action(parse.json).async { implicit request =>
    request.body.asOpt[ProductUI] match {
      case Some(productUI) =>
        ProductUI.validate(productUI) match {
          case errors if errors.isEmpty =>
            productService
              .createProduct(productUI)
              .map(_ => Ok)
          case errors =>
            Future.successful(BadRequest(Json.toJson(errors)))
        }
      case _ =>
        Future.successful(BadRequest)
    }
  }

  def updateProduct = Action(parse.json).async { implicit request =>
    request.body.asOpt[ProductUI] match {
      case Some(productUI) =>
        ProductUI.validate(productUI) match {
          case errors if errors.isEmpty =>
            productService
              .updateProduct(productUI)
              .map(_ => Ok)
          case errors =>
            Future.successful(BadRequest(Json.toJson(errors)))
        }
      case _ =>
        Future.successful(BadRequest)
    }
  }

  def removeProduct(id: Long) = Action.async {
    productService
      .removeProduct(id)
      .map(_ => Ok)
  }

}

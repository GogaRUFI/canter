package controllers

import javax.inject._

import akka.actor.ActorSystem
import model.ui.ProductUI
import play.api.mvc._
import services.ProductService

import scala.concurrent.ExecutionContext

@Singleton
class ProductViewController @Inject()(productService: ProductService,
                                      cc: ControllerComponents,
                                      actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def productsList = Action.async {
    productService.getAllProducts.map(products =>
      Ok(views.html.list(products map ProductUI.convert))
    )
  }

  def editProduct(id: Long) = Action.async {
    productService.getProduct(id).map {
      case Some(product) =>
        Ok(views.html.edit(product))
      case _ =>
        NotFound
    }
  }

  def createProduct = Action { implicit request =>
    Ok(views.html.create())
  }

}

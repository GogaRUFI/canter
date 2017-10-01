package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.routing.JavaScriptReverseRouter

@Singleton
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def javascriptRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("jsRoutes")(
        routes.javascript.ProductApiController.createProduct,
        routes.javascript.ProductApiController.updateProduct,
        routes.javascript.ProductApiController.removeProduct
      )
    ).as("text/javascript")
  }
}

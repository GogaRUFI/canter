package model.ui

import model.Product
import play.api.libs.json.Json

case class ProductUI(id: Option[String] = None,
                     code: String,
                     name: String,
                     category: String,
                     price: String)

object ProductUI {
  val NonSignificantDecimalZeros = """(0*)$""".r

  implicit val productJsFormat = Json.format[ProductUI]

  implicit def convert(productUI: ProductUI): Product = {
    Product(
      id = productUI.id.map(_.toLong),
      code = productUI.code,
      name = productUI.name,
      category = productUI.category,
      price = (BigDecimal(productUI.price).setScale(2) * 100).toLongExact
    )
  }

  implicit def convert(product: Product): ProductUI = {
    ProductUI(
      id = product.id.map(_.toString),
      code = product.code,
      name = product.name,
      category = product.category,
      price = BigDecimal(product.price / 100D).setScale(2).toString
    )
  }

  def validate(productUI: ProductUI): List[(String, String)] = {
    var errors = List[(String, String)]()

    errors = errors ++: validatePrice(productUI.price)
    errors = errors ++: validateLength(productUI.name, "Name")
    errors = errors ++: validateLength(productUI.category, "Category")
    errors = errors ++: validateLength(productUI.code, "Code")

    errors
  }

  private def validatePrice(value: String): List[(String, String)] = {
    var errors = List[(String, String)]()

    errors = errors ++: validateLength(value, "Price")

    if (errors.isEmpty) {
      try {
        val price = BigDecimal(value.replaceFirst(NonSignificantDecimalZeros.regex, ""))

        if (price.scale > 2) {
          errors = errors :+ ("Price", "value can include only two digits after its decimal point.")
        } else {
          // throws ArithmeticException if the price value doesn't fit into Long
          (price * 100).toLongExact
        }
      } catch {
        case e: NumberFormatException =>
          errors = errors :+ ("Price", "value can include only digits and one decimal point.")
        case e: ArithmeticException =>
          errors = errors :+ ("Price", "price value is too big.")
      }
    }

    errors
  }

  private def validateLength(value: String, name: String, mandatory: Boolean = true): List[(String, String)] = {
    var errors = List[(String, String)]()

    val MAX_LENGTH = 16

    if (value.length > MAX_LENGTH) {
      errors = errors :+ (name, "value can't include more than " + MAX_LENGTH + " signs.")
    }

    if (mandatory && value.trim.isEmpty) {
      errors = errors :+ (name, "value can't be empty.")
    }

    errors
  }
}
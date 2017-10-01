package services

import javax.inject.{Inject, Singleton}

import model.{Product, ProductDAO}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductService @Inject()(productDAO: ProductDAO)(implicit executionContext: ExecutionContext) {
  def getProduct(id: Long): Future[Option[Product]] = {
    productDAO.getById(id)
  }

  def getAllProducts: Future[Seq[Product]] = {
    productDAO.getAll()
  }

  def createProduct(product: Product): Future[Long] = {
    productDAO.insert(product)
  }

  def updateProduct(product: Product): Future[Int] = {
    productDAO.update(product)
  }

  def removeProduct(id: Long): Future[Int] = {
    productDAO.remove(id)
  }
}



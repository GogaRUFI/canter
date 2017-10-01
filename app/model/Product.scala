package model

import javax.inject.{Inject, Singleton}

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcProfile
import slick.lifted.Tag

import scala.concurrent.{ExecutionContext, Future}

case class Product(id: Option[Long] = None,
                   code: String,
                   name: String,
                   category: String,
                   price: Long,
                   createdAt: Long = System.currentTimeMillis,
                   modifiedAt: Option[Long] = None,
                   removedAt: Option[Long] = None)

class ProductTable(tag: Tag) extends Table[Product](tag, "product") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def code = column[String]("code", O.Length(20))

  def name = column[String]("name")

  def category = column[String]("category")

  def price = column[Long]("price")

  def createdAt = column[Long]("createdAt")

  def modifiedAt = column[Option[Long]]("modifiedAt")

  def removedAt = column[Option[Long]]("removedAt")

  //May be useful to define for testing
  def codeConstrain = index("code", code, unique = true)

  def * = (id.?, code, name, category, price, createdAt, modifiedAt, removedAt) <> (Product.tupled, Product.unapply)
}

@Singleton
class ProductDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  val Query = TableQuery[ProductTable]

  def length: Future[Int] = db.run(Query.length.result)

  def createTable: Future[Unit] = db.run(Query.schema.create)

  def recreateTable: Future[Unit] = db.run(Query.schema.drop.andThen(Query.schema.create))

  def insert(product: Product): Future[Long] =
    db.run(
      Query
        .returning(Query.map(_.id))
        .+=(product)
    )

  def update(product: Product): Future[Int] = {
    db.run(
      Query
        .filter(_.id === product.id)
        .update(product.copy(modifiedAt = Some(System.currentTimeMillis)))
    )
  }

  def remove(id: Long): Future[Int] = {
    db.run(
      Query
        .filter(_.id === id)
        .map(_.removedAt)
        .update(Some(System.currentTimeMillis))
    )
  }

  def getById(id: Long): Future[Option[Product]] =
    db.run(
      Query
        .filter(_.id === id)
        .filter(_.removedAt.isEmpty)
        .result.headOption
    )

  def getAll(): Future[Seq[Product]] =
    db.run(
      Query
        .filter(_.removedAt.isEmpty)
        .sortBy(_.name.desc)
        .result
    )
}
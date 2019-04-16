package forcomprehension

object ForComprehension extends App{
  case class Book(author: String, title: String)
  val books: Seq[ForComprehension.Book] = (0 to 5).map(x => Book(s"author:$x", s"title:$x"))

  val res: Seq[String] = for {
    book <- books
    if book.author startsWith "bob"
  } yield book.title
  val resAlt: Seq[String] = books.withFilter(book => book.author startsWith "bob").map(book => book.title)

  case class Book2(authors: List[String], title: String)
  val books2 = (0 to 5).map(x => Book2(List(s"author:$x"), s"title:$x"))
  val res2: Seq[String] = for {
    book <- books2
    author <- book.authors
    if author startsWith "bob"
  } yield book.title
  val res2Alt: Seq[String] = books2.flatMap(book => book.authors.withFilter(author => author startsWith "bob").map(author => book.title))

  val optA : Option[String] = Some("a value")
  val optB : Option[String] = Some("b value")
  val res3: Option[(String, String)] = for {
    a <- optA
    b <- optB
  } yield (a,b)
  val res3Alt: Option[(String, String)] = optA.flatMap(a => optB.map(b => (a, b)))


  val optNumbers = List(Some(1), Some(2), None, Some(3))
  val res4: Seq[Int] = for {
    optNumber <- optNumbers
    value <- optNumber
  } yield value +1
  val res4Alt = optNumbers.flatMap(optNumber => optNumber.map(value => value+1))
}

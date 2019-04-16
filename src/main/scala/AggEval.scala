object AggEval extends App {

  sealed trait Agg { def keyword: String }

  case object Sum extends Agg {
    val keyword = "sum"
  }
  case object Count extends Agg {
    val keyword = "count"
  }

  def buildQuery(agg: Agg): String = {
    s"Aggregating with ${agg.keyword}"
  }

  println(buildQuery(Sum))
  println(buildQuery(Count))

  def describeAgg(agg: Agg): String = {
    agg match {
      case Sum => "sum will add things up"
      // Without "case Count" or "case _" then the compiler will issue a warning stating:
      // "match may not be exhaustive. It would fail on the following input: Count"
      case _ => ""
    }
  }

}
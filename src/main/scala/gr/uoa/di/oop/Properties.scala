package gr.uoa.di.oop

import scala.io.Source

object Properties {
  val properties: Map[String, String] = {
    val stream = getClass.getResourceAsStream("/oop-labs.properties")
    Source.fromInputStream(stream).getLines.foldLeft(Map[String, String]()) { (soFar, line) => {
      val (name, value) = line.split("=") match { case Array(t1, t2) => (t1, t2) }
      soFar + (name -> value)
    }}
  }

  val year: Int = properties.getOrElse("year", "").toInt
  val threadIds: Seq[Int] = properties.getOrElse("registration_threads", "").split(",").map(_.toInt)
  val limit: Int = properties.getOrElse("limit", "").toInt
}

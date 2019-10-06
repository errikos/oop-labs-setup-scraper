import scala.language.postfixOps

class Registrations(val registrationList: Seq[Seq[String]]) {
  def allocate: Seq[Seq[String]] = {
    registrationList.foldLeft(Seq.empty[Seq[String]]) {
      case (soFar, regList) => {
        soFar :+ (regList.zipWithIndex.sortBy { case (id, seq) => (-Registrations.extractYear(id), seq) } map { _._1 })
      }
    }
  }
}

object Registrations {
  def apply(registrationList: Seq[Seq[String]]) = new Registrations(registrationList)

  // Student IDs have the form sdiXXYYYYY, where XX is the registration year.
  def extractYear(username: String): Int = s"20${username.substring(3, 5)}".toInt
}

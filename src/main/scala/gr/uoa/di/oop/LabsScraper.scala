package gr.uoa.di.oop

import scala.language.postfixOps

import java.security.SecureRandom
import java.security.cert.X509Certificate

import javax.net.ssl.{HttpsURLConnection, SSLContext, TrustManager, X509TrustManager}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

object LabsScraper {
  def setupTLS(): Unit = {
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, Array[TrustManager](
      new X509TrustManager {
        override def checkClientTrusted(chain: Array[X509Certificate], authType: String): Unit = {}

        override def checkServerTrusted(chain: Array[X509Certificate], authType: String): Unit = {}

        override def getAcceptedIssuers: Array[X509Certificate] = Array[X509Certificate]()
      }
    ), new SecureRandom())
    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory)
  }

  def exportAllocation(labId: Int, allocated: Seq[String], requestChange: Seq[String]): Unit = {
    println(s"Lab: ${labId}")
    print("- Allocated: ")
    allocated.foreach { s => print(s + " ") }
    println
    print("- Request change: ")
    requestChange.foreach { s => print(s + " ") }
    println
  }

  def main(args: Array[String]): Unit = {
    setupTLS()

    val browser = JsoupBrowser()

    val registrationList = Properties.threadIds map { threadId =>
      val doc = browser.get(s"https://lists.di.uoa.gr/showthread.php?t=$threadId&pp=50")  // list 50 posts per page
      val pages = doc >> elementList("div.pagenav td.vbmenu_control") take 1 map { _ >> allText last } headOption match {
        // this assumes that the threads will never grow to have more than 9 pages
        case Some(n) => n.asDigit  // a fair assumption, since we list 50 posts per page
        case None => 1
      }
      (1 to pages).foldLeft(Seq.empty[String]) {
        case (soFar, page) =>
          val doc = browser.get(s"https://lists.di.uoa.gr/showthread.php?t=$threadId&pp=50&page=$page")
          val regs = doc >> elementList("a.bigusername") map { _ >> allText }
          if (page == 1)
            soFar ++ regs tail  // the first post of the first page is always by spyros
          else
            soFar ++ regs
      }
    }

    val registrations = Registrations(registrationList)
    val allocations = registrations.allocate

    allocations.zipWithIndex.foreach {
      case (allocation, labId) =>
        val allocated = allocation.take(Properties.limit)
        val requestChange = allocation.drop(Properties.limit)
        exportAllocation(labId + 1, allocated, requestChange)
    }
  }
}

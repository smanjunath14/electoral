package com.electoral.parser

/**
 * Created by mshankar on 10/8/17.
 */
object ElectoralTextParser {

  val isDigit = "\\d".r
  
  private def getId(item: String) = item.split(" ").collect{
    case x if x.size ==10  && x.substring(3,10).matches("^[0-9]*$") => x
  } match {
    case x if x.size ==1 => Some("id"->x(0))
    case _ => None
  }

  private def getName(item: String, prefix: String, key: String) = item match {
    case x if item.contains(prefix) =>
      val name = item.substring(item.indexOf(prefix)+prefix.size).replace("name","").replace(":","").trim
      Some(key -> name)
    case _ => None
  }

  private def getAddress(item: String) = item match {
    case x if item.contains("house no.:") =>
      val houseNum = item.substring(item.indexOf("house no.:")+"house no.:".size).trim
      Some("address"->houseNum)
    case _ => None
  }

  private def getAge(item: String) = item match {
    case x if item.contains("age:") =>
      val mayBeAge = item.split(" ").filter(x=>isDigit.findFirstIn(x).isDefined).headOption
      mayBeAge.map("age"->_)
    case _ => None
  }

  private def getSex(item: String) = item match {
    case x if item.contains("sex: male") => Some("sex"->"Male")
    case x if item.contains("sex: female") => Some("sex"->"Female")
    case _ => None
  }

  def parseElectoralList(list: List[String]): List[(String, String)] = {
    val filterEmptyList = list.collect{
      case l if !l.trim.isEmpty => l.trim.toLowerCase
    }.toList

    filterEmptyList.map { item =>
      List(getId(item),
        getName(item, "name :", "name"),
        getName(item, "father:", "father"),
        getName(item, "father :", "father"),
        getName(item, "father's", "father"),
        getName(item, "husband:", "husband"),
        getName(item, "husband :", "husband"),
        getName(item, "husband's", "husband"),
        getAge(item),
        getAddress(item),
        getSex(item)).flatten
    }.flatten
  }
  
}

import net.liftweb.json.DefaultFormats
import net.liftweb.json._
import net.liftweb.json.Extraction._
case class Electoral(id: Option[String],
           name: Option[String] ,
           father: Option[String],
           husband: Option[String],
           address: Option[String],
           age: Option[Int],
           sex: Option[String] )

def voter(id: Option[String] = None, 
          name: Option[String] =None, 
          father: Option[String] =None, 
          address: Option[String]=None,
          husband: Option[String]=None,
          age: Option[Int]=None, 
          sex: Option[String]=None )= Electoral(id,name, father, husband, address, age, sex)

val it = scala.io.Source.fromFile("/Users/mshankar/Documents/Electoral/ElectoralList2.txt").getLines()
val isDigit = "\\d".r

val list = it.collect{
  case l if !l.trim.isEmpty => l.trim.toLowerCase
}.toList

def getId(item: String) = item.split(" ").collect{
  case x if x.size ==10  && x.substring(3,10).matches("^[0-9]*$") => x
} match {
  case x if x.size ==1 => Some("id"->x(0))
  case _ => None  
}

def getName(item: String, prefix: String, key: String) = item match {
  case x if item.contains(prefix) =>
    val name = item.substring(item.indexOf(prefix)+prefix.size).replace("name","").replace(":","").trim
    Some(key -> name)
  case _ => None
}

def getAddress(item: String) = item match {
  case x if item.contains("house no.:") =>
    val houseNum = item.substring(item.indexOf("house no.:")+"house no.:".size).trim
    Some("address"->houseNum)
  case _ => None  
}

def getAge(item: String) = item match {
  case x if item.contains("age:") => 
    val mayBeAge = item.split(" ").filter(x=>isDigit.findFirstIn(x).isDefined).headOption
    mayBeAge.map("age"->_)
  case _ => None  
}

def getSex(item: String) = item match {
  case x if item.contains("sex: male") => Some("sex"->"Male")
  case x if item.contains("sex: female") => Some("sex"->"Female")
  case _ => None    
}


def convertToElectoralObjects(input: List[(String,String)]) = {
  var electoralMap = scala.collection.mutable.Map[String, Electoral]()
  var tempId: String = ""
  for (item <- input) {
    if (item._1 == "id" && !item._2.equals(tempId)) {
      tempId = item._2.toUpperCase
      electoralMap += (tempId -> voter(id = Some(tempId)))
    }
    if (item._1 == "name")
      electoralMap(tempId) = electoralMap(tempId).copy(name = Some(item._2))

    if (item._1 == "age")
      electoralMap(tempId) = electoralMap(tempId).copy(age = Some(Integer.parseInt(item._2)))
    if (item._1 == "father")
      electoralMap(tempId) = electoralMap(tempId).copy(father = Some(item._2))

    if (item._1 == "husband")
      electoralMap(tempId) = electoralMap(tempId).copy(husband = Some(item._2))

    if (item._1 == "address")
      electoralMap(tempId) = electoralMap(tempId).copy(address = Some(item._2))

    if (item._1 == "sex")
      electoralMap(tempId) = electoralMap(tempId).copy(sex = Some(item._2))
  }
  electoralMap.map(x=>x._2)
}

val electoralList: List[(String, String)] = list.map{item =>
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

val electorals = convertToElectoralObjects(electoralList).toList

implicit val formats = DefaultFormats
val voterJson = decompose(electorals).removeField(x=>x.name.equals("$outer"))
compactRender(voterJson)


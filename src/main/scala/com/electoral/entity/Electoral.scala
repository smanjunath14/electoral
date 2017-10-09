package com.electoral.entity

case class Electoral(id: Option[String],
                     name: Option[String] ,
                     father: Option[String],
                     husband: Option[String],
                     address: Option[String],
                     age: Option[Int],
                     sex: Option[String] )

object Electoral {
  def voter(id: Option[String] = None,
            name: Option[String] = None,
            father: Option[String] = None,
            address: Option[String] = None,
            husband: Option[String] = None,
            age: Option[Int] = None,
            sex: Option[String] = None) = Electoral(id, name, father, husband, address, age, sex)

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
}

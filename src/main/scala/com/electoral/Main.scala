package com.electoral

import java.io.File
import java.nio.file.Files

import net.liftweb.json.DefaultFormats
import net.liftweb.json._
import net.liftweb.json.Extraction._
import com.electoral.parser._
import com.electoral.entity._
/**
 * Created by mshankar on 10/8/17.
 */
object Main extends App{
  
  val filePath = "./src/main/resources/ElectoralList/ElectoralList1.txt"
  
  try {
    val rawElectoralList = scala.io.Source.fromFile(filePath).getLines().toList

    val electoralList = ElectoralTextParser.parseElectoralList(rawElectoralList)
    
    val electorals = Electoral.convertToElectoralObjects(electoralList).toList

    implicit val formats = DefaultFormats
    val voterJson = decompose(electorals).removeField(x => x.name.equals("$outer"))
    val electroalJson = prettyRender(voterJson)
    println(electroalJson)
    
    
    
  }catch {
    case t:Throwable => println(s"Error processing file: $filePath", t)
  }
  
  
  
}

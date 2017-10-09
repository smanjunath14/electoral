package com.electoral

import java.io.File
import java.nio.file.Files

import net.liftweb.json.DefaultFormats
import net.liftweb.json._
import net.liftweb.json.Extraction._
import com.electoral.parser._
import com.electoral.entity._
import util.FileUtils

/**
 * Created by mshankar on 10/8/17.
 */
object Main extends App{
  
  val inputDir  = "./src/main/resources/ElectoralList"
  implicit val outputDir = "./out/processed_electoral_list"
  
  
  FileUtils.getListOfFiles(inputDir).foreach { f =>
    processFile(f).map(
    FileUtils.writeToFile(_, f.getName+".json")
    )
  }
  
  def processFile(f:File):Option[String] = {
    try {
      //Read raw electoral list from file.
      val rawElectoralList = scala.io.Source.fromFile(f).getLines().toList

      //parse raw electoral list into key value pairs.
      val electoralList = ElectoralTextParser.parseElectoralList(rawElectoralList)

      //Convert electoral key value pairs to ElectoralObjects.
      val electorals = Electoral.convertToElectoralObjects(electoralList).toList

      //Convert the object to json representation.
      implicit val formats = DefaultFormats
      val voterJson = decompose(electorals).removeField(x => x.name.equals("$outer"))
      Some(prettyRender(voterJson))
      
    }catch {
      case t:Throwable => 
        println(s"Error processing file: $f skipping...", t)
        None
    }  
  }
  
  
}

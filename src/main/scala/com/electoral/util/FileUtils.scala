package com.electoral.util

import java.io.File

/**
 * Created by mshankar on 10/8/17.
 */
object FileUtils {

  def getListOfFiles(dir: String):List[File] = {
    val d = new File(dir)
    d.exists match {
      case true if d.isDirectory => d.listFiles.filter(_.isFile).toList
      case true if d.isFile => List(d)
      case false => println(s"directory doesnot exists: $dir");List()
    }
  }
  
  def writeToFile(inputContent:String, outputFileName: String)(implicit outputDir:String) = {
    import java.io._
    val d: File = new File(outputDir)
    d.exists match {
      case true => write(outputDir+"/"+outputFileName)
      case false => d.mkdirs() match {
        case true => write(outputDir+"/"+outputFileName)
        case false => throw new Exception(s"user doesn't have permission to create dir: $outputDir")
      }
    }

    def write(filePath: String) = {
      val pw = new PrintWriter(new File(filePath))
      pw.write(inputContent)
      pw.close
    }
  }
}

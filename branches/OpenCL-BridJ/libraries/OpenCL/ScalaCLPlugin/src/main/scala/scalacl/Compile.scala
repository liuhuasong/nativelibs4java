/*
 * ScalaCL - putting Scala on the GPU with JavaCL / OpenCL
 * http://scalacl.googlecode.com/
 *
 * Copyright (c) 2009-2010, Olivier Chafik (http://ochafik.free.fr/)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Olivier Chafik nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY OLIVIER CHAFIK AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package scalacl

import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import scala.concurrent.ops
import scala.io.Source
import scala.tools.nsc.CompilerCommand
import scala.tools.nsc.Global
import scala.tools.nsc.Settings
import scala.tools.nsc.reporters.ConsoleReporter
import scala.tools.nsc.reporters.Reporter
//import scalacl.ScalaCLPlugin

object Compile {

  def main(args: Array[String]) {
    compilerMain(args, true)
  }
  lazy val copyrightMessage: Unit = {
    println("""ScalaCL Compiler Plugin
Copyright Olivier Chafik 2010""")
  }
  
  def compilerMain(args: Array[String], enablePlugins: Boolean) = {
    copyrightMessage
    
    val settings = new Settings

    //val scalaHome = new File(System.getenv("SCALA_HOME"))
    //if (!scalaHome.exists)
    //    error("SCALA_HOME environment variable is not defined !")
        
    //val scalaLib = new File(scalaHome, "lib").getAbsolutePath//"/Users/ochafik/bin/scala-2.8.0.final/lib/"
    val extraArgs = List(
      //"-Ybrowse:scalaclfunctionstransform",
      "-optimise",
      "-bootclasspath", System.getProperty("java.class.path",".")
    )
      //List(new File(scalaLib, "scala-library.jar"), new File(scalaLib, "scala-compiler.jar")).map(_.getAbsolutePath).mkString(File.pathSeparator))
      /*++
      List(
        "-Ybrowse:scalaclfunctionstransform",
        "-uniqid",
        "-Ydebug",
        "-classpath",
        Seq(
          //"/Users/ochafik/nativelibs4javaBridJed/OpenCL/ScalaCL2/target/classes",
          //"/Users/ochafik/nativelibs4javaBridJed/OpenCL/ScalaCL2/target/scala_2.8.0/classes"
          "/Users/ochafik/nativelibs4javaBridJed/OpenCL/ScalaCL2/target/scalacl2-bridj-1.0-SNAPSHOT-shaded.jar"
        ).mkString(File.pathSeparator)
      )*/
    //settings.debug.value = true

    val command = new CompilerCommand((args ++ extraArgs).toList, settings) {
      override val cmdName = "scalacl"
    }

    if (command.ok) {
      //settings.debug.value = false

      class ScalaCLPluginRunner(settings: Settings, reporter: Reporter) extends Global(settings, reporter) {
        override protected def computeInternalPhases() {
          super.computeInternalPhases
          if (enablePlugins)
            for (phase <- ScalaCLPlugin.components(this))
              phasesSet += phase
        }
      }
      val runner = new ScalaCLPluginRunner(settings, new ConsoleReporter(settings))
      val run = new runner.Run
      run.compile(command.files)
    }
  }
}

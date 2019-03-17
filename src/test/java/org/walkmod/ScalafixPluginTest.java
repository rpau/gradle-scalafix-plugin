package org.walkmod;

import org.gradle.testkit.runner.GradleRunner;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ScalafixPluginTest {

  @Test
  public void runsScalafixStdout() {

    String output = GradleRunner.create()
            .withProjectDir(new File("src/test/resources/sample"))
            .withArguments("clean", "build", "scalafixStdout")
            .withPluginClasspath()
            .build()
            .getOutput();

    Assert.assertTrue(output.contains("import scala.collection.immutable\n" +
            "object Foo {\n" +
            "  immutable.Seq.empty[Int]\n" +
            "\n" +
            "  def debug: Unit = { println(\"debug\") }\n" +
            "\n" +
            "}"));
  }

}

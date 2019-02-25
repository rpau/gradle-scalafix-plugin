package org.walkmod;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScalafixPluginTest {

  @Test
  public void runsScalafixStdout() {

    String classpath = System.getProperty("java.class.path");
    String[] classpathEntries = classpath.split(File.pathSeparator);

    List<File> files = Arrays.asList(classpathEntries).stream()
            .map(entry -> new File(entry))
            .collect(Collectors.toList());
    GradleRunner runner = GradleRunner.create();
    BuildResult res = runner
            .withProjectDir(new File("src/test/resources/sample"))
            .withArguments("clean", "build", "scalafixStdout")
            .withPluginClasspath(files)
            .build();

    String output = res.getOutput();

    Assert.assertTrue(output.contains("import scala.collection.immutable\n" +
            "object Foo {\n" +
            "  immutable.Seq.empty[Int]\n" +
            "\n" +
            "  def debug: Unit = { println(\"debug\") }\n" +
            "\n" +
            "}"));
  }

}

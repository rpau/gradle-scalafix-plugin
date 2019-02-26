package org.walkmod;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ScalafixPlugin implements Plugin<Project> {

  private static String EXTENSION = "scalafix";

  public void apply(Project project) {
    ScalafixExtension extension = project.getExtensions().create(EXTENSION, ScalafixExtension.class);

    project.getTasks().register("scalafix", Scalafix.class, scalafix -> {
      scalafix.isVerbose = extension.isVerbose();
    });

    project.getTasks().register("scalafixStdout", Scalafix.class, scalafix -> {
      scalafix.isVerbose = extension.isVerbose();
      scalafix.action = "--stdout";
    });

    project.getTasks().register("scalafixDiff", Scalafix.class, scalafix -> {
      scalafix.isVerbose = extension.isVerbose();
      scalafix.action = "--diff";
    });

    project.getTasks().register("scalafixDiffBase",  Scalafix.class, scalafix -> {
      scalafix.isVerbose = extension.isVerbose();
      scalafix.action = "--diff-base";
    });

  }
}
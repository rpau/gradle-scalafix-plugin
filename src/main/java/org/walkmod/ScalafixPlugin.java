package org.walkmod;

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.scala.ScalaCompile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScalafixPlugin implements Plugin<Project> {

  private static String EXTENSION = "scalafix";
  private static List<String> compatibleVersions = Arrays.asList("2.11.12", "2.12.8");

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

    project.getTasks().register("scalafixDiffBase", Scalafix.class, scalafix -> {
      scalafix.isVerbose = extension.isVerbose();
      scalafix.action = "--diff-base";
    });


    project.getPluginManager().withPlugin("scala", appliedPlugin -> {

      project.getConfigurations().getByName("runtime").getAllDependencies()
          .matching(dependency -> "org.scala-lang".equals(dependency.getGroup())
              && "scala-library".equals(dependency.getName()))
          .all(dependency -> {
            if (!compatibleVersions.contains(dependency.getVersion())) {
              throw new GradleException("Scalafix requires scala-library to be one of these versions: " + compatibleVersions);
            } else {
              applyScalafix(project, dependency.getVersion());
            }
          });

    });
  }

  private void applyScalafix(Project project, String scalaVersion) {
    project.getRepositories().mavenCentral();
    Configuration scalaCompilerPluginConfig = project.getConfigurations().maybeCreate("scalaCompilerPlugin");
    project.getDependencies()
        .add("scalaCompilerPlugin", "org.scalameta:semanticdb-scalac_" + scalaVersion + ":4.1.4");
    project.afterEvaluate(p -> {
      p.getTasks().withType(
          ScalaCompile.class,
          scalaCompile -> {
            List<String> additionalParameters = new ArrayList<>(Arrays.asList(
                "-Ywarn-unused",
                "-P:semanticdb:sourceroot:" + p.getProjectDir(),
                "-Yrangepos",
                "-Xplugin:" + scalaCompilerPluginConfig.getAsPath())
            );
            if (scalaCompile.getScalaCompileOptions().getAdditionalParameters() != null) {
              additionalParameters.addAll(scalaCompile.getScalaCompileOptions().getAdditionalParameters());
            }
            scalaCompile.getScalaCompileOptions().setAdditionalParameters(additionalParameters);
          }
      );
    });
  }
}
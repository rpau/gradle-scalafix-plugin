package org.walkmod;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.scala.ScalaCompile;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ScalafixPlugin implements Plugin<Project> {

  private static String EXTENSION = "scalafix";

  private String getClasspath(Project project) {
    List<String> classesDir = project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets()
            .stream().flatMap(sourceSet -> sourceSet.getOutput().getClassesDirs().getFiles().stream())
            .map(File::getPath)
            .collect(Collectors.toList());
    return String.join(File.pathSeparator, classesDir);
  }

  private String getAdditionalParameters(Project project) {
    List<String> additionalParametersList = project.getTasks().withType(ScalaCompile.class).stream().findFirst()
            .map(compile ->
                    compile.getScalaCompileOptions().getAdditionalParameters())
            .orElse(Collections.emptyList());
    String addionalParameters = String.join(",", additionalParametersList);
    return addionalParameters;
  }

  public void apply(Project project) {
    ScalafixExtension extension = project.getExtensions().create(EXTENSION, ScalafixExtension.class);

    Scalafix scalaFix = project.getTasks().create("scalafix", Scalafix.class);
    scalaFix.doLast((task) -> {
      extension.setClassPath(getClasspath(project));
      extension.setScalaOptions(getAdditionalParameters(project));
      extension.setSourceRoot(project.getRootDir().getAbsolutePath());
      scalaFix.apply(extension);
    });


    Scalafix scalaFixStdout = project.getTasks().create("scalafixStdout", Scalafix.class);
    scalaFixStdout.doLast((task) -> {
      extension.setClassPath(getClasspath(project));
      extension.setScalaOptions(getAdditionalParameters(project));
      extension.setSourceRoot(project.getRootDir().getAbsolutePath());
      scalaFixStdout.stdOut(extension);
    });

    Scalafix scalaFixDiff = project.getTasks().create("scalafixDiff", Scalafix.class);
    scalaFixDiff.doLast((task) -> {
      extension.setClassPath(getClasspath(project));
      extension.setScalaOptions(getAdditionalParameters(project));
      extension.setSourceRoot(project.getRootDir().getAbsolutePath());
      scalaFixDiff.diff(project.getExtensions().getByType(ScalafixExtension.class));
    });

    Scalafix scalaFixDiffBase = project.getTasks().create("scalafixDiffBase",  Scalafix.class);
    scalaFixDiffBase.doLast((task) -> {
      extension.setClassPath(getClasspath(project));
      extension.setScalaOptions(getAdditionalParameters(project));
      extension.setSourceRoot(project.getRootDir().getAbsolutePath());
      scalaFixDiffBase.diffBase(extension);
    });

  }
}
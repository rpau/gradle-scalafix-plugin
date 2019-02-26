package org.walkmod;

import org.gradle.api.DefaultTask;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.scala.ScalaCompile;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Scalafix extends DefaultTask {

  @Input
  private String getClasspath() {
    List<String> classesDir = getProject().getConvention().getPlugin(JavaPluginConvention.class).getSourceSets()
            .stream().flatMap(sourceSet -> sourceSet.getOutput().getClassesDirs().getFiles().stream())
            .map(File::getPath)
            .collect(Collectors.toList());
    return String.join(File.pathSeparator, classesDir);
  }

  @Input
  private String getAdditionalParameters() {
    List<String> additionalParametersList = getProject().getTasks().withType(ScalaCompile.class).stream().findFirst()
            .map(compile ->
                    compile.getScalaCompileOptions().getAdditionalParameters())
            .orElse(Collections.emptyList());
    return String.join(",", additionalParametersList);
  }

  @Input
  private String getSourceRoot() {
    return getProject().getRootDir().getAbsolutePath();
  }

  @Input
  String action = null;

  @Input
  boolean isVerbose = false;

  private String[] args(String action) {
    List<String> args = new LinkedList<>();
    if (action != null) {
      args.add(action);
    }
    args.add("--config");
    args.add(getSourceRoot() + "/.scalafix.conf");
    if (isVerbose) {
      args.add("--verbose");
    }
    args.add("--scalacOptions");
    args.add(getAdditionalParameters());
    args.add("--sourceroot");
    args.add(getSourceRoot());
    args.add("--classpath");
    args.add(getClasspath());
    args.add("--no-sys-exit");
    String[] result = new String[args.size()];
    args.toArray(result);
    return result;
  }

  @TaskAction
  public void run() {
    scalafix.v1.Main.main(args(action));
  }
}

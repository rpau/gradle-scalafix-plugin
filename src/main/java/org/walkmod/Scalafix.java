package org.walkmod;

import org.gradle.api.DefaultTask;

import java.util.LinkedList;
import java.util.List;

public class Scalafix extends DefaultTask {

  public void stdOut(ScalafixExtension extension) {
    scalafix.v1.Main.main(args(extension, "--stdout"));
  }

  public void apply(ScalafixExtension extension) {
    scalafix.v1.Main.main(args(extension, null));
  }

  public void diffBase(ScalafixExtension extension) {
    scalafix.v1.Main.main(args(extension, "--diff-base"));
  }

  public void diff(ScalafixExtension extension) {
    scalafix.v1.Main.main(args(extension,"--diff"));
  }

  private String[] args(ScalafixExtension extension, String action) {
    List<String> args = new LinkedList<String>();
    if (action != null) {
      args.add(action);
    }
    args.add("--config");
    args.add(extension.getSourceRoot() + "/.scalafix.conf");
    if (extension.isVerbose()) {
      args.add("--verbose");
    }
    args.add("--scalacOptions");
    args.add(extension.getScalaOptions());
    args.add("--sourceroot");
    args.add(extension.getSourceRoot());
    args.add("--classpath");
    args.add(extension.getClassPath());
    args.add("--no-sys-exit");
    String[] result = new String[args.size()];
    args.toArray(result);
    return result;
  }
}

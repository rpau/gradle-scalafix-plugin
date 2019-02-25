package org.walkmod;

public class ScalafixExtension {

  private boolean isVerbose;

  private String classPath;

  private String scalaOptions = "";

  private String sourceRoot = "";

  public boolean isVerbose() {
    return isVerbose;
  }

  public void setVerbose(boolean verbose) {
    isVerbose = verbose;
  }

  public String getScalaOptions() {
    return scalaOptions;
  }

  public void setScalaOptions(String scalaOptions) {
    this.scalaOptions = scalaOptions;
  }

  public String getClassPath() {
    return classPath;
  }

  public void setClassPath(String classPath) {
    this.classPath = classPath;
  }

  public String getSourceRoot() {
    return sourceRoot;
  }

  public void setSourceRoot(String sourceRoot) {
    this.sourceRoot = sourceRoot;
  }
}

package com.uipath.sonar.plugin.uipath;

import java.util.List;

/**
 * This class represents a deserialized project.json file.
 */
public class ProjectJson {

    public String name;
    public String description;
    public String main;
    public Object dependencies;
    public List<String> excludedData;
    public double toolVerison;
    public Object packOptions;
    public Object runtimeOptions;
}

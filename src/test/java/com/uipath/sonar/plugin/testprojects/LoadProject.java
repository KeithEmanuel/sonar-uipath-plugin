package com.uipath.sonar.plugin.testprojects;

import com.uipath.sonar.plugin.uipath.Project;
import org.dom4j.DocumentException;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;

public class LoadProject {

    private LoadProject(){}

    private static final String BASE_DIR = "src/test/java/com/uipath/sonar/plugin/testprojects/";

    public static Project withPath(String path){
       try{
           return new Project(new File (BASE_DIR + path));
       }
       catch (Exception ex){
           throw new RuntimeException(ex);
       }
    }

    public static Project argsAndVars() {
        return withPath("ArgsAndVars");
    }
    public static Project workflowConventions() {
        return withPath("WorkflowConventions");
    }
}

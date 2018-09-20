package com.uipath.sonarqube.plugin.checks;

import com.uipath.sonarqube.plugin.AbstractProjectCheck;
import com.uipath.sonarqube.plugin.uipath.Project;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

@Rule(
    key = "MainProjectCheck",
    name = "Validate Main workflow.",
    description =  "Verifies that the Main workflow in project.json is valid.",
    status = "BETA",
    priority = Priority.BLOCKER,
    tags = {"project"}
)
public class MainProjectCheck extends AbstractProjectCheck {

    public MainProjectCheck(){
        super();
    }

    @Override
    public void execute(Project project){

        String mainPath = project.getProjectJson().main;

        if(!project.getSensorContext().fileSystem().resolvePath(mainPath).exists()){
            project.reportIssue(getRuleKey(), "Main workflow '" + mainPath + "' does not exist!");
        }
    }
}

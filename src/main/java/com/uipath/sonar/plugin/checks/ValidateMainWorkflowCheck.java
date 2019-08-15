package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractProjectCheck;
import com.uipath.sonar.plugin.uipath.Project;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.io.File;
import java.nio.file.Files;

@Rule(
    key = "ValidateMainWorkflowCheck",
    name = "Check that the 'Main' workflow exists",
    description =  "Verifies that the Main workflow in project.json exists and is valid.",
    status = "BETA",
    priority = Priority.BLOCKER,
    tags = {"project", "bug"}
)
public class ValidateMainWorkflowCheck extends AbstractProjectCheck {

    public ValidateMainWorkflowCheck(){
        super();
    }

    @Override
    public void execute(Project project){

        String mainPath = project.getProjectJson().main;

        if(!Files.exists(new File(project.getDirectory().toURI().resolve(project.getProjectJson().main)).toPath())){
            reportIssue(project, "Main workflow '" + mainPath + "' does not exist!");
        }
    }
}

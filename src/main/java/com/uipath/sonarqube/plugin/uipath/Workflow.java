package com.uipath.sonarqube.plugin.uipath;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.internal.apachecommons.io.FilenameUtils;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Workflow {

    private static final Logger LOG = Loggers.get(Workflow.class);
    private Project project;
    private InputFile inputFile;
    private List<WorkflowArgument> arguments = new ArrayList<>();


    public Workflow(Project project, InputFile inputFile){
        this.project = project;
        this.inputFile = inputFile;

        Load();
    }

    public void Load(){
        // todo : load args
    }

    public Project getProject(){
        return project;
    }

    public InputFile getInputFile(){ return inputFile; }

    public void reportIssue(RuleKey ruleKey, String message){

        NewIssue issue = project.getSensorContext().newIssue()
            .forRule(ruleKey);
        NewIssueLocation location = issue.newLocation();
        location
            .on(inputFile)
            .message(message);
        issue.at(location);

        issue.save();
    }
}

package com.uipath.sonarqube.plugin.uipath;

import java.net.URI;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.apache.commons.io.FilenameUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Workflow {

    private static final Logger LOG = Loggers.get(Workflow.class);
    private Project project;
    private Document xamlDocument;
    private InputFile inputFile;
    private List<WorkflowArgument> arguments;


    public Workflow(Project project, InputFile inputFile) throws IOException, DocumentException {
        this.project = project;
        this.inputFile = inputFile;

        Initialize();
    }

    private void Initialize() throws IOException, DocumentException {
        LoadXamlDocument();
        LoadArguments();
    }

    private void LoadXamlDocument() throws IOException, DocumentException {
        SAXReader saxReader = new SAXReader();
        xamlDocument = saxReader.read(inputFile.inputStream());
    }

    private void LoadArguments(){
        this.arguments = WorkflowArgument.LoadFromWorkflow(this);
    }

    public String getName(){
        return FilenameUtils.getName(inputFile.uri().getPath());
    }

    public Project getProject(){
        return project;
    }

    public Document getXamlDocument(){
        return xamlDocument;
    }

    public List<WorkflowArgument> getArguments(){
        return arguments;
    }

    public InputFile getInputFile(){ return inputFile; }

    public URI getUri(){
        return getInputFile().uri();
    }

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

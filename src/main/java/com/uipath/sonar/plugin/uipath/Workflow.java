package com.uipath.sonar.plugin.uipath;

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
import java.util.List;

/**
 * Workflow represents a XAML workflow file.
 * This class wraps an InputFile object, which represents the underlying XAML file. Issues created using reportIssue
 * will be created on this underlying inputFile.
 *
 * This getXamlDocument method provides a DOM representation of the underlying XAML file. It uses the dom4j library.
 * Refer to dom4j documentation for usage. Most use cases should be easily fulfilled by a simple XPATH query.
 */
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

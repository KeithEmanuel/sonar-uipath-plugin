package com.uipath.sonarqube.plugin.uipath;

import com.google.gson.Gson;
import com.uipath.sonarqube.plugin.UiPathSensor;
import org.dom4j.DocumentException;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;

import javax.swing.text.Document;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

public class Project {

    private static final String screenshotsFolderName = ".screenshots";

    private UiPathSensor sensor;
    private SensorContext sensorContext;
    private InputFile projectJsonInputFile;
    private ProjectJson projectJson;
    private List<Workflow> workflows = new ArrayList<>();
    private List<Issue> issues = new ArrayList<>();

    private Gson gson = new Gson();

    private Project(UiPathSensor sensor, SensorContext sensorContext) throws IOException, DocumentException{
        this.sensor = sensor;
        this.sensorContext = sensorContext;

        initialize();
    }

    public static Project FromSensorContext(UiPathSensor sensor, SensorContext sensorContext){

        if(!sensor.hasProjectJson() || !sensor.hasWorkflows()){
            throw new UnsupportedOperationException("Not a valid project!");  // Todo, make exception class
        }

        try{
            return new Project(sensor, sensorContext);
        }
        catch(IOException e){
            throw new RuntimeException("An unexpected IOException occurred.", e);
        }
        catch(DocumentException e){
            throw new RuntimeException("An unexpected DocumentException occurred.", e);
        }
    }

    private void initialize() throws IOException, DocumentException {

        projectJsonInputFile = sensor.getProjectJson();

        Iterable<InputFile> inputFiles = sensor.getWorkflows();

        for(InputFile inputFile : inputFiles){
            workflows.add(new Workflow(this, inputFile));
        }

        projectJson = gson.fromJson(projectJsonInputFile.contents(), ProjectJson.class);
    }

    public InputFile getInputFile(){
        return projectJsonInputFile;
    }

    public Path getScreenshotsPath(){
        return Paths.get(projectJsonInputFile.uri().toString(), screenshotsFolderName);
    }

    public boolean doesScreenshotExists(String screenshotId){

        File file = new File(Paths.get(getScreenshotsPath().toString(), screenshotId + ".png").toUri());
        return file.exists() && !file.isDirectory();
    }

    public List<Workflow> getWorkflows(){
        return workflows;
    }

    public Optional<Workflow> getWorkflowWithPath(String path){
        try{
            return(getWorkflowWithPath(new URI(path)));
        }
        catch (URISyntaxException e){
            throw new RuntimeException("Exception encountered when parsing the URI string with value '" + path + "'", e);
        }
    }

    public Optional<Workflow> getWorkflowWithPath(URI uri){
        for(Workflow workflow : workflows){
            if(workflow.getUri().equals(uri)){
                return Optional.of(workflow);
            }
        }

        return Optional.empty();
    }

    public ProjectJson getProjectJson(){
        return projectJson;
    }

    public UiPathSensor getSensor(){
        return sensor;
    }

    public SensorContext getSensorContext(){
        return sensorContext;
    }

    public void reportIssue(RuleKey ruleKey, String message){

        NewIssue issue = sensorContext.newIssue()
            .forRule(ruleKey);
        NewIssueLocation location = issue.newLocation();
        location
            .on(projectJsonInputFile)
            .message(message);
        issue.at(location);

        issue.save();
    }
}
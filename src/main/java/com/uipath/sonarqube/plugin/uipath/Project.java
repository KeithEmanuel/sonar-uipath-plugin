package com.uipath.sonarqube.plugin.uipath;

import com.uipath.sonarqube.plugin.UiPathSensor;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Project {

    private static final String screenshotsFolderName = ".screenshots";

    private SensorContext sensorContext;
    private InputFile projectJsonInputFile;
    private List<Workflow> workflows = new ArrayList<>();
    private List<Issue> issues = new ArrayList<>();

    private Project(UiPathSensor sensor, SensorContext sensorContext){

        this.sensorContext = sensorContext;

        projectJsonInputFile = sensor.getProjectJson();
        Iterable<InputFile> inputFiles = sensor.getWorkflows();

        for(InputFile inputFile : inputFiles){
            workflows.add(new Workflow(this, inputFile));
        }
    }

    public static Project FromSensorContext(UiPathSensor sensor, SensorContext sensorContext){

        if(!sensor.hasProjectJson() || !sensor.hasWorkflows()){
            throw new UnsupportedOperationException("Not a valid project!");  // Todo, make exception class
        }

        return new Project(sensor, sensorContext);
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
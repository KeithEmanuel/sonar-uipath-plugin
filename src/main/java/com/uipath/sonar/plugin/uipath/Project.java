package com.uipath.sonar.plugin.uipath;

import com.google.gson.Gson;
import com.uipath.sonar.plugin.HasInputFile;
import com.uipath.sonar.plugin.UiPathSensor;
import com.uipath.sonar.plugin.checks.ArgumentConventionCheck;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import sun.nio.cs.StreamDecoder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Project represents a UiPath project, built from a project.json file.
 *
 * It contains a List of Workflow objects that belong to the project.
 * Issues must be created on a SensorContext and reported on InputFile objects. This class wraps an InputFile
 * for project.json. Any calls to reportIssue will be created on the project.json file.
 */
public class Project implements HasInputFile {

    private static final Logger LOG = Loggers.get(ArgumentConventionCheck.class);

    private static final String screenshotsFolderName = ".screenshots";

    private UiPathSensor sensor;
    private SensorContext sensorContext;
    private InputFile projectJsonInputFile;
    private ProjectJson projectJson;
    private List<Workflow> workflows = new ArrayList<>();
    private static Gson gson = new Gson();

    private Project(UiPathSensor sensor, SensorContext sensorContext) throws IOException, DocumentException{
        this.sensor = sensor;
        this.sensorContext = sensorContext;

        initialize();
    }

    /**
     * For unit testing.
     * @param workflows
     */
    private Project(ProjectJson projectJson, List<Workflow> workflows){
        this.projectJson = projectJson;
        this.workflows = workflows;
    }

    public static Project FromSensorContext(UiPathSensor sensor, SensorContext sensorContext){

        if(!sensor.hasProjectJson() || !sensor.hasWorkflows()){
            throw new UnsupportedOperationException("Not a valid project!");
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

    public static Project FromDirectory(Path path) throws IOException, DocumentException {
        ArrayList<Workflow> workflows = new ArrayList<>();

        for(Path xaml : Files.walk(path).filter(f -> f.endsWith(".xaml")).collect(Collectors.toList())){
            workflows.add(new Workflow(xaml.toFile()));
        }

        File projectJsonFile = Arrays.stream(path.toFile().listFiles()).filter(f -> f.getName() == "project.json").findFirst().get();
        ProjectJson projectJson = gson.fromJson(new FileReader(projectJsonFile), ProjectJson.class);

        return new Project(projectJson, workflows);
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
        // THIS IS UNTESTED
        File file = new File(Paths.get(getScreenshotsPath().toString(), screenshotId + ".png").toUri());
        return file.exists() && !file.isDirectory();
    }

    public List<Workflow> getWorkflows(){
        return workflows;
    }

    public Optional<Workflow> getWorkflowWithPath(String path){

        try{
            URI uri = Utils.getURI(path);

            if(!uri.isAbsolute()){
                uri = getInputFile().uri().resolve(uri);
            }

            return getWorkflowWithPath(uri);
        }
        catch (URISyntaxException e){
            LOG.warn("Encountered an error when trying to parse URI of '" + path + "'", e);

            return Optional.empty();
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

    /*public void reportIssue(RuleKey ruleKey, String message){

        NewIssue issue = sensorContext.newIssue()
            .forRule(ruleKey);
        NewIssueLocation location = issue.newLocation();
        location
            .on(projectJsonInputFile)
            .message(message);
        issue.at(location);

        issue.save();
    }*/
}
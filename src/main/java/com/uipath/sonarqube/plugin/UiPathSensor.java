package com.uipath.sonarqube.plugin;

import com.uipath.sonarqube.plugin.uipath.Project;
import com.uipath.sonarqube.plugin.uipath.Workflow;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.Version;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class UiPathSensor implements Sensor{

    private static final Logger LOG = Loggers.get(UiPathSensor.class);
    private static final Version V6_0 = Version.create(6, 0);

    private final Checks<Object> checks;
    private final FileSystem fileSystem;
    private final FilePredicate projectJsonPredicate;
    private final FilePredicate workflowPredicate;
    private final FilePredicate allPredicate;

    public UiPathSensor(FileSystem fileSystem, CheckFactory checkFactory){
        this.checks = checkFactory.create(CheckRepository.REPOSITORY_KEY)
                .addAnnotatedChecks((Iterable<?>)CheckRepository.getProjectCheckClasses());

        this.fileSystem = fileSystem;

        this.projectJsonPredicate = fileSystem.predicates()
                .matchesPathPattern("**/project.json");

        this.workflowPredicate = fileSystem.predicates()
                .matchesPathPattern("**/*.xaml");

        this.allPredicate = this.fileSystem.predicates().or(projectJsonPredicate, workflowPredicate);
    }

    @Override
    public void describe(SensorDescriptor descriptor){

        descriptor.name("UiPath Project Sensor");
    }

    @Override
    public void execute(SensorContext context){

        Project project = Project.FromSensorContext(this, context);

        NewIssue issue = context.newIssue().forRule(RuleKey.of(CheckRepository.REPOSITORY_KEY, "MainProjectCheck"));
        NewIssueLocation location = issue.newLocation()
            .on(project.getInputFile())
            .message("It not right");
        issue.at(location);
        issue.save();

        if(project.getWorkflows().size() > 0){
            Workflow workflow = project.getWorkflows().get(0);

            NewIssue issue2 = context.newIssue().forRule(RuleKey.of(CheckRepository.REPOSITORY_KEY, "InvokeWorkflowCheck"));
            NewIssueLocation location2 = issue2.newLocation()
                .on(workflow.getInputFile())
                .message("It not right either");
            issue2.at(location2);
            issue2.save();
        }

    }

    public boolean hasProjectJson(){
        return fileSystem.hasFiles(projectJsonPredicate);
    }

    public InputFile getProjectJson(){
        return fileSystem.inputFile(projectJsonPredicate);
    }

    public boolean hasWorkflows(){
        return fileSystem.hasFiles(workflowPredicate);
    }

    public Iterable<InputFile> getWorkflows(){
        return fileSystem.inputFiles(workflowPredicate);
    }

    @Override
    public String toString(){
        return getClass().getSimpleName();
    }


}

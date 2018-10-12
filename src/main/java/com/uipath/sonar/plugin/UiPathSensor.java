package com.uipath.sonar.plugin;

import com.uipath.sonar.plugin.uipath.Workflow;
import com.uipath.sonar.plugin.uipath.Project;
import org.dom4j.XPathException;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.Version;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * UiPathSensor is the main component for scanning UiPath projects. The sensor identifies the files to analyze
 * and executes the checks.
 */
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
    public void execute(SensorContext context) {

        LOG.info("UiPathSensor is running...");

        Project project = Project.FromSensorContext(this, context);

        LOG.info("Project: " + project.getInputFile().uri().toString());

        for(AbstractProjectCheck check : CheckRepository.getProjectChecks()){
            try{
                LOG.info(String.format("Executing check %s...", check.getRule().name()));
                check.execute(project);
            }
            catch (Exception e){
                LOG.error("Error when executing check '" + check.getRule().name() + "'", e);
            }
        }

        for(Workflow workflow : project.getWorkflows()){

            LOG.info("Checking workflow: " + workflow.getName());

            for (AbstractWorkflowCheck check : CheckRepository.getWorkflowChecks()){
                try {
                    LOG.info(String.format("Executing check %s...", check.getRule().name()));
                    check.execute(project, workflow);
                }
                catch (XPathException e){
                    // This will catch errors where XPath queries are ran in a document that doesn't contain that namespace.
                    // IE: Exception: XPath expression uses unbound namespace prefix ui
                    LOG.warn("Encountered XPath exception when executing check '" + check.getRule().name() + "'. This is likely not an issue.", e);
                }
                catch (Exception e){
                    LOG.error("Error when executing check '" + check.getRule().name() + "'", e);
                }
            }
        }

        LOG.info("UiPathSensor finished!");
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

package com.uipath.sonar.plugin;

import com.uipath.sonar.plugin.settings.UiPathLanguageProperties;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.dom4j.XPathException;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.Version;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.File;
import java.util.Optional;

/**
 * UiPathSensor is the main component for scanning UiPath projects. The sensor identifies the files to analyze
 * and executes the checks.
 */
public class UiPathSensor implements Sensor{

    private static final Logger LOG = Loggers.get(UiPathSensor.class);
    private static final Version V6_0 = Version.create(6, 0);

    //private final Checks<Object> checks;
    private FileSystem fileSystem;
    private FilePredicate projectJsonPredicate;
    private FilePredicate workflowPredicate;
    private FilePredicate allPredicate;

    private String projectFilePredicatePattern;
    private String workflowFilePredicatePattern;

    public UiPathSensor(/*FileSystem fileSystem, CheckFactory checkFactory*/){
    //    this.checks = checkFactory.create(CheckRepository.REPOSITORY_KEY)
    //            .addAnnotatedChecks((Iterable<?>)CheckRepository.getProjectCheckClasses());
    }

    @Override
    public void describe(SensorDescriptor descriptor){

        descriptor.name("UiPath Project Sensor");
    }

    @Override
    public void execute(SensorContext context) {

        try{
            Project project;

            try{
                LOG.info("Configuring UiPathSensor...");

                loadSettings(context);
                configure(context);

                LOG.info("UiPathSensor is running...");

                File directory = new File(getProjectJson().uri()).getParentFile();
                project = new Project(directory, this, context);
            }
            catch(Exception ex){
                LOG.info("No UiPath project found. Skipping...");
                return;
            }

            LOG.info("Project: " + project.getInputFile().uri().toString());

            for(AbstractProjectCheck check : CheckRepository.getDefaultProjectChecks()){
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

                for (AbstractWorkflowCheck check : CheckRepository.getDefaultWorkflowChecks()){
                    try {
                        LOG.info(String.format("Executing check %s...", check.getRule().name()));
                        check.execute(project, workflow);
                    }
                    catch (XPathException e){
                        // This will catch errors where XPath queries are ran in a document that doesn't contain a namespace used in the XPath query.
                        // IE: Exception: XPath expression uses unbound namespace prefix ui
                        LOG.warn("Encountered XPath exception when executing check '" + check.getRule().name() + "'. This may or may not be an issue.", e);
                    }
                    catch (Exception e){
                        LOG.error("Error when executing check '" + check.getRule().name() + "'", e);
                    }
                }
            }

            LOG.info("UiPathSensor finished!");
        }
        catch (Exception ex){
            LOG.error("Encountered error when executing UiPath Plugin. This might not be a UiPath Project.", ex);
        }
    }

    private void loadSettings(SensorContext context){
        projectFilePredicatePattern = context.config().get(UiPathLanguageProperties.PROJECT_FILE_PREDICATE_KEY).orElse("");
        workflowFilePredicatePattern = context.config().get(UiPathLanguageProperties.WORKFLOW_FILE_PREDICATE_KEY).orElse("");

        // Setting a reference to the SensorContext in the Rule so that they can grab their own properties.
        for(AbstractCheck check : CheckRepository.getAllChecks()){
            check.SetContext(context);
        }
    }

    private void configure(SensorContext context){
        this.fileSystem = context.fileSystem();

        this.projectJsonPredicate = fileSystem.predicates()
            .matchesPathPattern(projectFilePredicatePattern);

        this.workflowPredicate = fileSystem.predicates()
            .matchesPathPattern(workflowFilePredicatePattern);

        this.allPredicate = this.fileSystem.predicates().or(projectJsonPredicate, workflowPredicate);

        Issues.setContext(context);
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

    public Iterable<InputFile> getWorkflowInputFiles(){
        return fileSystem.inputFiles(workflowPredicate);
    }

    public Optional<InputFile> getInputFileForWorkflow(File file) {
        for(InputFile inputFile : getWorkflowInputFiles()){
            if(inputFile.uri().equals(file.toURI())){
                return Optional.of(inputFile);
            }
        }

        return Optional.empty();
    }

    @Override
    public String toString(){
        return getClass().getSimpleName();
    }
}

package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.dom4j.Element;
import org.dom4j.Node;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Rule(
    key = "WorkflowNamingConventionCheck",
    name = "Workflow names should follow convention.",
    description =  "Checks that names of workflows follow convention.",
    status = "BETA",
    priority = Priority.MAJOR,
    tags = {"workflow"}
)
public class WorkflowNamingConventionCheck extends  AbstractWorkflowCheck{

    public static String WORKFLOW_NAMING_FORMAT_KEY = "uipath.checks.workflownamingconventioncheck.namingformat";
    public static String WORKFLOW_NAMING_FORMAT_DEFAULT_VALUE = "[A-z0-9_]+";

    public WorkflowNamingConventionCheck(){
        super();
    }

    @Override
    public List<PropertyDefinition> getProperties(){

        return Arrays.asList(
            PropertyDefinition.builder(WORKFLOW_NAMING_FORMAT_KEY)
                .defaultValue(WORKFLOW_NAMING_FORMAT_DEFAULT_VALUE)
                .name("Workflow naming convention regex")
                .description("Regular expression defining Workflow file names.")
                .onQualifiers(Qualifiers.PROJECT)
                .build()
        );
    }

    public void execute(Project project, Workflow workflow){

    }
}

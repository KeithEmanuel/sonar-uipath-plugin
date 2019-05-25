package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.Issues;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Rule(
    key = "VariableConventionCheck",
    name = "Variables should follow naming convention",
    description =  "Checks that workflow variables follow naming conventions.",
    status = "BETA",
    priority = Priority.MINOR,
    tags = {"workflow"}
)
public class WorkflowConventionCheck extends AbstractWorkflowCheck {

    public static final String VALID_FORMAT_KEY = "uipath.check.workflowconventioncheck.format";
    private static final String VALID_FORMAT_DEFAULT_VALUE = "^[A-Z_][\\w\\d_]*$";
    public static final String INVALID_FORMAT_KEY = "uipath.check.workflowconventioncheck.invalidformat";
    private static final String INVALID_FORMAT_DEFAULT_VALUE = ".*_[a-z]";

    public WorkflowConventionCheck(){
        super();
    }

    @Override
    public List<PropertyDefinition> defineProperties(){

        return Arrays.asList(
            PropertyDefinition.builder(VALID_FORMAT_KEY)
                .defaultValue(VALID_FORMAT_DEFAULT_VALUE)
                .name("Workflow Naming Convention Format")
                .description("Naming convention format for workflows. Accepts regular expressions.")
                .onQualifiers(Qualifiers.PROJECT)
                .build(),
            PropertyDefinition.builder(INVALID_FORMAT_KEY)
                .defaultValue(INVALID_FORMAT_DEFAULT_VALUE)
                .name("Workflow Naming Convention Format (INVALID)")
                .description("Regular expression for matching invalid workflow naming conventions. Leave blank to disable.")
                .onQualifiers(Qualifiers.PROJECT)
                .build());
    }
    @Override
    public void execute(Project project, Workflow workflow){
        Pattern validPattern = Pattern.compile(getPropertyValue(VALID_FORMAT_KEY, VALID_FORMAT_DEFAULT_VALUE));
        Pattern invalidPattern = Pattern.compile(getPropertyValue(INVALID_FORMAT_KEY, INVALID_FORMAT_DEFAULT_VALUE));

        if(!validPattern.matcher(workflow.getName()).find() || invalidPattern.matcher(workflow.getName()).find()){
            reportIssue(workflow, "Workflow '" + workflow.getName() + "' does not follow the naming convention.");
        }
    }
}

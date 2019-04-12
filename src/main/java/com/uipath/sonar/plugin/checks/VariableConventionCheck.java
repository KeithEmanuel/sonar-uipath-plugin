package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.Issues;
import com.uipath.sonar.plugin.uipath.Utils;
import com.uipath.sonar.plugin.uipath.Workflow;
import com.uipath.sonar.plugin.uipath.Project;
import org.dom4j.Element;
import org.dom4j.Node;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * VariableConventionCheck verifies that variables in a workflow follow naming conventions.
 *
 * TODO: Determine naming convention and implement.
 */

@Rule(
    key = "VariableConventionCheck",
    name = "Variables should follow naming convention",
    description =  "Checks that workflow variables follow naming conventions.",
    status = "BETA",
    priority = Priority.MINOR,
    tags = {"workflow"}
)
public class VariableConventionCheck extends AbstractWorkflowCheck {

    public static final String VARIABLE_FORMAT_KEY = "uipath.check.variableconventioncheck.format";

    public VariableConventionCheck(){
        super();
    }

    @Override
    public List<PropertyDefinition> getProperties(){

        return Arrays.asList(
            PropertyDefinition.builder(VARIABLE_FORMAT_KEY)
                .defaultValue("[camelCase]")
                .name("Variable Convention Format")
                .description("Naming convention format for variables. Accepts [PascalCase], [camelCase], [UPPERCASE], and [lowercase], case sensitive.")
                .onQualifiers(Qualifiers.PROJECT)
                .build());
    }

    @Override
    public void execute(Project project, Workflow workflow){

        List<Node> nodes = workflow.getXamlDocument().selectNodes("//xa:Variable");

        Pattern formatPattern = Utils.createRegexPatternForConvention(getVariableFormat());

        for(Node node : nodes){
           Element element = (Element)node;

           String name = element.attributeValue("Name");

           if(!formatPattern.matcher(name).find()){
               Issues.report(workflow, getRuleKey(), "Variable '" + name + "' does not follow convention. Variables should match the format '" + getVariableFormat() + "'.");
           }
        }
    }

    private String getVariableFormat(){
        return getPropertyValue(VARIABLE_FORMAT_KEY);
    }
}

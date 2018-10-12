package com.uipath.sonar.plugin.checks;

import com.uipath.sonar.plugin.AbstractWorkflowCheck;
import com.uipath.sonar.plugin.uipath.Workflow;
import com.uipath.sonar.plugin.uipath.Project;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Rule(
    key = "WorkflowAnnotationCheck",
    name = "Workflow Annotation Check",
    description =  "Checks that workflow files have a top level annotation.",
    status = "BETA",
    priority = Priority.INFO,
    tags = {"workflow"}
)
public class WorkflowAnnotationCheck extends AbstractWorkflowCheck {

    private static final Logger LOG = Loggers.get(ArgumentConventionCheck.class);

    public WorkflowAnnotationCheck(){
        super();
    }

    @Override
    public void execute(Project project, Workflow workflow){

        List<Node> nodes = workflow.getXamlDocument().selectNodes("/xa:Activity/*[@sap2010:Annotation.AnnotationText]");
        LOG.debug("SIZE: " + nodes.size());

        if(nodes.size() == 0){
            workflow.reportIssue(getRuleKey(), "Workflow '" + workflow.getName() + "' should have a top level annotation.");
        }
    }
}

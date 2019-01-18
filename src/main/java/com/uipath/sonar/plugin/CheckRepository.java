package com.uipath.sonar.plugin;

import com.uipath.sonar.plugin.checks.*;
import org.sonar.api.config.PropertyDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CheckRepository defines all available checks. After you create a check class, be sure to add your check
 * to the appropriate function below!
 */
public class CheckRepository {

    public static final String REPOSITORY_KEY = "uipath";
    public static final String REPOSITORY_NAME = "UipathAnalyzer";
    public static final String PROFILE_NAME = "UiPath Way";

    private static final List<AbstractWorkflowCheck> WORKFLOW_CHECKS =
        Arrays.asList(
            new InvokeWorkflowFileArgumentCheck(),
            new InvokeWorkflowFileExistsCheck(),
            new InvokeWorkflowFilePathCheck(),
            new ArgumentConventionCheck(),
            new VariableConventionCheck(),
            new WorkflowAnnotationCheck(),
            new AvoidGetPasswordCheck(),
            new AvoidChangingCurrentDirectory()
        );

    private static final List<AbstractProjectCheck> PROJECT_CHECKS =
        Arrays.asList(
            new ValidateMainWorkflowCheck()
        );

    private CheckRepository(){
    }

    // Add new WorkflowChecks here!
    public static List<AbstractWorkflowCheck> getWorkflowChecks(){
        return WORKFLOW_CHECKS;
    }

    // Add new ProjectChecks here!
    public static List<AbstractProjectCheck> getProjectChecks(){
        return PROJECT_CHECKS;
    }

    public static List<Class> getWorkflowCheckClasses(){
        return getWorkflowChecks().stream().map(AbstractWorkflowCheck::getClass).collect(Collectors.toList());
    }

    public static List<Class> getProjectCheckClasses(){
        return getProjectChecks().stream().map(AbstractProjectCheck::getClass).collect(Collectors.toList());
    }

    public static List<AbstractCheck> getAllChecks(){

        List<AbstractCheck> allChecks = new ArrayList<>();

        allChecks.addAll(getProjectChecks());
        allChecks.addAll(getWorkflowChecks());

        return allChecks;
    }

    public static List<Class> getAllCheckClasses(){

        List<Class> allClasses = new ArrayList<>();

        allClasses.addAll(getProjectCheckClasses());
        allClasses.addAll(getWorkflowCheckClasses());

        return allClasses;
    }

    public static List<PropertyDefinition> getAllProperties(){
        return getAllChecks().stream()
            .map(AbstractCheck::getProperties)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
}

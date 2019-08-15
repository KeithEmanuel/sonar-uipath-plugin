package com.uipath.sonar.plugin;

import com.google.common.collect.Streams;
import com.uipath.sonar.plugin.checks.*;
import org.sonar.api.config.PropertyDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CheckRepository defines all available checks. After you create a check class, be sure to add your check
 * to the appropriate place below!
 */
public class CheckRepository {

    public static final String REPOSITORY_KEY = "uipath";
    public static final String REPOSITORY_NAME = "UiPathAnalyzer";
    public static final String PROFILE_NAME = "UiPath Way";

    // Add workflow checks that should be added to the default quality profile here.
    private static final List<AbstractWorkflowCheck> DEFAULT_WORKFLOW_CHECKS =
        Arrays.asList(
            new InvokeWorkflowFileArgumentCheck(),
            new InvokeWorkflowFileExistsCheck(),
            new InvokeWorkflowFilePathCheck(),
            new ArgumentConventionCheck(),
            new VariableConventionCheck(),
            new WorkflowConventionCheck(),
            new WorkflowAnnotationCheck(),
            new AvoidGetPasswordCheck(),
            new AvoidChangingCurrentDirectory(),
            new EmptyCatchCheck()
        );

    // Add workflow checks that should NOT be added to the default quality profile here.
    private static final List<AbstractWorkflowCheck> OPTIONAL_WORKFLOW_CHECKS =
        Arrays.asList(
            new AvoidLogMessageCheck()
        );

    // Add project checks that should be added to the default quality profile here.
    private static final List<AbstractProjectCheck> DEFAULT_PROJECT_CHECKS =
        Arrays.asList(
            new ValidateMainWorkflowCheck()
        );

    // Add project checks that should NOT be added to the default quality profile here.
    private static final List<AbstractProjectCheck> OPTIONAL_PROJECT_CHECKS =
        new ArrayList();  // None at the moment


    private CheckRepository(){
    }

    public static List<AbstractWorkflowCheck> getDefaultWorkflowChecks(){
        return DEFAULT_WORKFLOW_CHECKS;
    }

    public static List<AbstractProjectCheck> getDefaultProjectChecks(){
        return DEFAULT_PROJECT_CHECKS;
    }

    public static List<AbstractWorkflowCheck> getOptionalWorkflowChecks(){
        return OPTIONAL_WORKFLOW_CHECKS;
    }

    public static List<AbstractProjectCheck> getOptionalProjectChecks(){
        return OPTIONAL_PROJECT_CHECKS;
    }

    public static List<AbstractWorkflowCheck> getAllWorkflowChecks(){
        return Streams.concat(getDefaultWorkflowChecks().stream(), getOptionalWorkflowChecks().stream())
            .collect(Collectors.toList());
    }

    public static List<AbstractProjectCheck> getAllProjectChecks(){
        return Streams.concat(getDefaultProjectChecks().stream(), getOptionalProjectChecks().stream())
            .collect(Collectors.toList());
    }

    public static List<Class> getWorkflowCheckClasses(){
        return getDefaultWorkflowChecks().stream().map(AbstractWorkflowCheck::getClass).collect(Collectors.toList());
    }

    public static List<Class> getProjectCheckClasses(){
        return getDefaultProjectChecks().stream().map(AbstractProjectCheck::getClass).collect(Collectors.toList());
    }

    public static List<AbstractCheck> getAllChecks(){

        List<AbstractCheck> allChecks = new ArrayList<>();

        allChecks.addAll(getDefaultProjectChecks());
        allChecks.addAll(getDefaultWorkflowChecks());

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
            .map(AbstractCheck::defineProperties)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
}

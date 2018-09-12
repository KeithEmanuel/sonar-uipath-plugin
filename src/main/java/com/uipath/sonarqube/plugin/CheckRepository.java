package com.uipath.sonarqube.plugin;

import com.uipath.sonarqube.plugin.checks.InvokeWorkflowCheck;
import com.uipath.sonarqube.plugin.checks.MainProjectCheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CheckRepository {

    public static final String REPOSITORY_KEY = "uipath";
    public static final String REPOSITORY_NAME = "UipathAnalyzer";
    public static final String PROFILE_NAME = "UiPath Way";

    private CheckRepository(){
    }

    // Add new WorkflowChecks here!
    public static List<AbstractWorkflowCheck> getWorkflowChecks(){
        return Arrays.asList(
            new InvokeWorkflowCheck()
        );
    }

    // Add new ProjectChecks here!
    public static List<AbstractProjectCheck> getProjectChecks(){
        return Arrays.asList(
            new MainProjectCheck()
        );
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
}

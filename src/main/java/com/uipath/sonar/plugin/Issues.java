package com.uipath.sonar.plugin;

import com.sun.tools.javac.util.List;
import com.uipath.sonar.plugin.uipath.Project;
import com.uipath.sonar.plugin.uipath.Workflow;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;

import java.util.ArrayList;

public class Issues {

    private static SensorContext sensorContext = null;
    private static ArrayList<Issue> issues;

    public static void setContext(SensorContext sensorContext){
        Issues.sensorContext = sensorContext;
    }

    public static void report(HasInputFile hasInputFile, RuleKey ruleKey, String message){
        issues.add(new Issue(hasInputFile, ruleKey, message));

        if(sensorContext != null){
            NewIssue issue = sensorContext.newIssue()
                .forRule(ruleKey);
            NewIssueLocation location = issue.newLocation();
            location
                .on(hasInputFile.getInputFile())
                .message(message);
            issue.at(location);

            issue.save();
        }
    }

    public static ArrayList<Issue> getIssues(){
        return issues;
    }

    public static void clearIssues(){
        issues.clear();
    }

    public static int getIssueCount(){
        return issues.size();
    }
}

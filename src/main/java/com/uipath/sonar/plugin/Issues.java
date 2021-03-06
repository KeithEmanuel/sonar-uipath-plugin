package com.uipath.sonar.plugin;

import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.ArrayList;

public class Issues {

    private static Logger LOG = Loggers.get(Issues.class);

    private static SensorContext sensorContext = null;
    private static ArrayList<Issue> issues = new ArrayList<>();

    public static void setContext(SensorContext sensorContext){
        Issues.sensorContext = sensorContext;
    }

    public static void report(HasInputFile hasInputFile, RuleKey ruleKey, String message){
        issues.add(new Issue(hasInputFile, ruleKey, message));

        LOG.debug("Adding issue " + ruleKey.rule() + " SCNULL? " + (sensorContext == null) + " HIF? " + hasInputFile.hasInputFile());

        if(sensorContext != null && hasInputFile.hasInputFile()){
            LOG.info("Creating issue " +  ruleKey.rule());

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

    public static ArrayList<Issue> getAll(){
        return issues;
    }

    public static void clear(){
        issues.clear();
    }

    public static int getCount(){
        return issues.size();
    }
}

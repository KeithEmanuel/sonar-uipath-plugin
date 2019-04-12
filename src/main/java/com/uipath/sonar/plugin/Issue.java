package com.uipath.sonar.plugin;

import javafx.css.Rule;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.rule.RuleKey;

public class Issue {

    private HasInputFile hasInputFile;
    private RuleKey ruleKey;
    private String message;

    public Issue(HasInputFile file, RuleKey ruleKey, String message){
        this.hasInputFile = file;
        this.ruleKey = ruleKey;
        this.message = message;
    }

    public InputFile getInputFile(){
        return hasInputFile.getInputFile();
    }

    public RuleKey getRuleKey(){
        return ruleKey;
    }

    public String getMessage(){
        return message;
    }
}

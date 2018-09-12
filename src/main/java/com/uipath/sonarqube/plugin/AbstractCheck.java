package com.uipath.sonarqube.plugin;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.AnnotationUtils;
import org.sonar.check.Rule;

public class AbstractCheck {

    private Rule rule;
    private RuleKey ruleKey;

    protected AbstractCheck(){
        rule = AnnotationUtils.getAnnotation(this.getClass(), Rule.class);
        ruleKey = RuleKey.of(getRepositoryKeyString(), getRuleKeyString());
    }

    public Rule getRule(){
        return rule;
    }

    public String getRepositoryKeyString(){
        return CheckRepository.REPOSITORY_KEY;
    }

    public String getRuleKeyString(){
        return rule.key();
    }

    public RuleKey getRuleKey(){
        return ruleKey;
    }
}

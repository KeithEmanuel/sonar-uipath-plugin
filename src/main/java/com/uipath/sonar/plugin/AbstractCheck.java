package com.uipath.sonar.plugin;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.AnnotationUtils;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

/**
 * AbstractCheck is the base class for AbstractProjectCheck and AbstractWorkflowCheck.
 * Be sure to call super() in any derived classes to initialize the Rule metadata.
 */
public class AbstractCheck {

    private static final Logger LOG = Loggers.get(AbstractCheck.class);

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

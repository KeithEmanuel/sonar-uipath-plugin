package com.uipath.sonar.plugin;

import com.uipath.sonar.plugin.settings.UiPathLanguageProperties;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.AnnotationUtils;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * AbstractCheck is the base class for AbstractProjectCheck and AbstractWorkflowCheck.
 * Be sure to call super() in any derived classes to initialize the Rule metadata.
 */
public class AbstractCheck {

    private static final Logger LOG = Loggers.get(AbstractCheck.class);

    private SensorContext sensorContext;
    private Rule rule;
    private RuleKey ruleKey;

    private HashMap<String, String> propertyOverrides;

    protected AbstractCheck(){
        rule = AnnotationUtils.getAnnotation(this.getClass(), Rule.class);
        ruleKey = RuleKey.of(getRepositoryKeyString(), getRuleKeyString());
        propertyOverrides = new HashMap<>();
    }

    public List<PropertyDefinition> defineProperties() {
        return new ArrayList<>();
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

    public void SetContext(SensorContext context){
        this.sensorContext = context;
    }

    /**
     * Allows you to overwrite a defined property value, or define them if one does not exist. Used for unit testing.
     * @param key The Property key, defined in the Check class, to overwrite.
     * @param value The overwritten value.
     */
    public void overwriteProperty(String key, String value){
        propertyOverrides.put(key, value);
    }

    protected String getPropertyValue(String key){

        if(propertyOverrides.containsKey(key)){
            return propertyOverrides.get(key);
        }

        try{
            return sensorContext.config().get(key).orElse("");
        } catch (NullPointerException ex){
            return null;
        }
    }

    protected String getPropertyValue(String key, String defaultValue){
        String value = getPropertyValue(key);
        return value == null ? defaultValue : value;
    }

    protected void reportIssue(HasInputFile hasInputFile, String message){
        Issues.report(hasInputFile, getRuleKey(), message);
    }
}

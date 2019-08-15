package com.uipath.sonar.plugin.rules;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Iterables;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.uipath.sonar.plugin.CheckRepository;
import com.uipath.sonar.plugin.languages.UiPathLanguage;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.debt.DebtRemediationFunction;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import org.sonar.api.utils.AnnotationUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * UiPathRulesDefinition loads the rules to a NewRepository.
 */
public class UiPathRulesDefinition implements RulesDefinition {

    private static final String RESOURCE_BASE_PATH = "/com/uipath/l10n/java/rules/squid";

    private final Gson gson = new Gson();

    @Override
    public void define(Context context){
        NewRepository repository = context
            .createRepository(CheckRepository.REPOSITORY_KEY, UiPathLanguage.KEY)
            .setName(CheckRepository.REPOSITORY_NAME);

        List<Class> checks = CheckRepository.getAllCheckClasses();

        new RulesDefinitionAnnotationLoader().load(repository, Iterables.toArray(checks, Class.class));

        for(Class ruleClass : checks){
            newRule(ruleClass, repository);
        }

        repository.done();
    }

    @VisibleForTesting
    protected void newRule(Class<?> ruleClass, NewRepository repository){

        org.sonar.check.Rule ruleAnnotation = AnnotationUtils.getAnnotation(ruleClass, org.sonar.check.Rule.class);

        if(ruleAnnotation == null){
            throw new IllegalArgumentException("No rule annotation was found on " + ruleClass);
        }

        String ruleKey = ruleAnnotation.key();
        if(StringUtils.isEmpty(ruleKey)){
            throw new IllegalArgumentException("No key is defined in Rule annotation of " + ruleKey);
        }
        NewRule rule = repository.rule(ruleKey);
        if(rule == null){
            throw new IllegalStateException("No rule was created for " + ruleClass + " in " + repository.key());
        }

        ruleMetadata(rule);

        /*rule.setTemplate(AnnotationUtils.getAnnotation(ruleClass, RuleTemplate.class) != null);

        if(ruleAnnotation.cardinality() == Cardinality.MULTIPLE){
            throw new IllegalArgumentException("Cardinality is not supported, use the RuleTemplate annotation instead for " + ruleClass);
        }*/
    }

    private void ruleMetadata(NewRule rule){
        String metadataKey = rule.key();
        addHtmlDescription(rule, metadataKey);
        addMetadata(rule, metadataKey);
    }

    private void addMetadata(NewRule rule, String metadataKey){
        URL resource = UiPathRulesDefinition.class.getResource(RESOURCE_BASE_PATH + "/" + metadataKey + "_uipath.json");
        if(resource != null){
            RuleMetadata metadata = gson.fromJson(readResource(resource), RuleMetadata.class);
            rule.setSeverity(metadata.defaultSeverity.toUpperCase(Locale.US));
            rule.setName(metadata.title);
            rule.addTags(metadata.tags);
            rule.setType(RuleType.valueOf(metadata.type));
            rule.setStatus(RuleStatus.valueOf(metadata.status.toUpperCase(Locale.US)));
            if (metadata.remediation != null) {
                rule.setDebtRemediationFunction(metadata.remediation.remediationFunction(rule.debtRemediationFunctions()));
                rule.setGapDescription(metadata.remediation.linearDesc);
            }
        }

    }

    private static void addHtmlDescription(NewRule rule, String metadataKey){
        URL resource = UiPathRulesDefinition.class.getResource(RESOURCE_BASE_PATH + "/" + metadataKey + "_uipath.html");
        if(resource != null){
            rule.setHtmlDescription(readResource(resource));
        }
    }

    private static String readResource(URL resource){
        try{
            return Resources.toString(resource, StandardCharsets.UTF_8);
        }
        catch(IOException e){
            throw new IllegalStateException("Failed to read: " + resource, e);
        }
    }

    private static class RuleMetadata {
        String title;
        String status;
        @Nullable
        Remediation remediation;

        String type;
        String[] tags;
        String defaultSeverity;
    }

    private static class Remediation {
        String func;
        String constantCost;
        String linearDesc;
        String linearOffset;
        String linearFactor;

        public DebtRemediationFunction remediationFunction(DebtRemediationFunctions drf) {
            if (func.startsWith("Constant")) {
                return drf.constantPerIssue(constantCost.replace("mn", "min"));
            }
            if ("Linear".equals(func)) {
                return drf.linear(linearFactor.replace("mn", "min"));
            }
            return drf.linearWithOffset(linearFactor.replace("mn", "min"), linearOffset.replace("mn", "min"));
        }
    }
}

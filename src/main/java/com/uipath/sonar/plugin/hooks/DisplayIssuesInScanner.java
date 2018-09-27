package com.uipath.sonar.plugin.hooks;

import org.sonar.api.batch.postjob.PostJob;
import org.sonar.api.batch.postjob.PostJobContext;
import org.sonar.api.batch.postjob.PostJobDescriptor;
import org.sonar.api.batch.postjob.issue.PostJobIssue;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * This is a sample hook that displays issues in the scanner.
 */
public class DisplayIssuesInScanner implements PostJob {

    private static final Logger LOG = Loggers.get(DisplayIssuesInScanner.class);

    @Override
    public void describe(PostJobDescriptor descriptor) {
        descriptor.name("Display issues");
    }

    @Override
    public void execute(PostJobContext context) {
        // issues are not accessible when the mode "issues" is not enabled
        // with the scanner property "sonar.analysis.mode=issues"
        if (context.analysisMode().isIssues()) {
            // all open issues
            for (PostJobIssue issue : context.issues()) {
                String ruleKey = issue.ruleKey().toString();
                Integer issueLine = issue.line();

                // just to illustrate, we dump some fields of the 'issue' in sysout (bad, very bad)
                LOG.info("OPEN {} : {}({})", ruleKey, issue.componentKey(), issueLine);
            }

            // all resolved issues
            for (PostJobIssue issue : context.resolvedIssues()) {
                LOG.info("RESOLVED {} : {}({})", issue.ruleKey(), issue.componentKey(), issue.line());
            }
        }
    }
}
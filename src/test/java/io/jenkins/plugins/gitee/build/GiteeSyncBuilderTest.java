package io.jenkins.plugins.gitee.build;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.SystemCredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import hudson.model.*;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class GiteeSyncBuilderTest {
    @Rule
    public JenkinsRule rule = new JenkinsRule();

    @Test
    public void builder() throws Exception {
        FreeStyleProject project = rule.createFreeStyleProject();

        StandardUsernameCredentials credentials = new UsernamePasswordCredentialsImpl(CredentialsScope.SYSTEM, null,
                null, "", "");
        SystemCredentialsProvider.getInstance().getCredentials().add(credentials);

        GiteeSyncBuilder builder = new GiteeSyncBuilder();
        builder.setSrcUrl("https://gitee.com/arch2surenpi/test");
        builder.setTargetUrl("https://gitee.com/arch2surenpi/test-1");
        builder.setTargetCredentialId(credentials.getId());

        project.getPublishersList().add(builder);

        rule.buildAndAssertSuccess(project);
    }
}

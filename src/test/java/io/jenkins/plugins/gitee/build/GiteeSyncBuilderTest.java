package io.jenkins.plugins.gitee.build;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.SystemCredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import hudson.model.*;
import hudson.plugins.git.GitSCM;
import hudson.plugins.git.UserRemoteConfig;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.IOException;
import java.util.Collections;

/**
 * @author suren
 */
public class GiteeSyncBuilderTest {
    @Rule
    public JenkinsRule rule = new JenkinsRule();

    @Test
    @Ignore
    public void testConfigRoundtrip() throws Exception {
        FreeStyleProject project = rule.createFreeStyleProject();

        project.getBuildersList().add(new GiteeSyncBuilder(null));

        project = rule.configRoundtrip(project);
        rule.assertEqualDataBoundBeans(new GiteeSyncBuilder(null), project.getBuildersList().get(0));
    }

    @Test
    public void testBuild() throws Exception {
        FreeStyleProject project = rule.createFreeStyleProject();

        UserRemoteConfig remoteConfig = new UserRemoteConfig(
                "https://gitee.com/arch2surenpi/test",
                "origin",
                null, null);

        GitSCM scm = new GitSCM(Collections.singletonList(remoteConfig),
                Collections.emptyList(),
                false,
                Collections.emptyList(), null, null, Collections.emptyList());
        project.setScm(scm);

        StandardUsernameCredentials credentials = new UsernamePasswordCredentialsImpl(CredentialsScope.SYSTEM, null,
                null, "", "");
        SystemCredentialsProvider.getInstance().getCredentials().add(credentials);

        GiteeSyncBuilder builder = new GiteeSyncBuilder("https://gitee.com/arch2surenpi/test-1");
        builder.setCredentialsId(credentials.getId());

        project.getBuildersList().add(builder);

        rule.buildAndAssertSuccess(project);
    }
}

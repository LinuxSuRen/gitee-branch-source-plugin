package io.jenkins.plugins.gitee.build;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.FreeStyleProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.plugins.git.Branch;
import hudson.security.ACL;
import hudson.tasks.*;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.gitclient.Git;
import org.jenkinsci.plugins.gitclient.GitClient;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author suren
 */
public class GiteeSyncBuilder extends Builder implements SimpleBuildStep
{
    private String targetUrl;
    private String targetName = "target";
    private String targetBranch = "master";
    private String credentialsId;

    @DataBoundConstructor
    public GiteeSyncBuilder(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher,
                        @Nonnull TaskListener listener) throws InterruptedException, IOException
    {
        Git git = new Git(listener, null);
        GitClient client = git.in(workspace).getClient();
        PrintStream logger = listener.getLogger();

        setCredential(client, credentialsId, logger);

        if(client.getRemoteUrl(getTargetName()) == null) {
            client.addRemoteUrl(getTargetName(), targetUrl);
        }

        branchSwitch(client, targetBranch, logger);

        logger.println(String.format("Prepare push to remote [%s] branch [%s].", targetUrl, targetBranch));

        try {
            client.push().to(new URIish(targetUrl)).ref(targetBranch).force(true).execute();
        } catch (URISyntaxException e) {
            listener.fatalError(String.format("Push to remote [%s] error, msg [%s]", targetName, e.getMessage()));
            e.printStackTrace();
        }
    }

    private void branchSwitch(GitClient client, String targetBranch, PrintStream logger) throws InterruptedException {
        boolean targetBranchExist = false;
        Set<Branch> branches = client.getBranches();
        if(branches != null) {
            targetBranchExist = branches.stream().anyMatch((branch) -> branch.getName().equals(targetBranch));
        }

        if(!targetBranchExist) {
            logger.println(String.format("Target branch [%s] don't need to create.", targetBranch));

            client.branch(targetBranch);
        }

        client.checkout(targetBranch);

        logger.println(String.format("Already switch to branch [%s].", targetBranch));
    }

    private void setCredential(GitClient client, String credentialId, PrintStream logger) {
        if(credentialId == null || "".equals(credentialId.trim())) {
            logger.println("No credential provide.");
            return;
        }

        StandardUsernameCredentials credential = getCredential(credentialId);
        if(credential != null) {
            client.setCredentials(credential);
        } else {
            logger.println(String.format("Can not found credential by id [%s].", credentialId));
        }
    }

    private StandardUsernameCredentials getCredential(String credentialId) {
        List<StandardUsernameCredentials> allCredentials = CredentialsProvider.lookupCredentials
                (StandardUsernameCredentials.class, Jenkins.getInstance(), ACL.SYSTEM, new ArrayList<>());

        Credentials credential = CredentialsMatchers.firstOrNull(
                allCredentials, CredentialsMatchers.withId(credentialId));

        if(credential != null) {
            return (StandardUsernameCredentials) credential;
        }

        return null;
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService()
    {
        return BuildStepMonitor.BUILD;
    }

    @Extension
    @Symbol("GitSync")
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        public ListBoxModel doFillCredentialsIdItems() {
            FreeStyleProject project = new FreeStyleProject(Jenkins.getInstance(),"fake-" + UUID.randomUUID().toString());

            return new StandardListBoxModel().includeEmptyValue()
                    .includeMatchingAs(ACL.SYSTEM, project,
                            StandardUsernameCredentials.class,
                            new ArrayList<>(),
                            CredentialsMatchers.withScopes(CredentialsScope.GLOBAL));
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType)
        {
            return true;
        }

        @Nonnull
        @Override
        public String getDisplayName()
        {
            return "Git Sync";
        }
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public String getTargetBranch() {
        return targetBranch;
    }

    @DataBoundSetter
    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }

    public String getTargetName() {
        return targetName;
    }

    @DataBoundSetter
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    @DataBoundSetter
    public void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId;
    }
}

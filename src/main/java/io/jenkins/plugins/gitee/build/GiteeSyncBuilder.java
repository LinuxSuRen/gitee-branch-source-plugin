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
import hudson.security.ACL;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;
import org.eclipse.jgit.transport.URIish;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.gitclient.Git;
import org.jenkinsci.plugins.gitclient.GitClient;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author suren
 */
public class GiteeSyncBuilder extends Recorder implements SimpleBuildStep
{
    private String srcUrl;
    private String srcName = "origin";
    private String srcRef = "master";
    private String srcCredentialId;

    private String targetUrl;
    private String targetName = "origin";
    private String targetRef = "master";
    private String targetCredentialId;

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher,
                        @Nonnull TaskListener listener) throws InterruptedException, IOException
    {
        FilePath src = workspace.child("src");
        FilePath target = workspace.child("target");

        Git git = new Git(listener, null);
        GitClient client = git.in(src).getClient();

        setCredential(client, getSrcCredentialId());
        client.clone(srcUrl, srcName, true, srcRef);
        client.checkout().ref(srcRef).execute();

        client = git.in(target).getClient();
        setCredential(client, getTargetCredentialId());
        client.clone(targetUrl, targetName, true, targetRef);
        client.checkout().ref("refs/remotes/" + targetName  + "/" + targetRef).execute();
        client.checkout().branch(targetRef).deleteBranchIfExist(true).ref("HEAD").execute();
//        client.checkout().branch(targetRef).deleteBranchIfExist(true).ref("HEAD").execute();
        src.copyRecursiveTo(target);
        client.add(".");
        client.commit("sdf");
        try {
            String targetURI = client.getRemoteUrl(targetName);
            client.push().to(new URIish(targetURI)).ref(targetRef).force(true).execute();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void setCredential(GitClient client, String credentialId) {
        if(credentialId == null) {
            return;
        }

        StandardUsernameCredentials credential = getCredential(credentialId);
        if(credential != null) {
            client.setCredentials(credential);
        }
    }

    private StandardUsernameCredentials getCredential(String credentialId) {
        List<StandardUsernameCredentials> allCredentials = CredentialsProvider.lookupCredentials
                (StandardUsernameCredentials.class, Jenkins.get(), ACL.SYSTEM, new ArrayList<>());

        Credentials credential = CredentialsMatchers.firstOrNull(
                allCredentials, CredentialsMatchers.withId(credentialId));

        if(credential != null)
        {
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
    @Symbol("hugoGitSubmodulePublsh")
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher>
    {
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType)
        {
            return true;
        }

        @Nonnull
        @Override
        public String getDisplayName()
        {
            return "";
        }
    }

    public String getSrcUrl() {
        return srcUrl;
    }

    @DataBoundSetter
    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    public String getSrcName() {
        return srcName;
    }

    @DataBoundSetter
    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getSrcRef() {
        return srcRef;
    }

    @DataBoundSetter
    public void setSrcRef(String srcRef) {
        this.srcRef = srcRef;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public String getSrcCredentialId() {
        return srcCredentialId;
    }

    @DataBoundSetter
    public void setSrcCredentialId(String srcCredentialId) {
        this.srcCredentialId = srcCredentialId;
    }

    @DataBoundSetter
    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTargetRef() {
        return targetRef;
    }

    @DataBoundSetter
    public void setTargetRef(String targetRef) {
        this.targetRef = targetRef;
    }

    public String getTargetName() {
        return targetName;
    }

    @DataBoundSetter
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetCredentialId() {
        return targetCredentialId;
    }

    @DataBoundSetter
    public void setTargetCredentialId(String targetCredentialId) {
        this.targetCredentialId = targetCredentialId;
    }
}

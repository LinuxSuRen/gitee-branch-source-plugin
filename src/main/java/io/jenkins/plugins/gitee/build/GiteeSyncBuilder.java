package io.jenkins.plugins.gitee.build;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;
import jenkins.tasks.SimpleBuildStep;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author suren
 */
public class GiteeSyncBuilder extends Recorder implements SimpleBuildStep
{
    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher,
                        @Nonnull TaskListener listener) throws InterruptedException, IOException
    {

    }

    @Override
    public BuildStepMonitor getRequiredMonitorService()
    {
        return null;
    }
}

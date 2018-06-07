package io.jenkins.plugins.gitee;

import jenkins.plugins.git.AbstractGitSCMSource;

public class GiteeSCMSource extends AbstractGitSCMSource
{
    @Override
    public String getCredentialsId()
    {
        return null;
    }

    @Override
    public String getRemote()
    {
        return null;
    }
}

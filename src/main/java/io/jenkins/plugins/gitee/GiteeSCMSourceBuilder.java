package io.jenkins.plugins.gitee;

import edu.umd.cs.findbugs.annotations.NonNull;
import jenkins.scm.api.trait.SCMSourceBuilder;

public class GiteeSCMSourceBuilder extends SCMSourceBuilder<GiteeSCMSourceBuilder, GiteeSCMSource> {
    public GiteeSCMSourceBuilder(@NonNull Class<GiteeSCMSource> clazz, @NonNull String projectName)
    {
        super(clazz, projectName);
    }

    @NonNull
    @Override
    public GiteeSCMSource build()
    {
        return null;
    }
}

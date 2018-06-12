package io.jenkins.plugins.gitee.client;

import com.surenpi.gitee.client.GitoscApiUtil;
import com.surenpi.gitee.client.GitoscConnection;
import com.surenpi.gitee.client.GitoscWebhook;
import com.surenpi.gitee.client.data.GitoscAuthData;
import com.surenpi.gitee.client.data.GitoscRepo;
import com.surenpi.gitee.client.data.GitoscRepoDetailed;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GitoscApiUtilTest {
    private GitoscConnection connect;

    @Before
    public void setup() {
        GitoscAuthData authData = GitoscAuthData.createSessionAuth(
                "https://gitee.com/",
                "zxjlwt@126.com",
                "walkman31415",
                "Pwt14i3tKYgjasWEVE9m");

        connect = new GitoscConnection(authData, true);
    }

    @Test
    @Ignore
    public void userRepos() throws IOException {
        List<GitoscRepo> repos = GitoscApiUtil.getUserRepos("arch2surenpi", connect);

        assertNotNull(repos);
        assertTrue(repos.size() > 0);
    }

    @Test
    @Ignore
    public void getDetailedRepoInfo() throws IOException {
        GitoscRepoDetailed info = GitoscApiUtil.getDetailedRepoInfo(connect, "arch2surenpi", "test");

        assertNotNull(info);
        assertNotNull(info.getFullName());
    }

    @Test
    @Ignore
    public void forceSyncProject() throws IOException {
        GitoscApiUtil.forceSyncProject(connect, "arch2surenpi", "jenkins-client-java");
    }

    @Test
    public void createWebhook() throws IOException {
        GitoscWebhook webhook = GitoscApiUtil.createWebhook(connect, "surenpi", "test");

        assertNotNull(webhook);
    }
}

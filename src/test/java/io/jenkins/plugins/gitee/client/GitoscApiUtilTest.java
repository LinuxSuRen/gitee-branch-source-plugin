package io.jenkins.plugins.gitee.client;

import com.surenpi.gitee.client.GitoscApiUtil;
import com.surenpi.gitee.client.GitoscConnection;
import com.surenpi.gitee.client.data.GitoscAuthData;
import com.surenpi.gitee.client.data.GitoscRepo;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GitoscApiUtilTest {
    private GitoscConnection connect;

    @Before
    public void setup() {
        GitoscAuthData authData = GitoscAuthData.createAnonymous();

        connect = new GitoscConnection(authData, true);
    }

    @Test
    public void createRepo() throws IOException {
        List<GitoscRepo> repos = GitoscApiUtil.getUserRepos("arch2surenpi", connect);

        assertNotNull(repos);
        assertTrue(repos.size() > 0);
    }
}

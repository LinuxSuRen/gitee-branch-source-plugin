/*
 * Copyright 2016-2017 码云
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.surenpi.gitee.client;

import com.google.gson.*;

import com.surenpi.gitee.client.data.*;
import com.surenpi.gitee.client.exceptions.GitoscAuthenticationException;
import com.surenpi.gitee.client.exceptions.GitoscConfusingException;
import com.surenpi.gitee.client.exceptions.GitoscJsonException;
import com.surenpi.gitee.client.exceptions.GitoscStatusCodeException;
import com.surenpi.gitee.client.requests.GitoscChangeIssueStateRequest;
import com.surenpi.gitee.client.requests.GitoscGistRequest;
import com.surenpi.gitee.client.requests.GitoscRepoRequest;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import static com.surenpi.gitee.client.GitoscConstants.AUTH_CLIENT_ID;
import static com.surenpi.gitee.client.GitoscConstants.AUTH_CLIENT_SECRET;
import static com.surenpi.gitee.client.GitoscConstants.AUTH_GRANT_TYPE;


/**
 * @author Yuyou Chow
 *


 */
public class GitoscApiUtil {
 private static final Gson gson = initGson();

	private static final String PER_PAGE = "per_page=100";

	private static final Header ACCEPT_V3_JSON_HTML_MARKUP = new BasicHeader("Accept", "application/vnd.github.v3.html+json");
	private static final Header ACCEPT_V3_JSON = new BasicHeader("Accept", "application/vnd.github.v3+json");

	private static Gson initGson(){
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
		return builder.create();
	}


	public static <T> T fromJson( JsonElement json, Class<T> classT) throws IOException {
		if (json == null) {
			throw new GitoscJsonException("Unexpected empty response");
		}

		T res;
		try {
			//cast as workaround for early java 1.6 bug
			//noinspection RedundantCast
			res = (T)gson.fromJson(json, classT);
		}
		catch (ClassCastException | JsonParseException e) {
			throw new GitoscJsonException("Parse exception while converting JSON to object " + classT.toString(), e);
		}
		if (res == null) {
			throw new GitoscJsonException("Empty Json response");
		}
		return res;
	}


	public static String getScopedToken(GitoscConnection connection, Collection<String> scopes, String note)
		throws IOException {
		try {
			return getNewScopedToken(connection, scopes, note).getAccessToken();
		}
		catch (GitoscStatusCodeException e) {
			if (e.getError() != null) {
				e.printStackTrace();
			}
			throw e;
		}
	}


	private static GitoscAuthorization getNewScopedToken(GitoscConnection connection,
														 Collection<String> scopes,
														 String note)
		throws IOException {

		try {
			GitoscAuthData.SessionAuth sessionAuth = connection.getAuth().getSessionAuth();
			assert sessionAuth != null && !"".equals(sessionAuth.getPassword());

			String requestBody = JOINER.join(
				AUTH_GRANT_TYPE, AUTH_CLIENT_ID, AUTH_CLIENT_SECRET,
				"scope=",
				"username=" + sessionAuth.getLogin(),
				"password=" + sessionAuth.getPassword()
			);
			return loadPost(connection, "/oauth/token", requestBody, GitoscAuthorization.class);
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't create token: scopes - " + scopes + " - note " + note);
			throw e;
		}
	}


	public static String getMasterToken(GitoscConnection connection, String note) throws IOException {
		// "projects" - read/write access to public/private repositories
		// "gists" - create/delete gists
		List<String> scopes = Arrays.asList("projects", "gists", "user_info");

		return getScopedToken(connection, scopes, note);
	}


	public static String getTasksToken(GitoscConnection connection,
	                                   String user,
	                                   String repo,
	                                   String note) throws IOException {

		List<String> scopes = Arrays.asList("projects", "issues", "user_info");
		return getScopedToken(connection, scopes, note);
	}

	//====================================================================================
	// git.oschina.net/api/v5 - method calls
	//====================================================================================
	public static void deleteGist(GitoscConnection connection, String id) throws IOException {
		try {
			String path = "/gists/" + id;
			connection.deleteRequest(path);
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't delete gist: id - " + id);
			throw e;
		}
	}


	public static GitoscGist getGist(GitoscConnection connection, String id) throws IOException {
		try {
			String path = "/gists/" + id;
			return load(connection, path, GitoscGist.class, ACCEPT_V3_JSON);
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't get gist info: id " + id);
			throw e;
		}
	}


	public static GitoscGist createGist(GitoscConnection connection,
										List<GitoscGistRequest.FileContent> contents,
										String description,
										boolean isPrivate) throws IOException {
		try {
			GitoscGistRequest request = new GitoscGistRequest(contents, description, !isPrivate);
			return post(connection, "/gists", request, GitoscGist.class, ACCEPT_V3_JSON);
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't create gist");
			throw e;
		}
	}

	public static GitoscWebhook createWebhook(GitoscConnection connection, String owner, String repo) throws IOException {
        Map<String, String> request = new HashMap<>();
        request.put("url", "http://baidu.com");

        String path = String.format("/repos/%s/%s/hooks", owner, repo);

        return post(connection, path, request, GitoscWebhook.class, ACCEPT_V3_JSON);
    }

	public static List<GitoscIssue> getIssuesQueried(GitoscConnection connection,
													 String user,
													 String repo,
													 String assignedUser,
													 String query,
													 boolean withClosed) throws IOException {
		try {
			String state = withClosed ? "" : " state:open";
			String assignee = StringUtils.isEmptyOrSpaces(assignedUser) ? "" : " assignee:" + assignedUser;
			query = StringUtils.isEmptyOrSpaces(query) ? "" : query;
			query = URLEncoder.encode(query + "+repo:" + user + "/" + repo + state + assignee, "UTF8");
			String path = "/search/issues?q=" + query;

			//TODO: Use bodyHtml for issues - GitHub does not support this feature for SearchApi yet
			return loadAll(connection, path, GitoscIssue[].class, ACCEPT_V3_JSON);
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't get queried issues: " + user + "/" + repo + " - " + query);
			throw e;
		}
	}

	/*
   * Open issues only
   */

	public static List<GitoscIssue> getIssuesAssigned(GitoscConnection connection,
	                                                  String user,
	                                                  String repo,
	                                                   String assigned,
	                                                  int max,
	                                                  boolean withClosed) throws IOException {
		try {
			String state = "state=" + (withClosed ? "all" : "open");
			String path;

			if (StringUtils.isEmptyOrSpaces(assigned)) {
				path = "/repos/" + user + "/" + repo + "/issues?" + PER_PAGE + "&" + state;
			}else {
				path = "/repos/" + user + "/" + repo + "/issues?assignee=" + assigned + "&" + PER_PAGE + "&" + state;
			}

			GitoscConnection.PagedRequest<GitoscIssue> request = new GitoscConnection.ArrayPagedRequest<>(path, GitoscIssue[].class, ACCEPT_V3_JSON);

			List<GitoscIssue> result = new ArrayList<>();
			while (request.hasNext() && max > result.size()) {
				result.addAll(request.next(connection));
			}
			return result;
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't get assigned issues: " + user + "/" + repo + " - " + assigned);
			throw e;
		}
	}


	public static List<GitoscIssueComment> getIssueComments(GitoscConnection connection,
															String user,
															String repo,
															String id)
		throws IOException {
		try {
			String path = "/repos/" + user + "/" + repo + "/issues/" + id + "/comments?" + PER_PAGE;
			return loadAll(connection, path, GitoscIssueComment[].class, ACCEPT_V3_JSON);
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't get issue comments: " + user + "/" + repo + " - " + id);
			throw e;
		}
	}


	public static GitoscIssue getIssue(GitoscConnection connection, String user, String repo, String id)
		throws IOException {
		try {
			String path = "/repos/" + user + "/" + repo + "/issues/" + id;
			return load(connection, path, GitoscIssue.class, ACCEPT_V3_JSON);
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't get issue info: " + user + "/" + repo + " - " + id);
			throw e;
		}
	}

	public static void setIssueState(GitoscConnection connection,
	                                 String user,
	                                 String repo,
	                                 String id,
	                                 String title,
	                                 boolean open)
		throws IOException {
		try {
			String path = "/repos/" + user + "/" + repo + "/issues/" + id;

			GitoscChangeIssueStateRequest request = new GitoscChangeIssueStateRequest(open ? "open" : "closed", title);
			JsonElement result = connection.patchRequest(path, gson.toJson(request), ACCEPT_V3_JSON);
			fromJson(result, GitoscIssue.class);
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't set issue state: " + user + "/" + repo + " - " + id + "@" + (open ? "open" : "closed"));
			throw e;
		}
	}


	public static GitoscUserDetailed getCurrentUser(GitoscConnection connection) throws IOException {
		try {
			String requestBody;

			switch (connection.getAuth().getAuthType()){
				case TOKEN:
					return load(connection, "/user", GitoscUserDetailed.class);
				default:
					GitoscAuthData.SessionAuth sessionAuth = connection.getAuth().getSessionAuth();
					assert sessionAuth != null;
					requestBody = JOINER.join("email=" + sessionAuth.getLogin(), "password=" + sessionAuth.getPassword());
					return loadPost(connection, "/session", requestBody, GitoscUserDetailed.class);
			}
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't get user info");
			throw e;
		}
	}


	private static <T> List<T> loadAll(GitoscConnection connection,
	                                   String path,
	                                   Class<? extends T[]> type,
	                                   Header... headers) throws IOException {
		GitoscConnection.PagedRequest<T> request = new GitoscConnection.ArrayPagedRequest<>(path, type, headers);
		return request.getAll(connection);
	}


	private static <T> T load(GitoscConnection connection,
	                          String path,
	                          Class<? extends T> type,
	                          Header... headers) throws IOException {
		JsonElement result = connection.getRequest(path, headers);
		return fromJson(result, type);
	}


	private static <T> T post(GitoscConnection connection,
	                          String path,
	                          Object request,
	                          Class<? extends T> type,
	                          Header... headers) throws IOException {

		JsonElement result = connection.postRequest(path, gson.toJson(request), headers);
		return fromJson(result, type);
	}

    private static void post(GitoscConnection connection,
                              String path,
                              Object request,
                              Header... headers) throws IOException {
        connection.postRequest(path, gson.toJson(request), headers);
    }


	private static <T> T loadPost(GitoscConnection connection,
	                              String path,
	                               String requestBody,
	                              Class<? extends T> type,
	                              Header... headers) throws IOException {

		JsonElement result = connection.postRequest(path, requestBody, headers);
		return fromJson(result, type);
	}


	public static GitoscAuthorization getAuthorization(GitoscConnection connection) throws IOException {
		try {
			if(connection.getAuth().getAuthType() != GitoscAuthData.AuthType.SESSION) {
				throw new GitoscAuthenticationException("Get Authorization AuthType Error: " + connection.getAuth().getAuthType());
			}

			GitoscAuthData.SessionAuth sessionAuth = connection.getAuth().getSessionAuth();
			assert sessionAuth != null;

			String requestBody = JOINER.join(
				AUTH_GRANT_TYPE, AUTH_CLIENT_ID,AUTH_CLIENT_SECRET,
				"username=" + sessionAuth.getLogin(),
				"password=" + sessionAuth.getPassword()
			);
			return loadPost(connection, "/oauth/token", requestBody, GitoscAuthorization.class);
		}
		catch (GitoscAuthenticationException e){
			e.printStackTrace();
			throw e;
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't get user info");
			throw e;
		}
	}

	//====================================================================================
	// git.oschina.net/api/v3 - method calls
	//====================================================================================


	public static List<GitoscRepo> getAvailableRepos(String user, GitoscConnection connection) throws IOException {
		try{
			List<GitoscRepo> repos = new ArrayList<GitoscRepo>();
			repos.addAll(getUserRepos(user, connection, true));
			return repos;
		}catch (GitoscConfusingException e){
			e.setDetails("Can't get available repositories");
			throw e;
		}
	}


	public static GitoscUserDetailed getCurrentUserDetailed(GitoscConnection connection) throws IOException {
		try {
			String requestBody;

			switch (connection.getAuth().getAuthType()){
				case TOKEN:
					return load(connection, "/user", GitoscUserDetailed.class);
				default:
					GitoscAuthData.SessionAuth sessionAuth = connection.getAuth().getSessionAuth();
					assert sessionAuth != null;
					requestBody = JOINER.join("email=" + sessionAuth.getLogin(), "password=" + sessionAuth.getPassword());
					return loadPost(connection, "/session", requestBody, GitoscUserDetailed.class);
			}
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't get user info");
			throw e;
		}
	}


	public static GitoscRepoDetailed getDetailedRepoInfo(GitoscConnection connection, String owner, String name)
		throws IOException {
		try {
			switch (connection.getAuth().getAuthType()){
				case TOKEN:
					final String request = "/repos/" + owner + "/" + name;
					return load(connection, request, GitoscRepoDetailed.class, ACCEPT_V3_JSON);
				default:
					final String path = "/repos/" + owner + "/" + name;
					return load(connection, path, GitoscRepoDetailed.class, ACCEPT_V3_JSON);
			}
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't get repository info: " + owner + "/" + name);
			throw e;
		}
	}


	public static List<GitoscRepo> getUserRepos(String user, GitoscConnection connection) throws IOException {
		return getUserRepos(user , connection, false);
	}


	public static List<GitoscRepo> getUserRepos(String user, GitoscConnection connection, boolean allAssociated) throws IOException {
		try {
			String path;

			switch (connection.getAuth().getAuthType()){
				case TOKEN:
					String type = allAssociated ? "" : "type=owner&";
					path = "/user/repos?" + type + PER_PAGE;
					return loadAll(connection, path, GitoscRepo[].class, ACCEPT_V3_JSON);
//                case SESSION:
//                    break;
				default:
					path = "/users/" + user + "/repos?1=1";
					return loadAll(connection, path, GitoscRepo[].class, ACCEPT_V3_JSON);
//					GitoscConnection.PagedRequest<GitoscRepo> request = new GitoscConnection.PagedRequest<GitoscRepo>(path, GitoscRepo.class, GitoscRepoRaw[].class);
//					return request.getAll(connection);
			}
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't get user repositories");
			throw e;
		}
	}


	public static GitoscRepo createRepo(GitoscConnection connection,
									 String name,
									 String description,
										boolean isPrivate)
		throws IOException {

		try {
			String path;
			switch (connection.getAuth().getAuthType()) {
				case TOKEN:
					path = "/user/repos";
					GitoscRepoRequest request = new GitoscRepoRequest(name, description, isPrivate);
					assert connection.getAuth().getTokenAuth() != null;
					request.setAccessToken(connection.getAuth().getTokenAuth().getToken());
					return post(connection, path, request, GitoscRepo.class, ACCEPT_V3_JSON);
				default:
					path = "/projects";
					String requestBody = JOINER.join("name=" + name, "description=" + description, "private=" + (isPrivate? 1 : 0));
					return loadPost(connection, path, requestBody, GitoscRepo.class, ACCEPT_V3_JSON);
//					return createDataFromRaw(fromJson(connection.postRequest(path, requestBody), GitoscRepoRaw.class), GitoscRepo.class);
			}
		}
		catch (GitoscConfusingException e) {
			e.setDetails("Can't create repository: " + name);
			throw e;
		}
	}

	public static void forceSyncProject(GitoscConnection connect, String owner, String project) throws IOException {
        //https://gitee.com/arch2surenpi/jenkins-client-java/force_sync_project
        String user = "zxjlwt@126.com";
        String passwd = "walkman31415";
        String token = "lW9DIWWyhUyTrV6234Uhk2VWd936Tud6Ho36I8YG3i0=";
        String path = "/" + owner + "/" + project + "/force_sync_project";
        post(connect, path, new ForceSyncProject(user, passwd, token), ACCEPT_V3_JSON,
                new BasicHeader("X-CSRF-Token", token),
                new BasicHeader("X-Requested-With", "XMLHttpRequest"),
                new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"));
    }

    static class ForceSyncProject {
	    private String user_sync_code;
	    private String password_sync_code;
	    private String authenticity_token;

        public ForceSyncProject(String user_sync_code, String password_sync_code, String authenticity_token) {
            this.user_sync_code = user_sync_code;
            this.password_sync_code = password_sync_code;
            this.authenticity_token = authenticity_token;
        }

        public String getUser_sync_code() {
            return user_sync_code;
        }

        public void setUser_sync_code(String user_sync_code) {
            this.user_sync_code = user_sync_code;
        }

        public String getPassword_sync_code() {
            return password_sync_code;
        }

        public void setPassword_sync_code(String password_sync_code) {
            this.password_sync_code = password_sync_code;
        }

        public String getAuthenticity_token() {
            return authenticity_token;
        }

        public void setAuthenticity_token(String authenticity_token) {
            this.authenticity_token = authenticity_token;
        }
    }
}

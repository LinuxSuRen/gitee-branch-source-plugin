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
package com.surenpi.gitee.client.data;

import com.surenpi.gitee.client.GitoscConstants;
import com.surenpi.gitee.client.GitoscFullPath;
import com.surenpi.gitee.client.StringUtils;

/**
 * @author Yuyou Chow
 *
 * Base on https://github.com/JetBrains/intellij-community/blob/master/plugins/github/src/org/jetbrains/plugins/github/util/GithubUrlUtil.java
 * @author JetBrains s.r.o.
 * @author Aleksey Pivovarov
 */
public class GitoscUrlUtil {

	public static String removeProtocolPrefix(String url) {
		int index = url.indexOf('@');
		if (index != -1) {
			return url.substring(index + 1).replace(':', '/');
		}
		index = url.indexOf("://");
		if (index != -1) {
			return url.substring(index + 3);
		}
		return url;
	}


	public static String removeTrailingSlash(String s) {
		if (s.endsWith("/")) {
			return s.substring(0, s.length() - 1);
		}
		return s;
	}

    /**
     * @return E.g.: https://git.oschina.net/api/v3
     */
	public static String getApiUrl() {
		return getApiUrl(getHost());
	}

    /**
     * @return host
     */
	public static String getHost() {
	    return "https://gitee.com/";
    }


	/**
	 * @param urlFromSettings
	 * @return api url
	 */
	public static String getApiUrl(String urlFromSettings) {
		return getApiProtocolFromUrl(urlFromSettings) + getApiUrlWithoutProtocol(urlFromSettings);
	}

    /**
     * @param auth
     * @return api url
     */
	public static String getApiUrl(GitoscAuthData auth) {
		return getApiProtocolFromUrl(auth.getHost()) + getApiUrlWithoutProtocol(auth);
	}

    /**
     * @return api protocol
     */
	public static String getApiProtocol() {
		return getApiProtocolFromUrl(getHost());
	}

    /**
     * @param urlFromSettings
     * @return api protocol
     */
	public static String getApiProtocolFromUrl(String urlFromSettings) {
		if (StringUtils.startsWithIgnoreCase(urlFromSettings.trim(), "http://")){
			return "http://";
		}

		return "https://";
	}

	/**
	 * Returns the "host" part of Gitosc URLs.
	 * E.g.: https://git.oschina.net
	 * Note: there is no trailing slash in the returned url.
     * @return git host
	 */
	public static String getGitoscHost() {
		return getApiProtocol() + getGitHostWithoutProtocol();
	}

	/**
	 * E.g.: https://git.oschina.net/suffix/ -> git.oschina.net
	 *       git.oschina.net:8080/ -> git.oschina.net
     * @return  host
	 */
	public static String getHostFromUrl(String url) {
		String path = removeProtocolPrefix(url).replace(':', '/');
		int index = path.indexOf('/');
		if (index == -1) {
			return path;
		}
		else {
			return path.substring(0, index);
		}
	}


	/**
	 * @return E.g.: git.oschina.net
	 */
	public static String getGitHostWithoutProtocol() {
		return removeTrailingSlash(removeProtocolPrefix(getHost()));
	}

    /**
     * @param urlFromSettings
     * @return getApiUrlWithoutProtocol
     */
	public static String getApiUrlWithoutProtocol(String urlFromSettings) {
		String url = removeTrailingSlash(removeProtocolPrefix(urlFromSettings.toLowerCase()));

		final String API_SUFFIX = "/api/v3";

		if (url.equals(GitoscConstants.DEFAULT_GITOSC_HOST)) {
			return url + API_SUFFIX;
		}
		else if (url.equals(GitoscConstants.DEFAULT_GITOSC_HOST + API_SUFFIX)) {
			return url;
		}
		else{
			// have no custom GitOSC url yet.
			return GitoscConstants.DEFAULT_GITOSC_HOST + API_SUFFIX;
		}
	}


	public static String getApiUrlWithoutProtocol(GitoscAuthData auth) {
		String url = removeTrailingSlash(removeProtocolPrefix(auth.getHost().toLowerCase()));

		final String API_SUFFIX = (auth.getTokenAuth() == null && auth.getSessionAuth() == null) ? "" : "/api/v5";

		if (url.equals(GitoscConstants.DEFAULT_GITOSC_HOST)) {
			return url + API_SUFFIX;
		}
		else if (url.equals(GitoscConstants.DEFAULT_GITOSC_HOST + API_SUFFIX)) {
			return url;
		}
		else{
			// have no custom GitOSC url yet.
			return GitoscConstants.DEFAULT_GITOSC_HOST + API_SUFFIX;
		}
	}

	/**
	 * 是否GitOSC仓库地址
	 * */
	public static boolean isGitoscUrl(String url) {
		return isGitoscUrl(url, getHost());
	}

	public static boolean isGitoscUrl(String url, String host) {
		host = getHostFromUrl(host);
		url = removeProtocolPrefix(url);
		return StringUtils.startsWithIgnoreCase(url, host)
			&& !(url.length() > host.length() && ":/".indexOf(url.charAt(host.length())) == -1);
	}

	/**
	 * assumed isGitoscUrl(remoteUrl)
	 *
	 * git@git.oschina.net:user/repo.git -> user/repo
	 */

	public static GitoscFullPath getUserAndRepositoryFromRemoteUrl(String remoteUrl) {
		remoteUrl = removeProtocolPrefix(removeEndingDotGit(remoteUrl));
		int index1 = remoteUrl.lastIndexOf('/');
		if (index1 == -1) {
			return null;
		}
		String url = remoteUrl.substring(0, index1);
		int index2 = Math.max(url.lastIndexOf('/'), url.lastIndexOf(':'));
		if (index2 == -1) {
			return null;
		}
		final String username = remoteUrl.substring(index2 + 1, index1);
		final String reponame = remoteUrl.substring(index1 + 1);
		if (username.isEmpty() || reponame.isEmpty()) {
			return null;
		}
		return new GitoscFullPath(username, reponame);
	}


	private static String removeEndingDotGit(String url) {
		url = removeTrailingSlash(url);
		final String DOT_GIT = ".git";
		if (url.endsWith(DOT_GIT)) {
			return url.substring(0, url.length() - DOT_GIT.length());
		}
		return url;
	}

	/**
	 * assumed isGitoscUrl(remoteUrl)
	 *
	 * git@git.oschina.net:user/repo -> https://git.oschina.net/user/repo
	 */

	public static String makeGitoscRepoUrlFromRemoteUrl(String remoteUrl) {
		return makeGitoscRepoUrlFromRemoteUrl(remoteUrl, getGitoscHost());
	}


	public static String makeGitoscRepoUrlFromRemoteUrl(String remoteUrl, String host) {
		GitoscFullPath repo = getUserAndRepositoryFromRemoteUrl(remoteUrl);
		if (repo == null) {
			return null;
		}
		return host + '/' + repo.getUser() + '/' + repo.getRepository();
	}


	public static String getCloneUrl(GitoscFullPath path) {
		return getCloneUrl(path.getUser(), path.getRepository());
	}


	public static String getCloneUrl(String user, String repo) {
		if (true) {
			return "git@" + getGitHostWithoutProtocol() + ":" + user + "/" + repo + ".git";
		}
		else {
			return getApiProtocol() + getGitHostWithoutProtocol() + "/" + user + "/" + repo + ".git";
		}
	}
}

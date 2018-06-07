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
import com.surenpi.gitee.client.StringUtils;

/**
 * @author Yuyou Chow
 *
 * Based on https://github.com/JetBrains/intellij-community/blob/master/plugins/github/src/org/jetbrains/plugins/github/util/GithubAuthData.java
 * @author JetBrains s.r.o.
 * @author Aleksey Pivovarov
 */
public class GitoscAuthData {
	public enum AuthType {
		SESSION, BASIC, TOKEN, ANONYMOUS
	}

	 private final AuthType myAuthType;
	 private final String myHost;
	private final BasicAuth myBasicAuth;
	private final TokenAuth myTokenAuth;
	private final SessionAuth mySessionAuth;

	private final boolean myUseProxy;

	private GitoscAuthData( AuthType authType,
	                        String host,
	                       BasicAuth basicAuth,
	                       TokenAuth tokenAuth,
	                       SessionAuth sessionAuth,
	                       boolean useProxy) {
		myAuthType = authType;
		myHost = host;
		myBasicAuth = basicAuth;
		myTokenAuth = tokenAuth;
		mySessionAuth = sessionAuth;
		myUseProxy = useProxy;
	}


	public AuthType getAuthType() {
		return myAuthType;
	}


	public String getHost() {
		return myHost;
	}


	public BasicAuth getBasicAuth() {
		return myBasicAuth;
	}


	public TokenAuth getTokenAuth() {
		return myTokenAuth;
	}


	public SessionAuth getSessionAuth() {
		return mySessionAuth;
	}

	public boolean isUseProxy() {
		return myUseProxy;
	}

	public void setAccessToken(String token){
		if(mySessionAuth != null){
			mySessionAuth.setAccessToken(token);
		}
	}

	//============================================================
	// create auths
	//============================================================
	public static GitoscAuthData createFromSettings(){
		return null;
	}

	public static GitoscAuthData createAnonymous(){
		return createAnonymous(GitoscConstants.DEFAULT_GITOSC_HOST);
	}

	public static GitoscAuthData createAnonymous( String host){
		return new GitoscAuthData(AuthType.ANONYMOUS, host, null, null, null, true);
	}

	public static GitoscAuthData createSessionAuth( String host,  String login,  String password) {
		return new GitoscAuthData(AuthType.SESSION, host, null, null, new SessionAuth(login, password, null), true);
	}

	public static GitoscAuthData createSessionAuth( String host,  String login,  String password, String accessToken) {
		return new GitoscAuthData(AuthType.SESSION, host, null, null, new SessionAuth(login, password, accessToken), true);
	}

	public static GitoscAuthData createTokenAuth( String host,  String token) {
		return new GitoscAuthData(AuthType.TOKEN, host, null, new TokenAuth(token), null,true);
	}

	public static GitoscAuthData createTokenAuth( String host,  String token, boolean useProxy) {
		return new GitoscAuthData(AuthType.TOKEN, host, null, new TokenAuth(token), null, useProxy);
	}

	//============================================================
	// static classes
	//============================================================

	public static class BasicAuth {
		 private final String myLogin;
		 private final String myPassword;
		private final String myCode;

		private BasicAuth( String login,  String password) {
			this(login, password, null);
		}

		private BasicAuth( String login,  String password, String code) {
			myLogin = login;
			myPassword = password;
			myCode = code;
		}


		public String getLogin() {
			return myLogin;
		}


		public String getPassword() {
			return myPassword;
		}


		public String getCode() {
			return myCode;
		}
	}

	public static class TokenAuth {
		 private final String myToken;

		private TokenAuth( String token) {
			myToken = StringUtils.trim(token);
		}


		public String getToken() {
			return myToken;
		}
	}

	public static class SessionAuth {
		 private final String myLogin;
		 private final String myPassword;

		private String myAccessToken;

		 private boolean myTryGetNewAccessToken = true;

		private SessionAuth( String login,  String password, String accessToken) {
			myLogin = login;
			myPassword = password;
			myAccessToken = accessToken;
		}


		public String getLogin() {
			return myLogin;
		}


		public String getPassword() {
			return myPassword;
		}


		public String getAccessToken() {
			return myAccessToken;
		}

		private void setAccessToken( String myAccessToken) {
			this.myAccessToken = myAccessToken;
		}


		public boolean isTryGetNewAccessToken() {
			return StringUtils.isEmptyOrSpaces(myLogin) && StringUtils.isEmptyOrSpaces(myPassword) && myTryGetNewAccessToken;
		}

		public void setTryGetNewAccessToken( boolean myTryGetNewAccessToken) {
			this.myTryGetNewAccessToken = myTryGetNewAccessToken;
		}
	}
}

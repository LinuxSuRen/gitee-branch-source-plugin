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






import java.util.Date;

@SuppressWarnings("UnusedDeclaration")
public class GitoscUser {
	 private String login;
	private Long id;

	private String url;
	 private String htmlUrl;

	private Integer followers;
	private Integer following;
	private String avatarUrl;
	private String blog;

	private Date createdAt;


	public String getLogin() {
		// compati
		return login == null ? username : login;
	}


	public String getHtmlUrl() {
		return htmlUrl;
	}


	public String getAvatarUrl() {
		return avatarUrl;
	}

	// v3 session api
	private String username;

//	 private final String myLogin;
//	 private final String myHtmlUrl;
//	 private final String myAvatarUrl;
//
//	public GitoscUser( String myLogin,  String myHtmlUrl,  String myAvatarUrl) {
//		this.myLogin = myLogin;
//		this.myHtmlUrl = myHtmlUrl;
//		this.myAvatarUrl = myAvatarUrl;
//	}

//
//	public String getLogin() {
//		return myLogin;
//	}
//
//
//	public String getHtmlUrl() {
//		return myHtmlUrl;
//	}
//
//
//	public String getAvatarUrl() {
//		return myAvatarUrl;
//	}
}

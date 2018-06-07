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


@SuppressWarnings("UnusedDeclaration")
public class GitoscUserDetailed extends GitoscUser{
	private String name;
	private String email;
	private String type;
	private String weibo;
	private String bio;

	private String privateToken;

	private Integer publicRepos;
	private Integer publicGists;

	private Integer totalPrivateRepos;
	private Integer ownedPrivateRepos;

	private Integer privateGists;

	private UserAddress address;

	public static class UserAddress {
		private String name;
		private String tel;
		private String address;
		private String province;
		private String city;
		private String zipCode;
		private String comment;
	}

	
	public String getName() {
		return name;
	}

	
	public String getEmail() {
		return email;
	}

	// v3 session api
//	 public Long id;
//	 public String username;
//	 public String name;
//	 public String bio;
//	 public String weibo;
//	 public String blog;
	private Integer themeId;
	private String state;
//	 public Date createdAt;
	private String portrait;
//	 public String email;
	private String newPortrait;
	private Follow follow;
//	 public String privateToken;
	private Boolean isAdmin;
	private Boolean canCreateGroup;
	private Boolean canCreateProject;
	private Boolean canCreateTeam;


	public String getPrivateToken() {
		return privateToken;
	}

	
	public static class Follow{
		private Long followers;
		private Long starred;
		private Long following;
		private Long watched;

		public Long getFollowers() {
			return followers;
		}
		public Long getStarred() {
			return starred;
		}
		public Long getFollowing() {
			return following;
		}
		public Long getWatched() {
			return watched;
		}
	}

//	 private final String myName;
//	 private final String myEmail;
//	 private final String myPrivateToken;

//	public GitoscUserDetailed( String myLogin,
//	                         String myHtmlUrl,
//	                           String myAvatarUrl,
//	                           String name,
//	                           String email,
//	                           String privateToken) {
//		super(myLogin, myHtmlUrl, myAvatarUrl);
//
//		myName = name;
//		myEmail = email;
//		myPrivateToken = privateToken;
//	}

}

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

import com.google.gson.annotations.SerializedName;
import com.surenpi.gitee.client.GitoscFullPath;
import com.surenpi.gitee.client.StringUtils;


import java.util.Date;


@SuppressWarnings("UnusedDeclaration")
public class GitoscRepo {
	private Long id;
	 private String name;
	private String fullName;
	private String description;


	 private Boolean isPrivate;

	 private Boolean isFork;

	private String path;
	private String url;

	 private String htmlUrl;

	private String forksUrl;
	private String keysUrl;
	private String collaboratorsUrl;
	private String hooksUrl;
	private String branchesUrl;
	private String tagsUrl;
	private String blobsUrl;
	private String stargazersUrl;
	private String contributorsUrl;
	private String commitsUrl;
	private String commentsUrl;
	private String issueCommentUrl;
	private String issuesUrl;
	private String pullsUrl;
	private String milestonesUrl;
	private String notificationsUrl;
	private String labelsUrl;
	private String releasesUrl;

	private Boolean recommand;

	private String homepage;
	private String language;

	private Integer forksCount;
	private Integer stargazersCount;
	private Integer watchersCount;
	private Integer openIssuesCount;

	private String masterBranch;
	private String defaultBranch;

	private Boolean hasIssues;
	private Boolean hasWiki;
	private Boolean hasDownloads;
	private Boolean hasPage;

	private Boolean pullRequestsEnabled;
	private String license;

	 private GitoscUser owner;

	private String pass;

	private Boolean stared;
	private Boolean watched;

	private Permission permisson;

	private Date pushedAt;
	private Date createdAt;
	private Date updatedAt;


	public String getName() {
		return name;
	}


	public String getDescription() {
		return StringUtils.notNullize(description);
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public boolean isFork() {
		return isFork;
	}


	public String getHtmlUrl() {
		return htmlUrl;
	}


	public String getDefaultBranch() {
		return defaultBranch;
	}


	public GitoscUser getOwner() {
		return owner;
	}


	public String getUserName() {
		return getOwner().getLogin();
	}


	public String getFullName() {
		return getUserName() + "/" + getName();
	}


	public GitoscFullPath getFullPath() {
		return new GitoscFullPath(getUserName(), getName());
	}


	
	public static class Permission{
		private Boolean pull;
		private Boolean push;
		private Boolean admin;
	}

	// v3 session api
	 private final String myName;
	 private final String myDesc;

	private final boolean myIsPublic;
	private final boolean myIsFork;

	 private final String myPath;
	 private final String myPathWithNamespace;

	 private final String myDefaultBranch;

	 private final GitoscUser myOwner;

	public GitoscRepo( String myName,
	                   String myDesc,
	                  boolean myIsPublic,
	                  boolean myIsFork,
	                   String myPath,
	                   String myPathWithNamespace,
	                   String myDefaultBranch,
	                   GitoscUser myOwner) {

		this.myName = myName;
		this.myDesc = myDesc;
		this.myIsPublic = myIsPublic;
		this.myIsFork = myIsFork;
		this.myPath = myPath;
		this.myPathWithNamespace = myPathWithNamespace;
		this.myDefaultBranch = myDefaultBranch;
		this.myOwner = myOwner;
	}

//
//	public String getName() {
//		return myName;
//	}

//
//	public String getDesc() {
//		return myDesc;
//	}

	public boolean isPublic() {
		return myIsPublic;
	}

//	public boolean isFork() {
//		return myIsFork;
//	}


	public String getPath() {
		return myPath;
	}


	public String getPathWithNamespace() {
		return myPathWithNamespace;
	}

//
//	public String getDefaultBranch() {
//		return myDefaultBranch;
//	}

//
//	public GitoscUser getOwner() {
//		return myOwner;
//	}

//
//	public String getUserName(){
//		return getOwner().getLogin();
//	}

//
//	public GitoscFullPath getFullPath(){
//		String[] paths = getPathWithNamespace().split("/");
//		return new GitoscFullPath(paths[0], paths[1]);
//	}

//
//	public String getHtmlUrl(){
//		String[] paths = getPathWithNamespace().split("/");
//		if(paths.length == 2){
//			return GitoscUrlUtil.getApiProtocol() + GitoscUrlUtil.getGitHostWithoutProtocol() + "/" + paths[0] + "/" + paths[1] + ".git";
//		}else{
//			return GitoscUrlUtil.getApiProtocol() + GitoscUrlUtil.getGitHostWithoutProtocol() + "/" + getUserName() + "/" + paths[0] + ".git";
//		}
//	}
}

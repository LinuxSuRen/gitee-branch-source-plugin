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
public class GitoscRepoDetailed extends GitoscRepo {
	private GitoscRepo parent;
	private GitoscRepo source;


  private final Long myParentId;

	public GitoscRepoDetailed( String name,
	                           String description,
	                          boolean isPrivate,
	                          boolean isFork,
	                           String htmlUrl,
	                           String cloneUrl,
	                           String defaultBranch,
	                           GitoscUser owner,
	                           Long parentId) {
		super(name, description, isPrivate, isFork, htmlUrl, cloneUrl, defaultBranch, owner);
		myParentId = parentId;
	}


	public Long getParentId() {
		return myParentId;
	}


	public GitoscRepo getParent() {
		return parent;
	}


	public GitoscRepo getSource() {
		return source;
	}
}

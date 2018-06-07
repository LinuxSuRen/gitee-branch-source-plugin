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


/**
 * @author Yuyou Chow
 *
 * Based on https://github.com/JetBrains/intellij-community/blob/master/plugins/github/src/org/jetbrains/plugins/github/api/GithubFullPath.java
 * @author JetBrains s.r.o.
 * @author Aleksey Pivovarov
 */
public class GitoscFullPath {
 private final String myUserName;
 private final String myRepositoryName;

	public GitoscFullPath(String myUserName, String myRepositoryName) {
		this.myUserName = myUserName;
		this.myRepositoryName = myRepositoryName;
	}


	public String getUser() {
		return myUserName;
	}


	public String getRepository() {
		return myRepositoryName;
	}


	public String getFullName(){
		return myUserName + "/" + myRepositoryName;
	}

	@Override
	public String toString() {
		return "'" + getFullName() + "'";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GitoscFullPath that = (GitoscFullPath)o;

		if (!StringUtils.equalsIgnoreCase(myRepositoryName, that.myRepositoryName)) return false;
		if (!StringUtils.equalsIgnoreCase(myUserName, that.myUserName)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = myUserName.hashCode();
		result = 31 * result + myRepositoryName.hashCode();
		return result;
	}

}

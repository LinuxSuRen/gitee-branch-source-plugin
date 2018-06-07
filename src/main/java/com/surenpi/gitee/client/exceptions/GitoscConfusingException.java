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

package com.surenpi.gitee.client.exceptions;

import java.io.IOException;

/**
 * @author Yuyou Chow
 *
 * Based on https://github.com/JetBrains/intellij-community/blob/master/plugins/github/src/org/jetbrains/plugins/github/exceptions/GithubConfusingException.java
 * @author JetBrains s.r.o.
 * @author Aleksey Pivovarov
 */
public class GitoscConfusingException extends IOException {
	private String myDetails;

	public GitoscConfusingException() {
	}

	public GitoscConfusingException(String message) {
		super(message);
	}

	public GitoscConfusingException(String message, Throwable cause) {
		super(message, cause);
	}

	public GitoscConfusingException(Throwable cause) {
		super(cause);
	}

	public void setDetails(String myDetails) {
		this.myDetails = myDetails;
	}

	@Override
	public String getMessage() {
		if(myDetails == null){
			return super.getMessage();
		}else{
			return myDetails + "\n\n" + super.getMessage();
		}
	}
}

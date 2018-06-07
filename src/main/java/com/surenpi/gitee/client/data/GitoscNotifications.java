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

import com.surenpi.gitee.client.exceptions.GitoscOperationCanceledException;
import org.apache.http.client.ClientProtocolException;

import java.awt.*;
import java.net.UnknownHostException;

/**
 * @author Yuyou Chow
 *
 * Based on https://github.com/JetBrains/intellij-community/blob/master/plugins/github/src/org/jetbrains/plugins/github/util/GithubNotifications.java
 * @author JetBrains s.r.o.
 */
public class GitoscNotifications {
	private static boolean isOperationCanceled(Exception e) {
		return e instanceof GitoscOperationCanceledException;
	}


	public static String getErrorTextFromException(Exception e) {
		if (e instanceof UnknownHostException) {
			return "Unknown host: " + e.getMessage();
		}else if(e instanceof ClientProtocolException){
			return e.getCause().getMessage();
		}
		return e.getMessage();
	}
}

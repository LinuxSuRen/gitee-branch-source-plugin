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





import java.util.List;


@SuppressWarnings("UnusedDeclaration")
public class GitoscErrorMessage {

	public String message;
	 public List<Error> errors;

	public static class Error {
		 public String resource;
		 public String field;
		 public String code;
		 public String message;
	}


	public String getMessage() {
		if (errors == null) {
			return message;
		}
		else {
			StringBuilder s = new StringBuilder();
			s.append(message);
			for (Error e : errors) {
				s.append(String.format("<br/>[%s; %s]%s: %s", e.resource, e.field, e.code, e.message));
			}
			return s.toString();
		}
	}

	public boolean containsReasonMessage( String reason) {
		if (message == null) return false;
		return message.contains(reason);
	}

	public boolean containsErrorCode( String code) {
		if (errors == null) return false;
		for (Error error : errors) {
			if (error.code != null && error.code.contains(code)) return true;
		}
		return false;
	}

	public boolean containsErrorMessage( String message) {
		if (errors == null) return false;
		for (Error error : errors) {
			if (error.code != null && error.code.contains(message)) return true;
		}
		return false;
	}
}

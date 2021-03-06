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

import com.google.common.base.Joiner;

import java.util.logging.Logger;

/**
 * @author Yuyou Chow
 * */
public class GitoscConstants {
	// Logger
	public static final Logger LOG = Logger.getLogger("gitosc");

	// Utils
	public static final Joiner JOINER = Joiner.on("&").skipNulls();

	public static final Joiner SPACE_JOINER = Joiner.on(" ").skipNulls();

	// String
	public static final String TITLE_ACCESS_TO_GITOSC = new String("gitosc.access.dialog.title");

	public static final String DEFAULT_GITOSC_HOST = "gitee.com";

	public static final String AUTH_GRANT_TYPE = "grant_type=password";
	public static final String AUTH_CLIENT_ID = "client_id=fc439d90cb2ffc20cffeb70a6a4039e69847485e0fa56cfa0d1bf006098e24dd";
	public static final String AUTH_CLIENT_SECRET = "client_secret=386f187646ee361049f69cd213424bdba5af03e820d10a68a68e5fb520902596";
}

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

import com.surenpi.gitee.client.StringUtils;

import java.util.Date;

public class GitoscIssue {
  private String url;

  private String htmlUrl;

  private String number;

  private String state;

  private String title;
  private String body;


  private GitoscUser user;
  private GitoscUser assignee;

  private Date closedAt;

  private Date createdAt;

  private Date updatedAt;


  public String getHtmlUrl() {
    return htmlUrl;
  }

  public String getNumber() {
    return number;
  }


  public String getState() {
    return state;
  }


  public String getTitle() {
    return title;
  }


  public String getBody() {
    return StringUtils.notNullize(body);
  }


  public GitoscUser getUser() {
    return user;
  }


  public GitoscUser getAssignee() {
    return assignee;
  }


  public Date getClosedAt() {
    return closedAt;
  }


  public Date getCreatedAt() {
    return createdAt;
  }


  public Date getUpdatedAt() {
    return updatedAt;
  }
}

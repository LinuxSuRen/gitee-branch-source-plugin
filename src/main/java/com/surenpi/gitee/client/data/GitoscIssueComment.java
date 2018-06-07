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
public class GitoscIssueComment {

  private Long id;

  private String url;

  private String htmlUrl;
  private String body;

  private String bodyHtml;


  private Date createdAt;

  private Date updatedAt;


  private GitoscUser user;

  public long getId() {
    return id;
  }


  public String getHtmlUrl() {
    return htmlUrl;
  }


  public String getBodyHtml() {
    return bodyHtml;
  }


  public Date getCreatedAt() {
    return createdAt;
  }


  public Date getUpdatedAt() {
    return updatedAt;
  }


  public GitoscUser getUser() {
    return user;
  }
}

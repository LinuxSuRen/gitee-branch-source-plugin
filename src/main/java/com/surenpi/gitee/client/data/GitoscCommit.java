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
import java.util.List;


@SuppressWarnings("UnusedDeclaration")
public class GitoscCommit extends GitoscCommitSha {
  private GitoscUser author;
  private GitoscUser committer;


  private GitCommit commit;


  private List<GitoscCommitSha> parents;


  public static class GitCommit {
    private String url;
  
    private String message;

  
    private GitUser author;
  
    private GitUser committer;


    public String getMessage() {
      return message;
    }


    public GitUser getAuthor() {
      return author;
    }


    public GitUser getCommitter() {
      return committer;
    }
  }


  public static class GitUser {
  
    private String name;
  
    private String email;
  
    private Date date;


    public String getName() {
      return name;
    }


    public String getEmail() {
      return email;
    }


    public Date getDate() {
      return date;
    }
  }


  public GitoscUser getAuthor() {
    return author;
  }


  public GitoscUser getCommitter() {
    return committer;
  }


  public List<GitoscCommitSha> getParents() {
    return parents;
  }


  public GitCommit getCommit() {
    return commit;
  }
}

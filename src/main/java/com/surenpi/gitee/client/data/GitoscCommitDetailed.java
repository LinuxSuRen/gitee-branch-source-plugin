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
public class GitoscCommitDetailed extends GitoscCommit {

  private CommitStats stats;

  private List<GitoscFile> files;

  
  public static class CommitStats {
  
    private Integer additions;
  
    private Integer deletions;
  
    private Integer total;

    public int getAdditions() {
      return additions;
    }

    public int getDeletions() {
      return deletions;
    }

    public int getTotal() {
      return total;
    }
  }


  public CommitStats getStats() {
    return stats;
  }


  public List<GitoscFile> getFiles() {
    return files;
  }
}

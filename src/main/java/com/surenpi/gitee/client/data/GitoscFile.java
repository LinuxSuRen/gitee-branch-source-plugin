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

public class GitoscFile {

  private String filename;


  private Integer additions;

  private Integer deletions;

  private Integer changes;

  private String status;

  private String rawUrl;
  private String blobUrl;

  private String patch;

  public String getFilename() {
    return filename;
  }

  public int getAdditions() {
    return additions;
  }

  public int getDeletions() {
    return deletions;
  }

  public int getChanges() {
    return changes;
  }


  public String getStatus() {
    return status;
  }


  public String getRawUrl() {
    return rawUrl;
  }


  public String getPatch() {
    return patch;
  }
}

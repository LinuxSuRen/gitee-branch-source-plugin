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

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedDeclaration")
public class GitoscGist {

  private String id;
  private String description;



  private Boolean isPublic;

  private String url;

  private String htmlUrl;
  private String gitPullUrl;
  private String gitPushUrl;


  private Map<String, GistFile> files;

  private GitoscUser owner;

  private Date createdAt;


  public static class GistFile {
    private Long size;
  
    private String filename;
  
    private String content;

  
    private String raw_url;

    private String type;
    private String language;

  
    public String getFilename() {
      return filename;
    }

  
    public String getContent() {
      return content;
    }

  
    public String getRawUrl() {
      return raw_url;
    }
  }


  public String getId() {
    return id;
  }


  public String getDescription() {
    return description;
  }

  public boolean isPublic() {
    return isPublic;
  }


  public String getHtmlUrl() {
    return htmlUrl;
  }


  public List<GistFile> getFiles() {
    return new ArrayList<>(files.values());
  }


  public GitoscUser getUser() {
    return owner;
  }
}

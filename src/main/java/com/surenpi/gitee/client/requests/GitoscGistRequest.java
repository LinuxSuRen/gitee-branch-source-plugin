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
package com.surenpi.gitee.client.requests;

import com.google.gson.annotations.SerializedName;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration", "MismatchedQueryAndUpdateOfCollection"})
public class GitoscGistRequest {

  private final String description;

  private final Map<String, GistFile> files;


  private final boolean isPublic;

  public static class GistFile {

    private final String content;

    public GistFile( String content) {
      this.content = content;
    }
  }

  public GitoscGistRequest( List<FileContent> files,  String description, boolean isPublic) {
    this.description = description;
    this.isPublic = isPublic;

    this.files = new HashMap<>();
    for (FileContent file : files) {
      this.files.put(file.getFileName(), new GistFile(file.getContent()));
    }
  }

  public static class FileContent {

    private final String myFileName;

    private final String myContent;

    public FileContent( String fileName,  String content) {
      myFileName = fileName;
      myContent = content;
    }


    public String getFileName() {
      return myFileName;
    }


    public String getContent() {
      return myContent;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      FileContent that = (FileContent)o;

      if (!myContent.equals(that.myContent)) return false;
      if (!myFileName.equals(that.myFileName)) return false;

      return true;
    }

    @Override
    public int hashCode() {
      int result = myFileName.hashCode();
      result = 31 * result + myContent.hashCode();
      return result;
    }
  }
}

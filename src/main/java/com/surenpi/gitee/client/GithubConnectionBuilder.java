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

import com.surenpi.gitee.client.data.GitoscAuthData;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Yuyou Chow
 *

 * */
class GithubConnectionBuilder {

  private final GitoscAuthData myAuth;

  private final String myApiURL;

  public GithubConnectionBuilder(GitoscAuthData auth, String apiURL) {
    myAuth = auth;
    myApiURL = apiURL;
  }


  public CloseableHttpClient createClient() {
    HttpClientBuilder builder = HttpClients.custom();

    builder
      .setDefaultRequestConfig(createRequestConfig())
      .setDefaultConnectionConfig(createConnectionConfig())
      .setDefaultHeaders(createHeaders())
//      .setSslcontext(CertificateManager.getInstance().getSslContext())
    ;

    setupCredentialsProvider(builder);

    return builder.build();
  }


  private RequestConfig createRequestConfig() {
    RequestConfig.Builder builder = RequestConfig.custom();

    int timeout = 1000; // TODO ß
    builder.setConnectTimeout(timeout).setSocketTimeout(timeout);

    if (myAuth.isUseProxy()) {
//      IdeHttpClientHelpers.ApacheheHttpClient4.setProxyForUrlIfEnabled(builder, myApiURL);
    }

    return builder.build();
  }


  private ConnectionConfig createConnectionConfig() {
    return ConnectionConfig.custom()
      .setCharset(Consts.UTF_8)
      .build();
  }



  private CredentialsProvider setupCredentialsProvider( HttpClientBuilder builder) {
    CredentialsProvider provider = new BasicCredentialsProvider();

    // Basic authentication
    GitoscAuthData.BasicAuth basicAuth = myAuth.getBasicAuth();
    if (basicAuth != null) {
      AuthScope authScope = getBasicAuthScope();

      provider.setCredentials(authScope, new UsernamePasswordCredentials(basicAuth.getLogin(), basicAuth.getPassword()));
      builder.addInterceptorFirst(new PreemptiveBasicAuthInterceptor(authScope));
    }
    builder.setDefaultCredentialsProvider(provider);

    if (myAuth.isUseProxy()) {
//      IdeHttpClientHelpers.ApacheHttpClient4.setProxyCredentialsForUrlIfEnabled(provider, myApiURL);
    }

    return provider;
  }


  private AuthScope getBasicAuthScope() {
    try {
      URIBuilder builder = new URIBuilder(myApiURL);
      return new AuthScope(builder.getHost(), builder.getPort(), AuthScope.ANY_REALM, AuthSchemes.BASIC);
    }
    catch (URISyntaxException e) {
      return AuthScope.ANY;
    }
  }


  private Collection<? extends Header> createHeaders() {
    List<Header> headers = new ArrayList<>();

    headers.add(new BasicHeader("User-Agent", "CC/1.0-JB." + "suren"));

    // TODO for look like github
    GitoscAuthData.TokenAuth tokenAuth = myAuth.getTokenAuth();
    if (tokenAuth != null) {
      headers.add(new BasicHeader("Authorization", "token " + tokenAuth.getToken()));
    }

    return headers;
  }

  private static class PreemptiveBasicAuthInterceptor implements HttpRequestInterceptor {

    private final AuthScope myBasicAuthScope;

    public PreemptiveBasicAuthInterceptor( AuthScope basicAuthScope) {
      myBasicAuthScope = basicAuthScope;
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
      CredentialsProvider provider = (CredentialsProvider)context.getAttribute(HttpClientContext.CREDS_PROVIDER);
      Credentials credentials = provider.getCredentials(myBasicAuthScope);
      if (credentials != null) {
        request.addHeader(new BasicScheme(Consts.UTF_8).authenticate(credentials, request, context));
      }
    }
  }
}

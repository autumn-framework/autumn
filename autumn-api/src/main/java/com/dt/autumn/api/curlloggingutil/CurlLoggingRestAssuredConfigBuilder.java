package com.dt.autumn.api.curlloggingutil;


import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Builds `RestAssuredConfig` that allows REST-assured to logs each HTTP request as CURL command.
 */
public class CurlLoggingRestAssuredConfigBuilder {

    private final CurlLoggingInterceptor.Builder interceptorBuilder;
    private final RestAssuredConfig config;

    public CurlLoggingRestAssuredConfigBuilder(RestAssuredConfig config, Boolean captureAPIDetails) {
        this.config = config;
        this.interceptorBuilder = CurlLoggingInterceptor.defaultBuilder(captureAPIDetails);
    }

    public CurlLoggingRestAssuredConfigBuilder(Boolean captureAPIDetails) {
        this(RestAssured.config(), captureAPIDetails);
    }


    /**
     * Configures {@link RestAssuredConfig} to print a stacktrace where curl command has been
     * generated.
     */
    public CurlLoggingRestAssuredConfigBuilder logStacktrace() {
        interceptorBuilder.logStacktrace();
        return this;
    }

    /**
     * Configures {@link RestAssuredConfig} to not print a stacktrace where curl command has
     * been generated.
     */
    public CurlLoggingRestAssuredConfigBuilder dontLogStacktrace() {
        interceptorBuilder.dontLogStacktrace();
        return this;
    }

    /**
     * Configures {@link RestAssuredConfig} to print a curl command in multiple lines.
     */
    public CurlLoggingRestAssuredConfigBuilder printMultiliner() {
        interceptorBuilder.printMultiliner();
        return this;
    }

    /**
     * Configures {@link RestAssuredConfig} to print a curl command in a single line.
     */
    public CurlLoggingRestAssuredConfigBuilder printSingleliner() {
        interceptorBuilder.printSingleliner();
        return this;
    }

    public RestAssuredConfig build() {
        return config
                .httpClient(HttpClientConfig.httpClientConfig()
                        .reuseHttpClientInstance()
                        .httpClientFactory(new MyHttpClientFactory(interceptorBuilder.build())));

    }

    private static class MyHttpClientFactory implements HttpClientConfig.HttpClientFactory {

        private final CurlLoggingInterceptor curlLoggingInterceptor;

        public MyHttpClientFactory(CurlLoggingInterceptor curlLoggingInterceptor) {
            this.curlLoggingInterceptor = curlLoggingInterceptor;
        }

        @Override
        public HttpClient createHttpClient() {
            AbstractHttpClient client = new DefaultHttpClient();
            client.addRequestInterceptor(curlLoggingInterceptor);
            return client;
        }
    }

}

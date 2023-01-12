/**
 * MIT License
 *
 * Copyright (c) 2023 Shunyi Chen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.shunyi.cloud.pandanus.siteapi.controller;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.containsString;


/**
 * Integration tests for resource server.
 *
 * @author Shunyi Chen
 */
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "36000")
@ActiveProfiles("test")
public class ServerSiteControllerTests {
    Consumer<HttpHeaders> noScopesToken = (http) -> http.setBearerAuth(
            "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzdWJqZWN0IiwiZXhwIjo0NjgzODA1MTI4fQ.ULEPdHG-MK5GlrTQMhgqcyug2brTIZaJIrahUeq9zaiwUSdW83fJ7W1IDd2Z3n4a25JY2uhEcoV95lMfccHR6y_2DLrNvfta22SumY9PEDF2pido54LXG6edIGgarnUbJdR4rpRe_5oRGVa8gDx8FnuZsNv6StSZHAzw5OsuevSTJ1UbJm4UfX3wiahFOQ2OI6G-r5TB2rQNdiPHuNyzG5yznUqRIZ7-GCoMqHMaC-1epKxiX8gYXRROuUYTtcMNa86wh7OVDmvwVmFioRcR58UWBRoO1XQexTtOQq_t8KYsrPZhb9gkyW8x2bAQF-d0J0EJY8JslaH6n4RBaZISww");

    Consumer<HttpHeaders> messageReadToken = (http) -> http.setBearerAuth(
            "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzdWJqZWN0Iiwic2NvcGUiOiJtZXNzYWdlOnJlYWQiLCJleHAiOjQ2ODM4MDUxNDF9.h-j6FKRFdnTdmAueTZCdep45e6DPwqM68ZQ8doIJ1exi9YxAlbWzOwId6Bd0L5YmCmp63gGQgsBUBLzwnZQ8kLUgUOBEC3UzSWGRqMskCY9_k9pX0iomX6IfF3N0PaYs0WPC4hO1s8wfZQ-6hKQ4KigFi13G9LMLdH58PRMK0pKEvs3gCbHJuEPw-K5ORlpdnleUTQIwINafU57cmK3KocTeknPAM_L716sCuSYGvDl6xUTXO7oPdrXhS_EhxLP6KxrpI1uD4Ea_5OWTh7S0Wx5LLDfU6wBG1DowN20d374zepOIEkR-Jnmr_QlR44vmRqS5ncrF-1R0EGcPX49U6A");

    @Autowired
    private WebTestClient rest;

    @Test
    void getWhenValidBearerTokenThenAllows() {
        // @formatter:off
        this.rest.get().uri("/")
                .headers(this.noScopesToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Hello, subject!");
        // @formatter:on
    }

    // -- tests with scopes

    @Test
    void getWhenValidBearerTokenThenScopedRequestsAlsoWork() {
        // @formatter:off
        this.rest.get().uri("/message")
                .headers(this.messageReadToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("secret message");
        // @formatter:on
    }

    @Test
    void getWhenInsufficientlyScopedBearerTokenThenDeniesScopedMethodAccess() {
        // @formatter:off
        this.rest.get().uri("/message")
                .headers(this.noScopesToken)
                .exchange()
                .expectStatus().isForbidden()
                .expectHeader().value(HttpHeaders.WWW_AUTHENTICATE, containsString("Bearer error=\"insufficient_scope\""));
        // @formatter:on
    }

}

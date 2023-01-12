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

import com.shunyi.cloud.pandanus.siteapi.config.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

/**
 * SiteController tests
 *
 * @author Shunyi Chen
 */
@WebFluxTest(SiteController.class)
@Import(SecurityConfiguration.class)
public class SiteControllerTests {

    @Autowired
    WebTestClient rest;

    @MockBean
    ReactiveJwtDecoder jwtDecoder;

    @Test
    void indexGreetsAuthenticatedUser() {
        // @formatter:off
        this.rest.mutateWith(mockJwt().jwt((jwt) -> jwt.subject("test-subject")))
                .get()
                .uri("/")
                .exchange()
                .expectBody(String.class).isEqualTo("Hello, test-subject!");
        // @formatter:on
    }

    @Test
    void messageCanBeReadWithScopeMessageReadAuthority() {
        // @formatter:off
        this.rest.mutateWith(mockJwt().jwt((jwt) -> jwt.claim("realm_access", "message_read")))
                .get()
                .uri("/message")
                .exchange()
                .expectBody(String.class).isEqualTo("secret message");

        this.rest.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_message_read")))
                .get()
                .uri("/message")
                .exchange()
                .expectBody(String.class).isEqualTo("secret message");
        // @formatter:on
    }

    @Test
    void messageCanNotBeReadWithoutScopeMessageReadAuthority() {
        // @formatter:off
        this.rest.mutateWith(mockJwt())
                .get()
                .uri("/message")
                .exchange()
                .expectStatus().isForbidden();
        // @formatter:on
    }

    @Test
    void messageCanNotBeCreatedWithoutAnyScope() {
        Jwt jwt = jwt().claim("scope", "").build();
        when(this.jwtDecoder.decode(anyString())).thenReturn(Mono.just(jwt));
        // @formatter:off
        this.rest.post()
                .uri("/message")
                .headers((headers) -> headers.setBearerAuth(jwt.getTokenValue()))
                .bodyValue("Hello message")
                .exchange()
                .expectStatus().isForbidden();
        // @formatter:on
    }

    @Test
    void messageCanNotBeCreatedWithScopeMessageReadAuthority() {
        Jwt jwt = jwt().claim("scope", "message:read").build();
        when(this.jwtDecoder.decode(anyString())).thenReturn(Mono.just(jwt));
        // @formatter:off
        this.rest.post()
                .uri("/message")
                .headers((headers) -> headers.setBearerAuth(jwt.getTokenValue()))
                .bodyValue("Hello message")
                .exchange()
                .expectStatus().isForbidden();
        // @formatter:on
    }

    @Test
    void messageCanBeCreatedWithScopeMessageWriteAuthority() {
        Jwt jwt = jwt().claim("scope", "message:write").build();
        when(this.jwtDecoder.decode(anyString())).thenReturn(Mono.just(jwt));
        // @formatter:off
        this.rest.post()
                .uri("/message")
                .headers((headers) -> headers.setBearerAuth(jwt.getTokenValue()))
                .bodyValue("Hello message")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Message was created. Content: Hello message");
        // @formatter:on
    }

    private Jwt.Builder jwt() {
        return Jwt.withTokenValue("token").header("alg", "none");
    }

}

package com.shunyi.cloud.pandanus.gateway.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthController {

    @GetMapping(value = "/token")
    public Mono<String> getHome(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        return Mono.just(authorizedClient.getAccessToken().getTokenValue());
    }

//    @GetMapping("/")
//    public Mono<String> index(WebSession session) {
//        return Mono.just(session.getId());
//    }
//
//    @GetMapping(value = "/sso-logout")
//    public Mono<String> ssoLogout(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient, WebSession session) {
//        return Mono.just("logged out.");
//    }
}

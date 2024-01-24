package com.example.application.services;

import com.example.application.entities.User;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import dev.hilla.BrowserCallable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Optional;

@BrowserCallable
@AnonymousAllowed
public class UserAuthenticationService {

    @Autowired
    private AuthenticationContext authenticationContext;

    public Optional<User> getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof OidcUser oidcUser) {
            OidcUserInfo userInfo = oidcUser.getUserInfo();
            ArrayList<String> roles = new ArrayList<>();
            if (oidcUser.hasClaim("roles")) {
                roles.addAll(oidcUser.getClaim("roles"));
            }
            return Optional.of(new User(userInfo, roles));
        }
        return Optional.empty();
    }

    public String getLogoutUrl() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof OidcUser user) {
            return UriComponentsBuilder
                    .fromUriString(user.getIssuer() + "/protocol/openid-connect/logout")
                    .queryParam("id_token_hint", user.getIdToken().getTokenValue())
                    .queryParam("post_logout_redirect_uri", "http://localhost:8080").toUriString();
        }
        return "/logout";
    }
}

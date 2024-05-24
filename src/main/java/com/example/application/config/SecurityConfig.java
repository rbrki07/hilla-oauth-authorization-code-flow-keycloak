package com.example.application.config;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setOAuth2LoginPage(http, "/oauth2/authorization/keycloak");
        http.logout(logout -> logout.logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))));
    }

    @Bean
    GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            return authorities.stream()
                .filter(authority -> OidcUserAuthority.class.isInstance(authority))
                .map(authority -> {
                    OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
                    OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();
                    List<String> roles = userInfo.getClaim("roles");
                    return roles.stream().map(r -> new SimpleGrantedAuthority(r));
                })
                .reduce(Stream.empty(), (joinedAuthorities, roleAuthorities) -> Stream.concat(joinedAuthorities, roleAuthorities))
                .collect(Collectors.toList());
        };
    }
}

package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/web/index.html", "/web/js/**", "/web/css/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/login/**", "/app/logout/**").permitAll()
                .antMatchers(HttpMethod.GET, "/web/**", "/api/clients/current/**").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/transactions").hasAuthority("CLIENT")
                .antMatchers("/admin/**", "h2-console").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/**").hasAuthority("ADMIN")
                .anyRequest().denyAll();

        /*http.authorizeRequests()
                .antMatchers("/web/index.html", "/web/js/index.js", "/web/img/**", "/web/css/style.css").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients", "/api/login", "/api/logout").permitAll() // To create a client
                .antMatchers("/web/**").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET, "/api/clients/current").hasAuthority("CLIENT")
                .antMatchers("/admin/**", "/h2-console", "/web/**", "/api/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/**").hasAuthority("ADMIN");
                //.anyRequest().denyAll();*/

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");
        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        http.csrf().disable();

        http.headers().frameOptions().sameOrigin();

        http.exceptionHandling().authenticationEntryPoint((req,res,exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        http.formLogin().successHandler((req,res,auth) -> clearAuthenticationAttributes(req));

        http.formLogin().failureHandler((req,res,exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}

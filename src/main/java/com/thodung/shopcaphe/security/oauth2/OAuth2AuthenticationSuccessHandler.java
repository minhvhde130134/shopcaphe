package com.thodung.shopcaphe.security.oauth2;

import static com.thodung.shopcaphe.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thodung.shopcaphe.model.User;
import com.thodung.shopcaphe.security.TokenProvider;
import com.thodung.shopcaphe.security.UserPrincipal;
import com.thodung.shopcaphe.util.CookieUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private TokenProvider tokenProvider;

    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider,
            HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.tokenProvider = tokenProvider;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {

        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        String token = tokenProvider.createToken(user.getUser());
        User u = user.getUser();
        try {
            return UriComponentsBuilder.fromUriString(targetUrl).queryParam("accessToken", token)
                    .queryParam("fullName", URLEncoder.encode(u.getFullName(), "UTF-8"))
                    .queryParam("phone", u.getPhone()).queryParam("avatarLink", u.getAvatarLink())
                    .queryParam("emailVerify", true).queryParam("role", "ROLE_USER").build().toUriString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return UriComponentsBuilder.fromUriString(targetUrl).queryParam("accessToken", token)
                    .queryParam("fullName", u.getFullName()).queryParam("phone", u.getPhone())
                    .queryParam("avatarLink", u.getAvatarLink()).queryParam("emailVerify", true)
                    .queryParam("role", "ROLE_USER").build().toUriString();
        }

    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}

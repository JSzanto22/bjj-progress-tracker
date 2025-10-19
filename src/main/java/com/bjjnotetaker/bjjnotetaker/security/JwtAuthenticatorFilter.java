package com.bjjnotetaker.bjjnotetaker.security;

import com.bjjnotetaker.bjjnotetaker.domain.User;
import com.bjjnotetaker.bjjnotetaker.service.AuthService;
import com.bjjnotetaker.bjjnotetaker.service.UserService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

public class JwtAuthenticatorFilter extends OncePerRequestFilter {

  private final AuthService authService;
  private final UserService userService;

  public JwtAuthenticatorFilter(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
    throws ServletException, IOException, java.io.IOException {
    //skips any filtering when accessing public endpoints.
    String path = request.getServletPath();
    if (path.startsWith("/api/auth/")) {
      filterChain.doFilter(request, response);
      return;
    }

    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring(7);
    if (authService.validateToken(token)) {
      String username = authService.getUsernameFromToken(token);
      User user = userService.getUserByUsername(username);

      List<GrantedAuthority> authorities =
        List.of(new SimpleGrantedAuthority("ROLE_USER"));

      UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(user, null, authorities);

      SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    filterChain.doFilter(request, response);
  }

}

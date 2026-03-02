package org.example.ovr.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ovr.Entity.User;
import org.example.ovr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if(jwtProvider.validateToken(token)) {
                String email = jwtProvider.getEmail(token);
                Optional<User> userOpt = userService.findByEmail(email);
                if(userOpt.isPresent()) {
                    User user = userOpt.get();
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            user, null, List.of(new SimpleGrantedAuthority(user.getRole())));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}




/*

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        System.out.println("=== JwtAuthenticationFilter ===");
        System.out.println("Authorization header: " + header);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtProvider.validateToken(token)) {
                String email = jwtProvider.getEmail(token);
                String role = jwtProvider.getRole(token); // e.g. ROLE_ADMIN

                System.out.println("JWT valid for email: " + email + ", role: " + role);

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, List.of(authority));

                SecurityContextHolder.getContext().setAuthentication(auth);

                System.out.println("Authentication set in SecurityContext:");
                System.out.println("Principal: " + auth.getPrincipal());
                System.out.println("Authorities: " + auth.getAuthorities());
            } else {
                System.out.println("JWT invalid!");
            }
        } else {
            System.out.println("No Bearer token found in header.");
        }

        filterChain.doFilter(request, response);
    }
}*/
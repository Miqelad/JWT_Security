/**
 * JWTFilter каждого запроса будет выполняться реализация OncePerRequestFilter интерфейса
 * и проверка наличия токена-носителя в заголовке авторизации.
 * Если токен присутствует, токен будет проверен,
 * и данные аутентификации будут установлены для пользователя
 * для этого запроса путем установки свойства аутентификации SecurityContext
 * с использованием SecurityContextHolder . Именно здесь ваш JWT вступает в действие и гарантирует,
 * что вы аутентифицированы и можете получить доступ к защищенным ресурсам, которые требуют,
 * чтобы вы вошли в систему / прошли проверку подлинности.
 *
 * @author BomboRa
 */
package com.miq.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final MyUserDetailsService userDetailsService;
    private final JWTService jwtService;

    /**
     * Извлекает заголовок Authorization из Header
     * Проверяет присутствует ли заголовок в Bearer token
     * Если присутствует, то извлекаем email и создаем токен
     * так же меняет настройку аутентификации в контексте безопасности
     * с использованием созданного токена
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            if (jwt == null || jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token in Bearer Header");
            } else {
                try {
                    String email = jwtService.validateTokenAndRetrieveSubject(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException exc) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

package mg.bmoi.agiosdevise.config;

import mg.bmoi.agiosdevise.entity.UserApp;
import mg.bmoi.agiosdevise.exception.AccessDeniedException;
import mg.bmoi.agiosdevise.repository.UserAppRepository;
import mg.bmoi.agiosdevise.service.TokenBlacklistService;
import mg.bmoi.agiosdevise.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TokenAuthenticationFilter extends GenericFilter {

    private final JwtUtil jwtUtil;
    private final UserAppRepository userAppRepository;
    private final TokenBlacklistService tokenBlacklistService;

    public TokenAuthenticationFilter(JwtUtil jwtUtil, UserAppRepository userAppRepository, TokenBlacklistService tokenBlacklistService) {
        this.jwtUtil = jwtUtil;
        this.userAppRepository = userAppRepository;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = null;

        // Lecture du cookie 'jwt'
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null && jwtUtil.validateToken(token) && !tokenBlacklistService.isBlacklisted(token)) {
            String username = jwtUtil.extractUsername(token);

            UserApp user = userAppRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())
            );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
         else if (token != null && tokenBlacklistService.isBlacklisted(token)) {
            // Token blacklisté : refuse l'accès
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ((HttpServletResponse) response).getWriter().write("{\"error\": \"Token blacklisté\"}");
            return;
        }

        try {
            chain.doFilter(request, response);
        } catch (AccessDeniedException e) {
            // Log spécifique pour les accès refusés
            System.err.println("Accès refusé à " + req.getRequestURI() +
                    " - Raison: " + e.getMessage());

            // Envoyer une réponse JSON détaillée
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setContentType("application/json");
            httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpResponse.getWriter().write(String.format(
                    "{\"status\": 403, \"error\": \"Forbidden\", \"message\": \"%s\", \"path\": \"%s\"}",
                    e.getMessage(),
                    req.getRequestURI()
            ));
        }
    }
}
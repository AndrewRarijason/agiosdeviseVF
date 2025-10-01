package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.service.AuthService;
import mg.bmoi.agiosdevise.service.TokenBlacklistService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LogoutController {
    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;

    public LogoutController(AuthService authService, TokenBlacklistService tokenBlacklistService) {
        this.authService = authService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(
            @CookieValue(value = "jwt", required = false) String token) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Déconnexion réussie");

        if (token != null) {
            long expiration = authService.getJwtUtil().validateToken(token)
                    ? authService.getJwtUtil().extractExpiration(token).getTime() - System.currentTimeMillis()
                    : 0;
            tokenBlacklistService.blacklistToken(token, expiration);
        }

        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(result);
    }
}
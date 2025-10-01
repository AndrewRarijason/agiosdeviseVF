package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.entity.UserApp;
import mg.bmoi.agiosdevise.repository.UserAppRepository;
import mg.bmoi.agiosdevise.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    private final AuthService authService;
    private final UserAppRepository userAppRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    public AuthController(AuthService authService, UserAppRepository userAppRepository) {
        this.authService = authService;
        this.userAppRepository = userAppRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response) {

        Map<String, Object> authResult = authService.authenticate(username, password);

        if ((boolean) authResult.get("success")) {
            // Création du cookie JWT
            String token = (String) authResult.get("token");
            long expiration = (long) authResult.get("expiration");

            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(false) // En production, active HTTPS sinon désactive temporairement
                    .path("/")
                    .sameSite("Strict")
                    .build();

            Map<String, Object> result = new HashMap<>(authResult);
            result.remove("token");
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(result);
        } else {
            authResult.put("message", "Identifiants incorrects");
            return ResponseEntity.status(401).body(authResult);
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(
            @CookieValue(value = "jwt", required = false) String token) {
        Map<String, Object> result = new HashMap<>();
        if (token != null && authService.getJwtUtil().validateToken(token)) {
            String username = authService.getJwtUtil().extractUsername(token);
            Optional<UserApp> userOpt = userAppRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                result.put("valid", true);
                result.put("username", userOpt.get().getUsername());
                result.put("role", userOpt.get().getRole().getName());
                return ResponseEntity.ok(result);
            }
        }
        result.put("valid", false);
        return ResponseEntity.status(401).body(result);
    }

}
package mg.bmoi.agiosdevise.service;

import mg.bmoi.agiosdevise.entity.UserApp;
import mg.bmoi.agiosdevise.repository.UserAppRepository;
import mg.bmoi.agiosdevise.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final UserAppRepository userAppRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    public AuthService(UserAppRepository userAppRepository) {
        this.userAppRepository = userAppRepository;
    }

    public Map<String, Object> authenticate(String username, String password) {
        UserApp user = userAppRepository.findByUsername(username).orElse(null);

        boolean isAuthenticated = user != null
                && passwordEncoder.matches(password, user.getMdp())
                && user.getIs_active() == 1;
        logger.debug("Résultat authentification pour {}: {}", username, isAuthenticated);

        Map<String, Object> result = new HashMap<>();
        result.put("success", isAuthenticated);
        result.put("message", isAuthenticated ? "Connexion réussie" : "Échec de connexion");

        if (isAuthenticated) {
            long expiration;
            String role = user.getRole().getName();
            switch (user.getRole().getId()) {
                case 1: // ADMIN
                    expiration = 3600000L; // 1h
                    break;
                case 2: // PUPITREUR
                    expiration = 7200000L; // 2h
                    break;
                case 3: // CONSULTANT
                    expiration = 900000L; // 15mn
                    break;
                case 4: // TESTEUR
                    expiration = 3600000L; // 1h
                    break;
                default:
                    expiration = 900000L; // 15mn
            }
            String token = jwtUtil.generateToken(username, expiration, role);
            result.put("role", role);
            result.put("token", token);
            result.put("expiration", expiration);
        }

        return result;
    }

    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }
}
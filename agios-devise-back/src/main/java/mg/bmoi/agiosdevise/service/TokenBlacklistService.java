// src/main/java/mg/bmoi/agiosdevise/service/TokenBlacklistService.java
package mg.bmoi.agiosdevise.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {
    private static final Logger logger = LoggerFactory.getLogger(TokenBlacklistService.class);
    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    public void blacklistToken(String token, long expirationMillis) {
        blacklist.put(token, System.currentTimeMillis() + expirationMillis);
    }

    public boolean isBlacklisted(String token) {
        Long expiry = blacklist.get(token);
        if (expiry == null) return false;
        if (expiry < System.currentTimeMillis()) {
            blacklist.remove(token);
            return false;
        }
        return true;
    }
}
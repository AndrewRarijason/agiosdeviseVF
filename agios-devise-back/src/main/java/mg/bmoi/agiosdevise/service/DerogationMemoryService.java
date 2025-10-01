package mg.bmoi.agiosdevise.service;

import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
public class DerogationMemoryService {
    private final Set<String> derogations = new HashSet<>();

    public void addDerogation(String ncp, String dev) {
        derogations.add(ncp + ":" + dev);
    }

    public void clear() {
        derogations.clear();
    }

    public boolean isDerogation(String ncp, String dev) {
        return derogations.contains(ncp + ":" + dev);
    }
}
package mg.bmoi.agiosdevise.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ApplicationScope
public class ExportProgressService {
    private AtomicInteger totalAccounts = new AtomicInteger(0);
    private AtomicInteger processedAccounts = new AtomicInteger(0);
    private String currentStatus = "Non démarré";
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public void startExport(int total) {
        this.totalAccounts.set(total);
        this.processedAccounts.set(0);
        this.currentStatus = "En cours";
        this.startTime = LocalDateTime.now();
        this.endTime = null;
    }

    public void incrementProgress() {
        processedAccounts.incrementAndGet();
    }

    public void completeExport() {
        this.currentStatus = "Terminé";
        this.endTime = LocalDateTime.now();
    }

    public void errorExport(String errorMessage) {
        this.currentStatus = "Erreur: " + errorMessage;
        this.endTime = LocalDateTime.now();
    }

    public Map<String, Object> getProgress() {
        Map<String, Object> progress = new HashMap<>();
        progress.put("total", totalAccounts.get());
        progress.put("processed", processedAccounts.get());
        progress.put("percentage", totalAccounts.get() > 0 ?
                (processedAccounts.get() * 100) / totalAccounts.get() : 0);
        progress.put("status", currentStatus);
        progress.put("startTime", startTime);
        progress.put("endTime", endTime);
        progress.put("remaining", totalAccounts.get() - processedAccounts.get());
        return progress;
    }

    public void reset() {
        totalAccounts.set(0);
        processedAccounts.set(0);
        currentStatus = "Non démarré";
        startTime = null;
        endTime = null;
    }
}
package mg.bmoi.agiosdevise.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class FolderUtil {

    public static Path createAndCleanFolder(String folderName) throws IOException {
        Path folderPath = Paths.get(folderName);

        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        } else {
            cleanFolder(folderPath);
        }

        return folderPath;
    }

    public static void cleanFolder(Path folderPath) throws IOException {
        Files.walk(folderPath)
                .filter(path -> !path.equals(folderPath))
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new RuntimeException("Erreur lors du nettoyage du dossier: " + path, e);
                    }
                });
    }
}
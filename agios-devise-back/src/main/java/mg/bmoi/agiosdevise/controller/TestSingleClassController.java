package mg.bmoi.agiosdevise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@RestController
@RequestMapping("/api/test")
public class TestSingleClassController {

    private final DataSource dataSource;

    @Autowired
    public TestSingleClassController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertNom() {
        String nom = "Copilot"; // donnée en dur
        String sql = "INSERT INTO test (nom) VALUES (?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.executeUpdate();
            return ResponseEntity.ok("Insertion réussie : " + nom);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur d'insertion : " + e.getMessage());
        }
    }
}
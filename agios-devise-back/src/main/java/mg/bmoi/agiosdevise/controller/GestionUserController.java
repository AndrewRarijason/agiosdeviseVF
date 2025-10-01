// src/main/java/mg/bmoi/agiosdevise/controller/GestionUserController.java
package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.entity.UserApp;
import mg.bmoi.agiosdevise.entity.Role;
import mg.bmoi.agiosdevise.exception.UserValidationException;
import mg.bmoi.agiosdevise.repository.UserAppRepository;
import mg.bmoi.agiosdevise.repository.RoleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class GestionUserController {

    private final UserAppRepository userAppRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public GestionUserController(UserAppRepository userAppRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userAppRepository = userAppRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // DTO pour la réponse utilisateur
    public static class UserDTO {
        public int id;
        public String username;
        public String email_bmoi;
        public String role;
        public int is_active;

        public UserDTO(UserApp user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email_bmoi = user.getEmail_bmoi();
            this.role = user.getRole() != null ? user.getRole().getName() : null;
            this.is_active = user.getIs_active();
        }
    }

    // 1. Lister tous les utilisateurs avec leur rôle
    @GetMapping
    public List<UserDTO> listUsers() {
        return userAppRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    // 2. Ajouter un utilisateur
    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody Map<String, Object> payload) {
        String username = (String) payload.get("username");
        String email = (String) payload.get("email_bmoi");

        String mdp = (String) payload.get("mdp");
        // Hachage du mot de passe
        String hashedPassword = passwordEncoder.encode(mdp);

        Integer roleId = (Integer) payload.get("role");
        Integer isActive = payload.get("is_active") != null ? (Integer) payload.get("is_active") : 1;

        Optional<Role> roleOpt = roleRepository.findById(roleId);

        if (username == null || email == null || mdp == null || roleId == null) {
            throw new UserValidationException("Champs obligatoires manquants");
        }
        if (userAppRepository.findByUsername(username).isPresent()) {
            throw new UserValidationException("Ce nom d'utilisateur est déjà utilisé");
        }
        if (!email.endsWith("@bmoi.mg")) {
            throw new UserValidationException("L'adresse email doit se terminer par @bmoi.mg");
        }
        if (mdp.trim().isEmpty()) {
            throw new UserValidationException("Le mot de passe ne peut pas être vide");
        }
        if (!roleRepository.findById(roleId).isPresent()) {
            throw new UserValidationException("Rôle invalide");
        }

        UserApp user = new UserApp();
        user.setUsername(username);
        user.setEmail_bmoi(email);
        user.setMdp(hashedPassword);
        user.setRole(roleOpt.get());
        user.setIs_active(isActive);
        user.setCreated_at(new Date());

        userAppRepository.save(user);
        return ResponseEntity.ok(new UserDTO(user));
    }

    // 3. Modifier un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody Map<String, Object> payload) {
        Optional<UserApp> userOpt = userAppRepository.findById(id);
        if (!userOpt.isPresent()) return ResponseEntity.notFound().build();

        UserApp user = userOpt.get();
        if (payload.containsKey("username")) user.setUsername((String) payload.get("username"));
        if (payload.containsKey("email_bmoi")) user.setEmail_bmoi((String) payload.get("email_bmoi"));
        if (payload.containsKey("mdp")) user.setMdp((String) payload.get("mdp"));
        if (payload.containsKey("role")) {
            Integer roleId = (Integer) payload.get("role");
            Optional<Role> roleOpt = roleRepository.findById(roleId);
            roleOpt.ifPresent(user::setRole);
        }
        if (payload.containsKey("is_active")) user.setIs_active((Integer) payload.get("is_active"));

        userAppRepository.save(user);
        return ResponseEntity.ok(new UserDTO(user));
    }

    // 4. Supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        if (!userAppRepository.existsById(id)) return ResponseEntity.notFound().build();
        userAppRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<Map<String, String>> handleUserValidation(UserValidationException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
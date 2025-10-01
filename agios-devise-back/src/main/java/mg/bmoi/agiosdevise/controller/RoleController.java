package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.entity.Role;
import mg.bmoi.agiosdevise.repository.RoleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
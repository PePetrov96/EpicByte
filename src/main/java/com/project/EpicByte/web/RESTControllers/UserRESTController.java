package com.project.EpicByte.web.RESTControllers;

import com.project.EpicByte.model.dto.RESTDTOs.UserRESTViewDTO;
import com.project.EpicByte.service.REST.UserRESTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class UserRESTController {
    private final UserRESTService userRESTService;

    @Autowired
    public UserRESTController(UserRESTService userRESTService) {
        this.userRESTService = userRESTService;
    }

    // GET all users from the database
    @GetMapping("/users")
    public ResponseEntity<List<UserRESTViewDTO>> getAllUsers() {
        List<UserRESTViewDTO> users = userRESTService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // GET a single user from the database
    @GetMapping("/users/{id}")
    public ResponseEntity<UserRESTViewDTO> getUserById(@PathVariable UUID id) {
        UserRESTViewDTO user = userRESTService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}

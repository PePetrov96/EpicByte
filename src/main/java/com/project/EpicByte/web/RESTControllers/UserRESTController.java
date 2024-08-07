package com.project.EpicByte.web.RESTControllers;

import com.project.EpicByte.aop.SlowExecutionWarning;
import com.project.EpicByte.model.dto.RESTDTOs.UserRESTViewDTO;
import com.project.EpicByte.service.UserRESTService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @ApiOperation(value = "Get all users", notes = "Returns a list of all the users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "If the user list was empty",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @SlowExecutionWarning
    @GetMapping("/users")
    public ResponseEntity<List<UserRESTViewDTO>> getAllUsers() {
        List<UserRESTViewDTO> users = userRESTService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @ApiOperation(value = "Get a single user", notes = "Returns a user based on the UUID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "If the user was not found",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @SlowExecutionWarning
    @GetMapping("/users/{id}")
    public ResponseEntity<UserRESTViewDTO> getUserById(@PathVariable UUID id) {
        UserRESTViewDTO user = userRESTService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}

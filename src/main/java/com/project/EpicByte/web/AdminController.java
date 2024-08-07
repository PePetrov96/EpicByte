package com.project.EpicByte.web;

import com.project.EpicByte.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;

@Controller
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(ADMIN_MANAGE_PRIVILEGES_URL)
    private String displayAdminManagePrivilegesPage(Model model) {
        return this.adminService.displayAdminManagePrivilegesPage(model);
    }

    @PostMapping(ADMIN_GIVE_PRIVILEGES_URL + "{id}")
    public String giveModeratorPrivileges(@PathVariable UUID id, Model model) {
        return this.adminService.giveModeratorPrivilegesToUser(id, model);
    }

    @PostMapping(ADMIN_REMOVE_PRIVILEGES_URL + "{id}")
    public String removeModeratorPrivileges(@PathVariable UUID id, Model model) {
        return this.adminService.removeModeratorPrivileges(id, model);
    }
}
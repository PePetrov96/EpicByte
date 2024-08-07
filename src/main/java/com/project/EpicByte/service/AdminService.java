package com.project.EpicByte.service;

import org.springframework.ui.Model;

import java.util.UUID;

public interface AdminService {
     String displayAdminManagePrivilegesPage (Model model);
     String giveModeratorPrivilegesToUser (UUID id, Model model);
     String removeModeratorPrivileges (UUID id, Model model);
}
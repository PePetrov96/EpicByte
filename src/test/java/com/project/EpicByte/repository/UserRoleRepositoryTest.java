package com.project.EpicByte.repository;

import com.project.EpicByte.model.entity.enums.UserRolesEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserRoleRepositoryTest {
    @Mock
    private UserRoleRepository userRoleRepository;

    @Test
    public void findUserRoleByRoleTest() {
        UserRolesEnum userRolesEnum = UserRolesEnum.USER; // use actual roles from your Enum
        userRoleRepository.findUserRoleByRole(userRolesEnum);

        verify(userRoleRepository, times(1)).findUserRoleByRole(userRolesEnum);
    }
}
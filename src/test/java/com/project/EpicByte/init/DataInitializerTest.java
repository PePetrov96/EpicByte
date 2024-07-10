package com.project.EpicByte.init;

import com.project.EpicByte.model.entity.UserRole;
import com.project.EpicByte.model.entity.enums.UserRolesEnum;
import com.project.EpicByte.repository.UserRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DataInitializerTest {
    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void run_withEmptyUserRoleDatabase_shouldFillDatabase() throws Exception {
        // Given
        when(userRoleRepository.count()).thenReturn(0L);

        // When
        dataInitializer.run();

        // Then
        ArgumentCaptor<UserRole> captor = ArgumentCaptor.forClass(UserRole.class);
        verify(userRoleRepository, times(3)).save(captor.capture());
        List<UserRole> capturedUserRoles = captor.getAllValues();

        Assertions.assertEquals(UserRolesEnum.USER, capturedUserRoles.get(0).getRole());
        Assertions.assertEquals(UserRolesEnum.MODERATOR, capturedUserRoles.get(1).getRole());
        Assertions.assertEquals(UserRolesEnum.ADMIN, capturedUserRoles.get(2).getRole());
    }

    @Test
    void run_withNonEmptyUserRoleDatabase_shouldNotFillDatabase() throws Exception {
        // Given
        when(userRoleRepository.count()).thenReturn(1L);

        // When
        dataInitializer.run();

        // Then
        verify(userRoleRepository, never()).save(any(UserRole.class));
    }
}
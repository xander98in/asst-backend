package com.unicuaca.asst.unicauca_asst.core.auth.domain.services;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.UserStatus;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.enums.UserStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.UserStatusQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserStatusQueryServiceTest {

    @Mock
    private UserStatusQueryRepository userStatusQueryRepository;

    @InjectMocks
    private UserStatusQueryService userStatusQueryService;

    // ==================================================================================
    // getAllUserStatuses
    // ==================================================================================

    @Nested
    @DisplayName("getAllUserStatuses")
    class GetAllUserStatuses {

        @Test
        @DisplayName("Debe retornar todos los estados de usuario disponibles")
        void should_returnAllUserStatuses() {
            // Arrange
            List<UserStatus> statuses = List.of(
                    UserStatus.builder().id(1L).name(UserStatusEnum.ACTIVE.getDescription()).build(),
                    UserStatus.builder().id(2L).name(UserStatusEnum.INACTIVE.getDescription()).build(),
                    UserStatus.builder().id(3L).name(UserStatusEnum.BLOCKED.getDescription()).build()
            );
            when(userStatusQueryRepository.getAllUserStatuses()).thenReturn(statuses);

            // Act
            List<UserStatus> result = userStatusQueryService.getAllUserStatuses();

            // Assert
            assertThat(result).hasSize(3);
        }
    }
}

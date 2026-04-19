package com.unicuaca.asst.unicauca_asst.core.auth.domain.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.Role;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.enums.RoleEnum;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.RoleQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleQueryServiceTest {

    @Mock
    private RoleQueryRepository roleQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private RoleQueryService roleQueryService;

    // ==================================================================================
    // getAllRoles
    // ==================================================================================

    @Nested
    @DisplayName("getAllRoles")
    class GetAllRoles {

        @Test
        @DisplayName("Debe retornar todos los roles disponibles")
        void should_returnAllRoles() {
            // Arrange
            List<Role> roles = List.of(
                    Role.builder().id(1L).name("Administrador").keyName(RoleEnum.ADMIN.getDescription()).build(),
                    Role.builder().id(2L).name("Profesional ASST").keyName(RoleEnum.PROFESIONAL_ASST.getDescription()).build(),
                    Role.builder().id(3L).name("Persona Evaluada").keyName(RoleEnum.PERSONA_EVALUADA.getDescription()).build()
            );
            when(roleQueryRepository.getAllRoles()).thenReturn(roles);

            // Act
            List<Role> result = roleQueryService.getAllRoles();

            // Assert
            assertThat(result).hasSize(3);
        }
    }

    // ==================================================================================
    // getRoleByKeyName
    // ==================================================================================

    @Nested
    @DisplayName("getRoleByKeyName")
    class GetRoleByKeyName {

        @Test
        @DisplayName("Debe retornar rol cuando existe por keyName")
        void should_returnRole_when_foundByKeyName() {
            // Arrange
            Role role = Role.builder()
                    .id(1L).name("Administrador").keyName(RoleEnum.ADMIN.getDescription()).build();
            when(roleQueryRepository.getRoleByKeyName(RoleEnum.ADMIN.getDescription()))
                    .thenReturn(Optional.of(role));

            // Act
            Role result = roleQueryService.getRoleByKeyName(RoleEnum.ADMIN.getDescription());

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getKeyName()).isEqualTo(RoleEnum.ADMIN.getDescription());
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el rol no existe por keyName")
        void should_throwEntityNotFound_when_roleNotFoundByKeyName() {
            // Arrange
            when(roleQueryRepository.getRoleByKeyName("INEXISTENTE"))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.ROLE_NOT_FOUND.getCode(),
                    ErrorCode.ROLE_NOT_FOUND.getMessageKey(),
                    "tech.role.not_found",
                    new Object[]{"INEXISTENTE"}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> roleQueryService.getRoleByKeyName("INEXISTENTE"))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.ROLE_NOT_FOUND,
                    "tech.role.not_found",
                    "INEXISTENTE");
        }
    }
}

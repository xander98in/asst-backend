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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output.SystemUserQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SystemUserQueryServiceTest {

    @Mock
    private SystemUserQueryRepository systemUserQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private SystemUserQueryService systemUserQueryService;

    // ==================================================================================
    // getSystemUserById
    // ==================================================================================

    @Nested
    @DisplayName("getSystemUserById")
    class GetSystemUserById {

        @Test
        @DisplayName("Debe retornar usuario cuando existe por ID")
        void should_returnUser_when_foundById() {
            // Arrange
            SystemUser user = SystemUser.builder()
                    .id(1L).email("admin@unicauca.edu.co").username("admin").build();
            when(systemUserQueryRepository.getSystemUserById(1L))
                    .thenReturn(Optional.of(user));

            // Act
            SystemUser result = systemUserQueryService.getSystemUserById(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("admin@unicauca.edu.co");
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el usuario no existe por ID")
        void should_throwEntityNotFound_when_userNotFoundById() {
            // Arrange
            when(systemUserQueryRepository.getSystemUserById(99L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessageKey(),
                    "user.user.not_found",
                    new Object[]{99L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserQueryService.getSystemUserById(99L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.USER_NOT_FOUND,
                    "user.user.not_found",
                    99L);
        }
    }

    // ==================================================================================
    // getSystemUserByEmail
    // ==================================================================================

    @Nested
    @DisplayName("getSystemUserByEmail")
    class GetSystemUserByEmail {

        @Test
        @DisplayName("Debe retornar usuario cuando existe por email")
        void should_returnUser_when_foundByEmail() {
            // Arrange
            SystemUser user = SystemUser.builder()
                    .id(1L).email("admin@unicauca.edu.co").username("admin").build();
            when(systemUserQueryRepository.getSystemUserByEmail("admin@unicauca.edu.co"))
                    .thenReturn(Optional.of(user));

            // Act
            SystemUser result = systemUserQueryService.getSystemUserByEmail("admin@unicauca.edu.co");

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getUsername()).isEqualTo("admin");
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el usuario no existe por email")
        void should_throwEntityNotFound_when_userNotFoundByEmail() {
            // Arrange
            when(systemUserQueryRepository.getSystemUserByEmail("noexiste@unicauca.edu.co"))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessageKey(),
                    "user.user.not_found",
                    new Object[]{"noexiste@unicauca.edu.co"}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> systemUserQueryService
                    .getSystemUserByEmail("noexiste@unicauca.edu.co"))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.USER_NOT_FOUND,
                    "user.user.not_found",
                    "noexiste@unicauca.edu.co");
        }
    }

    // ==================================================================================
    // listPaginatedUsers
    // ==================================================================================

    @Nested
    @DisplayName("listPaginatedUsers")
    class ListPaginatedUsers {

        @Test
        @DisplayName("Debe retornar página de usuarios")
        void should_returnPagedUsers() {
            // Arrange
            List<SystemUser> users = List.of(
                    SystemUser.builder().id(1L).username("admin").build(),
                    SystemUser.builder().id(2L).username("profesional").build()
            );
            Page<SystemUser> page = new PageImpl<>(users);
            when(systemUserQueryRepository.findAllPaged(eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);

            // Act
            Page<SystemUser> result = systemUserQueryService.listPaginatedUsers(0, 10);

            // Assert
            assertThat(result.getContent()).hasSize(2);
        }
    }

    // ==================================================================================
    // listPaginatedWithSearchTerm
    // ==================================================================================

    @Nested
    @DisplayName("listPaginatedWithSearchTerm")
    class ListPaginatedWithSearchTerm {

        @Test
        @DisplayName("Debe retornar página filtrando por término con trim")
        void should_returnFilteredPage_when_termIsValid() {
            // Arrange
            List<SystemUser> users = List.of(
                    SystemUser.builder().id(1L).username("admin").build()
            );
            Page<SystemUser> page = new PageImpl<>(users);
            when(systemUserQueryRepository.findAllWithSearchTermPaged(
                    eq("admin"), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);

            // Act
            Page<SystemUser> result = systemUserQueryService
                    .listPaginatedWithSearchTerm(0, 10, "  admin  ");

            // Assert
            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        @DisplayName("Debe normalizar término null a null")
        void should_normalizeNullTerm() {
            // Arrange
            Page<SystemUser> page = new PageImpl<>(List.of());
            when(systemUserQueryRepository.findAllWithSearchTermPaged(
                    isNull(), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);

            // Act
            Page<SystemUser> result = systemUserQueryService
                    .listPaginatedWithSearchTerm(0, 10, null);

            // Assert
            assertThat(result.getContent()).isEmpty();
        }

        @Test
        @DisplayName("Debe normalizar término en blanco a null")
        void should_normalizeBlankTermToNull() {
            // Arrange
            Page<SystemUser> page = new PageImpl<>(List.of());
            when(systemUserQueryRepository.findAllWithSearchTermPaged(
                    isNull(), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);

            // Act
            Page<SystemUser> result = systemUserQueryService
                    .listPaginatedWithSearchTerm(0, 10, "   ");

            // Assert
            assertThat(result.getContent()).isEmpty();
        }
    }
}

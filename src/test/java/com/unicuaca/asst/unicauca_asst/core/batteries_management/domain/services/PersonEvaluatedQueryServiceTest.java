package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

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

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonEvaluatedQueryServiceTest {

    @Mock
    private PersonEvaluatedQueryRepository personEvaluatedQueryRepository;

    @Mock
    private CatalogQueryRepository catalogQueryRepository;

    @Mock
    private ResultFormatterOutputPort resultFormatter;

    @InjectMocks
    private PersonEvaluatedQueryService personEvaluatedQueryService;

    // ==================================================================================
    // getPersonEvaluatedById
    // ==================================================================================

    @Nested
    @DisplayName("getPersonEvaluatedById")
    class GetPersonEvaluatedById {

        @Test
        @DisplayName("Debe retornar persona cuando existe por ID")
        void should_returnPerson_when_foundById() {
            // Arrange
            PersonEvaluated person = PersonEvaluated.builder()
                    .id(1L).firstName("JUAN CARLOS").build();
            when(personEvaluatedQueryRepository.getPersonEvaluatedById(1L))
                    .thenReturn(Optional.of(person));

            // Act
            PersonEvaluated result = personEvaluatedQueryService.getPersonEvaluatedById(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getFirstName()).isEqualTo("JUAN CARLOS");
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando la persona no existe por ID")
        void should_throwEntityNotFound_when_personNotFoundById() {
            // Arrange
            when(personEvaluatedQueryRepository.getPersonEvaluatedById(1L))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_NOT_FOUND.getMessageKey(),
                    "user.person.id_not_found",
                    new Object[]{1L}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedQueryService.getPersonEvaluatedById(1L))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.PERSON_NOT_FOUND,
                    "user.person.id_not_found",
                    1L);
        }
    }

    // ==================================================================================
    // queryByIdentity
    // ==================================================================================

    @Nested
    @DisplayName("queryByIdentity")
    class QueryByIdentity {

        @Test
        @DisplayName("Debe retornar página cuando el tipo de identificación existe")
        void should_returnPage_when_queryByIdentity() {
            // Arrange
            IdentificationType idType = IdentificationType.builder()
                    .id(1L).name("Cédula de Ciudadanía").abbreviation("CC").build();
            PersonEvaluated person = PersonEvaluated.builder().id(1L).build();
            Page<PersonEvaluated> page = new PageImpl<>(List.of(person));

            when(catalogQueryRepository.getIdTypeByAbbreviation("CC"))
                    .thenReturn(Optional.of(idType));
            when(personEvaluatedQueryRepository.queryByIdentity(
                    any(IdentificationType.class), eq("1061234567"), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);

            // Act
            Page<PersonEvaluated> result = personEvaluatedQueryService
                    .queryByIdentity("CC", "1061234567", 0, 10);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        @DisplayName("Debe lanzar EntityNotFound cuando el tipo de identificación no existe")
        void should_throwEntityNotFound_when_identificationTypeNotFound() {
            // Arrange
            when(catalogQueryRepository.getIdTypeByAbbreviation("XYZ"))
                    .thenReturn(Optional.empty());
            doThrow(new EntityNotFoundPersException(
                    ErrorCode.PERSON_ID_TYPE_NOT_FOUND.getCode(),
                    ErrorCode.PERSON_ID_TYPE_NOT_FOUND.getMessageKey(),
                    "user.person.id_type_invalid",
                    new Object[]{"XYZ"}))
                .when(resultFormatter).throwEntityNotFound(
                    any(ErrorCode.class), anyString(), any());

            // Act & Assert
            assertThatThrownBy(() -> personEvaluatedQueryService
                    .queryByIdentity("XYZ", "1061234567", 0, 10))
                    .isInstanceOf(EntityNotFoundPersException.class);

            verify(resultFormatter).throwEntityNotFound(
                    ErrorCode.PERSON_ID_TYPE_NOT_FOUND,
                    "user.person.id_type_invalid",
                    "XYZ");
            verify(personEvaluatedQueryRepository, never())
                    .queryByIdentity(any(), any(), any(), any(), any());
        }
    }

    // ==================================================================================
    // listPaginatedPersons
    // ==================================================================================

    @Nested
    @DisplayName("listPaginatedPersons")
    class ListPaginatedPersons {

        @Test
        @DisplayName("Debe retornar página con personas evaluadas")
        void should_returnPage_when_listPaginatedPersons() {
            // Arrange
            PersonEvaluated person = PersonEvaluated.builder().id(1L).build();
            Page<PersonEvaluated> page = new PageImpl<>(List.of(person));
            when(personEvaluatedQueryRepository.findAllPaged(eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);

            // Act
            Page<PersonEvaluated> result = personEvaluatedQueryService.listPaginatedPersons(0, 10);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
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
        void should_returnPage_when_listPaginatedWithSearchTerm() {
            // Arrange
            PersonEvaluated person = PersonEvaluated.builder().id(1L).build();
            Page<PersonEvaluated> page = new PageImpl<>(List.of(person));
            when(personEvaluatedQueryRepository.findAllWithSearchTermPaged(
                    eq("juan"), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);

            // Act
            Page<PersonEvaluated> result = personEvaluatedQueryService
                    .listPaginatedWithSearchTerm(0, 10, "  juan  ");

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        @DisplayName("Debe normalizar término null a null")
        void should_passNull_when_termIsNull() {
            // Arrange
            Page<PersonEvaluated> page = new PageImpl<>(List.of());
            when(personEvaluatedQueryRepository.findAllWithSearchTermPaged(
                    isNull(), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);

            // Act
            Page<PersonEvaluated> result = personEvaluatedQueryService
                    .listPaginatedWithSearchTerm(0, 10, null);

            // Assert
            assertThat(result.getContent()).isEmpty();
        }

        @Test
        @DisplayName("Debe normalizar término en blanco a null")
        void should_passNull_when_termIsBlank() {
            // Arrange
            Page<PersonEvaluated> page = new PageImpl<>(List.of());
            when(personEvaluatedQueryRepository.findAllWithSearchTermPaged(
                    isNull(), eq(0), eq(10), any(Sort.class)))
                    .thenReturn(page);

            // Act
            Page<PersonEvaluated> result = personEvaluatedQueryService
                    .listPaginatedWithSearchTerm(0, 10, "   ");

            // Assert
            assertThat(result.getContent()).isEmpty();
        }
    }
}

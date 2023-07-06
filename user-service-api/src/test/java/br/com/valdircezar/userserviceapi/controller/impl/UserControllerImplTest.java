package br.com.valdircezar.userserviceapi.controller.impl;

import br.com.valdircezar.userserviceapi.entity.User;
import br.com.valdircezar.userserviceapi.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static br.com.valdircezar.userserviceapi.creator.CreatorUtils.generateMock;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerImplTest {

    public static final String BASE_URI = "/api/users";
    public static final String UPDATE_URI = BASE_URI + "/{id}";
    public static final String VALID_EMAIL = "kj45klj23b5@mail.com";
    public static final String VALIDATION_EXCEPTION_MSG = "Validation Exception";
    public static final String VALIDATION_ATTRIBUTES_ERROR_MSG = "Exception in validation attributes";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should create a user with success")
    void testFindByIdWithSuccess() throws Exception {
        final var entity = generateMock(User.class);
        final var userId = userRepository.save(entity).getId();

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value(entity.getName()))
                .andExpect(jsonPath("$.email").value(entity.getEmail()))
                .andExpect(jsonPath("$.password").value(entity.getPassword()))
                .andExpect(jsonPath("$.profiles").isArray());

        userRepository.deleteById(userId);
    }

    @Test
    @DisplayName("Should throw a not found exception when user is not found")
    void testFindByIdWithNotFoundException() throws Exception {
        mockMvc.perform(get("/api/users/{id}", "123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Object not found. Id: 123, Type: UserResponse"))
                .andExpect(jsonPath("$.error").value(NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.path").value("/api/users/123"))
                .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("Should return a list of users with success")
    void testFindAllWithSuccess() throws Exception {
        final var entity1 = generateMock(User.class);
        final var entity2 = generateMock(User.class);

        userRepository.saveAll(List.of(entity1, entity2));

        mockMvc.perform(get(BASE_URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").isNotEmpty())
                .andExpect(jsonPath("$[1]").isNotEmpty())
                .andExpect(jsonPath("$[0].profiles").isArray());

        userRepository.deleteAll(List.of(entity1, entity2));
    }

    @Test
    @DisplayName("Should save a user with success")
    void testSaveUserWithSuccess() throws Exception {
        final var request = generateMock(CreateUserRequest.class).withEmail(VALID_EMAIL);

        mockMvc.perform(
                post(BASE_URI)
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
        ).andExpect(status().isCreated());

        userRepository.deleteByEmail(VALID_EMAIL);
    }

    @Test
    @DisplayName("Should throw a conflict exception when email is invalid")
    void testSaveUserWithConflict() throws Exception {
        final var entity = generateMock(User.class).withEmail(VALID_EMAIL);

        userRepository.save(entity);

        final var request = generateMock(CreateUserRequest.class).withEmail(VALID_EMAIL);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email [ " + VALID_EMAIL + " ] already exists"))
                .andExpect(jsonPath("$.error").value(CONFLICT.getReasonPhrase()))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(CONFLICT.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

        userRepository.deleteById(entity.getId());
    }

    @Test
    @DisplayName("Should throw a bad request exception when name is empty")
    void testSaveUserWithNameEmptyThenThrowBadRequest() throws Exception{
        final var request = generateMock(CreateUserRequest.class).withName("").withEmail(VALID_EMAIL);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='name' && @.message=='Name must contain between 3 and 50 characters')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='name' && @.message=='Name cannot be empty')]").exists());
    }

    @Test
    @DisplayName("Should throw a bad request exception when name is null")
    void testSaveUserWithNameNullThenThrowBadRequest() throws Exception{
        final var request = generateMock(CreateUserRequest.class).withName(null).withEmail(VALID_EMAIL);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='name' && @.message=='Name cannot be empty')]").exists());
    }

    @Test
    @DisplayName("Should throw a bad request exception when name is blank")
    void testSaveUserWithNameContainingOnlySpacesThenThrowBadRequest() throws Exception{
        final var request = generateMock(CreateUserRequest.class).withName("   ").withEmail(VALID_EMAIL);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='name' && @.message=='Name cannot be empty')]").exists());
    }

    @Test
    @DisplayName("Should throw a bad request exception when name has less than tree characters")
    void testSaveUserWithNameContainingLessThenTreeCharactersThenThrowBadRequest() throws Exception{
        final var request = generateMock(CreateUserRequest.class).withName("ab").withEmail(VALID_EMAIL);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='name' && @.message=='Name must contain between 3 and 50 characters')]").exists());
    }

    @Test
    @DisplayName("Should throw a bad request exception when email is invalid")
    void testSaveUserWithInvalidEmailThenThrowBadRequest() throws Exception{
        final var request = generateMock(CreateUserRequest.class);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='email' && @.message=='Invalid email')]").exists());
    }

    @Test
    @DisplayName("Should throw a bad request exception when email is empty")
    void testSaveUserWithNullEmailThenThrowBadRequest() throws Exception{
        final var request = generateMock(CreateUserRequest.class).withEmail(null);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='email' && @.message=='Email cannot be empty')]").exists());
    }

    @Test
    @DisplayName("Should throw a bad request exception when email has less than six characters")
    void testSaveUserWithEmailContainingLessThenSixCharactersThenThrowBadRequest() throws Exception{
        final var request = generateMock(CreateUserRequest.class).withEmail("a@b.c");

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='email' && @.message=='Email must contain between 6 and 50 characters')]").exists());
    }

    @Test
    @DisplayName("Should throw a bad request exception when password is empty")
    void testSaveUserWithNullPasswordThenThrowBadRequest() throws Exception{
        final var request = generateMock(CreateUserRequest.class).withEmail(VALID_EMAIL).withPassword(null);

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='password' && @.message=='Password cannot be empty')]").exists());
    }

    @Test
    @DisplayName("Should throw a bad request exception when password has only empty spaces")
    void testSaveUserWithPasswordContainingOnlySpacesThenThrowBadRequest() throws Exception{
        final var request = generateMock(CreateUserRequest.class).withEmail(VALID_EMAIL).withPassword("   ");

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='password' && @.message=='Password must contain between 6 and 50 characters')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='password' && @.message=='Password cannot be empty')]").exists());
    }

    @Test
    @DisplayName("Should throw a bad request exception when password has less than six characters")
    void testSaveUserWithPasswordContainingLessThenSixCharactersThenThrowBadRequest() throws Exception{
        final var request = generateMock(CreateUserRequest.class).withEmail(VALID_EMAIL).withPassword("   ");

        mockMvc.perform(
                        post(BASE_URI)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='password' && @.message=='Password must contain between 6 and 50 characters')]").exists());
    }

    @Test
    @DisplayName("Should throw a bad request exception when name is less than three characters")
    void testUpdateUserWithNameLessThenThreeCharactersThenThrowBadRequest() throws Exception{
        final var request = generateMock(UpdateUserRequest.class).withName("ab");
        final var VALID_ID = userRepository.save(generateMock(User.class)).getId();

        mockMvc.perform(
                        put(UPDATE_URI, VALID_ID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI + "/" + VALID_ID))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='name' && @.message=='Name must contain between 3 and 50 characters')]").exists());

        userRepository.deleteById(VALID_ID);
    }

    @Test
    @DisplayName("Should throw a bad request exception when email is less than six characters")
    void testUpdateUserWithEmailLessThenSixCharactersThenThrowBadRequest() throws Exception{
        final var request = generateMock(UpdateUserRequest.class).withEmail("a@b.c");
        final var VALID_ID = userRepository.save(generateMock(User.class)).getId();

        mockMvc.perform(
                        put(UPDATE_URI, VALID_ID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI + "/" + VALID_ID))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='email' && @.message=='Email must contain between 6 and 50 characters')]").exists());

        userRepository.deleteById(VALID_ID);
    }

    @Test
    @DisplayName("Should throw a bad request exception when email is formatted incorrectly")
    void testUpdateUserWithEmailFormattedIncorrectlyThenThrowBadRequest() throws Exception{
        final var request = generateMock(UpdateUserRequest.class).withEmail("abc");
        final var VALID_ID = userRepository.save(generateMock(User.class)).getId();

        mockMvc.perform(
                        put(UPDATE_URI, VALID_ID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI + "/" + VALID_ID))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='email' && @.message=='Invalid email')]").exists());

        userRepository.deleteById(VALID_ID);
    }

    @Test
    @DisplayName("Should throw a bad request exception when password has less than six characters")
    void testUpdateUserWithPasswordLessThenSixCharactersThenThrowBadRequest() throws Exception{
        final var request = generateMock(UpdateUserRequest.class).withPassword("12345").withEmail(VALID_EMAIL);
        final var VALID_ID = userRepository.save(generateMock(User.class).withId(null)).getId();

        mockMvc.perform(
                        put(UPDATE_URI, VALID_ID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(VALIDATION_ATTRIBUTES_ERROR_MSG))
                .andExpect(jsonPath("$.error").value(VALIDATION_EXCEPTION_MSG))
                .andExpect(jsonPath("$.path").value(BASE_URI + "/" + VALID_ID))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.fieldName=='password' && @.message=='Password must contain between 6 and 50 characters')]").exists());

        userRepository.deleteById(VALID_ID);
    }

    @Test
    @DisplayName("Should throw a not found exception when id is not found")
    void testUpdateUserWithIdNotFoundThenThrowNotFoundException() throws Exception{
        final var request = generateMock(UpdateUserRequest.class).withEmail(VALID_EMAIL);

        mockMvc.perform(
                        put(UPDATE_URI, 1L)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request))
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Object not found. Id: 1, Type: UserResponse"))
                .andExpect(jsonPath("$.error").value(NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.path").value(BASE_URI + "/1"))
                .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    private String toJson(final Object object) throws Exception {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (final Exception e) {
            throw new Exception("Error to convert object to json", e);
        }
    }
}
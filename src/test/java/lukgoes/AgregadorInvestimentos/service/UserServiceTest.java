package lukgoes.AgregadorInvestimentos.service;

import lukgoes.AgregadorInvestimentos.controller.CreateUserDto;
import lukgoes.AgregadorInvestimentos.controller.UpdateUserDto;
import lukgoes.AgregadorInvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import lukgoes.AgregadorInvestimentos.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class createUser{

        @Test
        @DisplayName("Deve criar um usuário com sucesso")
        void shouldCreateAUSerWithSuccess() {

            //Arrange
            var user = new User(
                UUID.randomUUID(),
                "username",
                "email@email.com",
                "password",
                Instant.now(),
                null);

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            var input = new CreateUserDto(
                   "username",
                    "email@email.com",
                    "123");

            //Act
           var output = userService.createUser(input);

            //Assert
            assertNotNull(output);
            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());

        }

        @Test
        @DisplayName("Lançar uma exceção quando ocorrer um erro")
        void shouldThrowExceptionWhenErrorOccurs() {

            doThrow(new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDto(
                    "username",
                    "email@email.com",
                    "123");

            //Act e Assert
            assertThrows(RuntimeException.class, () -> userService.createUser(input));

        }
    }

    @Nested
    class getUserByID{

        @Test
        @DisplayName("Deve achar o usuário pelo ID com Sucesso quando opcional está presente")
        void shouldGetUserByIdWithSuccessWhenOptionalIsPresent() {

            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null);

            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());

            //Act
           var output = userService.getUserById(user.getUserId().toString());

            //Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Deve achar o usuário pelo ID com Sucesso quando opcional não está presente")
        void shouldGetUserByIdWithSuccessWhenOptionalIsEmpty() {

            //Arrange
            var userId = UUID.randomUUID();
            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            //Act
            var output = userService.getUserById(userId.toString());

            //Assert
            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }

    }

    @Nested
    class listUsers{

        @Test
        @DisplayName("Deve listar todos os usuários com sucesso")
        void shouldReturnAllUsersWithSuccess() {
            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null);

            var userList = List.of(user);
            doReturn(userList)
                    .when(userRepository)
                    .findAll();

            //Act
            var output = userService.listUsers();

            //Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }

    }

    @Nested
    class deleteById{

        @Test
        @DisplayName("Deve deletar o usuário com sucesso quando ele existir")
        void shouldDeleteUserWithSuccessWhenUserExists() {

            doReturn(true)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());


            doNothing()
                    .when(userRepository)
                    .deleteById(uuidArgumentCaptor.capture());
            var userId = UUID.randomUUID();

            //Act
            userService.deleteById(userId.toString());

            //Assert
            var idList = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));

        }

        @Test
        @DisplayName("Não deve deletar o usuário quando ele NÂO existir")
        void shouldNotDeleteUserWhenUserNotExists() {

            doReturn(false)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());
            var userId = UUID.randomUUID();

            //Act
            userService.deleteById(userId.toString());

            //Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1))
                    .existsById(uuidArgumentCaptor.getValue());

            verify(userRepository, times(0)).deleteById(any());

        }

    }
    
    @Nested
    class updateUserById{

        @Test
        @DisplayName("Deve atualizar o usuário por ID quando ele existir e usuário e senha estiverem preenchidos")
        void shouldUpdateUserByIdWhenUserExistsAndUsernameAndPasswordIsFilled() {

            //Arrange
            var updateUserDto = new UpdateUserDto(
                    "newUsername",
                    "newPassword"
            );

            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null);

            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            doReturn(user)
                    .when(userRepository)
                    .save(userArgumentCaptor.capture());

            //Act
            userService.updateUserById(user.getUserId().toString(), updateUserDto);

            //Assert
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(updateUserDto.username(), userCaptured.getUsername());
            assertEquals(updateUserDto.password(), userCaptured.getPassword());

            verify(userRepository, times(1))
                    .findById(uuidArgumentCaptor.getValue());

            verify(userRepository, times(1))
                    .save(user);
        }

        @Test
        @DisplayName("Não deve atualizar o usuário quando ele não existir")
        void shouldNotUpdateUserWhenUserNotExists() {

            //Arrange
            var updateUserDto = new UpdateUserDto(
                    "newUsername",
                    "newPassword"
            );

            var userId = UUID.randomUUID();

            doReturn(Optional.empty())
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            //Act
            userService.updateUserById(userId.toString(), updateUserDto);

            //Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1))
                    .findById(uuidArgumentCaptor.getValue());

            verify(userRepository, times(0))
                    .save(any());
        }

    }

}
package lukgoes.AgregadorInvestimentos.service;

import lukgoes.AgregadorInvestimentos.controller.CreateUserDto;
import lukgoes.AgregadorInvestimentos.entity.User;
import lukgoes.AgregadorInvestimentos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUser(CreateUserDto createUserDto){

        var entity = new User(
                null,
                createUserDto.username(),
                createUserDto.email(),
                createUserDto.password(),
                Instant.now(),
                null);

       var userSaved = userRepository.save(entity);
       return userSaved.getUserId();
    }


}

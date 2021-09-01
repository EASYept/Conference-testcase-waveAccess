package com.testcase.conference.service;

import com.testcase.conference.dto.UserDto;
import com.testcase.conference.entity.User;
import com.testcase.conference.repository.UserRepository;
import com.testcase.conference.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.status;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDto> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(this::mapUserToDto).collect(Collectors.toList());
    }

    public ResponseEntity<UserDto> findById(UUID uuid) {
        Optional<User> userOptional = userRepository.findById(uuid);
        if (userOptional.isPresent()) {
            return status(HttpStatus.OK).body(mapUserToDto(userOptional.get()));
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Void> save(UserDto userDto) {
        if ( (userDto.getUserLogin() == null)
                && (userDto.getRole() == null)
                && (userDto.getEncrPassword() == null)
                && (userRepository.existsByUserLogin(userDto.getUserLogin())) ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = new User(userDto.getUserLogin(),
                            passwordEncoder.encode(userDto.getEncrPassword()),
                            userDto.getRole());

        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<Void> update(UserDto userDto) {
        Optional<User> byId = userRepository.findById(userDto.getUuid());
        if (byId.isPresent()) {
            userRepository.save(mapDtoToUser(userDto, byId.get()));
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteById(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByUserLogin(login);
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> byUserLogin = userRepository.findByUserLogin(login);
        if (byUserLogin.isPresent()){
            User user = byUserLogin.get();
            return new UserDetailsImpl(user);
        } else {
            return new UserDetailsImpl(new User());
        }
    }

    //MAPPING

    private UserDto mapUserToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUuid(user.getUuid());
        dto.setUserLogin(user.getUserLogin());
        dto.setRole(user.getRole());
        dto.setEncrPassword(user.getEncrPassword());
        return dto;
    }

    private User mapDtoToUser(UserDto userDto, User user) {
        user.setUserLogin(userDto.getUserLogin());
        user.setRole(userDto.getRole());
        return user;
    }

}

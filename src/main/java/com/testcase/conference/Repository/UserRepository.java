package com.testcase.conference.Repository;

import com.testcase.conference.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    List<User> findAll();

    Optional<User> findByUserLogin(String login);

    boolean existsByUserLogin(String login);

}

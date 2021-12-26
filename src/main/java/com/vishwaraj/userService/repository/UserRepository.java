package com.vishwaraj.userService.repository;

import com.vishwaraj.userService.domain.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<Users, String> {
    Users findByUserName (String userName);
    Users findByUserId (String userId);
}

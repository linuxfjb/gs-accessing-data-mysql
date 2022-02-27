package com.example.accessingdatamysql;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.example.accessingdatamysql.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {
	long deleteByName(String name);
	List<User> findByName(String name);
}

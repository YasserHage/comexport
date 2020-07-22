package com.comexport.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.comexport.Models.User;
import com.comexport.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    /** Meant to run all CRUD like verbs.*/
	@Autowired
	private UserRepository userRepository;
	
	/**
	* The findAll() method will retrieve all users from database.
	*
	* @return List<User> - all users. 
	*/
	public List<User> findAll(){
		// Collection containing all users
		List<User> users = new ArrayList<>();
		
		// Populating the users list with all users	in the DB
        this.userRepository.findAll().forEach(users :: add);
        
        return users;	
	}
	
	/**
	 * The findById(Integer) method will find a user by it's id.
	 * 
	 * @param id				- the user's primary key. 
	 * @return Optional<User>  - the user if it exists.
	 */
	public Optional<User> findById(Integer id){
		return this.userRepository.findById(id);
	}
	
	/**
	 * The save(User) method will create or update a user into the database.
	 * 
	 * @param user			- the user to be saved.
	 * @return User			- the saved user.
	 */
	public User save(User user) {
		return this.userRepository.save(user);
	}
	
	/**
	 * The delete(Integer) method will delete a user by it's id.
	 * 
	 * @param id			- the user's primary key.
	 */
	public void delete(Integer id) {
		this.userRepository.deleteById(id);
	}
}
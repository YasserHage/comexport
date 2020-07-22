package com.comexport.Utils;

import java.util.Calendar;
import java.util.Date;

import com.comexport.DTOs.UserInputDTO;
import com.comexport.DTOs.UserOutputDTO;
import com.comexport.Models.User;

import org.springframework.stereotype.Service;

@Service
public class UserUtils {
    
	/**
	 * The toUserOutputDTO(User) method will transform a User into a userOutputDTO.
	 * 
	 * @param user			- The User to be transformed.
	 * @return UserOutputDTO - The transformed UserOutputDTO.
	 */
	public UserOutputDTO toUserOutputDTO(User user) {
		Calendar birthCalendar = Calendar.getInstance();
		if(user.getDateOfBirth() != null){
			birthCalendar.setTime(user.getDateOfBirth());
		}
		int age = Calendar.getInstance().get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

		return UserOutputDTO
				.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.age(age)
				.address(user.getAddress())
				.creationDate(user.getCreationDate())
				.lastUpdate(user.getLastUpdate())
				.build();
	}
	
	/**
	 * The toUser(UserInputDTO) method will transform a UserInputDTO into a User.
	 * 
	 * @param user			- The UserInputDTO to be transformed.
	 * @return User			- The transformed User.
	 */
	public User toUser(UserInputDTO user) {
		return User
				.builder()
				.name(user.getName())
				.email(user.getEmail())
				.dateOfBirth(user.getDateOfBirth())
				.address(user.getAddress())
				.creationDate(user.getCreationDate())
				.lastUpdate(user.getLastUpdate())
				.build();
    }
    
}
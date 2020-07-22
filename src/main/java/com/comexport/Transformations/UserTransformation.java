package com.comexport.Transformations;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.comexport.DTOs.UserInputDTO;
import com.comexport.DTOs.UserOutputDTO;
import com.comexport.Models.User;
import com.comexport.Utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTransformation {
    
	/**
	 * the UserUtils class is a kind of utilitary class regarding the user's Model and it's DTOs.
	 */
	@Autowired
	private UserUtils userUtils;
	
	/**
	 * This convert(User) method will transform a User into a UserOutputDTO.
	 * 
	 * @param user			- the User that will be transformed into a UserOutputDTO.
	 * @return UserOutputDTO - the transformed UserOutputDTO.
	 */
	public UserOutputDTO convert(User user) {
		return this.userUtils.toUserOutputDTO(user);
	}
	
	/**
	 * This convert(UserInputDTO) method will transform a UserInputDTO into a User.
	 * 
	 * @param user			- the UserInputDTO that will be transformed into a User.
	 * @return User			- the transformed User.
	 */
	public User convert(UserInputDTO user) {
		return this.userUtils.toUser(user);
	}
	
	/**
	 * It will transform Collection<User> into List<UserOutputDTO>.
	 * 
	 * @param users					- the collection that will be transformed into List<UserOutputDTO>.
	 * @return List<UserOutputDTO>	- the transformed List<UserOutputDTO>.
	 */
	public List<UserOutputDTO> convert(Collection<User> users){
		return users
				.stream()
				.map(this :: convert)
				.collect(Collectors.toList());
	}
}
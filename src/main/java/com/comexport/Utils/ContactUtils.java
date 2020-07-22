package com.comexport.Utils;


import com.comexport.DTOs.ContactInputDTO;
import com.comexport.DTOs.ContactOutputDTO;
import com.comexport.Models.Contact;
import com.comexport.Models.User;

import org.springframework.stereotype.Service;

@Service
public class ContactUtils {

    /**
	 * The toContactOutputDTO(Contact) method will transform a Contact into a ContactOutputDTO.
	 * 
	 * @param contact			- The Contact to be transformed.
	 * @return ContactOutputDTO - The transformed ContactOutputDTO.
	 */
	public ContactOutputDTO toContactOutputDTO(Contact contact) {
	
		return ContactOutputDTO
				.builder()
				.id(contact.getId())
				.type(contact.getType())
				.detail(contact.getDetail())							
				.creationDate(contact.getCreationDate())
				.lastUpdate(contact.getLastUpdate())
				.build();
	}
	
	/**
	 * The toContact(ContactInputDTO) method will transform a ContactInputDTO into a Contact.
	 * 
	 * @param contact			- The ContactInputDTO to be transformed.
	 * @return Contact			- The transformed Contact.
	 */
	public Contact toContact(ContactInputDTO contact) {
		return Contact
                .builder()
                .owner(contact.getOwner().getId())
				.type(contact.getType())
				.detail(contact.getDetail())							
				.creationDate(contact.getCreationDate())
				.lastUpdate(contact.getLastUpdate())
				.build();
    }

}
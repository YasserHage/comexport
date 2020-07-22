package com.comexport.Transformations;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.comexport.DTOs.ContactInputDTO;
import com.comexport.DTOs.ContactOutputDTO;
import com.comexport.Models.Contact;
import com.comexport.Utils.ContactUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactTransformation {
    
    /**
	 * the ContactUtils class is a kind of utilitary class regarding the contact's Model and it's DTOs.
	 */
	@Autowired
	private ContactUtils contactUtils;
	
	/**
	 * This convert(Contact) method will transform a Contact into a ContactOutputDTO.
	 * 
	 * @param contact			- the Contact that will be transformed into a ContactOutputDTO.
	 * @return ContactOutputDTO - the transformed ContactOutputDTO.
	 */
	public ContactOutputDTO convert(Contact contact) {
		return this.contactUtils.toContactOutputDTO(contact);
	}
	
	/**
	 * This convert(ContactInputDTO) method will transform a ContactInputDTO into a Contact.
	 * 
	 * @param contact			- the ContactInputDTO that will be transformed into a Contact.
	 * @return Contact			- the transformed Contact.
	 */
	public Contact convert(ContactInputDTO contact) {
		return this.contactUtils.toContact(contact);
	}
	
	/**
	 * It will transform Collection<Contact> into List<ContactOutputDTO>.
	 * 
	 * @param contact				- the collection that will be transformed into List<ContactOutputDTO>.
	 * @return List<ContactOutputDTO>	- the transformed List<ContactOutputDTO>.
	 */
	public List<ContactOutputDTO> convert(Collection<Contact> contacts){
		return contacts
				.stream()
				.map(this :: convert)
				.collect(Collectors.toList());
    }
    
}
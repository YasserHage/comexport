package com.comexport.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.comexport.Models.Contact;
import com.comexport.Repositories.ContactRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    
    /** Meant to run all CRUD like verbs.*/
	@Autowired
	private ContactRepository contactRepository;
	
	/**
	* The findAll() method will retrieve all contacts from the database.
	*
	* @return List<Contact> - all contacts. 
	*/
	public List<Contact> findAll(){
		// Collection containing all contacts
		List<Contact> contacts = new ArrayList<>();
		
		// Populating the contact list with all contacts in the DB
        this.contactRepository.findAll().forEach(contacts :: add);
        
        return contacts;	
	}
	
	/**
	 * The findById(Integer) method will find a contact by it's id.
	 * 
	 * @param id				- the contact's primary key. 
	 * @return Optional<Contact>  - the contact if it exists.
	 */
	public Optional<Contact> findById(Integer id){
		return this.contactRepository.findById(id);
	}
	
	/**
	 * The save(Contact) method will create or update a contact into the database.
	 * 
	 * @param contact			- the contact to be saved.
	 * @return Contact			- the saved contact.
	 */
	public Contact save(Contact contact) {
		return this.contactRepository.save(contact);
	}
	
	/**
	 * The delete(Integer) method will delete a contact by it's id.
	 * 
	 * @param id			- the contact's primary key.
	 */
	public void delete(Integer id) {
		this.contactRepository.deleteById(id);
    }
    
}
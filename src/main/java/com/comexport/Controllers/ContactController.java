package com.comexport.Controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.comexport.DTOs.ContactInputDTO;
import com.comexport.DTOs.ContactOutputDTO;
import com.comexport.Models.Contact;
import com.comexport.Services.ContactService;
import com.comexport.Transformations.ContactTransformation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/comexport/contacts")
public class ContactController {
    
    /**Logger from ContactController.*/
	private Logger logger = LogManager.getLogger(ContactController.class);
	
    /**
    * ContactService class meant to run all CRUD verbs.
    */
    @Autowired
    private ContactService contactService;
   
    /**
    * ContactTransformation used as an utility regarding the contact's transformation.
    */
    @Autowired
    private ContactTransformation contactTransformation;
   
    /**
    * The get() method will settup a Http Get Request endpoint that will return a list 
    * with all the contacts. The list can be filtered with the owner
    *
    * @param owner           - An optional filter regarding the owner.
    * @return ResponseEntity - A 200 OK with an ContactOutputDTO list 
    * if it have found at least one contact or a 404 Not Found if it haven't.
    */
    @GetMapping
    @ApiOperation(value = "Get a list of contacts that may be filtered")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved at least one contact"),
        @ApiResponse(code = 404, message = "No contact were found on the database")
    })
    public ResponseEntity get(Optional<Integer> owner) {
        logger.info("Fetching contacts from database...");
        List<ContactOutputDTO> contacts =
            contactTransformation
                .convert(this.contactService
                .findAll()
                .stream()
                .filter(c -> c.getOwner() == (owner.isPresent() ? owner.get() : c.getOwner()))
                .collect(Collectors.toList()));
        if(contacts.isEmpty()) {
            // If no contact was found.
            logger.info("There is no contact on database.");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        logger.info("Fetched {} contacts.", contacts.size());
        return new ResponseEntity(contacts, HttpStatus.OK);
    }
   
    /**
    * The get(Integer) method will settup a Http Get Request endpoint that will return a single 
    * contact with a given id.
    * 
    * @param  id              - The id of the contact to be fetched. 
    * @return ResponseEntity  - A 200 OK with a ContactOutputDTO list if it have found a contact 
    * with the given id or a 404 Not Found if it haven't.
    */
    @GetMapping("/{id}")
    @ApiOperation(value = "Get an contact by it's id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved the contact"),
        @ApiResponse(code = 404, message = "The contact was not found on the database")
    })
    public ResponseEntity get(@PathVariable Integer id) {

        logger.info("Fetching contact {} from database...", id);
        
        //Fetching contact from database
        Optional<Contact> fetchedContact = this.contactService.findById(id);
        
        if(!fetchedContact.isPresent()) {
            // If no contact was found.
            logger.info("There is no contact with id {} on database.", id);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        
        // If there is a fetched contact from database
        ContactOutputDTO contactOutputDTO = contactTransformation.convert(fetchedContact.get());
        logger.info("Fetched {}.", contactOutputDTO);
        return new ResponseEntity(contactOutputDTO, HttpStatus.OK);
    }
   
    /**
    * The post(ContactInputDTO) method will settup a Http Post Request endpoint that will create 
    * a new contact.
    * 
    * @param  contactInputDTO    - The contact to be created. 
    * @return ResponseEntity     - A 201 Created.
    */
    @PostMapping
    @ApiOperation(value = "Insert a new contact into the database")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created the contact")
    })
    public ResponseEntity post(@RequestBody ContactInputDTO contactInputDTO) {

        Contact contact = contactTransformation.convert(contactInputDTO);
        logger.info("Adding a new contact {} to the database", contact);
        
        // Saving the contact!
        this.contactService.save(contact);

        //Building the location header
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(contact.getId())
            .toUri();
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        logger.info("Contact created with id {}", contact.getId());
        return new ResponseEntity(responseHeaders, HttpStatus.CREATED);
    }
   
    /**
    * The put(ContactInputDTO, Integer) method will settup a Http Put Request endpoint 
    * that will replace an existing contact with a given id by the new given contact.
    * 
    * @param  id              - The id of the contact to be replaced. 
    * @param  contactInputDTO    - the contact to be replaced.
    * @return ResponseEntity  - A 204 No Content if the Update was successfull
    *  or a 404 Not Found if no contact with the given id was found.
    */
    @PutMapping("/{id}")
    @ApiOperation(value = "Update a contact on the database")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Successfully updated the contact"),
        @ApiResponse(code = 404, message = "The contact to be updated was not found")
    })
    public ResponseEntity put(@RequestBody ContactInputDTO contactInputDTO, @PathVariable Integer id) {
        
        Contact contact = contactTransformation.convert(contactInputDTO);
        
        // Fetching contact from database layer...
        Optional<Contact> fetchedContact = this.contactService.findById(id);

        if (!fetchedContact.isPresent()) {
             // If no contact was found.
             logger.info("There is no contact with id {} on database.", id);
             return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        // Updating the fetched contact's values...
        fetchedContact.get().setType(contact.getType());
        fetchedContact.get().setDetail(contact.getDetail());
        fetchedContact.get().setOwner(contact.getOwner());
        
        
        logger.info("Updating the contact {} on database...", contact);
        contactService.save(fetchedContact.get());

        logger.info("Update completed");
        return new ResponseEntity(HttpStatus.NO_CONTENT);        
    }
   
    /**
    * The delete(ContactInputDTO, Integer) method will settup a Http Delete Request endpoint 
    * that will delete an existing contact with a given id.
    * 
    * @param  id              - The id of the contact to be deleted.
    * @return ResponseEntity  - A 204 No Content if the Deletion was successfull.
    */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete an contact into the database")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Successfully deleted the contact")
    })
    public ResponseEntity delete(@PathVariable Integer id) {
        logger.info("Deleting the contact with id {} from the database...", id);
        contactService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
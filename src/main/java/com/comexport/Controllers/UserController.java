package com.comexport.Controllers;


import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.comexport.DTOs.ContactInputDTO;
import com.comexport.DTOs.ContactOutputDTO;
import com.comexport.DTOs.UserInputDTO;
import com.comexport.DTOs.UserOutputDTO;
import com.comexport.Models.Contact;
import com.comexport.Models.User;
import com.comexport.Services.ContactService;
import com.comexport.Services.UserService;
import com.comexport.Transformations.ContactTransformation;
import com.comexport.Transformations.UserTransformation;

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

@RestController
@RequestMapping("/comexport/users")
public class UserController {

    /**Logger from UserController.*/
	private Logger logger = LogManager.getLogger(UserController.class);
    
    /** 
    * ContactService class meant to run all CRUD verbs.
    */
    @Autowired
    private ContactService contactService;

    /**
    * UserService class meant to run all CRUD verbs.
    */
    @Autowired
    private UserService userService;
   
    /**
    * ContactTransformation class used as an utility regarding the contact's transformation.
    */
    @Autowired
    private ContactTransformation contactTransformation;

    /**
    * UserTransformation class used as an utility regarding the user's transformation.
    */
    @Autowired
    private UserTransformation userTransformation;
   
    /**
    * The get() method will settup a Http Get Request endpoint that will return a list 
    * with all the users. The list can be filtered with the users name, email and date of birth.
    * @param name            - An optional filter regarding the user's name.
    * @param email           - An optional filter regarding the user's email.
    * @param dateOfBirth     - An optional filter regarding the user's date of birth in milliseconds.
    * @return ResponseEntity - A 200 OK with an UserOutputDTO list 
    * if it have found at least one user or a 404 Not Found if it haven't.
    */
    @GetMapping
    public ResponseEntity get(Optional<String> name, Optional<String> email, Optional<Long> dateOfBirth) {
        logger.info("Fetching users from database...");
        List<UserOutputDTO> users =
            userTransformation
                .convert(this.userService
                .findAll()
                .stream()
                .filter(u -> u.getName().equals(name.isPresent() ? name.get() : u.getName()))
                .filter(u -> u.getEmail().equals(email.isPresent() ? email.get() : u.getEmail()))
                .filter(u -> u.getDateOfBirth().getTime() == (dateOfBirth.isPresent() ? dateOfBirth.get() : u.getDateOfBirth().getTime()))
                .collect(Collectors.toList()));
        if(users.isEmpty()) {
            // If no user was found.
            logger.info("There is no user on database.");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        // Fetching contacts from database
        List<Contact> contacts = contactService.findAll();
        for (UserOutputDTO user : users) {
            List<ContactOutputDTO> contactOutputDTOs = contactTransformation.convert(contacts
                .stream()
                .filter(c -> c.getOwner() == user.getId())
                .collect(Collectors.toList()));
            user.setContacts(contactOutputDTOs);
        }


        logger.info("Fetched {} users.", users.size());
        return new ResponseEntity(users, HttpStatus.OK);
    }
   
    /**
    * The get(Integer) method will settup a Http Get Request endpoint that will return a single 
    * user with a given id.
    * 
    * @param  id              - The id of the user to be fetched. 
    * @return ResponseEntity  - A 200 OK with an UserOutputDTO list if it have found an user 
    * with the given id or a 404 Not Found if it haven't.
    */
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Integer id) {

        logger.info("Fetching user {} from database...", id);
        
        //Fetching user from database
        Optional<User> fetchedUser = this.userService.findById(id);
        
        if(!fetchedUser.isPresent()) {
            // If no user was found.
            logger.info("There is no user with id {} on database.", id);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        
        //Fetching user's contacts from database
        List<ContactOutputDTO> contacts =
            contactTransformation.convert(
                this.contactService
                .findAll()
                .stream()
                .filter(c -> c.getOwner() == fetchedUser.get().getId())
                .collect(Collectors.toList()));

        // If there is a fetched user from database
        UserOutputDTO userOutputDTO = userTransformation.convert(fetchedUser.get());
        userOutputDTO.setContacts(contacts);
        logger.info("Fetched {}.", userOutputDTO);
        return new ResponseEntity(userOutputDTO, HttpStatus.OK);
    }
   
    /**
    * The post(UserInputDTO) method will settup a Http Post Request endpoint that will create 
    * a new user.
    * 
    * @param  userInputDTO    - The user to be created. 
    * @return ResponseEntity  - A 201 Created if the required fields are filled 
    *  or a 400 Bad Request if they aren't.
    */
    @PostMapping
    public ResponseEntity post(@RequestBody UserInputDTO userInputDTO) {
        
        if(userInputDTO.getName() == null || userInputDTO.getEmail() == null || userInputDTO.getDateOfBirth() == null) {
            // If any required field is empty .
            logger.info("You need to inform the user Name, Email and Date of Birth.");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User user = userTransformation.convert(userInputDTO);
        logger.info("Adding a new user {} to the database", user);
        
        // Saving the user!
        this.userService.save(user);
        
        if(!userInputDTO.getContacts().isEmpty()){
            List<ContactInputDTO> contacts = userInputDTO.getContacts();
            // Saving the user contacts
            for (ContactInputDTO contact : contacts) {
                contact.setOwner(user);
                contactService.save(contactTransformation.convert(contact));
            }
        }
        //Building the location header
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(user.getId())
            .toUri();
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);

        logger.info("User created with id {}", user.getId());
        return new ResponseEntity(responseHeaders, HttpStatus.CREATED);
    }
   
    /**
    * The put(UserInputDTO, Integer) method will settup a Http Put Request endpoint 
    * that will replace an existing user with a given id by the new given user.
    * 
    * @param  id              - The id of the user to be replaced. 
    * @param  userInputDTO    - the user to be replaced.
    * @return ResponseEntity  - A 204 No Content if the Update was successfull
    *  or a 404 Not Found if no user with the given id was found.
    */
    @PutMapping("/{id}")
    public ResponseEntity put(@RequestBody UserInputDTO userInputDTO, @PathVariable Integer id) {
        
        User user = userTransformation.convert(userInputDTO);
        
        // Fetching user from database layer...
        Optional<User> fetchedUser = this.userService.findById(id);

        if (!fetchedUser.isPresent()) {
             // If no user was found.
             logger.info("There is no user with id {} on database.", id);
             return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        // Updating the fetched user's values
        fetchedUser.get().setEmail(user.getEmail());
        fetchedUser.get().setDateOfBirth(user.getDateOfBirth());
        fetchedUser.get().setAddress(user.getAddress());

        logger.info("Updating the user {} on database...", user);
        userService.save(fetchedUser.get());

        logger.info("Update completed");
        return new ResponseEntity(HttpStatus.NO_CONTENT);        
    }
   
    /**
    * The delete(UserInputDTO, Integer) method will settup a Http Delete Request endpoint 
    * that will delete an existing user with a given id.
    * 
    * @param  id              - The id of the user to be deleted.
    * @return ResponseEntity  - A 204 No Content if the Deletion was successfull.
    */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        logger.info("Deleting the user with id {} from the database...", id);
        
        // Fetching user's contacts
        List<Contact> contacts = this.contactService
        .findAll()
        .stream()
        .filter(c -> c.getOwner() == id)
        .collect(Collectors.toList());

        // Deleting user's contacts
        for (Contact contact : contacts) {
            contactService.delete(contact.getId());
        }

        logger.info("Deletion completed.");
        // Deleting user
        userService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
package com.comexport.DTOs;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserOutputDTO {

        /** The user's primary key. */     
        private Integer id = null;
        
        /** The user's name. */
        private String name = null;
    
        /** The user's email address. */
        private String email = null;
    
        /** The user's age. */
        private Integer age = null;
    
        /** The user's address. */
        private String address = null;

        /** The user's contact list */
        private List<ContactOutputDTO> contacts = null;
        
        /** The user's creation date. */
        private Date creationDate = null;
        
        /** The user's last update date. */
        private Date lastUpdate = null; 
}
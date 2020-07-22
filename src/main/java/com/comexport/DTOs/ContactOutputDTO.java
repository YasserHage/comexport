package com.comexport.DTOs;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactOutputDTO {
    
     /** The contact's primary key. */     
     private Integer id = null;

     /** The contact type */
     private String type = null;
 
     /** Some details about the contact */
     private String detail = null;
 
     /** The contact's creation date. */
     private Date creationDate = null;
     
     /** The contact's last update date. */
     private Date lastUpdate = null; 

}
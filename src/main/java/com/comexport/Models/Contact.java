package com.comexport.Models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    
    /** The contact's primary key. */     
    @Id   
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;
    
    /** The User to be contacted */
    private Integer owner = null;
    
    /** The contact type */
    private String type = null;

    /** Some details about the contact */
    private String detail = null;

    /** The contact's creation date. */
    private Date creationDate = null;
    
    /** The contact's last update date. */
    private Date lastUpdate = null; 
}
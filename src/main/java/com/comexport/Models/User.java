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
public class User {

    /** The user's primary key. */     
    @Id   
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;
    
    /** The user's name. */
    private String name = null;

    /** The user's email address. */
    private String email = null;

    /** The user's date of birth. */
    private Date dateOfBirth = null;

    /** The user's address. */
    private String address = null;
    
    /** The user's creation date. */
    private Date creationDate = null;
    
    /** The user's last update date. */
    private Date lastUpdate = null; 
}
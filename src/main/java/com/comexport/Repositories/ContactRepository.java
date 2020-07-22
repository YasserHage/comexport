package com.comexport.Repositories;

import com.comexport.Models.Contact;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer>{
    
}
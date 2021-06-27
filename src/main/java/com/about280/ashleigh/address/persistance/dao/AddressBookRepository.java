package com.about280.ashleigh.address.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.about280.ashleigh.address.persistance.model.AddressBook;

public interface AddressBookRepository extends JpaRepository<AddressBook, Long>  {


}

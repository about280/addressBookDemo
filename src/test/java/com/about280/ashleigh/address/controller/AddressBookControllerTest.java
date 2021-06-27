package com.about280.ashleigh.address.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.about280.ashleigh.address.persistance.model.AddressBook;
import com.about280.ashleigh.address.persistance.model.AddressBookEntry;
import com.about280.ashleigh.address.service.AddressBookServiceInterface;

@ExtendWith(SpringExtension.class)
public class AddressBookControllerTest {
	
	@Mock
	private AddressBookServiceInterface addressBookService;
	
	@InjectMocks
	AddressBookController unitUnderTest = new AddressBookController();
	
	@Test
	void testNotFoundFromService() throws Exception {
		when(addressBookService.getAddressBook(eq(1l))).thenReturn(Optional.empty());
		ResponseEntity<AddressBook> entity = unitUnderTest.getAddressBook(1l);
		assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
	}
	
	@Test
	void testFoundFromService() throws Exception {
		when(addressBookService.getAddressBook(eq(1l))).thenReturn(Optional.of(new AddressBook()));
		ResponseEntity<AddressBook> entity = unitUnderTest.getAddressBook(1l);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}
	
	@Test
	void testGetUniqueAddressBookEntries() throws Exception {
		when(addressBookService.getUniqueAddressBookEntries()).thenReturn(Collections.emptyList());
		ResponseEntity<List<AddressBookEntry>> entity = unitUnderTest.getUniqueAddressBookEntries();
		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}
	
	@Test
	void testDeleteEntryNotFound() throws Exception {
		when(addressBookService.getAddressBook(eq(1l))).thenReturn(Optional.empty());
		ResponseEntity<Void> entity = unitUnderTest.deleteAddressBookEntry(1l, 1l);
		assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
	}
	
	@Test
	void testDeleteEntrySuccess() throws Exception {
		AddressBookEntry entry = new AddressBookEntry();
		entry.setId(1l);
		entry.setName("test");
		entry.setPhoneNumber("test");
		AddressBook addressBook = new AddressBook();
		
		addressBook.setAddressBookEntries(Collections.singletonList(entry));
		
		when(addressBookService.getAddressBook(eq(1l))).thenReturn(Optional.of(addressBook));
		ResponseEntity<Void> entity = unitUnderTest.deleteAddressBookEntry(1l, 1l);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}
	
	@Test
	void testAddAddressBookEntryNotFound() throws Exception {
		AddressBookEntry entry = new AddressBookEntry();
		entry.setName("test");
		entry.setPhoneNumber("test");
		when(addressBookService.getAddressBook(eq(1l))).thenReturn(Optional.empty());
		ResponseEntity<AddressBookEntry> entity = unitUnderTest.addAddressBookEntry(1l, entry);
		assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
	}
	
	@Test
	void testAddAddressBookEntryConflict() throws Exception {
		AddressBookEntry entry = new AddressBookEntry();
		entry.setId(1l);
		entry.setName("test");
		entry.setPhoneNumber("test");
		AddressBook addressBook = new AddressBook();
		
		addressBook.setAddressBookEntries(Collections.singletonList(entry));
		
		when(addressBookService.getAddressBook(eq(1l))).thenReturn(Optional.of(addressBook));
		ResponseEntity<AddressBookEntry> entity = unitUnderTest.addAddressBookEntry(1l, entry);
		assertEquals(HttpStatus.CONFLICT, entity.getStatusCode());
	}
	
	@Test
	void testAddAddressBookEntrySuccess() throws Exception {
		AddressBookEntry entry = new AddressBookEntry();
		entry.setId(1l);
		entry.setName("test");
		entry.setPhoneNumber("test");
		AddressBook addressBook = new AddressBook();
		
		addressBook.setAddressBookEntries(Collections.singletonList(entry));
		
		when(addressBookService.getAddressBook(eq(1l))).thenReturn(Optional.of(addressBook));
		when(addressBookService.saveAddressBookEntry(any(), any())).thenReturn(entry);
		ResponseEntity<AddressBookEntry> entity = unitUnderTest.addAddressBookEntry(1l, entry);
		assertEquals(HttpStatus.CREATED, entity.getStatusCode());
	}
	
	@Test
	void testAddAddressBook() throws Exception {
		AddressBook addressBook = new AddressBook();
		addressBook.setId(1l);
		when(addressBookService.saveAddressBook(any())).thenReturn(addressBook);
		ResponseEntity<AddressBook> entity = unitUnderTest.addAddressBook(addressBook);
		assertEquals(HttpStatus.CREATED, entity.getStatusCode());
	}
	
}

package com.about280.ashleigh.address.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.about280.ashleigh.address.persistance.dao.AddressBookEntryRepository;
import com.about280.ashleigh.address.persistance.dao.AddressBookRepository;
import com.about280.ashleigh.address.persistance.model.AddressBook;
import com.about280.ashleigh.address.persistance.model.AddressBookEntry;

@ExtendWith(SpringExtension.class)
public class AddressBookServiceTest {

	@Mock
	private AddressBookRepository addressBookRepository;
	
	@Mock
	private AddressBookEntryRepository addressBookEntryRepository;
	
	private AddressBookService unitUnderTest;
	
	@BeforeEach
	public void setup()
	{
		unitUnderTest = new AddressBookService(addressBookEntryRepository,
				addressBookRepository);
	}
	
	@Test
	void testSaveAddressBookEntry() {
		AddressBookEntry entry = new AddressBookEntry();
		entry.setName("test");
		entry.setPhoneNumber("test");
		AddressBook addressBook = new AddressBook();
		addressBook.setAddressBookEntries(Collections.singletonList(entry));
		
		AddressBookEntry savedEntry = unitUnderTest.saveAddressBookEntry(addressBook, entry);
		assertNull(savedEntry);
	}
	
	@Test
	void testSaveAddressBookEntrySuccess() {
		AddressBookEntry entry = new AddressBookEntry();
		entry.setName("test");
		entry.setPhoneNumber("test");
		AddressBook addressBook = new AddressBook();
		addressBook.setAddressBookEntries(new ArrayList<AddressBookEntry>());
		
		AddressBook addressBook2 = new AddressBook();
		addressBook2.setAddressBookEntries(Collections.singletonList(entry));

		when(addressBookRepository.save(any())).thenReturn(addressBook2);
		
		AddressBookEntry savedEntry = unitUnderTest.saveAddressBookEntry(addressBook, entry);
		assertNotNull(savedEntry);
	}

	@Test
	void testGetUniqueAddressBookEntries() {
		AddressBookEntry entry = new AddressBookEntry();
		entry.setName("test");
		entry.setPhoneNumber("test");
		
		AddressBookEntry entry2 = new AddressBookEntry();
		entry2.setName("test");
		entry2.setPhoneNumber("test");
		
		AddressBookEntry entry3 = new AddressBookEntry();
		entry3.setName("testa");
		entry3.setPhoneNumber("test");
		
		
		List<AddressBookEntry> addressBookEntries = new ArrayList<AddressBookEntry>();
		addressBookEntries.add(entry);
		addressBookEntries.add(entry2);
		addressBookEntries.add(entry3);
		
		when(addressBookEntryRepository.findAll()).thenReturn(addressBookEntries);
		List<AddressBookEntry> addressBookEntires = unitUnderTest.getUniqueAddressBookEntries();
		assertEquals(2, addressBookEntires.size());
	}

}

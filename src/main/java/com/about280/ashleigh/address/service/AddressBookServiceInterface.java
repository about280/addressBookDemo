package com.about280.ashleigh.address.service;

import java.util.List;
import java.util.Optional;

import com.about280.ashleigh.address.persistance.model.AddressBook;
import com.about280.ashleigh.address.persistance.model.AddressBookEntry;

public interface AddressBookServiceInterface {
	
	public void deleteAddressBookEntry(AddressBookEntry addressBookEntry);
	
	public AddressBook saveAddressBook(AddressBook addressBook);
	
	public AddressBookEntry saveAddressBookEntry(AddressBook addressBook, AddressBookEntry addressBookEntry);
	
	public AddressBookEntry getAddressBookEntry(Long addressBookEntryId);
	
	public Optional<AddressBook> getAddressBook(Long addressBookId);
	
	public List<AddressBookEntry> getUniqueAddressBookEntries();
}

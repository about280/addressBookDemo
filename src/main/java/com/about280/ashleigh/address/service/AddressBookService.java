package com.about280.ashleigh.address.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.about280.ashleigh.address.persistance.dao.AddressBookEntryRepository;
import com.about280.ashleigh.address.persistance.dao.AddressBookRepository;
import com.about280.ashleigh.address.persistance.model.AddressBook;
import com.about280.ashleigh.address.persistance.model.AddressBookEntry;

@Service
public class AddressBookService implements AddressBookServiceInterface {

	private final AddressBookRepository addressBookRepository;
	
	private final AddressBookEntryRepository addressBookEntryRepository;
	
	@Autowired
	public AddressBookService(AddressBookEntryRepository addressBookEntryRepository,
			AddressBookRepository addressBookRepository) {
		this.addressBookEntryRepository = addressBookEntryRepository;
		this.addressBookRepository = addressBookRepository;
	}
	
	public void deleteAddressBookEntry(AddressBookEntry addressBookEntry) {
		addressBookEntryRepository.delete(addressBookEntry);
	}
	
	public AddressBook saveAddressBook(AddressBook addressBook) {
		return addressBookRepository.save(addressBook);
	}
	
	public AddressBookEntry saveAddressBookEntry(AddressBook addressBook, AddressBookEntry addressBookEntry) {
		String nameAndPhoneNumber = addressBookEntry.getName() + addressBookEntry.getPhoneNumber();
		AddressBookEntry addressBookEntryExisting = addressBook.getAddressBookEntries().stream()
				.filter(entry -> nameAndPhoneNumber.equals(entry.getName() + entry.getPhoneNumber())).findFirst().orElse(null);
		// check that we don't already have this entry
		if (addressBookEntryExisting != null) {
			return null;
		}
		addressBook.getAddressBookEntries().add(addressBookEntry);
		AddressBook savedAddressBook = addressBookRepository.save(addressBook);
		return savedAddressBook.getAddressBookEntries().stream()
				.filter(entry -> nameAndPhoneNumber.equals(entry.getName() + entry.getPhoneNumber()))
				.findFirst().orElse(null);
	}
	
	public AddressBookEntry getAddressBookEntry(Long addressBookEntryId) {
		return addressBookEntryRepository.getById(addressBookEntryId);
	}
	
	public Optional<AddressBook> getAddressBook(Long addressBookId) {
		return addressBookRepository.findById(addressBookId);
	}
	
	public List<AddressBookEntry> getUniqueAddressBookEntries() {
		List<AddressBookEntry> allEntries = addressBookEntryRepository.findAll();
		return allEntries.stream().filter(distinctByKey(p -> p.getName() + p.getPhoneNumber()))
				.collect(Collectors.toList());
	}
	
	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    final Set<Object> seen = new HashSet<>();
	    return t -> seen.add(keyExtractor.apply(t));
	}

}

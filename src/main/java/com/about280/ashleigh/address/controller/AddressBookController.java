package com.about280.ashleigh.address.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.about280.ashleigh.address.persistance.model.AddressBook;
import com.about280.ashleigh.address.persistance.model.AddressBookEntry;
import com.about280.ashleigh.address.service.AddressBookServiceInterface;

import io.swagger.annotations.ApiOperation;

@RestController
public class AddressBookController {

	@Autowired
	private AddressBookServiceInterface addressBookService;

	@ApiOperation(value = "Retrieve address book", notes = "Retrieve a single address book for a given id, including all entries")
	@GetMapping(value = "/addressBooks/{addressBookId}")
	public ResponseEntity<AddressBook> getAddressBook(@PathVariable Long addressBookId) {
		Optional<AddressBook> addressBook = addressBookService.getAddressBook(addressBookId);
		if (addressBook.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
        return ResponseEntity.ok().body(addressBook.get());
	}
	
	@ApiOperation(value = "Retrieve address book entries", notes = "Retrieve all unique address book entries, regardless of address book")
	@GetMapping(value = "/addressBookEntries")
	public ResponseEntity<List<AddressBookEntry>> getUniqueAddressBookEntries() {
        return ResponseEntity.ok().body(addressBookService.getUniqueAddressBookEntries());
	}
	
	@ApiOperation(value = "Delete an address book entry")
	@DeleteMapping(value = "/addressBooks/{addressBookId}/entry/{addressBookEntryId}")
	public ResponseEntity<Void> deleteAddressBookEntry(@PathVariable Long addressBookId, @PathVariable Long addressBookEntryId) {
		Optional<AddressBook> addressBook = addressBookService.getAddressBook(addressBookId);
		if (addressBook.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AddressBookEntry addressBookEntry = addressBook.get().getAddressBookEntries().stream()
				.filter(entry -> addressBookEntryId.equals(entry.getId())).findFirst().orElse(null);
		if (addressBookEntry == null) {
			return ResponseEntity.notFound().build();
		}
		addressBookService.deleteAddressBookEntry(addressBookEntry);
        return ResponseEntity.ok().build();
	}
	
	@ApiOperation(value = "Add an address book entry")
	@PutMapping(value = "/addressBooks/{addressBookId}/entry")
	public ResponseEntity<AddressBookEntry> addAddressBookEntry(@PathVariable Long addressBookId,
			@RequestBody AddressBookEntry addressBookEntry) {
		Optional<AddressBook> addressBook = addressBookService.getAddressBook(addressBookId);
		if (addressBook.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		AddressBookEntry addressBookEntrySaved = addressBookService.saveAddressBookEntry(addressBook.get(), addressBookEntry);
		if (addressBookEntrySaved == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
        return ResponseEntity.created(URI.create("/addressBooks/" + addressBookId + "/entry/" + addressBookEntrySaved.getId()))
        		.body(addressBookEntrySaved);
	}
	   
	@ApiOperation(value = "Add a new address book")
	@PutMapping(value = "/addressBooks")
	public ResponseEntity<AddressBook> addAddressBook(@RequestBody AddressBook addressBook) {
		AddressBook addressBookSaved = addressBookService.saveAddressBook(addressBook);
        return ResponseEntity.created(URI.create("/addressBooks/" + addressBookSaved.getId())).body(addressBookSaved);
	}
}

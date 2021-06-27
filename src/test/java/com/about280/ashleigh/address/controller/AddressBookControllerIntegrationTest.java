package com.about280.ashleigh.address.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.about280.ashleigh.address.persistance.model.AddressBook;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class AddressBookControllerIntegrationTest {
	
	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;
	
	private ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	public void setUp(RestDocumentationContextProvider restDocumentation) {
		this.mvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(documentationConfiguration(restDocumentation)).build();
	}
	
	@Test
	void testErrorFromClient() throws Exception {
		mvc.perform(get("/addressBooks/adffdfad")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().is4xxClientError());
	}
	
	@Test
	void testGetAddressBooksById() throws Exception {
		mvc.perform(get("/addressBooks/432342234")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isNotFound());
	}
	
	@Test
	void testAllAddressBookEntries() throws Exception {
		mvc.perform(get("/addressBookEntries")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	@Test
	void testDeeleteAddressBookEntryById() throws Exception {
		mvc.perform(delete("/addressBooks/1111/entry/11111")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isNotFound());
	}
	
	@Test
	void testAddAddressBooksEntry() throws Exception {
		mvc.perform(put("/addressBooks/432342234")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().is4xxClientError());
	}
	
	@Test
	void testAddAddressBook() throws Exception {
		AddressBook addressBook = new AddressBook();
		mvc.perform(put("/addressBooks")
				  .content(mapper.writeValueAsString(addressBook))
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isCreated());
	}

}

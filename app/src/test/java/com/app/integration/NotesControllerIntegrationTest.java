package com.app.integration;

import com.app.dao.NotesDao;
import com.app.model.Note;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class NotesControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private NotesDao notesDao;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		notesDao.deleteAll();
	}

	@Test
	public void givenNoteObject_whenCreateNote_thenReturnSavedNote() throws Exception {
		// setup
		Note note = Note.builder().title("Not spring related").createdBy("user1").text("Notes on kubernetes").build();

		ResultActions response = mockMvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(note)));

		response.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.title", is(note.getTitle())))
				.andExpect(jsonPath("$.createdBy", is(note.getCreatedBy())))
				.andExpect(jsonPath("$.text", is(note.getText())));

	}

	@Test
	public void givenNotesList_whenCreateNote_thenReturnSavedNotes() throws Exception {
		// setup
		Note note1 = Note.builder().title("Not spring related").createdBy("user1").text("Notes on kubernetes").build();
		Note note2 = Note.builder().title("Not spring related").createdBy("user2").text("Notes on Docker").build();
		List<Note> notes = new ArrayList<>(List.of(note1, note2));

		ResultActions response = mockMvc.perform(post("/api/notes/addnotes").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(notes)));

		response.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.size()", is(notes.size())))
				.andExpect(jsonPath("$[1].title", is(note2.getTitle())))
				.andExpect(jsonPath("$[1].createdBy", is(note2.getCreatedBy())))
				.andExpect(jsonPath("$[1].text", is(note2.getText())));

	}

	@Test
	public void whenGetAllNotes_thenReturnNotesList() throws Exception {
		// setup
		Note note1 = Note.builder().title("Not spring related").createdBy("user1").text("Notes on Spring Boot").build();
		Note note2 = Note.builder().title("Not spring related").createdBy("user2").text("Notes on Spring MVC").build();
		Note note3 = Note.builder().title("Not spring related").createdBy("user2").text("Notes on Spring Security")
				.build();
		List<Note> notes = new ArrayList<>(List.of(note1, note2, note3));
		notesDao.saveAll(notes);

		ResultActions response = mockMvc.perform(get("/api/notes"));

		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(notes.size())));

	}

	// positive scenario - valid note user
	@Test
	public void whenGetNoteByUser_thenReturnNoteObject() throws Exception {
		// setup
		Note note1 = Note.builder().title("Not spring related").createdBy("user1").text("Notes on Spring Boot").build();
		notesDao.save(note1);

		ResultActions response = mockMvc.perform(get("/api/notes/byuser/{user}", note1.getCreatedBy()));

		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(1)))
				.andExpect(jsonPath("$[0].title", is(note1.getTitle())))
				.andExpect(jsonPath("$[0].createdBy", is(note1.getCreatedBy())))
				.andExpect(jsonPath("$[0].text", is(note1.getText())));

	}

	// negative scenario - note user has no notes
	@Test
	public void whenGetNoteByUser_thenReturnEmpty() throws Exception {
		// given - precondition or setup
		long noteId = 1L;
		Note note1 = Note.builder().title("Not spring related").createdBy("user1").text("Notes on Spring Boot").build();
		notesDao.save(note1);

		ResultActions response = mockMvc.perform(get("/api/notes/byuser/{user}", noteId));

		response.andExpect(status().is2xxSuccessful()).andExpect(status().isNoContent()).andDo(print());

	}

	@Test
	public void whenSearchNote_thenReturnResultsmarchingInTitleOrText() throws Exception {
		// given - precondition or setup
		long noteId = 1L;
		Note note1 = Note.builder().title("Not spring related").createdBy("user1").text("Notes on Docker").build();
		Note note2 = Note.builder().title("Not spring related").createdBy("user1").text("Notes on kubernetes").build();
		Note note3 = Note.builder().title("MVC").createdBy("user2").text("Notes on Spring MVC").build();
		Note note4 = Note.builder().title("Security").createdBy("user2").text("Notes on Spring Security").build();
		List<Note> notes = new ArrayList<>(List.of(note1, note2, note3, note4));
		notesDao.saveAll(notes);

		ResultActions response = mockMvc.perform(get("/api/notes/search/{keyword}", "spring"));

		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(2)))
				.andExpect(jsonPath("$[0].title", is(note1.getTitle())))
				.andExpect(jsonPath("$[0].createdBy", is(note1.getCreatedBy())))
				.andExpect(jsonPath("$[0].text", is(note1.getText())));

	}

	@Test
	public void whenEditNote_thenReturnUpdateNote() throws Exception {
		// setup
		Note savedNote = Note.builder().title("Not spring related").createdBy("user1").text("Notes on Docker").build();
		notesDao.save(savedNote);

		Note updatedNote = Note.builder().title("Docker Notes").createdBy("user1")
				.text("Notes on Docker - love to use it!").build();

		ResultActions response = mockMvc.perform(put("/api/notes/{id}", savedNote.getId())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedNote)));

		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.title", is(updatedNote.getTitle())))
				.andExpect(jsonPath("$.createdBy", is(updatedNote.getCreatedBy())))
				.andExpect(jsonPath("$.text", is(updatedNote.getText())));
	}

	@Test
	public void whenEditNote_thenReturnNotFound() throws Exception {
		long noteId = 1L;
		Note note1 = Note.builder().title("Not spring related").createdBy("user1").text("Notes on Spring Boot").build();
		notesDao.save(note1);

		Note updatedNote = Note.builder().title("Docker Notes").createdBy("user1")
				.text("Notes on Docker - love to use it!").build();

		ResultActions response = mockMvc.perform(put("/api/notes/{id}", noteId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedNote)));

		response.andExpect(status().is4xxClientError()).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void whenDeleteNote_thenReturn200() throws Exception {
		Note note1 = Note.builder().title("Not spring related").createdBy("user1").text("Notes on Spring Boot").build();
		notesDao.save(note1);

		ResultActions response = mockMvc.perform(delete("/api/notes/{id}", note1.getId()));

		response.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void whenDeleteNoteByUser_thenReturn200() throws Exception {
		Note note1 = Note.builder().title("Not spring related").createdBy("user1").text("Notes on Spring Boot").build();
		Note note2 = Note.builder().title("Not spring related").createdBy("user1").text("Notes on kubernetes").build();
		Note note3 = Note.builder().title("MVC").createdBy("user2").text("Notes on Spring MVC").build();
		Note note4 = Note.builder().title("Security").createdBy("user2").text("Notes on Spring Security").build();
		List<Note> notes = new ArrayList<>(List.of(note1, note2, note3, note4));
		notesDao.saveAll(notes);

		ResultActions response = mockMvc.perform(delete("/api/notes/byuser/{user}", note1.getCreatedBy()));

		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(2)));
	}

}

package com.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.Note;
import com.app.service.NotesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notes")
public class NotesController {

	@Autowired
	NotesService notesService;

	@GetMapping(produces = { "application/json" })
	public ResponseEntity<List<Note>> getAllNotes() throws Exception {
		return notesService.getAllNotes();

	}

	@GetMapping("/byuser/{user}")
	public ResponseEntity<List<Note>> getNotesByUser(@PathVariable("user") String createdBy) throws Exception {
		return notesService.getNotesByUser(createdBy);

	}

	@GetMapping("/bytitle/{title}")
	public ResponseEntity<List<Note>> getNotesByTitle(@PathVariable String title) throws Exception {
		return notesService.getNotesByTitle(title);

	}

	@GetMapping("search/{keyword}")
	public ResponseEntity<List<Note>> searchByKeyword(@PathVariable String keyword) throws Exception {
		return notesService.searchByKeyword(keyword);

	}

	@GetMapping("load")
	public ResponseEntity<List<Note>> addAllData() {
		return notesService.load();

	}

	@PostMapping
	public ResponseEntity<Note> addNote(@Valid @RequestBody Note note) throws Exception {
		return notesService.addNote(note);
	}

	@PostMapping("addnotes")
	public ResponseEntity<List<Note>> addNotes(@RequestBody List<Note> notes) throws Exception {
		return notesService.addNotes(notes);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Note> editNote(@PathVariable Long id, @RequestBody Note note) {
		return notesService.editNote(note, id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable Long id) throws Exception {
		return notesService.deleteById(id);
	}

	@DeleteMapping("/byuser/{user}")
	public ResponseEntity<List<Note>> deleteNote(@PathVariable("user") String createdBy) throws Exception {
		return notesService.deleteNoteByCreatedBy(createdBy);
	}

	@DeleteMapping("/bytitle/{title}")
	public ResponseEntity<List<Note>> deleteNoteByTitle(@PathVariable String title) throws Exception {
		return notesService.deleteNoteByTitle(title);
	}

}

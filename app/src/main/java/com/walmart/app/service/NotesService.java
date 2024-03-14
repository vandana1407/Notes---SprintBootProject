package com.walmart.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.app.dao.NotesDao;
import com.walmart.app.model.Note;

@Service
public class NotesService {

	@Autowired
	NotesDao notesDao;

	public ResponseEntity<List<Note>> getAllNotes() throws Exception {
		try {
			List<Note> notes = new ArrayList<Note>();

			notesDao.findAll().forEach(notes::add);

			if (notes.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(notes, HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception("Get all notes failed");
		}
	}

	public ResponseEntity<List<Note>> getNotesByUser(String createdBy) throws Exception {
		try {
			List<Note> notes = new ArrayList<Note>();

			notesDao.findByCreatedBy(createdBy).forEach(notes::add);

			if (notes.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(notes, HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception("Get notes by user failed");
		}
	}

	public ResponseEntity<List<Note>> getNotesByTitle(String title) throws Exception {
		try {
			List<Note> notes = new ArrayList<Note>();

			notesDao.findByTitle(title).forEach(notes::add);

			if (notes.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(notes, HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception("Get notes by title failed");
		}
	}

	public ResponseEntity<List<Note>> searchByKeyword(String keyword) throws Exception {
		try {
			List<Note> notes = new ArrayList<Note>();

			notesDao.searchByTitleContainingOrTextContaining(keyword, keyword).forEach(notes::add);

			if (notes.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(notes, HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception("Search notes by keyword failed");

		}
	}

	public ResponseEntity<Note> addNote(Note note) throws Exception {
		try {
			notesDao.save(note);
			return new ResponseEntity<>(note, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new Exception("Add note failed");
		}

	}

	public ResponseEntity<List<Note>> addNotes(List<Note> notes) throws Exception {
		try {
			notesDao.saveAll(notes);
			return new ResponseEntity<>(notes, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new Exception("Add notes failed");
		}
	}

	public ResponseEntity<Note> editNote(Note note, Long id) {
		Optional<Note> noteData = notesDao.findById(id);

		if (noteData.isPresent() && noteData.get().getId() == id) {
			Note _note = noteData.get();
			_note.setTitle(note.getTitle());
			_note.setCreatedBy(note.getCreatedBy());
			_note.setText(note.getText());
			return new ResponseEntity<>(notesDao.save(_note), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<HttpStatus> deleteById(Long id) throws Exception {
		try {
			notesDao.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception("Delete Note By Id failed");
		}
	}

	@Transactional
	public ResponseEntity<List<Note>> deleteNoteByCreatedBy(String createdBy) throws Exception {
		try {
			List<Note> notes = notesDao.deleteByCreatedBy(createdBy);
			return new ResponseEntity<>(notes, HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception("Delete Note By createdBy failed");
		}

	}

	@Transactional
	public ResponseEntity<List<Note>> deleteNoteByTitle(String title) throws Exception {
		try {
			List<Note> notes = notesDao.deleteByTitle(title);
			return new ResponseEntity<>(notes, HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception("Delete note by title failed");
		}

	}

	public ResponseEntity<List<Note>> load() {
		List<Note> notes = new ArrayList<>(List.of(new Note(3, "Not spring related", "user1", "Notes on kubernetes"),
				new Note(4, "Not spring related", "user2", "Notes on Docker"),
				new Note(5, "title3", "user3", "Notes on Spring MVC"),
				new Note(6, "title4", "user4", "Notes on Spring Security"),
				new Note(7, "title5", "user5", "Notes on Spring boot")));

		try {
			notesDao.saveAll(notes);
			return new ResponseEntity<>(notes, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}

	public Optional<Note> findById(Long id) {
		return notesDao.findById(id);
	}

}

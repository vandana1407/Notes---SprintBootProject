package com.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dao.NotesDao;
import com.app.model.Note;

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
			note.setTicketIds(extractTicketIds(note.getText()));
			notesDao.save(note);
			return new ResponseEntity<>(note, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new Exception("Add note failed");
		}

	}
	
	public Set<String> extractTicketIds(String text) {
		Pattern pattern = Pattern.compile("[a-zA-Z]{2,4}-[0-9]{5}");
		
		Matcher matcher = pattern.matcher(text);
		return (Set<String>) matcher.results().map(i->i.group()).collect(Collectors.toSet());
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
			//throw new Exception("Delete Note By createdBy failed");
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
			e.printStackTrace();
			throw new Exception("Delete note by title failed");
		}

	}

	public ResponseEntity<List<Note>> load() {
	/*	List<Note> notes = new ArrayList<>(List.of(new Note(3, "Not spring related", "Chen", "Management Fundamentals"),
				new Note(4, "Not spring related", "Amit", "Cooking Classes Onine"),
				new Note(5, "title3", "Josh", "Spring MVC Notes"),
				new Note(6, "title4", "Emily", "Spring Security Fundamentals"),
				new Note(7, "title5", "Ella", "Spring Boot Fundamentals")));
*/
		List<Note> notes = new ArrayList<>();
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

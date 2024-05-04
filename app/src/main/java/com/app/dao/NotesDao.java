package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.model.Note;

@Repository
public interface NotesDao extends JpaRepository<Note, Long> {
	List<Note> findByCreatedBy(String createdBy);

	List<Note> findByTitle(String title);

	List<Note> deleteByCreatedBy(String createdBy);

	List<Note> deleteByTitle(String title);

	List<Note> searchByTitleContainingOrTextContaining(String title, String text);

}

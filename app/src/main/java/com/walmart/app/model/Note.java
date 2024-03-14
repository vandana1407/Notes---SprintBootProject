package com.walmart.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Note {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	private String title;
	private String createdBy;
	private String text;

}

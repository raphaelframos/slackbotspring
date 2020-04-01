package com.raphaelframos.slackbot.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "badwords")
public class BadWord {
	
	@Id
	private String id;
	String user;
    String word;
    LocalDate updateDate = LocalDate.now();

}

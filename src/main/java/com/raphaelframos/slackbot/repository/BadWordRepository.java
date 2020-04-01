package com.raphaelframos.slackbot.repository;

import org.springframework.data.repository.CrudRepository;

import com.raphaelframos.slackbot.model.BadWord;

public interface BadWordRepository extends CrudRepository<BadWord, String>{

}

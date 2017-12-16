package com.springjpa;

import com.springjpa.repo.EventRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YandexTaskApplication implements CommandLineRunner{

	@Autowired
    EventRecordRepository repository;
	
	public static void main(String[] args){
		SpringApplication.run(YandexTaskApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		// clear all record if existed before running
		repository.deleteAll();
	}
}

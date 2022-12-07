package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {
	
	@Autowired
	public TodoRepository repository;
	
	public String testService() {
		TodoEntity entity = TodoEntity.builder().title("First todo item").build();

		repository.save(entity);

		TodoEntity savedEntity = repository.findById(entity.getId()).get();
		
		return savedEntity.getTitle();
	}
	
	// CREATE
	public List<TodoEntity> create(final TodoEntity entity) {
		validate(entity);
		repository.save(entity);
		log.info("Entity Id: {} is saved.", entity.getId());
		return repository.findByUserId(entity.getUserId());
	}
	
	// RETRIEVE
	public List<TodoEntity> retrieve(final String userId){
		return repository.findByUserId(userId);
	}
	
	// UPDATE
	public List<TodoEntity> update(final TodoEntity entity) {
		validate(entity);
		
		final Optional<TodoEntity> original = repository.findById(entity.getId());
		original.ifPresent(todo -> {
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			repository.save(todo);
			log.info("Entity Id: {} is updated. ", entity.getId());
		});
		
		return retrieve(entity.getUserId());
	}
	
	// DELETE
	public List<TodoEntity> delete(final TodoEntity entity){
		validate(entity);
		
		try {
			repository.delete(entity);
			log.info("Entity Id: {} is deleted. ", entity.getId());
		} catch(Exception e) {
			log.error("error deleting entity ", entity.getId(), e);
			throw new RuntimeException("error deleting entity " + entity.getId());
		}
		
		return retrieve(entity.getUserId());
	}
	
	private void validate(final TodoEntity entity) {
		if(entity == null) {
			log.warn("Entity cannot be null.");
			throw new RuntimeException("Entity cannot be null.");
		}
		
		if(entity.getUserId() == null) {
			log.warn("Unknown user.");
			throw new RuntimeException("Unknown user.");
		}
	}
}
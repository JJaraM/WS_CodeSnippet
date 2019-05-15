package com.jjara.microservice.post.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.jjara.microservice.post.pojo.CodeSnippet;

import reactor.core.publisher.Flux;

public interface CodeSnippetRepository extends ReactiveMongoRepository<CodeSnippet, Long> {
		
	@Query("{ id: { $exists: true }}")
	public Flux<CodeSnippet> findAll(Pageable page);

	@Query("{ type: { $in: [?0] }}")
	public Flux<CodeSnippet> findAllByType(Pageable page, String type);

	@Query("{ \"distinct\": \"codeSnippet\", \"key\": \"type\",\"query\": {} }")
	public Flux<CodeSnippet> findDistinctByType();
	
}
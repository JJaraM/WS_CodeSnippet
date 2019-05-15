package com.jjara.microservice.post.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.jjara.microservice.post.pojo.Language;

import reactor.core.publisher.Flux;

public interface LanguageRepository extends ReactiveMongoRepository<Language, Long> {

	@Query("{ id: { $exists: true }}")
	public Flux<Language> findAll(Pageable page);

	@Query("{ type: { $in: [?0] }}")
	public Flux<Language> findAllByType(Pageable page, String type);

}
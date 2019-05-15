package com.jjara.microservice.post.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import com.jjara.microservice.post.pojo.CodeSnippet;
import com.jjara.microservice.post.pojo.Language;
import com.jjara.microservice.post.repository.LanguageRepository;
import com.jjara.microservice.post.repository.SequenceRepository;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class LanguageService {

	@Autowired
	private LanguageRepository repository;
	
	@Autowired
	private SequenceRepository sequenceRepository;

	public Flux<Language> findAllByType(final int page, final int size, String type) {
		Flux<Language> result = null;
		if (type != null) {
			result = repository.findAllByType(PageRequest.of(page, size, new Sort(Direction.DESC, "id")), type);
		} else if (size > 0) {
			result = repository.findAll(PageRequest.of(page, size, new Sort(Direction.DESC, "id")));
		} else {
			result = repository.findAll();
		}
		return result;
	}

	public Flux<Language> findAllByPage(final int page, final int size) {
		Flux<Language> result = null;
		if (size > 0) {
			result = repository.findAll(PageRequest.of(page, size, new Sort(Direction.DESC, "id")));
		} else {
			result = repository.findAll();
		}
		return result;
	}

	
	public Mono<Language> get(long id) {
		return repository.findById(id);
	}

	public Mono<Language> update(long id, String name) {
		return repository.findById(id).map(language -> {
			language.setName(name);
			return language;
		}).flatMap(this.repository::save);
	}

	public Mono<Language> delete(long id) {
		return repository.findById(id).flatMap(p -> repository.deleteById(p.getId()).thenReturn(p));
	}

	public Mono<Language> create(String name) {
		final Language language = new Language();
		language.setId(sequenceRepository.getNextSequenceId());
		language.setName(name);
		return repository.save(language);
	}
}

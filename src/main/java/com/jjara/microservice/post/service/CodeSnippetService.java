package com.jjara.microservice.post.service;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.jjara.microservice.post.pojo.CodeSnippet;
import com.jjara.microservice.post.repository.CodeSnippetRepository;
import com.jjara.microservice.post.repository.SequenceRepository;

@Log4j2
@Service
public class CodeSnippetService {

	@Autowired
	private CodeSnippetRepository repository;
	
	@Autowired
	private SequenceRepository sequenceRepository;

	public Flux<CodeSnippet> findAllByType(final int page, final int size, String type) {
		Flux<CodeSnippet> result = null;
		if (type != null) {
			result = repository.findAllByType(PageRequest.of(page, size, new Sort(Direction.DESC, "id")), type);
		} else if (size > 0) {
			result = repository.findAll(PageRequest.of(page, size, new Sort(Direction.DESC, "id")));
		} else {
			result = repository.findAll();
		}
		return result;
	}

	public Flux<CodeSnippet> findAllByPage(final int page, final int size) {
		Flux<CodeSnippet> result = null;
		if (size > 0) {
			result = repository.findAll(PageRequest.of(page, size, new Sort(Direction.DESC, "id")));
		} else {
			result = repository.findAll();
		}
		return result;
	}

	
	public Mono<CodeSnippet> get(long id) {
		return repository.findById(id);
	}

	public Mono<CodeSnippet> update(long id, String title, String content, String type) {
		return repository.findById(id).map(codeSnippet -> {
			codeSnippet.setTitle(title);
			codeSnippet.setContent(content);
			codeSnippet.setType(type);
			return codeSnippet;
		}).flatMap(this.repository::save);
	}

	public Mono<CodeSnippet> delete(long id) {
		return repository.findById(id).flatMap(p -> repository.deleteById(p.getId()).thenReturn(p));
	}

	public Mono<CodeSnippet> create(String title, String content, String type) {
		final CodeSnippet codeSnippet = new CodeSnippet();
		codeSnippet.setId(sequenceRepository.getNextSequenceId());
		codeSnippet.setContent(content);
		codeSnippet.setTitle(title);
		codeSnippet.setType(type);
		return repository.save(codeSnippet);
	}

	@SuppressWarnings("unchecked")
	public Flux<CodeSnippet> findAllLanguages() {
		List<String> values = (List<String>) this.sequenceRepository.distict("codeSnippet", "type");
		List<CodeSnippet> list = values.stream().map(type -> {
			CodeSnippet snippet = new CodeSnippet();
			snippet.setType(type);
			return snippet;
		}).collect(Collectors.toList());
		return Flux.fromIterable(list);
	}

	public Flux<CodeSnippet> findAllByLanguage(String language) {
		return repository.findAllByType(language);
	}
}
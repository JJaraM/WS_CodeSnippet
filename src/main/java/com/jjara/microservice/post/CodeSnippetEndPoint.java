package com.jjara.microservice.post;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jjara.microservice.post.handler.PostHandler;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class CodeSnippetEndPoint {

	private final static String ENDPOINT = "/v1/codeSnippet";
	
	@Bean
	protected RouterFunction<ServerResponse> routes(final PostHandler handler) {
		return route(GET(ENDPOINT + "/{page}/{size}/{tag}"), handler::all)
				.andRoute(GET(ENDPOINT + "/{page}/{size}"), handler::findAllByPage)
				.andRoute(GET(ENDPOINT + "/{id}"), handler::getById)
				.andRoute(DELETE(ENDPOINT + "/{id}"), handler::deleteById)
				.andRoute(POST(ENDPOINT), handler::create)
				.andRoute(PUT(ENDPOINT+ "/{id}"), handler::updateById);
	}

	
}
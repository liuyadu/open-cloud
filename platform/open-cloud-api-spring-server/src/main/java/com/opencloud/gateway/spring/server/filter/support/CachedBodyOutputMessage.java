package com.opencloud.gateway.spring.server.filter.support;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * Implementation of {@link ClientHttpRequest} that saves body as a field.
 */
public class CachedBodyOutputMessage implements ReactiveHttpOutputMessage {

	private final DataBufferFactory bufferFactory;

	private final HttpHeaders httpHeaders;

	private Flux<DataBuffer> body = Flux.error(new IllegalStateException(
			"The body is not set. " + "Did handling complete with success?"));

	public CachedBodyOutputMessage(ServerWebExchange exchange, HttpHeaders httpHeaders) {
		this.bufferFactory = exchange.getResponse().bufferFactory();
		this.httpHeaders = httpHeaders;
	}

	@Override
	public void beforeCommit(Supplier<? extends Mono<Void>> action) {

	}

	@Override
	public boolean isCommitted() {
		return false;
	}

	@Override
	public HttpHeaders getHeaders() {
		return this.httpHeaders;
	}

	@Override
	public DataBufferFactory bufferFactory() {
		return this.bufferFactory;
	}

	/**
	 * Return the request body, or an error stream if the body was never set or when.
	 * @return body as {@link Flux}
	 */
	public Flux<DataBuffer> getBody() {
		return this.body;
	}

	public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
		this.body = Flux.from(body);
		return Mono.empty();
	}

	@Override
	public Mono<Void> writeAndFlushWith(
			Publisher<? extends Publisher<? extends DataBuffer>> body) {
		return writeWith(Flux.from(body).flatMap(p -> p));
	}

	@Override
	public Mono<Void> setComplete() {
		return writeWith(Flux.empty());
	}

}

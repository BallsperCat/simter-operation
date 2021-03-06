package tech.simter.operation.rest.webflux.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationService

/**
 * The [HandlerFunction] for get [Operation] by id
 *
 * @author zf
 */
@Component
class GetByIdHandler @Autowired constructor(
  private val operationService: OperationService
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> {
    val mono = operationService.get(request.pathVariable("id"))
    return mono.hasElement()
      .flatMap {
        if (it) ok().contentType(APPLICATION_JSON).body(mono)
        else notFound().build()
      }
  }

  companion object {
    val REQUEST_PREDICATE: RequestPredicate = GET("/{id}")
  }
} 
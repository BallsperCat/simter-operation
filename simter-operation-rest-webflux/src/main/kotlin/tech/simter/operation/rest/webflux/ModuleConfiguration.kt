package tech.simter.operation.rest.webflux

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.TEXT_PLAIN
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import tech.simter.operation.rest.webflux.handler.operation.FindByClusterHandler

private const val MODULE = "tech.simter.operation.rest.webflux"

/**
 * All configuration for this module.
 *
 * Register a `RouterFunction<ServerResponse>` with all routers for this module.
 * The default context-path of this router is '/'. And can be config by property `simter.rest.context-path.operation`.
 *
 * @author RJ
 * @author zh
 */
@Configuration("$MODULE.ModuleConfiguration")
@ComponentScan(MODULE)
class ModuleConfiguration @Autowired constructor(
  @Value("\${module.version.simter-operation:UNKNOWN}") private val version: String,
  @Value("\${module.rest-context-path.simter-operation:/operation}") private val contextPath: String,
  private val findByClusterHandler: FindByClusterHandler
) {
  private val logger = LoggerFactory.getLogger(ModuleConfiguration::class.java)

  init {
    logger.warn("module.version.simter-operation='{}'", version)
    logger.warn("module.rest-context-path.simter-operation='{}'", contextPath)
  }

  /** Register a `RouterFunction<ServerResponse>` with all routers for this module */
  @Bean("$MODULE.Routes")
  @ConditionalOnMissingBean(name = ["$MODULE.Routes"])
  fun operationRoutes() = router {
    contextPath.nest {
      // GET /cluster/{cluster} find Operations by cluster
      FindByClusterHandler.REQUEST_PREDICATE.invoke(findByClusterHandler::findByCluster)
      // GET /
      GET("/") { ok().contentType(TEXT_PLAIN).syncBody("simter-operation-$version") }
      // OPTIONS /*
      OPTIONS("/**") { noContent().build() }
    }
  }
}
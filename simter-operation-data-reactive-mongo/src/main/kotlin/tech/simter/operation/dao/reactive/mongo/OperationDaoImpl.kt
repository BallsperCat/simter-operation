package tech.simter.operation.dao.reactive.mongo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.po.Operation

/**
 * The Reactive MongoDB implementation of [OperationDao].
 *
 * @author RJ
 */
@Component
class OperationDaoImpl @Autowired constructor(
  private val repository: OperationReactiveRepository
) : OperationDao {
  override fun get(id: String): Mono<Operation> {
    return repository.findById(id)
  }
}
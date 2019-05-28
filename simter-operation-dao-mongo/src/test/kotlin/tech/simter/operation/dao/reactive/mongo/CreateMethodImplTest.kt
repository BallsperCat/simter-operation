package tech.simter.operation.dao.reactive.mongo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.test.test
import tech.simter.operation.dao.OperationDao
import tech.simter.operation.dao.reactive.mongo.TestHelper.randomOperation
import tech.simter.operation.dao.reactive.mongo.TestHelper.randomOperationItem

/**
 * Test [OperationDaoImpl]
 *
 * @author zh
 * @author RJ
 */
@SpringJUnitConfig(ModuleConfiguration::class)
@DataMongoTest
class CreateMethodImplTest @Autowired constructor(
  private val repository: OperationReactiveRepository,
  private val dao: OperationDao
) {
  @Test
  fun `success without items`() {
    // init data
    val po = randomOperation()

    // invoke and verify
    dao.create(po).test().verifyComplete()
    repository.findById(po.id).test().expectNext(po).verifyComplete()
  }

  @Test
  fun `success with items`() {
    // do create
    val po = randomOperation().apply {
      addItem(randomOperationItem(id = "field1"))
      addItem(randomOperationItem(id = "field2"))
    }
    dao.create(po).test().verifyComplete()

    // verify created
    repository.findById(po.id).test().expectNext(po).verifyComplete()
  }
}
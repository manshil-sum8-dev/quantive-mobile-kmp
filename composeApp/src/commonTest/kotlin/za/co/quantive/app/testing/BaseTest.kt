package za.co.quantive.app.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

/**
 * Base test class that sets up test dispatchers
 * Extend this for tests that use coroutines
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseTest {

    /**
     * Test dispatcher for coroutines
     * Can be overridden if a different dispatcher is needed
     */
    protected open val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @BeforeTest
    open fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    open fun tearDown() {
        Dispatchers.resetMain()
    }
}

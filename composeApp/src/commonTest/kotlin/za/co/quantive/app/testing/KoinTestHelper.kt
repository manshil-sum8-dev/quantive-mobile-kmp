package za.co.quantive.app.testing

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest

/**
 * Helper for setting up and tearing down Koin in tests
 * Use this in tests that need dependency injection
 */
abstract class KoinTestHelper : KoinTest {

    /**
     * Start Koin with test modules
     * Should be called in @BeforeTest
     */
    protected fun startKoinForTest(vararg modules: Module) {
        startKoin {
            modules(*modules)
        }
    }

    /**
     * Stop Koin
     * Should be called in @AfterTest
     */
    protected fun stopKoinAfterTest() {
        stopKoin()
    }
}

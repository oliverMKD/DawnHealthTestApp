package com.oliver.dawnhealthtestapp


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestInstancePostProcessor
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutineTestExtension::class)
interface CoroutineTest {
    var testCoroutineProvider: CoroutineProvider
    var testScope: TestScope
}

@ExperimentalCoroutinesApi
class CoroutineTestExtension : TestInstancePostProcessor, BeforeEachCallback, AfterEachCallback {

    private val testCoroutineDispatcher = UnconfinedTestDispatcher()
    private val testCoroutineScope = TestScope(testCoroutineDispatcher)

    private val coroutineProvider = object : CoroutineProvider {
        override fun createCoroutineScope(): CoroutineScope = testCoroutineScope
        override fun defaultContext(): CoroutineContext = testCoroutineDispatcher
        override fun mainContext(): CoroutineContext = testCoroutineDispatcher
        override fun ioContext(limit: Int): CoroutineContext = testCoroutineDispatcher
    }

    override fun postProcessTestInstance(testInstance: Any?, context: ExtensionContext?) {
        (testInstance as CoroutineTest).let { coroutineTest ->
            coroutineTest.testCoroutineProvider = coroutineProvider
            coroutineTest.testScope = TestScope(testCoroutineDispatcher)
        }
    }

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}

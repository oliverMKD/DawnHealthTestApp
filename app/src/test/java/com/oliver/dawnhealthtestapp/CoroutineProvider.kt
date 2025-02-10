package com.oliver.dawnhealthtestapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

interface CoroutineProvider {
    fun createJob(): Job = SupervisorJob()
    fun createCoroutineScope(): CoroutineScope
    fun mainContext(): CoroutineContext = Dispatchers.Main.immediate
    fun defaultContext(): CoroutineContext = Dispatchers.Default
    fun ioContext(limit: Int = 0): CoroutineContext =
        if (limit <= 0) Dispatchers.IO else Dispatchers.IO.limitedParallelism(limit)
}

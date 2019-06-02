package es.kix2902.santoral.data.threads

import android.os.Process
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object DefaultExecutorSupplier {

    private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()

    private var backgroundThreadExecutor: ThreadPoolExecutor
    private var mainThreadExecutor: Executor

    init {
        backgroundThreadExecutor = ThreadPoolExecutor(
            NUMBER_OF_CORES * 2,
            NUMBER_OF_CORES * 4,
            30L,
            TimeUnit.SECONDS,
            LinkedBlockingQueue<Runnable>(),
            PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND)
        )

        mainThreadExecutor = MainThreadExecutor()
    }

    fun forBackgroundTasks(): ThreadPoolExecutor {
        return backgroundThreadExecutor
    }

    fun forMainThreadTasks(): Executor {
        return mainThreadExecutor
    }

}
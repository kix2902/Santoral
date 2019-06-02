package es.kix2902.santoral.data.threads

import android.os.Process
import java.util.concurrent.ThreadFactory

class PriorityThreadFactory(threadPriority: Int) : ThreadFactory {

    private val mThreadPriority = threadPriority

    override fun newThread(runnable: Runnable): Thread {
        val wrapperRunnable = Runnable {
            try {
                Process.setThreadPriority(mThreadPriority)
            } catch (t: Throwable) {

            }

            runnable.run()
        }
        return Thread(wrapperRunnable)
    }

}
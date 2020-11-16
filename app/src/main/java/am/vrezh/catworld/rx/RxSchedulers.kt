package am.vrezh.catworld.rx

import io.reactivex.rxjava3.core.CompletableTransformer
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.SingleTransformer


abstract class RxSchedulers {

    abstract fun mainThread(): Scheduler

    abstract fun io(): Scheduler

    abstract fun computation(): Scheduler

    fun <T> ioToMain(): ObservableTransformer<T, T> {
        return ObservableTransformer { objectObservable ->
            objectObservable
                .subscribeOn(io())
                .observeOn(mainThread())
        }
    }

    fun ioToMainCompletable(): CompletableTransformer {
        return CompletableTransformer { completable ->
            completable
                .subscribeOn(io())
                .observeOn(mainThread())
        }
    }

    fun <T> ioToMainSingle(): SingleTransformer<T, T> {
        return SingleTransformer { objectObservable ->
            objectObservable
                .subscribeOn(io())
                .observeOn(mainThread())
        }
    }

    fun <T> computationToMain(): ObservableTransformer<T, T> {
        return ObservableTransformer { objectObservable ->
            objectObservable
                .subscribeOn(computation())
                .observeOn(mainThread())
        }
    }

    fun <T> computationToMainSingle(): SingleTransformer<T, T> {
        return SingleTransformer { objectObservable ->
            objectObservable
                .subscribeOn(computation())
                .observeOn(mainThread())
        }
    }

}

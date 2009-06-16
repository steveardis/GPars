package org.gparallelizer

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import org.gparallelizer.Asynchronizer
import java.lang.Thread.UncaughtExceptionHandler
import org.gparallelizer.actors.pooledActors.Pool
import org.gparallelizer.actors.pooledActors.DefaultPool

/**
 * AsyncEnhancer allows classes or instances to be enhanced with asynchronous variants of iterative methods,
 * like eachAsync(), collectAsync(), findAllAsync() and others. These operations split processing into multiple
 * concurrently executable tasks and perform them on the underlying instance of an ExecutorService.
 * The pool itself is stored in a final property threadPool and can be managed through static methods
 * on the AsyncEnhancer class.
 * All enhanced classes and instances will share the underlying pool.
 *
 * @author Vaclav Pech
 * Date: Jun 15, 2009
 */
public final class AsyncEnhancer {

    /**
     * Holds the internal ExecutorService instance wrapped into a DefaultPool
     */
    private final static DefaultPool threadPool = new DefaultPool(true)

    /**
     * Enhances a single instance by mixing-in an instance of AsyncEnhancer.
     */
    public static void enhanceInstance(Object collection) {
        collection.getMetaClass().mixin AsyncEnhancer
    }

    /**
     * Enhances a class and so all instances created in the future by mixing-in an instance of AsyncEnhancer.
     * Enhancing classes needs to be done with caution, since it may have impact in unrelated parts of the application.
     */
    public static void enhanceClass(Class clazz) {
        clazz.getMetaClass().mixin AsyncEnhancer
    }

    /**
     * Retrieves the underlying pool
     */
    public Pool getThreadPool() { return threadPool }

    /**
     * Iterates over a collection/object with the <i>each()</i> method using an asynchronous variant of the supplied closure
     * to evaluate each collection's element. A CountDownLatch is used to make the calling thread wait for all the results.
     * After this method returns, all the closures have been finished and all the potential shared resources have been updated
     * by the threads.
     * It's important to protect any shared resources used by the supplied closure from race conditions caused by multi-threaded access.
     * @throws AsyncException If any of the collection's elements causes the closure to throw an exception. The original exceptions will be stored in the AsyncException's concurrentExceptions field.
     */
    public def eachAsync(Closure cl) {
        Asynchronizer.withExistingAsynchronizer(threadPool.executorService) {
            AsyncInvokerUtil.eachAsync(mixedIn[Object], cl)
        }
    }

    /**
     * Iterates over a collection/object with the <i>collect()</i> method using an asynchronous variant of the supplied closure
     * to evaluate each collection's element.
     * After this method returns, all the closures have been finished and the caller can safely use the result.
     * It's important to protect any shared resources used by the supplied closure from race conditions caused by multi-threaded access.
     * @throws AsyncException If any of the collection's elements causes the closure to throw an exception. The original exceptions will be stored in the AsyncException's concurrentExceptions field.
     */
    public def collectAsync(Closure cl) {
        Asynchronizer.withExistingAsynchronizer(threadPool.executorService) {
            AsyncInvokerUtil.collectAsync(mixedIn[Object], cl)
        }
    }

    /**
     * Performs the <i>findAll()</i> operation using an asynchronous variant of the supplied closure
     * to evaluate each collection's/object's element.
     * After this method returns, all the closures have been finished and the caller can safely use the result.
     * It's important to protect any shared resources used by the supplied closure from race conditions caused by multi-threaded access.
     * @throws AsyncException If any of the collection's elements causes the closure to throw an exception. The original exceptions will be stored in the AsyncException's concurrentExceptions field.
     */
    public def findAllAsync(Closure cl) {
        Asynchronizer.withExistingAsynchronizer(threadPool.executorService) {
            AsyncInvokerUtil.findAllAsync(mixedIn[Object], cl)
        }
    }

    /**
     * Performs the <i>find()</i> operation using an asynchronous variant of the supplied closure
     * to evaluate each collection's/object's element.
     * After this method returns, all the closures have been finished and the caller can safely use the result.
     * It's important to protect any shared resources used by the supplied closure from race conditions caused by multi-threaded access.
     * @throws AsyncException If any of the collection's elements causes the closure to throw an exception. The original exceptions will be stored in the AsyncException's concurrentExceptions field.
     */
    public def findAsync(Closure cl) {
        Asynchronizer.withExistingAsynchronizer(threadPool.executorService) {
            AsyncInvokerUtil.findAsync(mixedIn[Object], cl)
        }
    }

    /**
     * Performs the <i>all()</i> operation using an asynchronous variant of the supplied closure
     * to evaluate each collection's/object's element.
     * After this method returns, all the closures have been finished and the caller can safely use the result.
     * It's important to protect any shared resources used by the supplied closure from race conditions caused by multi-threaded access.
     * @throws AsyncException If any of the collection's elements causes the closure to throw an exception. The original exceptions will be stored in the AsyncException's concurrentExceptions field.
     */
    public boolean allAsync(Closure cl) {
        Asynchronizer.withExistingAsynchronizer(threadPool.executorService) {
            AsyncInvokerUtil.allAsync(mixedIn[Object], cl)
        }
    }

    /**
     * Performs the <i>any()</i> operation using an asynchronous variant of the supplied closure
     * to evaluate each collection's/object's element.
     * After this method returns, all the closures have been finished and the caller can safely use the result.
     * It's important to protect any shared resources used by the supplied closure from race conditions caused by multi-threaded access.
     * @throws AsyncException If any of the collection's elements causes the closure to throw an exception. The original exceptions will be stored in the AsyncException's concurrentExceptions field.
     */
    public boolean anyAsync(Closure cl) {
        Asynchronizer.withExistingAsynchronizer(threadPool.executorService) {
            AsyncInvokerUtil.anyAsync(mixedIn[Object], cl)
        }
    }
}
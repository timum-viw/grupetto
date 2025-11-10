package com.spop.peloton.sensors.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.selects.SelectBuilder
import kotlinx.coroutines.selects.select
import kotlin.math.max
import kotlin.math.min

/**
 * Returns a flow of results of applying the given [transform] function to
 * an each list representing a view over the window of the given [size]
 * sliding along this collection with the given [step].
 *
 * Note that the list passed to the [transform] function is ephemeral and is valid only inside that function.
 * You should not store it or allow it to escape in some way, unless you made a snapshot of it.
 * Several last lists may have less elements than the given [size].
 *
 * This is more efficient, than using flow.windowed(...).map { ... }
 *
 * Both [size] and [step] must be positive and can be greater than the number of elements in this collection.
 * @param size the number of elements to take in each window
 * @param step the number of elements to move the window forward by on an each step.
 * @param partialWindows controls whether or not to keep partial windows in the end if any.
 */
@FlowPreview
fun <T, R> Flow<T>.windowed(size: Int, step: Int, partialWindows: Boolean, transform: suspend (List<T>) -> R): Flow<R> {
    require(size > 0 && step > 0) { "Size and step should be greater than 0, but was size: $size, step: $step" }

    return flow {
        val buffer = ArrayDeque<T>(size)
        val toDrop = min(step, size)
        val toSkip = max(step - size, 0)
        var skipped = toSkip

        collect { value ->
            if (toSkip == skipped) buffer.addLast(value)
            else skipped++

            if (buffer.size == size) {
                emit(transform(buffer))
                repeat(toDrop) { buffer.removeFirst() }
                skipped = 0
            }
        }

        while (partialWindows && buffer.isNotEmpty()) {
            emit(transform(buffer))
            repeat(min(toDrop, buffer.size)) { buffer.removeFirst() }
        }
    }
}

/**
 * Continuously executes a select statement until the coroutine scope is cancelled
 */
suspend fun <T> CoroutineScope.selectForever(selectBuilder: SelectBuilder<T>.() -> Unit) {
    while (isActive) {
        select(selectBuilder)
    }
}


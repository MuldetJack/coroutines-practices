package com.example.elementalskotlin

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale

fun main() {
    //coldFlow()
    //cancelFlow()
    //flowOperators()
    terminalFlowOperators()
}

fun terminalFlowOperators() {
    runBlocking {
        newTopic("Flow Terminales")
        newTopic("List")
        val list = getDataByFlow()
        //.toList()
        println("List: $list")

        newTopic("Single")
        val single = getDataByFlow()
        //.take(1).single()
        println("Single: $single")

        newTopic("First")
        val first = getDataByFlow()
        //.first()
        println("First: $first")

        newTopic("Last")
        val last = getDataByFlow()
        //.last()
        println("Last: $last")

        newTopic("Reduce")
        val saving = getDataByFlow()
            .reduce { accumulator, value ->
                println("Accumulator: $accumulator")
                println("Value: $value")
                println("Current saving: ${accumulator + value}")
                accumulator + value
            }
        println("Saving: $saving")

        newTopic("Fold")
        val lastSaving = saving
        val totalSaving = getDataByFlow()
            .fold(lastSaving, { accumulator, value ->
                println("Accumulator: $accumulator")
                println("Value: $value")
                println("Current total: ${accumulator + value}")
                accumulator + value
            })

        println("Total saving: $totalSaving")
    }
}

fun flowOperators() {
    runBlocking {
        newTopic("Flow operators")
        newTopic("Map")
        getDataByFlow()
            .map {
                setFormat(it)
                setFormat(converterCelsToFahr(it), "F")
            }//.collect { println(it) }

        newTopic("Filter")
        getDataByFlow().filter { it > 23 }
            .map { setFormat(it) }
            .collect { println(it) }

        newTopic("Transform")
        getDataByFlow().transform {
            emit(setFormat(it))
            emit(converterCelsToFahr(it))
        }//.collect { println(it) }

        newTopic("Take")
        getDataByFlow()
            .take(3)
            .map { setFormat(it) }
            .collect { println(it) }
    }
}

fun converterCelsToFahr(cels: Float): Float = ((cels) / 5) + 32

fun setFormat(temp: Float, degree: String = "C"): String =
    String.format(Locale.getDefault(), "%.1f°$degree", temp)

fun cancelFlow() {
    runBlocking {
        newTopic("Cancel flow")
        val job = launch {
            getDataByFlow().collect { println(it) }
        }
        delay(someTime() * 2)
        job.cancel()
    }
}

fun coldFlow() {
    newTopic("Flow are cold")
    runBlocking {
        val dataFlow = getDataByFlow()
        println("Waiting...")
        delay(someTime())
        dataFlow.collect {
            println(it)
        }
    }
}

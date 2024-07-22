package com.example.elementalskotlin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.random.Random

fun main() {
    //dispatchers()
    //nested()
    //changeWithContext()
    basicFlows()
}

fun basicFlows() {
    newTopic("Flows basico")
    runBlocking {
        launch {
            getDataByFlow().collect { println(it) }
        }

        launch {
            (1..50).forEach {
                delay(someTime() / 10)
                println("Tarea 2...")
            }
        }
    }
}

fun getDataByFlow(): Flow<Float> {
    return flow {
        (1..5).forEach {
            println("Procesando datos...")
            delay(someTime())
            emit(20 + it + Random.nextFloat())
        }
    }
}

fun changeWithContext() {
    runBlocking {
        newTopic("With Context")
        startMsg()
        withContext(newSingleThreadContext("CursoAndroidContext")) {
            startMsg()
            delay(someTime())
            println("Curso Android")
            endMsg()
        }

        withContext(Dispatchers.IO) {
            startMsg()
            delay(someTime())
            println("Peticion al servidor")
            endMsg()
        }
        endMsg()
    }
}

fun nested() {
    runBlocking {
        newTopic("Anidar")

        val job = launch {
            startMsg()

            launch {
                startMsg()
                delay(someTime())
                println("Otra tarea")
                endMsg()
            }

            val job2 = launch(Dispatchers.IO) {
                startMsg()

                launch(newSingleThreadContext("CursoAndroid2")) {
                    startMsg()
                    println("Otra tarea thread ant")
                    endMsg()
                }
                delay(someTime())
                println("Tarea en el servidor")
                endMsg()
            }

            job2.cancel()
            println("Job 2 cancelled")

            var sum = 0
            (1..100).forEach {
                sum += it
                delay(someTime() / 100)
            }
            println("Sum = $sum")
            endMsg()
        }

        delay(someTime() / 2)
        job.cancel()
        println("Job cancelled")
    }
}

fun dispatchers() {
    runBlocking {
        newTopic("Dispatchers")
        launch {
            startMsg()
            println("None")
            endMsg()
        }

        launch(Dispatchers.Unconfined) {
            startMsg()
            println("IO")
            endMsg()
        }

        launch(Dispatchers.Default) {
            startMsg()
            println("Default")
            endMsg()
        }

        launch(newSingleThreadContext("Curso coroutines")) {
            startMsg()
            println("Mi corrutina personalizada")
            endMsg()
        }

        newSingleThreadContext("CursosAndroid").use { myContext ->
            launch {
                startMsg()
                println("Mi corrutina personalizada 2 ")
                endMsg()
            }
        }
    }
}

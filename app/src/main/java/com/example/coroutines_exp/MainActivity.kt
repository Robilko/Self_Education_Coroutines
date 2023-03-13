package com.example.coroutines_exp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.coroutines_exp.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var formatter = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
    private lateinit var binding: ActivityMainBinding
    private val scope = CoroutineScope(Job())
    lateinit var job: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run.setOnClickListener { onRun() }
        binding.onRun2.setOnClickListener { onRun2() }
    }

    private fun onRun() {
        scope.launch {
            log("parent coroutine, start")

            val data = async { getData() }
            val data2 = async { getData2() }

            log("parent coroutine, wait until children return result")
            val result = "${data.await()}, ${ data2.await()}"
            log("parent coroutine, children returned: $result")

            log("parent coroutine, end")
        }
    }

    private suspend fun getData(): String {
        delay(1000)
        return "data"
    }

    private suspend fun getData2(): String {
        delay(1500)
        return "data2"
    }

    private fun onRun2() {

    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        scope.cancel()
    }

    private fun log(text: String) {
        Log.d("TAG", "${formatter.format(Date())} $text [${Thread.currentThread().name}]")
    }
}
package com.demirli.a22pomodoroclock

import android.graphics.Color
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    private var minute = 25
    private var second = 0

    private var flagForPhase = false

    private var progressBarProgress = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar.progressDrawable.setColorFilter(resources.getColor(R.color.colorPrimaryDark),android.graphics.PorterDuff.Mode.SRC_IN)

        runnable = Runnable {}
        handler = Handler()

        setButtons()
    }

    private fun setButtons() {

        stoppedState()

        start_btn.setOnClickListener {

            startedState()

            var delay: Long = 1000
            if (delay_et.text.toString() != ""){
                delay = delay_et.text.toString().toLong()
            }
            startTimer(delay)
        }

        reset_btn.setOnClickListener {

            stoppedState()

            handler.removeCallbacks(runnable)
            minute = 25
            second = 0
            timer_tv.setText("%02d".format(minute) + ":" +"%02d".format(second))
            progressBar.progress = 0
            progressBarProgress = 0f
        }

        stop_btn.setOnClickListener {

            stoppedState()

            handler.removeCallbacks(runnable)
        }
    }

    fun startTimer(delay: Long){

        runnable = object: Runnable{
            override fun run() {

                if (second == 0 && minute != 0){
                    minute--
                    second = 59
                }else if (second == 0 && minute == 0 && flagForPhase == true){
                    minute = 25
                    second = 0
                    flagForPhase = false
                    progressBarProgress = 0f
                }else if(second == 0 && minute == 0 && flagForPhase == false){
                    minute = 5
                    second = 0
                    flagForPhase = true
                    progressBarProgress = 0f
                }else{
                    second--
                }
                timer_tv.setText("%02d".format(minute) + ":" +"%02d".format(second))


                if(flagForPhase == false){
                    progressBar.progressDrawable.setColorFilter(resources.getColor(R.color.colorPrimaryDark),android.graphics.PorterDuff.Mode.SRC_IN)
                    var a :Float = 100f/(25f*60f)
                    progressBarProgress += a
                    progressBar.progress = progressBarProgress.toInt()
                }else if(flagForPhase == true){
                    progressBar.progressDrawable.setColorFilter(resources.getColor(R.color.colorPrimary),android.graphics.PorterDuff.Mode.SRC_IN)
                    var a :Float = 100f/(5f*60f)
                    progressBarProgress += a
                    progressBar.progress = progressBarProgress.toInt()
                }

                handler.postDelayed(runnable,delay)
            }
        }
        handler.postDelayed(runnable,delay)
    }

    fun startedState(){
        start_btn.visibility = View.INVISIBLE
        reset_btn.visibility = View.VISIBLE
        stop_btn.visibility = View.VISIBLE
    }

    fun stoppedState(){
        start_btn.visibility = View.VISIBLE
        reset_btn.visibility = View.INVISIBLE
        stop_btn.visibility = View.INVISIBLE
    }


}

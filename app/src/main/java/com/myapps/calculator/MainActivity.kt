package com.myapps.calculator


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.lang.Math.pow
import kotlin.math.pow


class MainActivity : AppCompatActivity()
{
    private var result: TextView? = null
    private var lastnumeric = true
    private var lastdecimal = false
    private var alrdecimal = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        result = findViewById(R.id.resultview)
    }

    fun input(view: View)
    {
        result?.append((view as Button).text)
        lastnumeric = true
        lastdecimal = false
        vibration()

    }

    fun on_clear(view: View)
    {
        result?.text = ""
        lastnumeric = true
        lastdecimal = false
        alrdecimal=false
        vibration()
    }

    fun decimal(view: View)
    {
        vibration()
        if (lastnumeric && !lastdecimal && !alrdecimal) {
            result?.append(".")
            lastnumeric=false
            lastdecimal=true
            alrdecimal=true
        }
    }

    private fun vibration(){
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
        vibrator.vibrate(vibrationEffect)
    }

    private fun isOperatorAdded(value: String): Boolean {

        return if (value.startsWith("-")) {
            false
        } else {
            (value.contains("/")
                    || value.contains("*")
                    || value.contains("-")
                    || value.contains("+"))
                    || value.contains("^")
                    || value.contains("%")
        }
    }

    fun onOperator(view: View) {
        vibration()
        result?.text?.let {
            if (lastnumeric && !isOperatorAdded(it.toString())) {
                result?.append((view as Button).text)
                lastnumeric = false // Update the flag
                lastdecimal = false    // Reset the DOT flag
                alrdecimal = false
            }
        }
    }

    fun onEqual(view: View) {
        // If the last input is a number only, solution can be found.
        if (lastnumeric) {
            vibration()
            // Read the textView value
            var resultValue = result?.text.toString()
            var prefix = ""
            try {
                // Here if the value starts with '-' then we will separate it and perform the calculation with value.
                if (resultValue.startsWith("-")) {
                    prefix = "-"
                    resultValue = resultValue.substring(1);
                }

                // If the inputValue contains the Division operator
                when {
                    resultValue.contains("/") -> {
                        // Will split the inputValue using Division operator
                        val splitedValue = resultValue.split("/")

                        1+2*3-5/7

                        var one = splitedValue[0] // Value One
                        val two = splitedValue[1] // Value Two

                        if (prefix.isNotEmpty()) { // If the prefix is not empty then we will append it with first value i.e one.
                            one = prefix + one
                        }

                        result?.text = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())
                    }
                    
                    resultValue.contains("*") -> {

                        val splitedValue = resultValue.split("*")

                        var one = splitedValue[0] // Value One
                        val two = splitedValue[1] // Value Two

                        if (prefix.isNotEmpty()) { // If the prefix is not empty then we will append it with first value i.e one.
                            one = prefix + one
                        }


                        result?.text = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
                    }
                    resultValue.contains("-") -> {


                        val splitedValue = resultValue.split("-")

                        var one = splitedValue[0] // Value One
                        val two = splitedValue[1] // Value Two

                        if (prefix.isNotEmpty()) { // If the prefix is not empty then we will append it with first value i.e one.
                            one = prefix + one
                        }

                        result?.text = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())
                    }
                    resultValue.contains("+") -> {

                        val splitedValue = resultValue.split("+")

                        var one = splitedValue[0] // Value One
                        val two = splitedValue[1] // Value Two

                        if (prefix.isNotEmpty()) { // If the prefix is not empty then we will append it with first value i.e one.
                            one = prefix + one
                        }

                        result?.text = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())
                    }

                    resultValue.contains("^") -> {

                        val splitedValue = resultValue.split("^")

                        var one = splitedValue[0] // Value One
                        val two = splitedValue[1] // Value Two

                        if (prefix.isNotEmpty()) { // If the prefix is not empty then we will append it with first value i.e one.
                            one = prefix + one
                        }

                        result?.text = removeZeroAfterDot((pow(one.toDouble(),two.toDouble()).toString()))
                    }

                    resultValue.contains("%") -> {

                        val splitedValue = resultValue.split("%")

                        var one = splitedValue[0] // Value One
                        val two = splitedValue[1] // Value Two

                        if (prefix.isNotEmpty()) { // If the prefix is not empty then we will append it with first value i.e one.
                            one = prefix + one
                        }

                        result?.text = removeZeroAfterDot(((one.toDouble()/100.0)*two.toDouble()).toString())
                    }

                }
            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
        else
        {
            result?.text="Syntax Error"
            val errvibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            val vibrationEffectError = VibrationEffect.createOneShot(30,40)
            errvibrator.vibrate(vibrationEffectError)
        }
    }

    // checks if the given string has a .0 after it and if it does removes it
    private fun removeZeroAfterDot(result: String): String {
        var value = result
        if (result.contains(".0")) {
            value = result.substring(0, result.length - 2)
        }
        return value
    }
}
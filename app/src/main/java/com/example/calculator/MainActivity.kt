package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.ArithmeticException

class MainActivity : AppCompatActivity() {

    var decimalUsed = false
    var operatorUsed = false
    var lastNumeric = false
    var operation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun write2input(view: View) {
        tvInput.append((view as Button).text)
    }

    fun onDigit(view: View) {
        this.write2input(view)
        this.lastNumeric = true
    }

    fun onClear(view: View) {
        tvInput.text = ""
        this.decimalUsed = false
        this.operatorUsed = false
        this.lastNumeric = false
    }

    fun onDecimal(view: View) {
        if (!decimalUsed) {
            if (tvInput.text == "" || !lastNumeric) {
                tvInput.append("0")
            }
            this.write2input(view)
            this.decimalUsed = true
            this.lastNumeric = false
        }
    }

    fun onOperator(view: View) {
        if (tvInput.text.isEmpty()) {
            if ((view as Button).text == "-") {
                this.write2input(view)
            }
        }
        else if (!operatorUsed && lastNumeric) {
            this.write2input(view)
            this.operatorUsed = true
            this.decimalUsed = false
            this.lastNumeric = false
            this.operation = (view as Button).text.toString()
        }
    }

    fun onEqual(view: View) {
        val numberPattern = "-?\\d*(\\.\\d+)?".toRegex()
        val pattern = ("^$numberPattern[-+*/]$numberPattern$").toRegex()
        if (pattern.containsMatchIn(tvInput.text)) {
            try {
                var expression = tvInput.text.toString()
                var firstNeg = 1

                if (expression.startsWith('-')) {
                    expression = expression.slice(1 until expression.length)
                    firstNeg = -1
                }

                val parts = expression.split("[-+*/]".toRegex())
                val one = parts[0].toDouble() * firstNeg
                val two = parts[1].toDouble()
                var results: String? = null

                if (this.operation == "+")
                    results = this.removeTrailingZeros(one + two)
                else if (this.operation == "-")
                    results = this.removeTrailingZeros(one - two)
                else if (this.operation == "*")
                    results = this.removeTrailingZeros(one * two)
                else if (this.operation == "/")
                    results = this.removeTrailingZeros(one / two)

                tvInput.text = results
                this.operatorUsed = false
            }
            catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    private fun removeTrailingZeros(results: Double): String {
        val floor = results.toInt()
        if (floor.compareTo(results) == 0) {
            return floor.toString()
        }
        return results.toString()
    }
}

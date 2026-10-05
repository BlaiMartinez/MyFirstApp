package edu.estudiantat.upc.myfirstapp

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView

    private var current = "0"
    private var accumulated = 0.0
    private var pending = '='
    private var newNumber = true
    private var radians = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        display = findViewById(R.id.textDisplay)

        findViewById<Button>(R.id.btn0).setOnClickListener { digit('0') }
        findViewById<Button>(R.id.btn1).setOnClickListener { digit('1') }
        findViewById<Button>(R.id.btn2).setOnClickListener { digit('2') }
        findViewById<Button>(R.id.btn3).setOnClickListener { digit('3') }
        findViewById<Button>(R.id.btn4).setOnClickListener { digit('4') }
        findViewById<Button>(R.id.btn5).setOnClickListener { digit('5') }
        findViewById<Button>(R.id.btn6).setOnClickListener { digit('6') }
        findViewById<Button>(R.id.btn7).setOnClickListener { digit('7') }
        findViewById<Button>(R.id.btn8).setOnClickListener { digit('8') }
        findViewById<Button>(R.id.btn9).setOnClickListener { digit('9') }
        findViewById<Button>(R.id.btnDot).setOnClickListener { dot() }
        findViewById<Button>(R.id.btnAdd).setOnClickListener { operator('+') }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { operator('-') }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { operator('*') }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { operator('/') }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { equals() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clear() }
        findViewById<Button>(R.id.btnSin).setOnClickListener { trig('s') }
        findViewById<Button>(R.id.btnCos).setOnClickListener { trig('c') }
        findViewById<Button>(R.id.btnTan).setOnClickListener { trig('t') }

        findViewById<Switch>(R.id.switchRadians).setOnCheckedChangeListener { _, checked ->
            radians = checked
        }

        show()
    }

    private fun digit(d: Char) {
        current = if (newNumber || current == "0") d.toString() else current + d
        newNumber = false
        show()
    }

    private fun dot() {
        if (newNumber) {
            current = "0."
            newNumber = false
        } else if (!current.contains('.')) {
            current += "."
        }
        show()
    }

    private fun operator(symbol: Char) {
        val value = current.toDoubleOrNull() ?: 0.0
        if (pending == '=') {
            accumulated = value
        } else if (!newNumber) {
            accumulated = calculate(accumulated, pending, value)
        }
        pending = symbol
        newNumber = true
        show()
    }

    private fun equals() {
        val value = current.toDoubleOrNull() ?: 0.0
        if (pending != '=') {
            current = format(calculate(accumulated, pending, value))
            pending = '='
        }
        newNumber = true
        show()
    }

    private fun trig(function: Char) {
        val value = current.toDoubleOrNull() ?: 0.0
        val angle = if (radians) value else Math.toRadians(value)
        val result = when (function) {
            's' -> sin(angle)
            'c' -> cos(angle)
            else -> tan(angle)
        }
        if (pending == '=') {
            accumulated = result
        }
        current = format(result)
        newNumber = true
        show()
    }

    private fun clear() {
        current = "0"
        accumulated = 0.0
        pending = '='
        newNumber = true
        show()
    }

    private fun calculate(left: Double, symbol: Char, right: Double): Double = when (symbol) {
        '+' -> left + right
        '-' -> left - right
        '*' -> left * right
        '/' -> left / right
        else -> right
    }

    private fun format(value: Double): String {
        if (value.isNaN() || value.isInfinite()) {
            return "Error"
        }
        if (value == 0.0) {
            return "0"
        }
        return BigDecimal.valueOf(value)
            .round(MathContext(10))
            .stripTrailingZeros()
            .toPlainString()
    }

    private fun show() {
        display.text = current
    }
}

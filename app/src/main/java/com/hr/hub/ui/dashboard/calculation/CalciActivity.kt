package com.hr.hub.ui.dashboard.calculation

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.Display
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hr.hub.R

class CalciActivity: AppCompatActivity() {

    private lateinit var display:TextView
    private var currentInput:String=""
    private var operator:String=""
    private var firstOperand:Double?=null

    private lateinit var vibrator: Vibrator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calci)

        vibrator=getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        display=findViewById(R.id.calcidisplay)

       findViewById<Button>(R.id.button0).setOnClickListener{
           setNumberinput("0")
           vibrate()
       }
        findViewById<Button>(R.id.button1).setOnClickListener{
            setNumberinput("1")
            vibrate()
        }
        findViewById<Button>(R.id.button2).setOnClickListener{
            setNumberinput("2")
            vibrate()
        }
        findViewById<Button>(R.id.button3).setOnClickListener{
            setNumberinput("3")
            vibrate()
        }
        findViewById<Button>(R.id.button4).setOnClickListener{
            setNumberinput("4")
            vibrate()
        }
        findViewById<Button>(R.id.button5).setOnClickListener{
            setNumberinput("5")
            vibrate()
        }
        findViewById<Button>(R.id.button6).setOnClickListener{
            setNumberinput("6")
            vibrate()
        }
        findViewById<Button>(R.id.button7).setOnClickListener{
            setNumberinput("7")
            vibrate()
        }
        findViewById<Button>(R.id.button8).setOnClickListener{
            setNumberinput("8")
            vibrate()
        }
        findViewById<Button>(R.id.button9).setOnClickListener{
            setNumberinput("9")
            vibrate()
        }

        findViewById<Button>(R.id.buttonDot).setOnClickListener{
            setNumberinput(".")
            vibrate()
        }

        findViewById<Button>(R.id.buttonAdd).setOnClickListener{
            setoperator("+")
            vibrate()
        }

        findViewById<Button>(R.id.buttonSubtract).setOnClickListener{
            setoperator("-")
            vibrate()
        }

        findViewById<Button>(R.id.buttonMultiply).setOnClickListener{
            setoperator("*")
            vibrate()
        }
        findViewById<Button>(R.id.buttonDivide).setOnClickListener{
            setoperator("/")
            vibrate()
        }

        findViewById<Button>(R.id.buttonEqual).setOnClickListener{
            calculateResult()
            vibrate()
        }

        findViewById<Button>(R.id.buttonCancel).setOnClickListener{
            clearInput()
            vibrate()
        }
        findViewById<Button>(R.id.buttonDelete).setOnClickListener{
            deleteLastdigit()
            vibrate()
        }
        findViewById<Button>(R.id.buttonSquareRoot).setOnClickListener{
            calsquareRoot()
            vibrate()
        }
        findViewById<Button>(R.id.buttonLog).setOnClickListener {
            calculateLog()
            vibrate()
        }
        findViewById<Button>(R.id.buttonPercent).setOnClickListener {
            calculatePercentage()
            vibrate()
        }
        findViewById<Button>(R.id.buttonPower).setOnClickListener {
            setOperator("^")
            vibrate()
        }
        findViewById<Button>(R.id.buttonPi).setOnClickListener {
            insertPi()
            vibrate()
        }
    }

    private fun setNumberinput(number:String){
            appendToInput(number)
    }

    private fun appendToInput(value:String){
        currentInput+=value
        display.text=currentInput
    }

    private fun setoperator(op:String){
        if(firstOperand==null){
            firstOperand=currentInput.toDoubleOrNull()
        }
        else{
            calculateResult()
        }
        operator=op
        currentInput=""
    }

    private fun calculateResult(){
        val secondoperand=currentInput.toDoubleOrNull()?:return

        val result=when(operator){
            "+"->firstOperand?.plus(secondoperand)
            "-"->firstOperand?.minus(secondoperand)
            "*"->firstOperand?.times(secondoperand)
            "/"->if(secondoperand!=0.0) {
                firstOperand?.div(secondoperand)
            }
            else{
                null
            }
            "^"->firstOperand?.let {Math.pow(it,secondoperand)  }
            else->null
        }

        if(result!=null){
            display.text=result.toString()
            firstOperand=result
        }
        else{
            display.text="Error"
        }
        currentInput=""
        operator=""
    }

    private fun clearInput(){
        currentInput=""
        operator=""
        firstOperand=null
        display.text=""
    }
    private fun deleteLastdigit(){
        if(currentInput.isNotEmpty()){
            currentInput=currentInput.dropLast(1)
            display.text=currentInput
        }
    }
    private fun calsquareRoot(){
        currentInput.toDoubleOrNull()?.let {
            display.text=Math.sqrt(it).toString()
            currentInput=""
        }
    }
    private fun calculateLog(){
        currentInput.toDoubleOrNull()?.let {
            display.text=Math.log10(it).toString()
            currentInput=""
        }
    }
    private fun calculatePercentage(){
        currentInput.toDoubleOrNull()?.let {
            display.text=(it/100).toString()
            currentInput=""
        }
    }

    private fun setOperator(op: String){
        if(firstOperand==null){
            firstOperand=currentInput.toDoubleOrNull()
        }
        else{
            calculateResult()
        }
        operator=op
        currentInput=""
    }
    private fun insertPi(){
        currentInput+=Math.PI.toString()
        display.text=currentInput

    }
    private fun vibrate(){
        if(vibrator.hasVibrator()){
            vibrator.vibrate(30)
        }
    }
}
package com.hr.hub.ui.dashboard.calculation

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.core.content.ContextCompat
import com.hr.hub.R

class BMIActivity: AppCompatActivity() {

    private lateinit var  rbtnmale:RadioButton
    private lateinit var rbtnfemale:RadioButton
    private lateinit var etage:EditText
    private lateinit var etheight:EditText
    private lateinit var etweight:EditText
    private lateinit var btncalculate:Button
    private lateinit var tvresult:TextView
    private lateinit var sclbmi: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        rbtnmale=findViewById(R.id.rbtn_male)
        rbtnfemale=findViewById(R.id.rbtn_female)
        etage=findViewById(R.id.et_age)
        etheight=findViewById(R.id.et_height)
        etweight=findViewById(R.id.et_weight)
        btncalculate=findViewById(R.id.btn_calbmi)
        tvresult=findViewById(R.id.tvBMIResult)
        sclbmi=findViewById(R.id.sl_bmi)

        btncalculate.setOnClickListener{
            if(validateInput() && getgender()){
                calculateBMI()
            }
        }
    }

    private fun getgender():Boolean{
        if (!rbtnmale.isChecked && !rbtnfemale.isChecked) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateInput():Boolean{
        if(etage.text.toString().isEmpty() || etheight.text.toString().isEmpty() || etweight.text.toString().isEmpty()){
            Toast.makeText(this,"Please Enter Valid Deatails",Toast.LENGTH_SHORT).show()
            return false
        }

        val age=etage.text.toString().toInt()
        val height=etheight.text.toString().toDouble()
        val weight=etweight.text.toString().toDouble()

        if(age<0 || height<0 || weight<0){
            Toast.makeText(this,"Invalid Input",Toast.LENGTH_SHORT).show()
            return false
        }
        return  true
    }

    private fun calculateBMI(){

        val height = etheight.text.toString().toDouble() * 0.3048 // Convert feet to meters
        val weight = etweight.text.toString().toDouble()

        val bmi=weight/(height*height)

        val bmiCategory:String
        val color:Int
        when {
            bmi < 18.5 -> {
                bmiCategory = "Underweight"
                color = ContextCompat.getColor(this, R.color.Yellow)
            }
            bmi in 18.5..24.9 -> {
                bmiCategory = "Normal weight"
                color = ContextCompat.getColor(this, R.color.Green)
            }
            bmi in 25.0..29.9 -> {
                bmiCategory = "Overweight"
                color = ContextCompat.getColor(this, R.color.Orange)
            }
            else -> {
                bmiCategory = "Obesity,\n You are near to Die!!!!!!\n ⚠\uFE0F⚠\uFE0F⚠\uFE0F⚠\uFE0F"
                color = ContextCompat.getColor(this, R.color.Red)
            }
        }
        sclbmi.setBackgroundColor(color)
        tvresult.text = String.format("BMI = %.2f\nCategory: %s", bmi, bmiCategory)
    }
}
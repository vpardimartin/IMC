package com.example.imc

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.imc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calcularButton.setOnClickListener {
            calculateIMC()
            // Cierra el teclado virtual asociado a entryAltura
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.entryAltura.windowToken, 0)

            // Cierra el teclado virtual asociado a entryPeso
            inputMethodManager.hideSoftInputFromWindow(binding.entryPeso.windowToken, 0)

        }
    }

    /*
    La anotación @SuppressLint("SetTextI18n") se utiliza para suprimir las
    advertencias de lint relacionadas con problemas de internacionalización
    en Android Studio. En este caso específico, está suprimiendo la advertencia
    de lint sobre el uso de cadenas de texto codificadas sin considerar la
    internacionalización.

    La advertencia está relacionada con el hecho de que establecer directamente
    el texto en la interfaz de usuario sin admitir la internacionalización puede
    causar problemas cuando la aplicación necesita ser traducida a diferentes idiomas.
    El uso de @SuppressLint("SetTextI18n") indica que el desarrollador es consciente
    de esto y está suprimiendo intencionalmente la advertencia.

    En el desarrollo de Android, generalmente se recomienda utilizar recursos de cadena
    para el texto de la interfaz de usuario para facilitar la internacionalización y
    hacer que la aplicación sea más adaptable a diferentes idiomas. Sin embargo, en
    algunos casos, los desarrolladores pueden optar por usar cadenas codificadas por
    simplicidad, y en tales casos, pueden usar esta anotación para suprimir la
    advertencia de lint correspondiente.
     */
    @SuppressLint("SetTextI18n")

    private fun calculateIMC() {
        val weightStr = binding.entryPeso.text.toString()
        val heightStr = binding.entryAltura.text.toString()

        if (weightStr.isEmpty() || heightStr.isEmpty()) {
            showToast("Por favor, complete ambos campos.")
            return
        }

        val radioGroup: RadioGroup = binding.radioGroup
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId

        if (selectedRadioButtonId == -1) {
            showToast("Por favor, seleccione el género.")
            return
        }

        val weight = weightStr.toDouble()
        val height = heightStr.toDouble() / 100 // Convert height to meters

        val radioButton: RadioButton = findViewById(selectedRadioButtonId)
        val gender = radioButton.text.toString()

        val imc = calculateIMCValue(weight, height)
        val status = getIMCStatus(gender, imc)

        binding.resultadoIMC.text = "%.2f".format(imc)
        binding.estadoIMC.text = "%s".format(status)

    }

    private fun calculateIMCValue(weight: Double, height: Double): Double {
        return weight / (height * height)
    }

    private fun getIMCStatus(gender: String, imc: Double): String {
        return when (gender) {
            getString(R.string.hombre) -> getMaleIMCStatus(imc)
            getString(R.string.mujer) -> getFemaleIMCStatus(imc)
            else -> ""
        }
    }

    private fun getMaleIMCStatus(imc: Double): String {
        return when {
            imc < 18.5 -> "Peso inferior al normal"
            imc in 18.5..24.9 -> "Normal"
            imc in 25.0..29.9 -> "Sobrepeso"
            imc > 30.0 -> "Obesidad"
            else -> ""
        }
    }

    private fun getFemaleIMCStatus(imc: Double): String {
        return when {
            imc < 18.5 -> "Peso inferior al normal"
            imc in 18.5..23.9 -> "Normal"
            imc in 24.0..28.9 -> "Sobrepeso"
            imc > 29.0 -> "Obesidad"
            else -> ""
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

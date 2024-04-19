package com.example.vamosracharbywilliams

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vamosracharbywilliams.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    lateinit var binding: ActivityMainBinding
    private var tts: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        tts = TextToSpeech(this, this)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.editValorConta.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calcularDivisao()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.editValorPessoas.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                calcularDivisao()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        fun falar(texto: String) {
            tts!!.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "")
        }

        binding.buttonFalar.setOnClickListener {
            if (!binding.textView2.text.isEmpty()) {
                falar(binding.textView2.text.toString())
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun calcularDivisao() {
        val conta = binding.editValorConta.text.toString().trim().toDoubleOrNull()
        val pessoas = binding.editValorPessoas.text.toString().trim().toIntOrNull()
        if (conta != null && pessoas != null && pessoas != 0) {
            val valorDividido = String.format("%.2f", conta / pessoas)
            binding.textView2.text = "Valor por pessoa: R$ ${valorDividido}"

            binding.buttonCompartilhar.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Valor por pessoa: T$ ${valorDividido}")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)

            }
        } else {
            binding.textView2.text = ""
        }
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "A linguagem especificada não é suportada.")
            } else {
                Log.e("TTS", "A inizialização falhou.")
            }
        }
    }

    public override fun onDestroy() {

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}
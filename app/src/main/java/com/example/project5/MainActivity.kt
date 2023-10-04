package com.example.project5

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.example.project5.TranslatorViewModel
import com.example.project5.databinding.ActivityMainBinding
import com.example.project5.databinding.FragmentTextScreenBinding
import com.google.mlkit.common.model.RemoteModelManager


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingFrag: FragmentTextScreenBinding
    private lateinit var viewModel: TranslatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingFrag = FragmentTextScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(TranslatorViewModel::class.java)
        val lookatview = binding.lookAt
        var edittextview: EditText = bindingFrag.typeHere

        binding.sourceLanguge.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.sEnglish -> viewModel.SetLanguages(
                    TranslateLanguage.ENGLISH,
                    viewModel.outputLanguageOption
                )

                R.id.sSpanish -> viewModel.SetLanguages(
                    TranslateLanguage.SPANISH,
                    viewModel.outputLanguageOption
                )

                R.id.sGerman -> viewModel.SetLanguages(
                    TranslateLanguage.GERMAN,
                    viewModel.outputLanguageOption
                )
            }


        }

        binding.Translation.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.tEnglish -> viewModel.SetLanguages(
                    viewModel.inputLanguageOption,
                    TranslateLanguage.ENGLISH
                )

                R.id.tSpanish -> viewModel.SetLanguages(
                    viewModel.inputLanguageOption,
                    TranslateLanguage.SPANISH
                )

                R.id.tGerman -> viewModel.SetLanguages(
                    viewModel.inputLanguageOption,
                    TranslateLanguage.GERMAN
                )
            }

        }


        viewModel.bindingFrag.observe(this) { bindingFrag ->
            viewModel.bindingFrag.value?.typeHere?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(mEdit: Editable) {
                    Log.i("INFO", "Hello")
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val current_text = s.toString()
                    println("After text changed callback")
                    println(current_text)

                    viewModel.currentStr.value = current_text

                    val translated_str = viewModel.DoTranslateWithTextViews(current_text)
                    binding.lookAt.setText(translated_str)
                }
            })
        }


    }
}


// output language code is something like TranslateLanguage.ENGLISH
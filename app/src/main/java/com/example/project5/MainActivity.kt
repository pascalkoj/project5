package com.example.project5

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: TranslatorViewModel by viewModels()

    }





    // output language code is something like TranslateLanguage.ENGLISH
    /*fun TranslateWithAutomaticInputLang(text: String, outputLanguageCode: String) : String
    {
        var output = ""
        val languageIdentifier = LanguageIdentification.getClient()
        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { languageCode ->
                if (languageCode == "und") {
                    Log.i("INFO", "Can't identify language.")
                } else {
                    Log.i("INFO", "Language: $languageCode")
                    output = TranslateWithLang(text, languageCode, outputLanguageCode)
                }
            }
            .addOnFailureListener {
                // Model couldn’t be loaded or other internal error.
                // ...
                Log.e("ERROR", "Failed to detect language of input string")
            }
        return output
    }
    // language codes are something like TranslateLanguage.ENGLISH
    fun TranslateWithLang(text: String, inputLanguageCode: String, outputLanguageCode: String) : String
    {
        var output = ""
        // Create an English-German translator:
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(inputLanguageCode)
            .setTargetLanguage(outputLanguageCode)
            .build()
        val translator = Translation.getClient(options)
        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                // Model downloaded successfully. Okay to start translating.
                // (Set a flag, unhide the translation UI, etc.)
                translator.translate(text)
                    .addOnSuccessListener { translatedText ->
                        // Translation successful.
                        println(translatedText)
                        output = translatedText
                    }
                    .addOnFailureListener { exception ->
                        // Error.
                        // ...
                        Log.e("ERROR",
                            "Failed to translate: $text  from $inputLanguageCode to $outputLanguageCode"
                        )
                    }
            }
            .addOnFailureListener { exception ->
                // Model couldn’t be downloaded or other internal error.
                // ...
                Log.e("ERROR", "Model $inputLanguageCode -> $outputLanguageCode couldn't be downloaded")
            }
        return output
    }
    */


    class TranslatorViewModel : ViewModel()
    {
        var inputLanguageOption = TranslateLanguage.ENGLISH
        var outputLanguageOption = TranslateLanguage.GERMAN

        var inputText: EditText? = null
        var outputTextView: TextView? = null

        // called on init. Caches references to the input/output text boxes
        fun SetTextViews(inputText: EditText, outputTextView: TextView)
        {
            this.inputText = inputText
            this.outputTextView = outputTextView
        }
        // call when user changes languages option
        fun SetLanguages(inputLanguageCode: String, outputLanguageCode: String)
        {
            this.inputLanguageOption = inputLanguageCode
            this.outputLanguageOption = outputLanguageCode
        }


        fun DoTranslateWithTextViews()
        {
            if (inputText == null || outputTextView == null)
            {
                return;
            }
            var input_str = inputText?.text.toString()
            var translated_str = ""
            if (inputLanguageOption == "")
            {
                translated_str = TranslateWithAutomaticInputLang(input_str, outputLanguageOption)
            }
            else
            {
                translated_str = TranslateWithLang(input_str, inputLanguageOption, outputLanguageOption)
            }
            outputTextView?.setText(translated_str)
        }

        // output language code is something like TranslateLanguage.ENGLISH
        fun TranslateWithAutomaticInputLang(text: String, outputLanguageCode: String) : String
        {
            var output = ""
            val languageIdentifier = LanguageIdentification.getClient()
            languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener { languageCode ->
                    // we've automatically detected the input language
                    if (languageCode == "und") {
                        Log.i("INFO", "Can't identify language.")
                    } else {
                        Log.i("INFO", "Language: $languageCode")
                        // translate input to output lang
                        output = TranslateWithLang(text, languageCode, outputLanguageCode)
                    }
                }
                .addOnFailureListener {
                    // Model couldn’t be loaded or other internal error.
                    // ...
                    Log.e("ERROR", "Failed to detect language of input string")
                }
            return output
        }

        // language codes are something like TranslateLanguage.ENGLISH
        fun TranslateWithLang(text: String, inputLanguageCode: String, outputLanguageCode: String) : String
        {
            var output = ""
            // Create an English-German translator:
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(inputLanguageCode)
                .setTargetLanguage(outputLanguageCode)
                .build()
            val translator = Translation.getClient(options)
            var conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()
            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    // Model downloaded successfully. Okay to start translating.
                    // (Set a flag, unhide the translation UI, etc.)
                    translator.translate(text)
                        .addOnSuccessListener { translatedText ->
                            // Translation successful.
                            println(translatedText)
                            output = translatedText
                        }
                        .addOnFailureListener { exception ->
                            // Error.
                            // ...
                            Log.e("ERROR",
                                "Failed to translate: $text  from $inputLanguageCode to $outputLanguageCode"
                            )
                        }
                }
                .addOnFailureListener { exception ->
                    // Model couldn’t be downloaded or other internal error.
                    // ...
                    Log.e("ERROR", "Model $inputLanguageCode -> $outputLanguageCode couldn't be downloaded")
                }
            return output
        }

    }
}
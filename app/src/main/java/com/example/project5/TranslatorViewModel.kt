package com.example.project5

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project5.databinding.FragmentTextScreenBinding
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslatorViewModel : ViewModel()
{
    var inputLanguageOption = TranslateLanguage.ENGLISH
    var outputLanguageOption = TranslateLanguage.GERMAN

    val bindingFrag: MutableLiveData<FragmentTextScreenBinding> = MutableLiveData()
    // Create a LiveData with a String
    val currentStr: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


    // call when user changes languages option
    fun SetLanguages(inputLanguageCode: String, outputLanguageCode: String)
    {
        this.inputLanguageOption = inputLanguageCode
        this.outputLanguageOption = outputLanguageCode
    }

    fun DoTranslateWithTextViews(input_str: String) : String
    {
        var translated_str = ""
        if (inputLanguageOption == "")
        {
            translated_str = TranslateWithAutomaticInputLang(input_str, outputLanguageOption)
        }
        else
        {
            translated_str = TranslateWithLang(input_str, inputLanguageOption, outputLanguageOption)
        }
        currentStr.postValue(translated_str)
        return translated_str
    }

    // output language code is something like TranslateLanguage.ENGLISH
    fun TranslateWithAutomaticInputLang(text: String, outputLanguageCode: String) : String
    {
        var output = ""
        val languageIdentifier = LanguageIdentification.getClient()
        var task = languageIdentifier.identifyLanguage(text)
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
        while (!task.isComplete) {}
        return task.result
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
        var task = translator.downloadModelIfNeeded(conditions)
        while (!task.isComplete){ }
        var task2 = translator.translate(text)
        while (!task2.isComplete){ }
        return task2.result
    }

}
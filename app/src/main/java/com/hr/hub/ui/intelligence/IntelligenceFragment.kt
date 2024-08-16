package com.hr.hub.ui.intelligence

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.ResponseStoppedException

import com.hr.hub.databinding.FragmentIntelligenceBinding
import com.hr.hub.ui.APIFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IntelligenceFragment : Fragment() {

    private var _binding: FragmentIntelligenceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntelligenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val enterprompt = binding.ETPrompt //val enterprompt:EditText=view.findViewById(R.id.text_prompt)
        val resultprompt = binding.TVResult
        val sendprompt = binding.btnPromptsend

        sendprompt.setOnClickListener {
            val prompt = enterprompt.text.toString()

            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                apiKey =APIFile.geminiAPI
            )

            fun generatetext(prompt: String) {
                CoroutineScope(Dispatchers.IO).launch {

                    try {
                        val response = generativeModel.generateContent(prompt)

                        withContext(Dispatchers.Main) {
                            resultprompt.text = response.text
                        }
                    } catch (e: ResponseStoppedException) {
                        Log.e("AIError", "content generation stopped: ${e.response}")
                    }
                }
            }
            return@setOnClickListener generatetext(prompt)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
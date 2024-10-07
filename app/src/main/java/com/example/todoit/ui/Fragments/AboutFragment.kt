package com.example.todoit.ui.Fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import com.example.todoit.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout and initialize view binding
        binding = FragmentAboutBinding.inflate(inflater, container, false)

        // Set styled text for Privacy Policy
        binding.textPrivacyPolicy.text = formatText(
            "Privacy Policy:",
            "Your privacy is important to us. This app does not collect any personal information or user data. All data remains stored locally on your device."
        )

        // Set styled text for Terms and Conditions
        binding.textTermsConditions.text = formatText(
            "Terms and Conditions:",
            "By using this app, you agree that no personal data will be shared or collected by the app. The app requires notification and alarm permissions for sending task notifications, which are used solely for the purpose of managing your tasks."
        )

        return binding.root
    }

    // Function to format text with bold and underline for the title
    private fun formatText(title: String, description: String): SpannableString {
        val spannable = SpannableString("$title\n\n$description")

        // Make the title bold and underlined
        spannable.setSpan(StyleSpan(android.graphics.Typeface.BOLD), 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(UnderlineSpan(), 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannable
    }
}

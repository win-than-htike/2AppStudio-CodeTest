package com.onething.a2appstudiotest.features.links.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.webkit.URLUtil
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.onething.a2appstudiotest.R
import com.onething.a2appstudiotest.databinding.DialogAddLinksBinding


class AddLinksDialog : DialogFragment() {

    companion object {
        const val TAG = "AddLinksDialog"
    }

    private lateinit var binding: DialogAddLinksBinding

    private var onClickAddLink: ((String) -> Unit)? = null

    fun setOnClickAddLink(onClickAddLink: (String) -> Unit) {
        this.onClickAddLink = onClickAddLink
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.dialog_add_links,  null, false)

        val onTextChanged = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                if (!URLUtil.isValidUrl(text)) binding.etLinks.error =
                    requireContext().getString(R.string.error_invalid_link)
            }
        }

        binding.etLinks.addTextChangedListener(onTextChanged)

        binding.btnAdd.setOnClickListener {
            val link = binding.etLinks.text.toString()
            if (URLUtil.isValidUrl(link)) {
                onClickAddLink?.invoke(link)
            }
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .setTitle("Add Link")
            .create()
    }

}
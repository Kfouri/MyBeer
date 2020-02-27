package com.kfouri.mybeer.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import com.kfouri.mybeer.R
import kotlinx.android.synthetic.main.activity_bar_details.*
import kotlinx.android.synthetic.main.rating_dialog.*
import kotlinx.android.synthetic.main.rating_dialog.ratingBar


class RatingDialog(context: Context, private val myVote: Float, private val listener: DialogListener) : Dialog(context), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.rating_dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnCancel.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
        ratingBar.rating = myVote
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSubmit -> listener.submitClick(ratingBar.rating)
            R.id.btnCancel -> { dismiss(); listener.cancelClick() }
        }
        dismiss()
    }

    interface DialogListener {
        fun submitClick(value: Float)
        fun cancelClick()
    }
}
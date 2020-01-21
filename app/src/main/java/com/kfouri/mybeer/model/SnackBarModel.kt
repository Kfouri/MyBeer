package com.kfouri.mybeer.model

class SnackBarModel(val text: String, val type: String) {

    companion object {
        const val SNACKBAR_ERROR = "snackbar_error"
        const val SNACKBAR_INFO = "snackbar_info"
    }
}




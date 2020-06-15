package com.example.anch_kotiln.Activity.Dialog

interface Dialog {
    fun setTitle(text: String?)
    fun setMessage(text: String?)
    fun setPositiveButton(text: String?)
    fun setNegativeButton(text: String?)
    fun setNeutralButton(text: String?)
}
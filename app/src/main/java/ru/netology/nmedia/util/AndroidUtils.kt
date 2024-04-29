package ru.netology.nmedia.util

import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import java.util.Date
import java.util.Locale

object AndroidUtils {
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun View.focusAndShowKeyboard() {
    /**
     * Его следует вызывать, когда окно уже имеет фокус.
     */
    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                // Мы по-прежнему публикуем вызов, на случай, если нас уведомят о фокусе Windows
                // но InputMethodManager еще не настроен должным образом.
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    requestFocus()
    if (hasWindowFocus()) {
        // Не нужно ждать, пока окно получит фокус.
        showTheKeyboardNow()
    } else {
        // Нам нужно подождать, пока окно не получит фокус.
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    // Это уведомление придет непосредственно перед настройкой InputMethodManager.
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        // Очень важно удалить этот прослушиватель, как только мы закончим.
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}
fun getTime(): String =
    android.icu.text.SimpleDateFormat("dd MMMM в HH:mm", Locale.getDefault()).format(Date())
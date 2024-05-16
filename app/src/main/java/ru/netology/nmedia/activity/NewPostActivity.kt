package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.editTextContent.requestFocus()
        val textContent = intent?.getStringExtra(Intent.EXTRA_TEXT)
        if (textContent.isNullOrBlank()) {
            binding.save.setImageResource(R.drawable.ic_add_24dp)
        } else {
            binding.save.setImageResource(R.drawable.ic_save_48)
            binding.editTextContent.setText(textContent)
        }
        
        binding.save.setOnClickListener {
            val text = binding.editTextContent.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                setResult(RESULT_OK, Intent().apply { putExtra(Intent.EXTRA_TEXT, text) })
            }
            finish()
        }
    }
}

object NewPostContract : ActivityResultContract<String?, String?>() {
    override fun createIntent(context: Context, input: String?) =
        Intent(context, NewPostActivity::class.java).putExtra(Intent.EXTRA_TEXT, input)


    override fun parseResult(resultCode: Int, intent: Intent?) =
        intent?.getStringExtra(Intent.EXTRA_TEXT)

}
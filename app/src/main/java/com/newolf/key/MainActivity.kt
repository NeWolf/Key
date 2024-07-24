package com.newolf.key

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    companion object{
        const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val viewKeyboard = findViewById<KeyboardView>(R.id.view_keyboard)
        Log.e(TAG, "onCreate: viewKeyboard = $viewKeyboard", )

       val  keyboradNumber = Keyboard(this, R.xml.keyboard_number)
       val  keyboradLetter = Keyboard(this, R.xml.keyboard_letter)
        viewKeyboard.keyboard = keyboradLetter

        viewKeyboard.setOnKeyboardActionListener(object : KeyboardView.OnKeyboardActionListener {
            override fun onPress(primaryCode: Int) {
                Log.e(TAG, "onPress: primaryCode = $primaryCode")
            }

            override fun onRelease(primaryCode: Int) {
                Log.e(TAG, "onRelease: primaryCode = $primaryCode")
            }

            override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
                Log.e(TAG, "onKey: primaryCode = $primaryCode , keyCodes = $keyCodes")
            }

            override fun onText(text: CharSequence?) {
                Log.e(TAG, "onText: text = $text")
            }

            override fun swipeLeft() {
                Log.e(TAG, "swipeLeft:")
            }

            override fun swipeRight() {
                Log.e(TAG, "swipeRight:")
            }

            override fun swipeDown() {
                Log.e(TAG, "swipeDown:")
            }

            override fun swipeUp() {
                Log.e(TAG, "swipeUp:")
            }
        })


        val et = findViewById<KeyBoardEditText>(R.id.ed_main)

        val llMain = findViewById<LinearLayout>(R.id.layout_main)
        val llRoot = findViewById<LinearLayout>(R.id.layout_root)
        et.setKeyboardType(llMain, viewKeyboard,true)
        et.listener = object : KeyBoardEditText.OnKeyboardStateChangeListener {
            var height = 0
            override fun show() {
                et.post {
                    val rect = IntArray(2)
                    et.getLocationOnScreen(rect)
                    val y = rect[1]

                    viewKeyboard?.getLocationOnScreen(rect)
                    val keyboardY = rect[1]

                     height = y-(keyboardY-et.measuredHeight)
                    llRoot.scrollBy(0, height)
                }
            }

            override fun hide() {
                llRoot.scrollBy(0,-height)
            }
        }

//        val keyboard = CustomKeyboard(this, R.xml.keyboard01)
//        keyboard.registerKeyboardView(findViewById(R.id.keyboardview))
//        keyboard.registerEditText(findViewById(R.id.edit_query), true)
//        findViewById<DragLayout>(R.id.drag_root).setLogEnable(true)

        val etQuery = findViewById<EditText>(R.id.edit_query)
        val kb = findViewById<com.custom.keyboard.KeyboardView>(R.id.keyboardview)
        kb.setOnKeyboardActionListener(object :
            com.custom.keyboard.KeyboardView.DefaultOnKeyboardActionListener() {
            override fun onPress(primaryCode: Int) {
                Log.e(TAG, "onPress: primaryCode = $primaryCode")
                val editable = etQuery.text
                val start = etQuery.selectionStart
                editable?.insert(start, primaryCode.toChar().toString())
            }

            override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
                super.onKey(primaryCode, keyCodes)
                Log.e(TAG, "onKey: primaryCode = $primaryCode")
                val editable = etQuery.text
                val start = etQuery.selectionStart
                editable?.insert(start, primaryCode.toChar().toString())
            }

            override fun onRelease(primaryCode: Int) {
                if (primaryCode == -999){
                    kb.visibility = View.GONE
                }
                Log.e(TAG, "onRelease: primaryCode = $primaryCode")


            }

        })



        findViewById<Button>(R.id.btn_show).setOnClickListener {
            kb.visibility = View.VISIBLE
            val manager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
         val  data  = ClipData.newPlainText("abc123", "abc123").apply {
                description.extras = PersistableBundle().apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                    }
                }
            }
            manager.setPrimaryClip(data)

        }

        findViewById<SeekBar>(R.id.seek_bar).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                kb.setBackgroundAlpha(progress)
//                kb.backgroundTintList = ColorStateList.valueOf(Color.argb(progress,5,5,5))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

    }
}
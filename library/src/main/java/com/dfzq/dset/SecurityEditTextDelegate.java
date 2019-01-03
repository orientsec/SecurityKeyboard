package com.dfzq.dset;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;

import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_NUM;

/**
 * Created by meihu on 2017/6/27.
 */

public class SecurityEditTextDelegate implements SecurityEditTextInterface {

    private boolean voice;

    private int type;

    private int keyboardFrameId;

    private EditText editText;

    public SecurityEditTextDelegate(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void init(Context context) {
        editText.setClickable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setShowSoftInputOnFocus(false);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            editText.setInputType(InputType.TYPE_NULL);
        }
    }

    @Override
    public void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SecurityEditText);
        voice = a.getBoolean(R.styleable.SecurityEditText_keyboardVoice, false);
        type = a.getInt(R.styleable.SecurityEditText_inputType, KEYBOARD_NUM);
        keyboardFrameId = a.getInt(R.styleable.SecurityEditText_keyboardFrameId, 0);
        a.recycle();
        init(context);
    }

    @Override
    public int getKeyboardFrameId() {
        return keyboardFrameId;
    }

    @Override
    public void setKeyboardFrameId(int keyboardFrameId) {
        this.keyboardFrameId = keyboardFrameId;
    }

    @Override
    public boolean isHideEnable() {
        return keyboardFrameId == 0;
    }

    @Override
    public boolean getVoice() {
        return voice;
    }

    @Override
    public void setVoice(boolean voice) {
        this.voice = voice;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void hideSystemSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    public boolean hideSoftKeyboard() {
        return KeyboardManager.getInstance().hideSoftInput(editText);
    }

    @Override
    public boolean performClick() {
        if (editText.isFocused()) {
            hideSystemSoftKeyboard();
            KeyboardManager.getInstance().showSoftInput(editText);
        }
        return false;
    }

    @Override
    public void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused) {
            hideSystemSoftKeyboard();
            KeyboardManager.getInstance().showSoftInput(editText);
        } else {
            hideSoftKeyboard();
        }
    }

    @Override
    public void onAttachedToWindow() {
        if (editText.isFocused()) {
            hideSystemSoftKeyboard();
            KeyboardManager.getInstance().showSoftInput(editText);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        if (editText.isFocused()) {
            hideSoftKeyboard();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus && editText.hasFocus()) {
            editText.post(new Runnable() {
                @Override
                public void run() {
                    hideSystemSoftKeyboard();
                    KeyboardManager.getInstance().showSoftInput(editText);
                }
            });
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK && hideSoftKeyboard();
    }

    @Override
    public void onVoiceStateChanged(int state) {

    }

    @Override
    public void onVoiceErrorState(int error) {

    }
}

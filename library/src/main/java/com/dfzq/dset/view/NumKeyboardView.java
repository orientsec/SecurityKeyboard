package com.dfzq.dset.view;

import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;
import android.util.AttributeSet;

import com.dfzq.dset.KeyboardManager;
import com.dfzq.dset.R;

import static android.inputmethodservice.Keyboard.KEYCODE_DELETE;
import static android.inputmethodservice.Keyboard.KEYCODE_DONE;
import static android.inputmethodservice.Keyboard.KEYCODE_MODE_CHANGE;

/**
 * ProductName:DsetKeyboard
 * PackageName:com.xiaomao.keyboard_id.keyboard_id
 * Dage:2016/10/14
 * Author:Fredric
 * Coding is an art not science
 */

public class NumKeyboardView extends SecretKeyboardView {
    public NumKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void draw(Keyboard.Key key, Canvas canvas) {
        int bgRes = 0;
        if (key.codes[0] == KEYCODE_DELETE) {
            bgRes = R.drawable.dset_btn_keyboard_key_num_delete;
        } else if (key.codes[0] == KEYCODE_DONE) {
            if (isHideEnable) {
                bgRes = R.drawable.dset_btn_keyboard_key_finish;
            } else {
                bgRes = R.drawable.dset_btn_keyboard_special_normal_bg;
            }
        } else if (key.codes[0] == KEYCODE_MODE_CHANGE) {
            bgRes = R.drawable.dset_keyboard_special_btn_bg;
        } else if (key.codes[0] == 46) {
            bgRes = R.drawable.dset_keyboard_special_btn_bg;
        } else if (key.codes[0] == 32) {
            bgRes = KeyboardManager.logo;
        }
        if (bgRes > 0) {
            drawKeyBackground(bgRes, canvas, key);
            drawText(canvas, key);
        }
    }
}

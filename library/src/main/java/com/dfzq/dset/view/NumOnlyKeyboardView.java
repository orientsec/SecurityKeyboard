package com.dfzq.dset.view;

import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;
import android.util.AttributeSet;

import com.dfzq.dset.KeyboardManager;
import com.dfzq.dset.R;

import static android.inputmethodservice.Keyboard.KEYCODE_DELETE;
import static android.inputmethodservice.Keyboard.KEYCODE_DONE;

/**
 * ProductName:DsetKeyboard
 * PackageName:com.xiaomao.keyboard_id.keyboard_id
 * Dage:2016/10/14
 * Author:Fredric
 * Coding is an art not science
 */

public class NumOnlyKeyboardView extends SecretKeyboardView {
    public NumOnlyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumOnlyKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        } else if (key.codes[0] == 32) {
            bgRes = KeyboardManager.logo;
        } else if (key.codes[0] == -8) {
            bgRes = R.drawable.dset_keyboard_btn_press;
        }
        if (bgRes > 0) {
            drawKeyBackground(bgRes, canvas, key);
            drawText(canvas, key);
        }
    }
}

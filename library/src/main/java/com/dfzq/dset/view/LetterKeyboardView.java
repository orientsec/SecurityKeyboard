package com.dfzq.dset.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import android.util.AttributeSet;


import com.dfzq.dset.KeyboardManager;
import com.dfzq.dset.R;

import static android.inputmethodservice.Keyboard.KEYCODE_DELETE;
import static android.inputmethodservice.Keyboard.KEYCODE_DONE;
import static android.inputmethodservice.Keyboard.KEYCODE_MODE_CHANGE;
import static android.inputmethodservice.Keyboard.KEYCODE_SHIFT;

/**
 * ProductName:DsetKeyboard
 * PackageName:com.xiaomao.keyboard_id.keyboard_id
 * Dage:2016/10/14
 * Author:Fredric
 * Coding is an art not science
 */

public class LetterKeyboardView extends SecretKeyboardView {

    private boolean upperCase;

    public LetterKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LetterKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void draw(Keyboard.Key key, Canvas canvas) {
        int bgRes = 0;
        // SHIFT
        if (key.codes[0] == KEYCODE_SHIFT) {
            bgRes = R.drawable.dset_btn_keyboard_key_shift;
        }
        // DELETE
        else if (key.codes[0] == KEYCODE_DELETE) {
            bgRes = R.drawable.dset_btn_keyboard_key_delete;
        }
        // CHANGE
        else if (key.codes[0] == KEYCODE_MODE_CHANGE) {
            bgRes = R.drawable.dset_keyboard_special_btn_bg;
        }
        // space
        else if (key.codes[0] == 32) {
            bgRes = KeyboardManager.logo;
        } else if (key.codes[0] == KEYCODE_DONE) {
            if (isHideEnable) {
                bgRes = R.drawable.dset_btn_keyboard_key_finish;
            } else {
                bgRes = R.drawable.dset_btn_keyboard_special_normal_bg;
            }
        }
        if (bgRes > 0) {
            drawKeyBackground(bgRes, canvas, key);
            drawText(canvas, key);
        }
    }

    @Override
    protected void drawText(Canvas canvas, Keyboard.Key key) {
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.parseColor("#333333"));
        if (key.codes[0] == KEYCODE_MODE_CHANGE) {
            paint.setTextSize(dip2px(20));
        } else if (key.codes[0] == 46) {
            paint.setTextSize(dip2px(21));
        } else {
            paint.setTextSize(dip2px(20));
        }
        paint.setAntiAlias(true);

        if (key.label != null) {
            paint.getTextBounds(key.label.toString(), 0, key.label.toString()
                    .length(), bounds);
            canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                    (key.y + key.height / 2) + bounds.height() / 2, paint);
        }
    }

    public boolean isUpperCase() {
        return upperCase;
    }

    public void setUpperCase(boolean upperCase) {
        this.upperCase = upperCase;
    }
}

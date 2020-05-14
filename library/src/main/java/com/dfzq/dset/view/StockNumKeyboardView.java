package com.dfzq.dset.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import android.util.AttributeSet;

import com.dfzq.dset.R;

import static android.inputmethodservice.Keyboard.KEYCODE_DELETE;
import static android.inputmethodservice.Keyboard.KEYCODE_DONE;
import static android.inputmethodservice.Keyboard.KEYCODE_MODE_CHANGE;

/**
 * 股票输入键盘
 * Created by zhanglei on 2017/5/10.
 */

public class StockNumKeyboardView extends SecretKeyboardView {
    private static final String KEY_LABEL_600 = "600";
    private static final String KEY_LABEL_601 = "601";
    private static final String KEY_LABEL_002 = "002";
    private static final String KEY_LABEL_300 = "300";
    private static final String KEY_LABEL_00 = "00";
    private static final String KEY_LABEL_IF = "IF";
    private static final String KEY_LABEL_688 = "688";
    public static final String KEY_LABEL_DOTE = ".";

    public StockNumKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StockNumKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void draw(Keyboard.Key key, Canvas canvas) {
        int bgRes = 0;
        String label = "";//getString(key.codes);
        if (null != key.label) {
            label = key.label.toString();
        }
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
        } else if (KEY_LABEL_600.equals(label)
                || KEY_LABEL_601.equals(label)
                || KEY_LABEL_002.equals(label)
                || KEY_LABEL_300.equals(label)
                || KEY_LABEL_00.equals(label)
                || KEY_LABEL_IF.equals(label)
                || KEY_LABEL_688.equals(label)
                || key.codes[0] == -16
                || KEY_LABEL_DOTE.equals(label)) {
            bgRes = R.drawable.dset_keyboard_special_btn_bg;
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
}

package com.dfzq.dset.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import java.util.List;

import static android.inputmethodservice.Keyboard.KEYCODE_MODE_CHANGE;

/**
 * Created by wangtao on 16/8/29.
 */
public abstract class SecretKeyboardView extends KeyboardView {
    public static final int KEYBOARD_NUM = 0x01;
    public static final int KEYBOARD_ID = 0x02;
    public static final int KEYBOARD_NUM_ONLY = 0x03;
    public static final int KEYBOARD_TYPICAL = 0x04;
    public static final int KEYBOARD_STOCK_NUM = 0x05;
    public static final int KEYBOARD_STOCK_LETTER = 0x06;

    protected boolean isHideEnable = true;

    public SecretKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SecretKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected int dip2px(int dip) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getKeyboard() != null) {
            List<Keyboard.Key> keys = getKeyboard().getKeys();
            for (Keyboard.Key key : keys) {
                draw(key, canvas);
            }
        }
    }

    protected abstract void draw(Keyboard.Key key, Canvas canvas);

    protected void drawKeyBackground(int drawableId, Canvas canvas, Keyboard.Key key) {
        Drawable npd = ContextCompat.getDrawable(getContext(), drawableId);
        int[] drawableState = key.getCurrentDrawableState();
        if (key.codes[0] != 0) {
            npd.setState(drawableState);
        }
        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                + key.height);
        npd.draw(canvas);
    }

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
            paint.setTextSize(dip2px(22));
        }
        paint.setAntiAlias(true);

        if (key.label != null) {
            paint.getTextBounds(key.label.toString(), 0, key.label.toString()
                    .length(), bounds);
            canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                    (key.y + key.height / 2) + bounds.height() / 2, paint);
        }
    }

    public void setHideEnable(boolean hideEnable) {
        isHideEnable = hideEnable;
    }
}

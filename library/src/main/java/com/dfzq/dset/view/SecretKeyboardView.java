package com.dfzq.dset.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.dfzq.dset.KeyboardManager;
import com.dfzq.dset.R;

import java.util.List;

import static android.inputmethodservice.Keyboard.KEYCODE_DONE;

/**
 * Created by wangtao on 16/8/29.
 */
public class SecretKeyboardView extends KeyboardView {
    public static final int KEYBOARD_NUM = 0x01;
    public static final int KEYBOARD_ID = 0x02;
    public static final int KEYBOARD_NUM_ONLY = 0x03;
    public static final int KEYBOARD_TYPICAL = 0x04;
    public static final int KEYBOARD_STOCK_NUM = 0x05;
    public static final int KEYBOARD_STOCK_LETTER = 0x06;

    protected boolean isHideEnable = true;

    private Paint paint = new Paint();

    public SecretKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SecretKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setHideEnable(boolean hideEnable) {
        isHideEnable = hideEnable;
    }

    protected int dip2px(int dip) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    private void init() {
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.parseColor("#333333"));
        paint.setAntiAlias(true);
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

    protected void draw(Keyboard.Key key, Canvas canvas) {
        int code = key.codes[0];
        if (!isHideEnable && code == KEYCODE_DONE) {
            key.icon = null;
        }
        // 特殊键位添加背景，同时重绘键位文字或icon
        if (code < 0 || code == 46) {
            drawKeyBackground(R.drawable.dset_btn_keyboard_special_bg, canvas, key);
            drawKeyText(canvas, key);
            drawKeyIcon(canvas, key);
        }
        // logo
        if (code == 32) {
            drawKeyBackground(KeyboardManager.logo, canvas, key);
        }
    }

    /**
     * 键位背景
     */
    protected void drawKeyBackground(int drawableId, Canvas canvas, Keyboard.Key key) {
        Drawable npd = ContextCompat.getDrawable(getContext(), drawableId);
        if (npd == null) return;
        int[] drawableState = key.getCurrentDrawableState();
        if (key.codes[0] != 0) {
            npd.setState(drawableState);
        }
        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                + key.height);
        npd.draw(canvas);
    }

    /**
     * 键位文字
     */
    protected void drawKeyText(Canvas canvas, Keyboard.Key key) {
        if (key.label == null) return;
        Rect bounds = new Rect();
        if (key.codes[0] == -16) {
            paint.setTextSize(dip2px(18));
        } else {
            paint.setTextSize(dip2px(20));
        }
        paint.getTextBounds(key.label.toString(), 0, key.label.toString()
                .length(), bounds);
        canvas.drawText(key.label.toString(), key.x + (float) (key.width / 2),
                (float) (key.y + key.height / 2) + (float) bounds.height() / 2, paint);
    }

    /**
     * 键位icon
     */
    protected void drawKeyIcon(Canvas canvas, Keyboard.Key key) {
        if (key.icon == null) return;
        Rect bound = new Rect(key.x + (key.width - key.icon.getIntrinsicWidth()) / 2,
                key.y + (key.height - key.icon.getIntrinsicHeight()) / 2,
                key.x + (key.width - key.icon.getIntrinsicWidth()) / 2 + key.icon.getIntrinsicWidth(),
                key.y + (key.height - key.icon.getIntrinsicHeight()) / 2 + key.icon.getIntrinsicHeight());
        key.icon.setBounds(bound);
        key.icon.draw(canvas);
    }


    /**
     * 键盘大小写切换
     */
    public void changeShift() {
        boolean upperCased = getKeyboard().isShifted();
        getKeyboard().setShifted(!upperCased);
        Keyboard keyboard = getKeyboard();
        changeKey(upperCased, getKeyboard().getKeys());
        Keyboard.Key shiftedKey = keyboard.getKeys().get(keyboard.getShiftKeyIndex());
        if (shiftedKey != null) {
            if (isShifted()) {
                shiftedKey.icon = ContextCompat.getDrawable(getContext(), R.drawable.dset_ic_keyboard_capslock_up_32dp);
            } else {
                shiftedKey.icon = ContextCompat.getDrawable(getContext(), R.drawable.dset_ic_keyboard_capslock_lower_32dp);
            }
        }
        invalidateAllKeys();
    }

    /**
     * 键位code值的大小写切换
     */
    private void changeKey(boolean isUpper, List<android.inputmethodservice.Keyboard.Key> keyList) {
        if (isUpper) {// 大写切小写
            for (android.inputmethodservice.Keyboard.Key key : keyList) {
                if (key.label != null && isWord(key.label.toString())) {
                    key.codes[0] = key.codes[0] + 32;
                }
            }
        } else {// 小写切大写
            for (android.inputmethodservice.Keyboard.Key key : keyList) {
                if (key.label != null && isWord(key.label.toString())) {
                    key.codes[0] = key.codes[0] - 32;
                }
            }
        }
    }

    /**
     * 判断是否为字母
     */
    private boolean isWord(String str) {
        String wordStr = "abcdefghijklmnopqrstuvwxyz";
        return wordStr.contains(str.toLowerCase());
    }
}

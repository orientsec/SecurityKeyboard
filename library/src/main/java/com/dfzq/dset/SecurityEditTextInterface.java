package com.dfzq.dset;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by zhanglei on 2017/5/17.
 */

interface SecurityEditTextInterface {
    void init(Context context, AttributeSet attrs);

    void init(Context context);

    int getKeyboardFrameId();

    void setKeyboardFrameId(int keyboardFrameId);

    boolean getVoice();

    void setVoice(boolean voice);

    int getType();

    void setType(int type);

    void hideSystemSoftKeyboard();

    boolean hideSoftKeyboard();

    boolean performClick();

    void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect);

    void onAttachedToWindow();

    void onDetachedFromWindow();

    void onWindowFocusChanged(boolean hasWindowFocus);

    boolean onKeyPreIme(int keyCode, KeyEvent event);

    /**
     * 语音输入状态发生变化
     *
     * @param state 语音输入状态
     *              0. idle
     *              1. 输入
     */
    void onVoiceStateChanged(int state);

    /**
     * 语音识别错误
     *
     * @param error 语音识别错误码
     */
    void onVoiceErrorState(int error);

    /**
     * 是否支持隐藏，FrameId不为0时不支持
     *
     * @return true enable
     */
    boolean isHideEnable();
}

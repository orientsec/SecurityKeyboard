package com.dfzq.dset;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

/**
 * Created by zhanglei on 2017/5/17.
 */

public class SecurityAutoCompleteEditText extends AppCompatAutoCompleteTextView implements SecurityEditTextInterface {
    private SecurityEditTextDelegate delegate;
    private VoiceInputStateChangeListener voiceInputStateChangeListener;

    public SecurityAutoCompleteEditText(Context context) {
        super(context);
    }

    public SecurityAutoCompleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SecurityAutoCompleteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setVoiceInputStateChangeListener(VoiceInputStateChangeListener voiceInputStateChangeListener) {
        this.voiceInputStateChangeListener = voiceInputStateChangeListener;
    }

    public int getType() {
        return delegate.getType();
    }

    public void setType(int type) {
        delegate.setType(type);
    }

    @Override
    public boolean getVoice() {
        return delegate.getVoice();
    }

    @Override
    public void setVoice(boolean voice) {
        delegate.setVoice(voice);
    }

    public int getKeyboardFrameId() {
        return delegate.getKeyboardFrameId();
    }

    public void setKeyboardFrameId(int keyboardFrameId) {
        delegate.setKeyboardFrameId(keyboardFrameId);
    }

    public void init(Context context, AttributeSet attrs) {
        delegate = new SecurityEditTextDelegate(this);
        delegate.init(context, attrs);
    }

    @Override
    public void init(Context context) {
        delegate = new SecurityEditTextDelegate(this);
        delegate.init(context);
    }

    @Override
    public boolean performClick() {
        delegate.performClick();
        return super.performClick();
    }

    @Override
    public void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        delegate.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        delegate.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        delegate.onDetachedFromWindow();
    }


    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        delegate.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (delegate.onKeyPreIme(keyCode, event)) {
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    public void onVoiceStateChanged(int state) {
        if (voiceInputStateChangeListener != null) {
            voiceInputStateChangeListener.onStateChanged(state);
        }
    }

    @Override
    public void onVoiceErrorState(int error) {
        if (voiceInputStateChangeListener != null) {
            voiceInputStateChangeListener.onErrorState(error);
        }
    }

    /**
     * 隐藏系统键盘
     */
    public void hideSystemSoftKeyboard() {
        delegate.hideSystemSoftKeyboard();
    }

    @Override
    public void showSystemSoftKeyboard() {
        delegate.showSystemSoftKeyboard();
    }

    @Override
    public boolean hideSoftKeyboard() {
        return delegate.hideSoftKeyboard();
    }

    @Override
    public boolean showSoftKeyboard() {
        return delegate.showSoftKeyboard();
    }

    @Override
    public boolean isHideEnable() {
        return delegate.isHideEnable();
    }

    @Override
    public boolean isHide() {
        return delegate.isHide();
    }

    @Override
    public void switchSoftKeyboardWithSystem() {
        delegate.switchSoftKeyboardWithSystem();
    }
}

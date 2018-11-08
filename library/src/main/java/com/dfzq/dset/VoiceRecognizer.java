package com.dfzq.dset;

import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.dfzq.dset.view.VoiceLayout;


import com.dfzq.dset.provider.*;

/**
 * Created by djy on 2018/1/4.
 */

public class VoiceRecognizer implements View.OnTouchListener, RecognizerListener {

    private EditText editText;
    private VoiceLayout lineView;
    private ImageView voiceImg;
    private String preText = "";
    private boolean stopFlag;
    private Recognizer recognizer;

    public VoiceRecognizer(Context context, View voiceLayout, VoiceLayout lineView) {
        this.lineView = lineView;
        if (voiceLayout != null) {
            voiceImg = (ImageView) voiceLayout.findViewById(R.id.iv_voice);
            voiceLayout.setOnTouchListener(this);
        }
        recognizer = getRecognizer();
        recognizer.setRecognizerListener(this);
    }

    private Recognizer getRecognizer() {
        Provider<Recognizer> provider = KeyboardManager.getInstance().getProvider();
        if (provider == null) {
            return new DefaultRecognizer();
        }
        return provider.get();
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public void cancel() {
        recognizer.cancel();
    }

    private void startVoice() {
        if (editText != null) {
            preText = editText.getText().toString();
        }
        recognizer.startVoice();
    }

    private void stopVoice() {
        recognizer.stopVoice();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startVoice();
                if (voiceImg != null) {
                    voiceImg.setBackgroundResource(R.drawable.dset_keyboard_voice_speech_on);
                }
                if (lineView != null) {
                    lineView.startWaveAnimation();
                }
                if (editText != null) {
                    ((SecurityEditTextInterface) editText).onVoiceStateChanged(1);
                }
                stopFlag = true;
                KeyboardManager.getInstance().showVoiceLineView(editText, true);
                break;
            case MotionEvent.ACTION_UP:
                if (voiceImg != null) {
                    voiceImg.setBackgroundResource(R.drawable.dset_keyboard_voice_speech_off);
                }
                if (lineView != null) {
                    lineView.stopWaveAnimation();
                }
                if (editText != null) {
                    ((SecurityEditTextInterface) editText).onVoiceStateChanged(0);
                }
                stopFlag = false;
                KeyboardManager.getInstance().showVoiceLineView(editText, false);
                stopVoice();
                break;
        }
        return true;
    }

    @Override
    public void onSuccess(String result) {
        result = preText + result;
        if (!TextUtils.isEmpty(result) && editText != null) {
            editText.setText(result);
            editText.setSelection(editText.getText().length());
        }
    }

    @Override
    public void onFailure(String errorMsg, int errorCode) {
        ((SecurityEditTextInterface) editText).onVoiceErrorState(errorCode);
    }

    @Override
    public void onVolumeChange(int volume) {
        if (lineView != null) {
            lineView.setLevel(volume + 14);
        }
    }

    @Override
    public void onExit() {
        if (stopFlag) {
            startVoice();
        }
    }

    @Override
    public void onNoPermission() {
        stopFlag = false;
        if (voiceImg != null) {
            voiceImg.setBackgroundResource(R.drawable.dset_keyboard_voice_speech_on);
        }
        if (lineView != null) {
            lineView.stopWaveAnimation();
        }
        KeyboardManager.getInstance().showVoiceLineView(editText, true);
    }
}

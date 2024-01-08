package com.dfzq.dset;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.dfzq.dset.provider.DefaultRecognizer;
import com.dfzq.dset.provider.Recognizer;
import com.dfzq.dset.provider.RecognizerListener;

import javax.inject.Provider;

/**
 * Created by djy on 2018/1/4.
 */

public class VoiceRecognizer implements View.OnTouchListener, RecognizerListener {

    private EditText editText;
    private ImageView voiceImg;
    private String preText = "";
    private boolean stopFlag;
    private final Recognizer recognizer;
    private TextView tvTitle;
    private final TextView changeTv;
    private LinearLayout voiceBg;

    public VoiceRecognizer(View voiceLayout, TextView changeTv) {
        if (voiceLayout != null) {
            voiceImg = voiceLayout.findViewById(R.id.iv_voice);
            tvTitle = voiceLayout.findViewById(R.id.tv_title);
            voiceBg = voiceLayout.findViewById(R.id.btn_voice);
            voiceLayout.setOnTouchListener(this);
        }
        this.changeTv = changeTv;
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
        stopSpeech();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startSpeech();
                break;
            case MotionEvent.ACTION_UP:
                stopSpeech();
                break;
            case MotionEvent.ACTION_MOVE:
                if (motionEvent.getY() < -ViewUtils.dpToPx(view.getContext(), 100)) {
                    stopSpeech();
                }
                break;
        }
        return true;
    }

    private void startSpeech() {
        if (ContextCompat.checkSelfPermission(editText.getContext(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            recognizer.requestPermission();
            return;
        }
        if (voiceImg != null) {
            voiceImg.setBackgroundResource(R.drawable.dset_keyboard_voice_speech_on_white);
        }
        if (tvTitle != null) {
            tvTitle.setText(R.string.dset_voice_release_tips);
            tvTitle.setTextColor(ContextCompat.getColor(tvTitle.getContext(), R.color.dset_white));
        }
        if (voiceBg != null) {
            voiceBg.setBackgroundResource(R.drawable.corner_gray_bg);
        }
        if (editText != null) {
            ((SecurityEditTextInterface) editText).onVoiceStateChanged(1);
        }
        if (changeTv != null) {
            changeTv.setVisibility(View.GONE);
        }
        stopFlag = true;
        KeyboardManager.getInstance().showVoiceLineView(editText, true);
        startVoice();
    }

    private void stopSpeech() {
        if (tvTitle != null) {
            tvTitle.setText(R.string.dset_voice_tips);
            tvTitle.setTextColor(ContextCompat.getColor(tvTitle.getContext(), R.color.dset_text_black));
        }
        if (voiceImg != null) {
            voiceImg.setBackgroundResource(R.drawable.dset_keyboard_voice_speech_off_gray);
        }
        if (editText != null) {
            ((SecurityEditTextInterface) editText).onVoiceStateChanged(0);
        }
        if (voiceBg != null) {
            voiceBg.setBackgroundResource(R.drawable.corner_white_bg);
        }
        if (changeTv != null) {
            changeTv.setVisibility(View.VISIBLE);
        }
        stopFlag = false;
        KeyboardManager.getInstance().showVoiceLineView(editText, false);
        stopVoice();
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
            voiceImg.setBackgroundResource(R.drawable.dset_keyboard_voice_speech_on_white);
        }
        KeyboardManager.getInstance().showVoiceLineView(editText, true);
    }
}

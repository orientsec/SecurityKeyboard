package com.dfzq.dset.demo;

import android.content.Context;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.dfzq.dset.provider.Recognizer;
import com.dfzq.dset.provider.RecognizerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaiduRecognizer implements Recognizer {
    private PermissionListener permissionListener;
    private static final class BaiduListener implements EventListener {
        private WeakReference<RecognizerListener> listenerWeakReference;
        private static final BaiduListener instance = new BaiduListener();

        void setRecognizerListener(RecognizerListener listener) {
            listenerWeakReference = new WeakReference<>(listener);
        }

        @Override
        public void onEvent(String name, String params, byte[] data, int offset, int length) {
            RecognizerListener listener = null;
            if (listenerWeakReference != null) {
                listener = listenerWeakReference.get();
            }
            if (listener == null) return;
            if (params != null) {
                try {
                    JSONObject obj = new JSONObject(params);
                    JSONArray result = obj.optJSONArray("results_recognition");
                    int error = obj.optInt("sub_error");
                    if (result != null && result.length() > 0) {
                        listener.onSuccess(result.get(0).toString());
                    }
                    if (3001 == error) {
                        listener.onNoPermission();
                    }
                    if (0 != error) {
                        listener.onFailure("", error);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_VOLUME)) {
                    //音量变化
                    try {
                        JSONObject json = new JSONObject(params);
                        int volumePercent = json.getInt("volume-percent");
                        listener.onVolumeChange(volumePercent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_EXIT)) {
                listener.onExit();
            }

        }
    }

    private final EventManager asr;

    public BaiduRecognizer(Context context, PermissionListener permissionListener) {
        asr = EventManagerFactory.create(context, "asr");
        asr.registerListener(BaiduListener.instance);
        this.permissionListener = permissionListener;
        loadOfflineEngine();
    }

    private void loadOfflineEngine() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
    }

    @Override
    public void requestPermission() {
        permissionListener.requestPermission();
    }

    @Override
    public void cancel() {
        if (asr != null) {
            asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
            unloadOfflineEngine();
        }
    }

    private void unloadOfflineEngine() {
        asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0);
    }

    @Override
    public void startVoice() {
        Map<String, Object> params = new LinkedHashMap<>();
        String event;
        event = SpeechConstant.ASR_START;
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, true);
        String json = new JSONObject(params).toString();
        asr.send(event, json, null, 0, 0);
    }

    @Override
    public void stopVoice() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
    }

    @Override
    public void setRecognizerListener(RecognizerListener listener) {
        BaiduListener.instance.setRecognizerListener(listener);
    }

    interface PermissionListener {
        void requestPermission();
    }
}

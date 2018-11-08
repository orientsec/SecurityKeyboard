package com.dfzq.dset.provider;

public interface Recognizer {
    /**
     * 取消语音识别服务
     */
    void cancel();

    /**
     * 开启语音识别服务
     */
    void startVoice();

    /**
     * 停止语音识别服务
     */
    void stopVoice();

    /**
     * 设置语音识别监听
     * @param listener listener
     */
    void setRecognizerListener(RecognizerListener listener);
}

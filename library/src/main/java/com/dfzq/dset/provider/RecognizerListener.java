package com.dfzq.dset.provider;

public interface RecognizerListener {
    /**
     * 语音识别成功
     *
     * @param result
     */
    void onSuccess(String result);

    /**
     * 语音识别失败
     *
     * @param errorMsg
     * @param errorCode
     */
    void onFailure(String errorMsg, int errorCode);

    /**
     * 语音音量变化
     *
     * @param volume
     */
    void onVolumeChange(int volume);

    /**
     * 语音识别结束
     */
    void onExit();

    /**
     * 无语音权限
     */
    void onNoPermission();
}

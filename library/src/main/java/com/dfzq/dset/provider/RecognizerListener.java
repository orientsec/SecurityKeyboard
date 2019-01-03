package com.dfzq.dset.provider;

public interface RecognizerListener {
    /**
     * 语音识别成功
     *
     * @param result 失败结果
     */
    void onSuccess(String result);

    /**
     * 语音识别失败
     *
     * @param errorMsg  错误
     * @param errorCode 错误码
     */
    void onFailure(String errorMsg, int errorCode);

    /**
     * 语音音量变化
     *
     * @param volume 音量
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

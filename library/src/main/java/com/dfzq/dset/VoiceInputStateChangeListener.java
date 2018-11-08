package com.dfzq.dset;

/**
 * Created by djy on 2018/1/4.
 */

public interface VoiceInputStateChangeListener {
    /**
     * 语音输入状态发生变化
     *
     * @param state 语音输入状态
     *              0. idle
     *              1. 输入
     */
    void onStateChanged(int state);

    /**
     * 语音识别错误
     *
     * @param error 语音识别错误码
     */
    void onErrorState(int error);

}

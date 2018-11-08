package com.dfzq.dset.provider;

public class DefaultRecognizer implements Recognizer {

    private RecognizerListener listener = new RecognizerListener() {
        @Override
        public void onSuccess(String result) {

        }

        @Override
        public void onFailure(String errorMsg, int errorCode) {

        }

        @Override
        public void onVolumeChange(int volume) {

        }

        @Override
        public void onExit() {

        }

        @Override
        public void onNoPermission() {

        }
    };


    @Override
    public void cancel() {

    }

    @Override
    public void startVoice() {

    }

    @Override
    public void stopVoice() {

    }

    @Override
    public void setRecognizerListener(RecognizerListener listener) {
        this.listener = listener;
    }
}

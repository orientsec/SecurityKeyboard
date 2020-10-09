package com.dfzq.dset;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.dfzq.dset.view.SecretKeyboardView;

import java.util.Arrays;
import java.util.List;

import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_ID;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_NUM;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_NUM_ONLY;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_STOCK_LETTER;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_STOCK_NUM;

/**
 * Created by meihu on 2017/3/17.
 */

class DsetKeyboard {
    private SecretKeyboardView keyboardView;
    private View voiceLayout;
    private ViewGroup frameLayout;
    private Dialog dialog;
    private EditText editText;
    private Context context;
    private VoiceRecognizer recognizer;
    private PopupWindow popupWindow;
    private LottieAnimationView animationView;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case 1:
                        dialog.show();
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    EditText getFocus() {
        return editText;
    }

    DsetKeyboard(Context context) {
        this.context = context;
        dialog = new KeyboardDialog(context, R.style.dsetKeyboardDialog);
        voiceLayout = dialog.findViewById(R.id.voice_layout);
        View btnVoice = dialog.findViewById(R.id.btn_voice);
        dialog.findViewById(R.id.rl_switch_system_keyboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText != null && editText instanceof SecurityEditTextInterface) {
                    ((SecurityEditTextInterface) editText).switchSoftKeyboardWithSystem();
                }
            }
        });
        frameLayout = dialog.findViewById(R.id.keyboard_view);
        recognizer = new VoiceRecognizer(btnVoice);
    }

    DsetKeyboard(ViewGroup parent) {
        this.context = parent.getContext();
        LayoutInflater.from(context).inflate(R.layout.dset_keyboard_layout, parent);
        voiceLayout = parent.findViewById(R.id.voice_layout);
        View btnVoice = parent.findViewById(R.id.btn_voice);
        parent.findViewById(R.id.rl_switch_system_keyboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText != null && editText instanceof SecurityEditTextInterface) {
                    ((SecurityEditTextInterface) editText).switchSoftKeyboardWithSystem();
                }
            }
        });
        frameLayout = parent.findViewById(R.id.keyboard_view);
        recognizer = new VoiceRecognizer(btnVoice);
    }

    void hideKeyboardImme() {
        this.editText = null;
        if (recognizer != null) {
            recognizer.cancel();
        }
        if (dialog != null) {
            handler.removeMessages(1);
            handler.removeMessages(2);
            dialog.dismiss();
        }
    }

    boolean hideKeyboard() {
        this.editText = null;
        if (recognizer != null) {
            recognizer.cancel();
        }
        if (dialog != null) {
            if (dialog.isShowing()) {
                handler.removeMessages(1);
                handler.sendEmptyMessageDelayed(2, 30);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    void showVoiceView(boolean flag) {
        if (flag) {
            showAnimation();
        } else {
            hideAnimation();
        }
    }

    private void showAnimation() {
        hideAnimation();
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(context).inflate(R.layout.dset_keyboard_animattion, new FrameLayout(context), false);
        popupWindow.setContentView(view);
        int navHeight = ViewUtils.getNavigationBarHeightIfRoom(editText.getContext());
        int height = keyboardView.getHeight() + voiceLayout.getHeight() + ViewUtils.dpToPx(context, 12) + navHeight;
        popupWindow.showAtLocation(editText, Gravity.BOTTOM, 0, height);

        animationView = view.findViewById(R.id.animation_view);
        if (!TextUtils.isEmpty(KeyboardManager.assetsFolder) && !TextUtils.isEmpty(KeyboardManager.animationName)) {
            animationView.setImageAssetsFolder(KeyboardManager.assetsFolder);
            animationView.setAnimation(KeyboardManager.animationName);
            animationView.playAnimation();
        }
    }

    private void hideAnimation() {
        if (animationView != null) {
            animationView.cancelAnimation();
        }
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    void showKeyboard(EditText editText, int type, boolean voice) {
        this.editText = editText;
        if (recognizer != null) {
            recognizer.setEditText(editText);
        }
        if (voice) {
            voiceLayout.setVisibility(View.VISIBLE);
        } else {
            voiceLayout.setVisibility(View.GONE);
        }
        setKeyboardView(type);
        if (dialog != null) {
            handler.removeMessages(2);
            handler.sendEmptyMessageDelayed(1, 30);
        }
    }

    private void setKeyboardView(int type) {
        EditText editText = getFocus();
        if (!(editText instanceof SecurityEditTextInterface)) {
            return;
        }

        if (keyboardView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            keyboardView = (SecretKeyboardView) inflater.inflate(R.layout.dset_keyboard, frameLayout, false);
            keyboardView.setOnKeyboardActionListener(new KeyboardListener(keyboardView, this));
            frameLayout.addView(keyboardView);
            keyboardView.setPreviewEnabled(false);
        }
        keyboardView.setHideEnable(((SecurityEditTextInterface) editText).isHideEnable());

        android.inputmethodservice.Keyboard keyboard;
        switch (type) {
            case KEYBOARD_ID:
                keyboard = new android.inputmethodservice.Keyboard(context, R.xml.dset_keyboard_id);
                break;
            case KEYBOARD_NUM:
                keyboard = new android.inputmethodservice.Keyboard(context, R.xml.dset_keyboard_num);
                if (KeyboardManager.needRandom) {
                    randomNumKey(keyboard.getKeys());
                }
                break;
            case KEYBOARD_NUM_ONLY:
                keyboard = new android.inputmethodservice.Keyboard(context, R.xml.dset_keyboard_num_only);
                if (KeyboardManager.needRandom) {
                    randomNumOnlyKey(keyboard.getKeys());
                }
                break;
            case KEYBOARD_STOCK_NUM:
                keyboard = new android.inputmethodservice.Keyboard(context, R.xml.dset_keyboard_stock_number);
                break;
            case KEYBOARD_STOCK_LETTER:
                keyboard = new android.inputmethodservice.Keyboard(context, R.xml.dset_keyboard_stock_letter);
                break;
            default:
                keyboard = new android.inputmethodservice.Keyboard(context, R.xml.dset_keyboard_abc);
                if (KeyboardManager.needRandom) {
                    randomTypicalKey(keyboard.getKeys());
                }
                break;
        }
        keyboardView.setKeyboard(keyboard);
    }

    /**
     * 随机数字键盘
     */
    private void randomNumKey(List<android.inputmethodservice.Keyboard.Key> keyList) {
        int size = keyList.size();

        for (int i = 0; i < size; i++) {

            int random_a = (int) (Math.random() * (size));
            int random_b = (int) (Math.random() * (size));

            if ((NUM_INDEX.contains(random_a))
                    && (NUM_INDEX.contains(random_b))) {
                //得到随机数a的 字符编码
                int code = keyList.get(random_a).codes[0];
                //得到随机数a的 字符
                CharSequence label = keyList.get(random_a).label;

                keyList.get(random_a).codes[0] = keyList.get(random_b).codes[0];
                keyList.get(random_a).label = keyList.get(random_b).label;

                keyList.get(random_b).codes[0] = code;
                keyList.get(random_b).label = label;
            }
        }
    }

    private static final List<Integer> NUM_INDEX = Arrays.asList(0, 1, 2, 4, 5, 6, 8, 9, 10, 11);

    /**
     * 随机数字键盘
     */
    private void randomNumOnlyKey(List<android.inputmethodservice.Keyboard.Key> keyList) {
        int size = keyList.size();

        for (int i = 0; i < size; i++) {

            int random_a = (int) (Math.random() * (size));
            int random_b = (int) (Math.random() * (size));

            if ((NUM_INDEX.contains(random_a))
                    && (NUM_INDEX.contains(random_b))) {
                int code = keyList.get(random_a).codes[0];
                CharSequence label = keyList.get(random_a).label;

                keyList.get(random_a).codes[0] = keyList.get(random_b).codes[0];
                keyList.get(random_a).label = keyList.get(random_b).label;

                keyList.get(random_b).codes[0] = code;
                keyList.get(random_b).label = label;
            }
        }
    }

    private static final List<Integer> NONE_TYPICAL_INDEX = Arrays.asList(19, 27, 28, 29, 30);

    /**
     * 随机全键盘
     */
    private void randomTypicalKey(List<android.inputmethodservice.Keyboard.Key> keyList) {
        int size = keyList.size();

        for (int i = 0; i < size; i++) {

            int random_a = (int) (Math.random() * (size - 10)) + 10;
            int random_b = (int) (Math.random() * (size - 10)) + 10;

            if ((!NONE_TYPICAL_INDEX.contains(random_a)) && (!NONE_TYPICAL_INDEX.contains(random_b))) {
                int code = keyList.get(random_a).codes[0];
                CharSequence label = keyList.get(random_a).label;

                keyList.get(random_a).codes[0] = keyList.get(random_b).codes[0];
                keyList.get(random_a).label = keyList.get(random_b).label;

                keyList.get(random_b).codes[0] = code;
                keyList.get(random_b).label = label;
            }
        }
    }
}

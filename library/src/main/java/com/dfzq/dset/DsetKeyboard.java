package com.dfzq.dset;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dfzq.dset.view.IDKeyboardView;
import com.dfzq.dset.view.LetterKeyboardView;
import com.dfzq.dset.view.NumKeyboardView;
import com.dfzq.dset.view.NumOnlyKeyboardView;
import com.dfzq.dset.view.SecretKeyboardView;
import com.dfzq.dset.view.StockLetterKeyboardView;
import com.dfzq.dset.view.StockNumKeyboardView;
import com.dfzq.dset.view.VoiceLayout;

import java.util.Arrays;
import java.util.List;

import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_ID;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_NUM;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_NUM_ONLY;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_STOCK_LETTER;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_STOCK_NUM;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_TYPICAL;

/**
 * Created by meihu on 2017/3/17.
 */

class DsetKeyboard {
    // 是否随机
    static boolean needRandom = true;
    private IDKeyboardView idKeyboardView;
    private LetterKeyboardView letterKeyboardView;
    private NumOnlyKeyboardView numOnlyKeyboardView;
    private NumKeyboardView numKeyboardView;
    private StockNumKeyboardView stockNumKeyboardView;
    private StockLetterKeyboardView stockLetterKeyboardView;
    private SecretKeyboardView keyboardView;
    private View voiceLayout;
    private VoiceLayout voiceView;
    private ViewGroup frameLayout;
    private Dialog dialog;
    private EditText editText;
    private Context context;
    private VoiceRecognizer recognizer;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
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
        voiceView = dialog.findViewById(R.id.voice_view);
        frameLayout = dialog.findViewById(R.id.keyboard_view);
        recognizer = new VoiceRecognizer(voiceLayout, voiceView);
    }

    DsetKeyboard(ViewGroup parent) {
        this.context = parent.getContext();
        LayoutInflater.from(context).inflate(R.layout.dset_keyboard_layout, parent);
        voiceLayout = parent.findViewById(R.id.voice_layout);
        voiceView = parent.findViewById(R.id.voice_view);
        frameLayout = parent.findViewById(R.id.keyboard_view);
        recognizer = new VoiceRecognizer(voiceLayout, voiceView);
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
            keyboardView.setVisibility(View.INVISIBLE);
            voiceView.setVisibility(View.VISIBLE);
        } else {
            keyboardView.setVisibility(View.VISIBLE);
            voiceView.setVisibility(View.INVISIBLE);
        }
    }

    void showKeyboard(EditText editText, int type, boolean voice) {
        this.editText = editText;
        if (recognizer != null) {
            recognizer.setEditText(editText);
        }
        voiceView.setVisibility(View.INVISIBLE);
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
        switch (type) {
            case KEYBOARD_NUM:
                if (numKeyboardView != null && keyboardView == numKeyboardView) {
                    return;
                } else {
                    keyboardView = numKeyboardView;
                }
                break;
            case KEYBOARD_ID:
                if (idKeyboardView != null && keyboardView == idKeyboardView) {
                    return;
                } else {
                    keyboardView = idKeyboardView;
                }
                break;
            case KEYBOARD_NUM_ONLY:
                if (numOnlyKeyboardView != null && keyboardView == numOnlyKeyboardView) {
                    return;
                } else {
                    keyboardView = numOnlyKeyboardView;
                }
                break;
            case KEYBOARD_STOCK_NUM:
                if (stockNumKeyboardView != null && keyboardView == stockNumKeyboardView) {
                    return;
                } else {
                    keyboardView = stockNumKeyboardView;
                }
                break;
            case KEYBOARD_STOCK_LETTER:
                keyboardView = stockLetterKeyboardView;
                break;
            case KEYBOARD_TYPICAL:
                keyboardView = letterKeyboardView;
                break;
        }
        frameLayout.removeAllViews();
//        if (frameLayout.getChildCount() > 1) {
//            frameLayout.removeAllViews();
//        }
        LayoutInflater inflater = LayoutInflater.from(context);
        if (keyboardView == null) {
            android.inputmethodservice.Keyboard keyboard;
            switch (type) {
                case KEYBOARD_ID:
                    idKeyboardView = (IDKeyboardView) inflater.inflate(R.layout.dset_keyboard_id, frameLayout, false);
                    keyboardView = idKeyboardView;
                    keyboard = new android.inputmethodservice.Keyboard(context, R.xml.dset_keyboard_id);
                    break;
                case KEYBOARD_NUM:
                    numKeyboardView = (NumKeyboardView) inflater.inflate(R.layout.dset_keyboard_num, frameLayout, false);
                    keyboardView = numKeyboardView;
                    keyboard = new android.inputmethodservice.Keyboard(context, R.xml.dset_keyboard_num);
                    if (needRandom) {
                        randomNumKey(keyboard.getKeys());
                    }
                    break;
                case KEYBOARD_NUM_ONLY:
                    numOnlyKeyboardView = (NumOnlyKeyboardView) inflater.inflate(R.layout.dset_keyboard_num_only, frameLayout, false);
                    keyboardView = numOnlyKeyboardView;
                    keyboard = new android.inputmethodservice.Keyboard(context, R.xml.dset_keyboard_num_only);
                    if (needRandom) {
                        randomNumOnlyKey(keyboard.getKeys());
                    }
                    break;
                case KEYBOARD_STOCK_NUM:
                    stockNumKeyboardView = (StockNumKeyboardView) inflater.inflate(R.layout.dset_keyboard_stock_num, frameLayout, false);
                    keyboardView = stockNumKeyboardView;
                    keyboard = new android.inputmethodservice.Keyboard(context, R.xml.dset_keyboard_stock_number);
                    break;
                case KEYBOARD_STOCK_LETTER:
                    stockLetterKeyboardView = (StockLetterKeyboardView) inflater.inflate(R.layout.dset_keyboard_stock_letter, frameLayout, false);
                    keyboardView = stockLetterKeyboardView;
                    keyboard = new android.inputmethodservice.Keyboard(context, R.xml.dset_keyboard_stock_letter);
                    break;
                case KEYBOARD_TYPICAL:
                default:
                    letterKeyboardView = (LetterKeyboardView) inflater.inflate(R.layout.dset_keyboard_letter, frameLayout, false);
                    keyboardView = letterKeyboardView;
                    keyboard = new android.inputmethodservice.Keyboard(context, R.xml.dset_keyboard_abc);
                    if (needRandom) {
                        randomTypicalKey(keyboard.getKeys());
                    }
                    break;
            }
            EditText editText = getFocus();
            if (editText instanceof SecurityEditTextInterface) {
                keyboardView.setHideEnable(((SecurityEditTextInterface) editText).isHideEnable());
            }
            keyboardView.setKeyboard(keyboard);
            keyboardView.setOnKeyboardActionListener(new KeyboardListener(keyboardView, this));
        }
        frameLayout.addView(keyboardView);
        keyboardView.setPreviewEnabled(false);
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

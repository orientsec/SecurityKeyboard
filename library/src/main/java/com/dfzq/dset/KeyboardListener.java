package com.dfzq.dset;

import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.widget.EditText;

import com.dfzq.dset.view.SecretKeyboardView;

import static android.inputmethodservice.Keyboard.KEYCODE_ALT;
import static android.inputmethodservice.Keyboard.KEYCODE_DELETE;
import static android.inputmethodservice.Keyboard.KEYCODE_DONE;
import static android.inputmethodservice.Keyboard.KEYCODE_MODE_CHANGE;
import static android.inputmethodservice.Keyboard.KEYCODE_SHIFT;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_NUM;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_NUM_ONLY;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_STOCK_LETTER;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_STOCK_NUM;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_TYPICAL;


/**
 * ProductName:DsetKeyboard
 * PackageName:com.xiaomao.keyboard_id.keyboard_id
 * Dage:2016/10/14
 * Author:Fredric
 * Coding is an art not science
 */

class KeyboardListener implements KeyboardView.OnKeyboardActionListener {
    public static final int SPACE = 32;         // 空格键
    private static final int KEYCODE_600 = -10;
    private static final int KEYCODE_601 = -11;
    private static final int KEYCODE_002 = -12;
    private static final int KEYCODE_300 = -13;
    private static final int KEYCODE_000 = -15;
    private static final int KEYCODE_SWITCH_SYSTEM = -16;
    private static final int KEYCODE_688 = -17;

    private SecretKeyboardView keyboardView;
    private DsetKeyboard dsetKeyboard;

    public void setKeyboardView(SecretKeyboardView keyboardView) {
        this.keyboardView = keyboardView;
    }

    public KeyboardListener(SecretKeyboardView keyboardView, DsetKeyboard dsetKeyboard) {
        this.keyboardView = keyboardView;
        this.dsetKeyboard = dsetKeyboard;
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        KeyboardManager keyboardManager = KeyboardManager.getInstance();
        EditText editText = dsetKeyboard.getFocus();
        if (!(editText instanceof SecurityEditTextInterface)) {
            return;
        }
        SecurityEditTextInterface securityEditTextInterface = (SecurityEditTextInterface) editText;
        Editable editable = editText.getText();
        int start = editText.getSelectionStart();
        int type = securityEditTextInterface.getType();

        switch (primaryCode) {
            case SPACE:
                break;
            case KEYCODE_SWITCH_SYSTEM:
                ((SecurityEditTextInterface) editText).switchSoftKeyboardWithSystem();
                break;
            case KEYCODE_600:
            case KEYCODE_601:
            case KEYCODE_300:
            case KEYCODE_000:
            case KEYCODE_002:
            case KEYCODE_688:// 解决多code 快速点击错误问题
                if (editable != null) {
                    editable.insert(start, getKeyLabel(primaryCode));
                }
                break;
            case KEYCODE_DELETE: {
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            }
            break;
            case KEYCODE_DONE:
            case KEYCODE_ALT: {
                if (((SecurityEditTextInterface) editText).isHideEnable()) {
                    ((SecurityEditTextInterface) editText).hideSoftKeyboard();
                }
            }
            break;
            case KEYCODE_SHIFT: {
                keyboardView.changeShift();
            }
            break;

            case KEYCODE_MODE_CHANGE: {
                if (type == KEYBOARD_STOCK_LETTER) {
                    securityEditTextInterface.setType(KEYBOARD_STOCK_NUM);
                } else if (type == KEYBOARD_STOCK_NUM) {
                    securityEditTextInterface.setType(KEYBOARD_STOCK_LETTER);
                } else if (type == KEYBOARD_NUM) {
                    securityEditTextInterface.setType(KEYBOARD_TYPICAL);
                } else {
                    securityEditTextInterface.setType(KEYBOARD_NUM);
                }
                KeyboardManager.getInstance().showSoftInput(editText);
            }
            break;
            default:
                if (editable != null) {
                    StringBuilder str = new StringBuilder();
                    boolean confuse = keyboardManager.isConfuse() && confusable(type);
                    for (int pChar : keyCodes) {
                        if (pChar == -1) {
                            break;
                        }
                        if (confuse) {
                            str.append((char) keyboardManager.confuse(pChar));
                        } else {
                            str.append((char) pChar);
                        }
                    }
                    editable.insert(start, str.toString());
                }
                break;
        }
    }

    private boolean confusable(int type) {
        return type == KEYBOARD_NUM ||
                type == KEYBOARD_NUM_ONLY ||
                type == KEYBOARD_TYPICAL;
    }

    private CharSequence getKeyLabel(int keyCode) {
        switch (keyCode) {
            case KEYCODE_600:
                return "600";
            case KEYCODE_601:
                return "601";
            case KEYCODE_300:
                return "300";
            case KEYCODE_000:
                return "000";
            case KEYCODE_002:
                return "002";
            case KEYCODE_688:
                return "688";
            default:
                return "";
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}

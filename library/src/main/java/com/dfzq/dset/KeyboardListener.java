package com.dfzq.dset;

import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.widget.EditText;

import com.dfzq.dset.view.LetterKeyboardView;
import com.dfzq.dset.view.NumKeyboardView;
import com.dfzq.dset.view.NumOnlyKeyboardView;
import com.dfzq.dset.view.SecretKeyboardView;
import com.dfzq.dset.view.StockLetterKeyboardView;
import com.dfzq.dset.view.StockNumKeyboardView;

import java.util.List;

import static android.inputmethodservice.Keyboard.KEYCODE_ALT;
import static android.inputmethodservice.Keyboard.KEYCODE_DELETE;
import static android.inputmethodservice.Keyboard.KEYCODE_DONE;
import static android.inputmethodservice.Keyboard.KEYCODE_MODE_CHANGE;
import static android.inputmethodservice.Keyboard.KEYCODE_SHIFT;
import static com.dfzq.dset.view.SecretKeyboardView.KEYBOARD_NUM;
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
    public static final int BLANK = -8;         // 空格键
    private static final int KEYCODE_600 = -10;
    private static final int KEYCODE_601 = -11;
    private static final int KEYCODE_002 = -12;
    private static final int KEYCODE_300 = -13;
    private static final int KEYCODE_00 = -14;
    private static final int KEYCODE_IF = -15;
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

        switch (primaryCode) {
            case SPACE:
            case BLANK:
                break;
            case KEYCODE_SWITCH_SYSTEM:
                ((SecurityEditTextInterface) editText).switchSoftKeyboardWithSystem();
                break;
            case KEYCODE_600:
            case KEYCODE_601:
            case KEYCODE_300:
            case KEYCODE_00:
            case KEYCODE_IF:
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
                if (keyboardView instanceof LetterKeyboardView) {
                    LetterKeyboardView letterKeyboardView = (LetterKeyboardView) keyboardView;
                    boolean upperCase = letterKeyboardView.isUpperCase();
                    changeKey(upperCase, letterKeyboardView.getKeyboard().getKeys());
                    letterKeyboardView.setUpperCase(!upperCase);
                    if (keyboardView instanceof StockLetterKeyboardView) {
                        securityEditTextInterface.setType(KEYBOARD_STOCK_LETTER);
                    } else {
                        securityEditTextInterface.setType(KEYBOARD_TYPICAL);
                    }
                    KeyboardManager.getInstance().showSoftInput(editText);
                }
            }
            break;

            case KEYCODE_MODE_CHANGE: {
                if (keyboardView instanceof StockLetterKeyboardView) {
                    securityEditTextInterface.setType(KEYBOARD_STOCK_NUM);
                } else if (keyboardView instanceof StockNumKeyboardView) {
                    securityEditTextInterface.setType(KEYBOARD_STOCK_LETTER);
                } else if (keyboardView instanceof LetterKeyboardView) {
                    securityEditTextInterface.setType(KEYBOARD_NUM);
                } else if (keyboardView instanceof NumKeyboardView) {
                    securityEditTextInterface.setType(KEYBOARD_TYPICAL);
                }
                KeyboardManager.getInstance().showSoftInput(editText);
            }
            break;
            default:
                if (editable != null) {
                    StringBuilder str = new StringBuilder();
                    boolean confuse = keyboardManager.isConfuse() && confusable();
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

    private boolean confusable() {
        return keyboardView instanceof NumKeyboardView || keyboardView instanceof NumOnlyKeyboardView || keyboardView instanceof LetterKeyboardView;
    }

    private CharSequence getKeyLabel(int keyCode) {
        switch (keyCode) {
            case KEYCODE_600:
                return "600";
            case KEYCODE_601:
                return "601";
            case KEYCODE_300:
                return "300";
            case KEYCODE_00:
                return "00";
            case KEYCODE_IF:
                return "IF";
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

    /**
     * 键盘大小写切换
     */
    private void changeKey(boolean isUpper, List<android.inputmethodservice.Keyboard.Key> keyList) {
        if (isUpper) {// 大写切小写
            for (android.inputmethodservice.Keyboard.Key key : keyList) {
                if (key.label != null && isWord(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            }
        } else {// 小写切大写
            for (android.inputmethodservice.Keyboard.Key key : keyList) {
                if (key.label != null && isWord(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                }
            }
        }
    }

    private boolean isWord(String str) {
        String wordStr = "abcdefghijklmnopqrstuvwxyz";
        return wordStr.contains(str.toLowerCase());
    }
}

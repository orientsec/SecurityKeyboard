package com.dfzq.dset;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Looper;
import android.util.SparseIntArray;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.dfzq.dset.provider.Recognizer;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;

/**
 * ProductName:DsetKeyboard
 * PackageName:com.xiaomao.keyboard_id.keyboard_id
 * Dage:2016/10/14
 * Author:Fredric
 * Coding is an art not science
 */

public class KeyboardManager {
    // 是否随机
    public static boolean needRandom = false;
    public static int logo = R.drawable.dset_keyboard_special_btn_bg;
    public static String assetsFolder = "";
    public static String animationName = "";
    private WeakReference<DsetKeyboard> keyboardWeakReference;
    private boolean confuse;
    private SparseIntArray keys = new SparseIntArray();
    private final Map<Activity, DsetKeyboard> dialogKeyboardMap = new HashMap<>();
    private Provider<Recognizer> provider;

    private static final class KeyboardManagerHolder {
        static final KeyboardManager keyboardManager = new KeyboardManager();
    }

    public static KeyboardManager getInstance() {
        return KeyboardManagerHolder.keyboardManager;
    }

    public Provider<Recognizer> getProvider() {
        return provider;
    }

    public void setProvider(Provider<Recognizer> provider) {
        this.provider = provider;
    }

    public void clearKeys() {
        keys = new SparseIntArray();
    }

    public void setKeys(SparseIntArray keys) {
        this.keys = keys;
    }

    public boolean isConfuse() {
        return confuse;
    }

    public void setConfuse(boolean confuse) {
        this.confuse = confuse;
    }

    /**
     * Validate EditText state
     *
     * @param editText 输入框
     * @return 输入框是否获取焦点
     */
    private boolean validEditText(EditText editText) {
        if (!(editText instanceof SecurityEditTextInterface)) {
            return false;
        }
        return editText.isAttachedToWindow();
    }

    public void showVoiceLineView(EditText editText, boolean flag) {
        if (editText != null) {
            DsetKeyboard keyboard = getKeyboard(editText, false);
            if (keyboard == null) {
                return;
            }
            keyboard.showVoiceView(flag);
        }
    }

    public boolean showSoftInput(EditText editText) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            return false;
        }
        if (!validEditText(editText)) {
            return false;
        }
        SecurityEditTextInterface securityEditTextInterface = (SecurityEditTextInterface) editText;
        DsetKeyboard keyboard = getKeyboard(editText);
        if (keyboard == null) {
            return false;
        }
        keyboard.showKeyboard(editText, securityEditTextInterface.getType(), securityEditTextInterface.getVoice());
        keyboardWeakReference = new WeakReference<>(keyboard);
        return true;

    }

    public void hideSoftInput() {
        DsetKeyboard keyboard = currentKeyboard();
        if (keyboard != null) {
            keyboard.hideKeyboard();
        }
    }

    boolean hideSoftInput(EditText editText) {
        DsetKeyboard keyboard = getKeyboard(editText, false);
        if (keyboard != null && keyboard.getFocus() == editText) {
            return keyboard.hideKeyboard();
        }
        return false;
    }

    private DsetKeyboard currentKeyboard() {
        if (keyboardWeakReference != null) {
            return keyboardWeakReference.get();
        } else {
            return null;
        }
    }

    int confuse(int code) {
        return keys.get(code, code);
    }

    private DsetKeyboard getKeyboard(EditText editText) {
        return getKeyboard(editText, true);
    }

    private DsetKeyboard getKeyboard(EditText editText, boolean autoBuild) {
        int frameId = ((SecurityEditTextInterface) editText).getKeyboardFrameId();
        DsetKeyboard keyboard = null;
        AppCompatActivity activity = getActivity(editText.getContext());
        if (frameId > 0) {
            ViewGroup parent = editText.getRootView().findViewById(frameId);
            Object tag = parent.getTag(R.id.dset_keyboard_id);
            if (tag instanceof DsetKeyboard) {
                keyboard = (DsetKeyboard) tag;
            } else if (tag == null && autoBuild) {
                keyboard = new DsetKeyboard(parent);
                parent.setTag(R.id.dset_keyboard_id, keyboard);
            }
            if (activity != null) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
            }
        } else {
            if (activity != null) {
                keyboard = dialogKeyboardMap.get(activity);
                if (dialogKeyboardMap.get(activity) == null && autoBuild) {
                    keyboard = new DsetKeyboard(activity);
                    dialogKeyboardMap.put(activity, keyboard);

                    activity.getLifecycle().addObserver(new DefaultLifecycleObserver() {
                        @Override
                        public void onDestroy(@NonNull LifecycleOwner owner) {
                            DsetKeyboard keyboard = dialogKeyboardMap.remove(activity);
                            if (keyboard != null) {
                                keyboard.hideKeyboardImme();
                            }
                        }
                    });
                }
            }
        }
        return keyboard;
    }

    private AppCompatActivity getActivity(Context context) {
        Context baseContext = context;
        while (baseContext instanceof ContextWrapper) {
            if (baseContext instanceof AppCompatActivity) {
                return (AppCompatActivity) baseContext;
            }
            baseContext = ((ContextWrapper) baseContext).getBaseContext();
        }
        return null;
    }

}

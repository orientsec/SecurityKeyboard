package com.dfzq.dset;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Looper;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dfzq.dset.provider.Recognizer;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * ProductName:DsetKeyboard
 * PackageName:com.xiaomao.keyboard_id.keyboard_id
 * Dage:2016/10/14
 * Author:Fredric
 * Coding is an art not science
 */

public class KeyboardManager {
    private static KeyboardManager keyboardManager;
    public static int logo = R.drawable.dset_keyboard_special_btn_bg;
    private WeakReference<DsetKeyboard> keyboardWeakReference;
    private boolean confuse;
    private SparseIntArray keys = new SparseIntArray();
    private Map<View, DsetKeyboard> viewKeyboardMap = new WeakHashMap<>();
    private Map<Context, DsetKeyboard> dialogKeyboardMap = new WeakHashMap<>();
    private Provider<Recognizer> provider;
    public static KeyboardManager getInstance() {
        if (keyboardManager == null) {
            synchronized (KeyboardManager.class) {
                if (keyboardManager == null) {
                    keyboardManager = new KeyboardManager();
                }
            }
        }
        return keyboardManager;
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
     * @param editText
     * @return
     */
    private boolean validEditText(EditText editText) {
        if (!(editText instanceof SecurityEditTextInterface)) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (editText.isAttachedToWindow()) {
                return true;
            } else {
                return false;
            }
        }
        if (!editText.isFocused()) {
            return false;
        }
        return true;
    }

    public void showVoiceLineView(EditText editText, boolean flag) {
        if (editText != null) {
            DsetKeyboard keyboard = getKeyboard(editText);
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
        DsetKeyboard keyboard;
        if (frameId > 0) {
            ViewGroup parent = editText.getRootView().findViewById(frameId);
            keyboard = viewKeyboardMap.get(parent);
            if (keyboard == null && autoBuild) {
                keyboard = new DsetKeyboard(parent);
                viewKeyboardMap.put(parent, keyboard);
            }
        } else {
            Activity activity = getActivity(editText.getContext());
            if (activity == null) {
                return null;
            }
            keyboard = dialogKeyboardMap.get(activity);
            if (dialogKeyboardMap.get(activity) == null && autoBuild) {
                keyboard = new DsetKeyboard(activity);
                dialogKeyboardMap.put(activity, keyboard);
                activity.getFragmentManager()
                        .beginTransaction()
                        .add(new MonitorFragment(), "DsetMonitorFragment")
                        .commit();
            }
        }
        return keyboard;
    }

    private Activity getActivity(Context context) {
        Context baseContext = context;
        while (baseContext instanceof ContextWrapper) {
            if (baseContext instanceof Activity) {
                return (Activity) baseContext;
            }
            baseContext = ((ContextWrapper) baseContext).getBaseContext();
        }
        return null;
    }

    public static final class MonitorFragment extends Fragment {
        @Override
        public void onDestroy() {
            super.onDestroy();
            DsetKeyboard keyboard = KeyboardManager.getInstance().dialogKeyboardMap.get(getActivity());
            if (keyboard != null) {
                keyboard.hideKeyboardImme();
            }
        }
    }
}

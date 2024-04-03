package com.dfzq.dset;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by xiaomao on 2016/10/16.
 */

class KeyboardDialog extends Dialog implements View.OnClickListener {


    public KeyboardDialog(Context context) {
        super(context);
        initialize();
    }

    public KeyboardDialog(Context context, int themeResId) {
        super(context, themeResId);
        initialize();
    }

    protected KeyboardDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initialize();
    }

    private void initialize() {
        Window window = getWindow();
        if (window == null) return;
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dsetDialogWindowAnim); //设置窗口弹出动画
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.flags = FLAG_NOT_FOCUSABLE;// | FLAG_ALT_FOCUSABLE_IM | FLAG_WATCH_OUTSIDE_TOUCH;// | FLAG_NOT_TOUCH_MODAL;
        //window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dset_keyboard_layout);
        findViewById(R.id.finish).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}

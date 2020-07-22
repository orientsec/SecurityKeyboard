package com.dfzq.dset.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dfzq.dset.R;

/**
 * Created by djy on 2018/1/8.
 */

public class VoiceLayout extends RelativeLayout {

    private static final int MSG_WAVE2_ANIMATION = 1;
    private static final int MSG_WAVE3_ANIMATION = 2;
    private static final int SIZE = 3;
    private int show_spacing_time = 800;
    private AnimationSet[] mAnimationSet = new AnimationSet[SIZE];
    private ImageView image_bg;
    private ImageView[] images = new ImageView[SIZE];

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WAVE2_ANIMATION:
                    images[MSG_WAVE2_ANIMATION].startAnimation(mAnimationSet[MSG_WAVE2_ANIMATION]);
                    break;
                case MSG_WAVE3_ANIMATION:
                    images[MSG_WAVE2_ANIMATION].startAnimation(mAnimationSet[MSG_WAVE3_ANIMATION]);
                    break;
            }
        }
    };

    public VoiceLayout(Context context) {
        super(context);
        initView(context);
    }

    public VoiceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public VoiceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setLayout(context);
        for (int i = 0; i < images.length; i++) {
            mAnimationSet[i] = initAnimationSet();
        }
    }

    private void setLayout(Context context) {
        float imageViewWidth = 100;
        float imageViewHeight = 100;
        LayoutParams params = new LayoutParams(dip2px(context, imageViewWidth), dip2px(context, imageViewHeight));
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        for (int i = 0; i < SIZE; i++) {
            images[i] = new ImageView(context);
            images[i].setImageResource(R.drawable.dset_keyboard_voice_circle);
            addView(images[i], params);
        }
        LayoutParams params_bg = new LayoutParams(dip2px(context, imageViewWidth) - 70, dip2px(context, imageViewHeight) - 70);
        //添加一个规则
        params_bg.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        image_bg = new ImageView(context);
        image_bg.setImageResource(R.drawable.dset_keyboard_voice_animation);
        addView(image_bg, params_bg);
    }

    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private AnimationSet initAnimationSet() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1f, 2f, 1f, 2f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(show_spacing_time * 3);
        sa.setRepeatCount(Animation.INFINITE);// 设置循环
        AlphaAnimation aa = new AlphaAnimation(1, 0.1f);
        aa.setDuration(show_spacing_time * 3);
        aa.setRepeatCount(Animation.INFINITE);//设置循环
        as.addAnimation(sa);
        as.addAnimation(aa);
        return as;
    }

    public void startWaveAnimation() {
        images[0].startAnimation(mAnimationSet[0]);
        mHandler.sendEmptyMessageDelayed(MSG_WAVE2_ANIMATION, show_spacing_time);
        mHandler.sendEmptyMessageDelayed(MSG_WAVE3_ANIMATION, show_spacing_time * 2);
    }

    public void stopWaveAnimation() {
        for (ImageView image : images) {
            image.clearAnimation();
        }
    }

    public void setLevel(int level) {
        Drawable drawable = image_bg.getDrawable();
        drawable.setLevel(3000 + 6000 * level / 100);
    }

}

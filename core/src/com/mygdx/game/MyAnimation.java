package com.mygdx.game;

public class MyAnimation {

    private OnTick onTick;
    private OnAnimationEndListener onAnimationEndListener;
    private float time;
    private float maxTime;
    private boolean enabled;


    public MyAnimation() {
        this(0f);
    }

    public MyAnimation(float maxTime) {
        this.maxTime = maxTime;
        time = 0.f;
        enabled = false;
    }

    public void setMaxTime(float maxTime) {
        if (!isEnabled())
            this.maxTime = maxTime;
    }

    public void setOnAnimationEndListener(OnAnimationEndListener onAnimationEndListener) {
        this.onAnimationEndListener = onAnimationEndListener;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setOnTick(OnTick onTick) {
        this.onTick = onTick;
    }

    public void start() {
        time = 0.f;
        enabled = true;
    }

    public void interrupt() {
        enabled = false;
        time = 0.f;
    }

    public void onAnimatorTick(float delta) {
        if (enabled) {
            time += delta;
            if (time >= maxTime) {
                enabled = false;
                time = maxTime;
                if (onAnimationEndListener != null)
                    onAnimationEndListener.onAnimationEnd();
            }
            if (onTick != null)
                onTick.onTick(delta);
        }
    }


    public interface OnTick {
        void onTick(float delta);
    }

    public interface OnAnimationEndListener {
        void onAnimationEnd();
    }
}

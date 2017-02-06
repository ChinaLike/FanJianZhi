package com.touchrom.fanjianzhi.help;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import com.arialyy.frame.util.show.L;

/**
 * Created by lyy on 2016/6/5.
 * 文章behavior
 */
@TargetApi(14)
public class ArtBehavior extends CoordinatorLayout.Behavior<View> {
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private int sinceDirectionChange;
    private              int    mDuration = 1000;
    private static final String TAG       = "ArtBehavior";
    private              int    h         = 0;
    ObjectAnimator mShowAnimator, mHintAnimator;

    public ArtBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && sinceDirectionChange < 0 || dy < 0 && sinceDirectionChange > 0) {
            child.animate().cancel();
            sinceDirectionChange = 0;
        }
        sinceDirectionChange += dy;
        if (sinceDirectionChange > child.getHeight() && child.getVisibility() == View.VISIBLE) {
            hide(child);
        } else if (sinceDirectionChange <= 0) {
            show(child);
        }
    }


    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    private void hide(final View view) {
        if (mHintAnimator == null || !mHintAnimator.isRunning()) {
            mHintAnimator = ObjectAnimator.ofFloat(view, "translationY", 0, -view.getHeight());
            mHintAnimator.setInterpolator(INTERPOLATOR);
            mHintAnimator.setDuration(mDuration);
            mHintAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    show(view);
                }
            });
            mHintAnimator.start();
        }
    }

    private void show(final View view) {
        if (mShowAnimator == null || !mShowAnimator.isRunning()) {
            int[] xy = new int[2];
            view.getLocationInWindow(xy);
            L.d(TAG, "y 坐标 ==> " + xy[1]);
            mShowAnimator = ObjectAnimator.ofFloat(view, "translationY", -view.getHeight(), 0);
            L.d(TAG, "HASH ==> " + mShowAnimator.hashCode());
            mShowAnimator.setDuration(mDuration);
            mShowAnimator.setInterpolator(INTERPOLATOR);
            mShowAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
//                    hide(view);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.VISIBLE);
                }
            });
            mShowAnimator.start();
        }else {
            mShowAnimator.cancel();
        }
    }
}

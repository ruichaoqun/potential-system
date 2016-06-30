package widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Created by Administrator on 2016/6/30.
 */
public class ReduceBehavior extends FloatingActionButton.Behavior {
    private boolean mIsAnimationOut = false;
    public static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    public ReduceBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                ||super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.i("AAA",dyConsumed+"");
        if(dyConsumed >0 && !mIsAnimationOut && child.getVisibility() == View.VISIBLE){
            animateReduce(child);
        }else if(dyConsumed <0 && mIsAnimationOut && child.getVisibility() == View.GONE){
            animateEnlarge(child);
        }
    }

    private void animateEnlarge(FloatingActionButton view) {
        ViewCompat.animate(view)
                .withLayer()
                .setInterpolator(INTERPOLATOR)
                .scaleX(1)
                .scaleY(1)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        mIsAnimationOut = false;
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        mIsAnimationOut = true;
                    }
                })
                .start();
    }

    private void animateReduce(View view) {
        ViewCompat.animate(view)
                .withLayer()
                .setInterpolator(INTERPOLATOR)
                .scaleX(0)
                .scaleY(0)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        mIsAnimationOut = true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        mIsAnimationOut = false;
                    }
                })
                .start();
    }
}

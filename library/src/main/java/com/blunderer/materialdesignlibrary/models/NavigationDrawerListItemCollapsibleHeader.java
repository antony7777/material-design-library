package com.blunderer.materialdesignlibrary.models;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.blunderer.materialdesignlibrary.adapters.NavigationDrawerTopAdapter;

/**
 * Animation credit to: Tom Esterez http://stackoverflow.com/a/13381228/189554
 */
public class NavigationDrawerListItemCollapsibleHeader extends NavigationDrawerListItemHeader implements View.OnLayoutChangeListener {
    private int visibility;
    private boolean animating;

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public boolean isAnimating() {
        return animating;
    }

    public void setAnimating(boolean animating) {
        this.animating = animating;
    }

    public void collapse(final AdapterView<?> adapterView, final View v, int position) {
        setVisibility(View.GONE);
        setAnimating(true);
        try {
            // get our adapter first
            NavigationDrawerTopAdapter ndta = null;
            if (adapterView.getAdapter() instanceof NavigationDrawerTopAdapter) {
                ndta = (NavigationDrawerTopAdapter) adapterView.getAdapter();
            } else {
                ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();
                return;
            }

            // get our child number
            int childCount = ndta.countSectionChildNo(position);

            // get our own view
            final int first = adapterView.getFirstVisiblePosition();
            final int last = adapterView.getLastVisiblePosition();

            View myView = null;
            int childIndex = 0;
            if (position >= first && position < last) {
                childIndex = position - first;
                myView = adapterView.getChildAt(childIndex);
            }

            if (myView == null) {
                ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();
                return;
            }

            // count the number of visible child
            childCount = Math.min(childCount, last - position);
            if (childCount <= 0) {
                ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();
                return;
            }

            while (childCount > 0) {
                final View childv = adapterView.getChildAt(childIndex+childCount);
                final int initialHeight = childv.getMeasuredHeight();

                Animation a = new Animation() {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        childv.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                        childv.requestLayout();
                    }
                };

                a.setDuration((int) (initialHeight / childv.getContext().getResources().getDisplayMetrics().density * childCount ));
                if (childCount == 1) {
                    a.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                }
                childv.startAnimation(a);
                childCount--;
            }
        } finally {
            setAnimating(false);
        }

    }

    public void expand(AdapterView<?> adapterView, View view, int i) {
        setAnimating(true);
        adapterView.addOnLayoutChangeListener(this);
        // animation will be provided by adapter
        ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        v.removeOnLayoutChangeListener(this);
        setAnimating(false);
    }
}

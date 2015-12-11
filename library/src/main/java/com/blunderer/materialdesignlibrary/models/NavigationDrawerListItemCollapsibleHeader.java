package com.blunderer.materialdesignlibrary.models;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.blunderer.materialdesignlibrary.adapters.NavigationDrawerTopAdapter;
import com.blunderer.materialdesignlibrary.interfaces.ListView;
import com.blunderer.materialdesignlibrary.interfaces.NavigationDrawer;

/**
 * Created by antony on 10/12/15.
 */
public class NavigationDrawerListItemCollapsibleHeader extends NavigationDrawerListItemHeader {
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

        // put visible child that will be animated into an array
        childCount = Math.min(childCount, last-position);
        final View[] views = new View[childCount];
        while (childCount > 0) {
            views[childCount-1] = adapterView.getChildAt(childIndex+childCount);
            childCount--;
        }

        final int initialHeight = v.getMeasuredHeight();
        final Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                for (View v:views) {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density * 2));

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(a);

    }

    public void expand(AdapterView<?> adapterView, View view, int i) {

    }
}

package com.blunderer.materialdesignlibrary.handlers;

import android.content.Context;

import com.blunderer.materialdesignlibrary.activities.NavigationDrawerActivity;
import com.blunderer.materialdesignlibrary.interfaces.TabLayoutController;

/**
 * Created by antony on 15/12/15.
 */
public abstract class TabLayoutHandler implements TabLayoutController {
    private NavigationDrawerActivity mContext;

    public void setup(NavigationDrawerActivity context) {
        mContext = context;
    }

    public void cleanup() {
        mContext = null;
    }

    public NavigationDrawerActivity getContext() {
        return mContext;
    }
}

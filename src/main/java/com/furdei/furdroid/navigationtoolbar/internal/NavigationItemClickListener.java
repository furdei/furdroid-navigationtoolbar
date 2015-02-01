package com.furdei.furdroid.navigationtoolbar.internal;

import android.view.MenuItem;
import android.view.View;

/**
 * Translates button clicks into Navigation Bar events
 *
 * @author Stepan Furdey
 */
public class NavigationItemClickListener implements View.OnClickListener {

    public interface NavigationClickListener {
        void onClick(MenuItem menuItem);
    }

    private final NavigationClickListener navigationClickListener;

    public NavigationItemClickListener(NavigationClickListener navigationClickListener) {
        this.navigationClickListener = navigationClickListener;
    }

    @Override
    public void onClick(View v) {
        MenuItem menuItem = (MenuItem) v.getTag();
        navigationClickListener.onClick(menuItem);
    }
}

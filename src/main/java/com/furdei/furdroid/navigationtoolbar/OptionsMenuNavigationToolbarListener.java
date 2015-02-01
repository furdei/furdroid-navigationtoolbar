package com.furdei.furdroid.navigationtoolbar;

import android.app.Activity;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * <p>
 * Out of the box implementation for {@link NavigationToolbar.NavigationToolbarListener} which
 * calls {@link android.app.Activity#onOptionsItemSelected(android.view.MenuItem)} method and
 * allows developer to react to navigation events in the same manner he would process ActionBar
 * events.
 * </p><p>
 * Also this implementation allows you to receive clicks on Home/Up button when user clicks an
 * Overflow button. This is default behaviour, but you can change it by calling
 * {@link #setClickHomeUpForOverflowButton(boolean)} method.
 * </p>
 *
 * @author Stepan Furdey
 */
public class OptionsMenuNavigationToolbarListener implements NavigationToolbar.NavigationToolbarListener {

    private WeakReference<Activity> activity;
    private boolean clickHomeUpForOverflowButton;

    public OptionsMenuNavigationToolbarListener(Activity activity) {
        setActivity(activity);
        setClickHomeUpForOverflowButton(true);
    }

    public Activity getActivity() {
        return activity.get();
    }

    public void setActivity(Activity activity) {
        this.activity = new WeakReference<Activity>(activity);
    }

    public boolean isClickHomeUpForOverflowButton() {
        return clickHomeUpForOverflowButton;
    }

    public void setClickHomeUpForOverflowButton(boolean clickHomeUpForOverflowButton) {
        this.clickHomeUpForOverflowButton = clickHomeUpForOverflowButton;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNavigationClicked(MenuItem menuItem) {
        Activity activity = this.getActivity();

        if (activity != null) {
            activity.onOptionsItemSelected(menuItem);

            // try to click Home/Up button if user clicked Overflow navigation button
            if (clickHomeUpForOverflowButton && menuItem.getItemId() == R.id.overflow) {
                View home;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    home = activity.getWindow().getDecorView().findViewById(android.R.id.home);
                } else {
                    home = activity.getWindow().getDecorView().findViewById(R.id.up);
                }

                if (home != null) {
                    home.performClick();
                }
            }
        }
    }

}

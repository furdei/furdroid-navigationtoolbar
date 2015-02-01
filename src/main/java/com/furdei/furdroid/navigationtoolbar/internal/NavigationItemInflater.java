package com.furdei.furdroid.navigationtoolbar.internal;

import dreamers.graphics.RippleDrawable;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.furdei.furdroid.components.graphics.drawable.TintedDrawable;
import com.furdei.furdroid.navigationtoolbar.R;

/**
 * Inflates Navigation Menu items from <code>menu</code> resource
 *
 * @author Stepan Furdey
 */
public class NavigationItemInflater {

    private final LayoutInflater layoutInflater;
    private final ViewGroup root;
    private final int resId;
    private final NavigationItemClickListener clickListener;
    private final ColorStateList iconColor;
    private final ColorStateList backgroundColor;

    public NavigationItemInflater(LayoutInflater layoutInflater, ViewGroup root, int resId,
                                  NavigationItemClickListener.NavigationClickListener
                                          navigationListener,
                                  ColorStateList iconColor, ColorStateList backgroundColor) {
        this.layoutInflater = layoutInflater;
        this.root = root;
        this.resId = resId;
        this.iconColor = iconColor;
        this.backgroundColor = backgroundColor;
        this.clickListener = new NavigationItemClickListener(navigationListener);
    }

    public View inflateView(MenuItem menuItem) {
        View view = layoutInflater.inflate(resId, root, false);
        root.addView(view);
        view.setVisibility(menuItem.isVisible() ? View.VISIBLE : View.GONE);
        view.setEnabled(menuItem.isEnabled());
        view.setId(menuItem.getItemId());
        view.setTag(menuItem);

        view.setFocusable(true);
        view.setClickable(true);
        view.setOnClickListener(clickListener);

        RippleDrawable.makeFor(view, backgroundColor);

        if (menuItem.getIcon() != null) {
            ImageView icon = (ImageView) view.findViewById(R.id.navigation_item_icon);

            if (icon != null) {
                icon.setImageDrawable(new TintedDrawable(menuItem.getIcon(), iconColor));
            }
        }

        return view;
    }
}

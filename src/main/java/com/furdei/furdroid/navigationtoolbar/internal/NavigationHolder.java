package com.furdei.furdroid.navigationtoolbar.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.furdei.furdroid.components.graphics.drawable.TintedDrawable;
import com.furdei.furdroid.navigationtoolbar.R;

import dreamers.graphics.RippleDrawable;

/**
 * Vertical bar to hold navigation items
 *
 * @author Stepan Furdey
 */
public class NavigationHolder extends FrameLayout {

    private View overflowView;
    private MenuItem menuItemOverflow;
    private ColorStateList iconColor;
    private ColorStateList backgroundColor;

    private static final int OVERFLOW_VIEW_INDEX = 0;
    private static final int FIRST_ACTION_VIEW_INDEX = 1;

    public NavigationHolder(Context context) {
        super(context);
        init();
    }

    public NavigationHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationHolder(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        final int count = getChildCount();

        if (count <= 0) {
            return;
        }

        // These are the far left and right edges in which we are performing layout.
        int leftPos = getPaddingLeft();
        int rightPos = right - left - getPaddingRight();

        // These are the top and bottom edges in which we are performing layout.
        int parentTop = getPaddingTop();
        int parentBottom = bottom - top - getPaddingBottom();
        int freeSpace = parentBottom - parentTop;
        boolean parentHasFreeSpace = true;

        for (int i = FIRST_ACTION_VIEW_INDEX; i < count; i++) {
            final View child = getChildAt(i);
            final MenuItem menuItem = (MenuItem) child.getTag();

            if (menuItem != null && menuItem.isVisible() && parentHasFreeSpace) {
                final int height = child.getMeasuredHeight();
                int childBottom = parentTop + height;
                freeSpace -= height;

                if (freeSpace >= 0) {
                    child.setVisibility(VISIBLE);
                    child.layout(leftPos, parentTop, rightPos, childBottom);
                    parentTop += height;
                } else {
                    parentHasFreeSpace = false;
                    measureChild(overflowView,
                            MeasureSpec.makeMeasureSpec(right - left, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(bottom - top, MeasureSpec.EXACTLY));
                    int overflowViewHeight = overflowView.getMeasuredHeight();

                    // hide all children starting from current and backwards until we have enough
                    // space to fit an overflow view into the bar.
                    for (int j = i;
                         j >= FIRST_ACTION_VIEW_INDEX && freeSpace < overflowViewHeight;
                         j--) {
                        final View viewToHide = getChildAt(j);
                        final MenuItem menuItemToHide = (MenuItem) viewToHide.getTag();

                        if (menuItemToHide.isVisible()) {
                            int hideHeight = viewToHide.getMeasuredHeight();
                            freeSpace += hideHeight;
                            viewToHide.setVisibility(GONE);
                        }
                    }

                    // lay out an overflow view in the bottom of the bar
                    overflowView.setVisibility(VISIBLE);
                    overflowView.layout(leftPos, parentBottom - overflowViewHeight,
                            rightPos, parentBottom);
                }
            } else {
                child.setVisibility(GONE);
            }
        }

        if (parentHasFreeSpace) {
            overflowView.setVisibility(GONE);
        }
    }

    public void removeMenuViews() {
        removeViews(FIRST_ACTION_VIEW_INDEX, getChildCount() - FIRST_ACTION_VIEW_INDEX);
    }

    public ColorStateList getIconColor() {
        return iconColor;
    }

    public void setIconColor(ColorStateList iconColor) {
        this.iconColor = iconColor;

        ImageView icon = (ImageView) overflowView.findViewById(R.id.navigation_item_icon);

        if (icon != null) {
            icon.setImageDrawable(new TintedDrawable(icon.getDrawable(), iconColor));
        }
    }

    public ColorStateList getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(ColorStateList backgroundColor) {
        this.backgroundColor = backgroundColor;
        RippleDrawable.makeFor(overflowView, backgroundColor);
    }

    private void init() {
        addView(createOverflowView());
        PopupMenu popupMenu = new PopupMenu(getContext(), null);
        Menu menu = popupMenu.getMenu();
        MenuInflater menuInflater = new MenuInflater(getContext());
        menuInflater.inflate(R.menu.overflow, menu);
        menuItemOverflow = menu.findItem(R.id.overflow);
    }

    private View createOverflowView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.navigation_item, this, false);
        view.setFocusable(true);
        view.setClickable(true);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) getContext();
                activity.onOptionsItemSelected(menuItemOverflow);

                View home;

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    home = activity.getWindow().getDecorView().findViewById(android.R.id.home);
                } else {
                    home = activity.getWindow().getDecorView().findViewById(R.id.up);
                }

                if (home != null) {
                    home.performClick();
                }
            }
        });

        ImageView icon = (ImageView) view.findViewById(R.id.navigation_item_icon);

        if (icon != null) {
            icon.setImageResource(R.drawable.ic_navigation_more);
        }

        overflowView = view;
        return view;
    }

}

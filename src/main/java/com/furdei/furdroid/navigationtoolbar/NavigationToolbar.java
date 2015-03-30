package com.furdei.furdroid.navigationtoolbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

import com.furdei.furdroid.navigationtoolbar.internal.NavigationHolder;
import com.furdei.furdroid.navigationtoolbar.internal.NavigationItemClickListener;
import com.furdei.furdroid.navigationtoolbar.internal.NavigationItemInflater;

/**
 * <p>
 * Navigation Toolbar appears on left edge of the screen and shows navigation items only
 * By default these are icons, so Navigation Toolbar looks like Action Bar.
 * The main difference is that it is on the left of the screen where navigation drawer usually
 * appears. So it should be much more convenient to a user to look for navigation buttons
 * at the Navigation Toolbar instead of Action Bar.
 * </p><p>
 * When using a Navigation Toolbar in your project you should put the content of your activity
 * inside the navigation bar as shown here:
 * </p>
 * <pre>
 * {@code
 * <!-- Left-side toolbar with quick navigation buttons -->
 * <com.furdei.furdroid.navigationtoolbar.NavigationToolbar
 *      android:id="@+id/navigation_toolbar"
 *      android:layout_width="match_parent"
 *      android:layout_height="match_parent" >
 *
 *      <!-- Main layout -->
 *      <FrameLayout
 *          android:id="@+id/activity_content"
 *          android:layout_width="match_parent"
 *          android:layout_height="match_parent">
 *      </FrameLayout>
 *
 * </com.furdei.furdroid.navigationtoolbar.NavigationToolbar>
 * }
 * </pre>
 * <h3>Populating a toolbar</h3>
 * <p>
 * Navigation Toolbar is populated with items inflated from <code>menu</code> resource. Call
 * {@link #setMenuResId(int)} to specify a menu for inflating items. You can also use a
 * <code>navigationToolbarMenu</code> XML property to specify a menu right in the layout.
 * </p>
 * <h3>Responding to user events</h3>
 * <p>
 * When user clicks a Navigation Toolbar button, the default implementation
 * calls {@link android.app.Activity#onOptionsItemSelected(android.view.MenuItem)} method and
 * allows developer to react to navigation events in the same manner he would process Action Bar
 * events. You can change this behaviour by calling {@link #setNavigationToolbarListener} method
 * and providing your own implementation of
 * {@link NavigationToolbar.NavigationToolbarListener}
 * interface. The default implementation is
 * {@link OptionsMenuNavigationToolbarListener} class.
 * </p><p>
 * {@link OptionsMenuNavigationToolbarListener}
 * also allows you to receive clicks on Home/Up button when user clicks an
 * Overflow button. This is the default behaviour, but you can change it by calling
 * {@link OptionsMenuNavigationToolbarListener#setClickHomeUpForOverflowButton(boolean)}
 * method.
 * </p>
 *
 * @author Stepan Furdey
 */
public class NavigationToolbar extends FrameLayout {

    /**
     * Listener for navigation events.
     */
    public interface NavigationToolbarListener {

        /**
         * Called when user clicks a navigation button.
         *
         * @param menuItem {@link android.view.MenuItem} associated with a pressed button
         */
        public void onNavigationClicked(MenuItem menuItem);
    }

    private static final int TRANSPARENT_COLOR_MASK = 0x00FFFFFF;

    private int menuResId;
    private Drawable shadowDrawable;
    private int actionBarWidth;
    private int shadowWidth;
    private ColorStateList iconColor;
    private ColorStateList backgroundColor;
    private NavigationHolder navigationView;
    private NavigationToolbarListener navigationToolbarListener;

    private NavigationItemClickListener.NavigationClickListener navigationClickListener =
            new NavigationItemClickListener.NavigationClickListener() {
                @Override
                public void onClick(MenuItem menuItem) {
                    if (navigationToolbarListener != null) {
                        navigationToolbarListener.onNavigationClicked(menuItem);
                    }
                }
            };

    public NavigationToolbar(Context context) {
        super(context);
        init(null, R.attr.navigationToolbarStyle, R.style.Widget_NavigationToolbar);
    }

    public NavigationToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.navigationToolbarStyle, R.style.Widget_NavigationToolbar);
    }

    public NavigationToolbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle, R.style.Widget_NavigationToolbar);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (shadowDrawable != null) {
            canvas.save();

            canvas.translate(actionBarWidth, 0f);
            shadowDrawable.draw(canvas);

            canvas.restore();
        }
    }

    /**
     * Shadow drawable is drawn over the content to make an effect of elevation of navigation bar.
     */
    public Drawable getShadowDrawable() {
        return shadowDrawable;
    }

    /**
     * Specify a shadow drawable. Shadow drawable is drawn over the content to make an effect
     * of elevation of navigation bar.
     *
     * @param shadowDrawable - drawable to replace a shadow. <code>null</code> means no shadow.
     */
    public void setShadowDrawable(Drawable shadowDrawable) {
        this.shadowDrawable = shadowDrawable;
    }

    public int getMenuResId() {
        return menuResId;
    }

    /**
     * Specify a menu resource to inflate navigation items. Note that inflation actually happens
     * right here, so make sure that you have configured all styleable properties before calling
     * <code>setMenuResId</code> method.
     *
     * @param menuResId menu resource to inflate navigation items
     */
    public void setMenuResId(int menuResId) {
        this.menuResId = menuResId;
        removeMenuViews();
        inflateMenu(menuResId);
    }

    /**
     * Clear all menu views and leave tha navigation bar empty
     */
    public void removeMenuViews() {
        navigationView.removeMenuViews();
    }

    /**
     * Returns a view group holding navigation items. You can use it for implementing animation
     * over navigation items for example.
     */
    public ViewGroup getNavigationView() {
        return navigationView;
    }

    /**
     * Returns a current listener for navigation toolbar events
     */
    public NavigationToolbarListener getNavigationToolbarListener() {
        return navigationToolbarListener;
    }

    /**
     * Replaces a listener for navigation toolbar events. The default listener is
     * {@link OptionsMenuNavigationToolbarListener
     * OptionsMenuNavigationToolbarListener} class. It routs all events to the current activity's
     * {@link android.app.Activity#onOptionsItemSelected(android.view.MenuItem)} method
     * and performs Home/Up button clicks when user clicks an Overflow button on the navigation
     * bar. You should take care yourself of this behaviour if you are going to replace the
     * listener.
     *
     * @param navigationToolbarListener
     */
    public void setNavigationToolbarListener(NavigationToolbarListener navigationToolbarListener) {
        this.navigationToolbarListener = navigationToolbarListener;
    }

    /**
     * Colors used to tint navigation icons depending on their state
     */
    public ColorStateList getIconColor() {
        return iconColor;
    }

    /**
     * Specify colors used to tint navigation icons depending on their state. Note that for
     * performance reasons this call will not affect anything but Overflow button after calling
     * {@link #setMenuResId(int)} method so make sure to call it before.
     */
    public void setIconColor(ColorStateList iconColor) {
        this.iconColor = iconColor;
        navigationView.setIconColor(iconColor);
    }

    /**
     * Colors used for ripple effect for navigation icons
     */
    public ColorStateList getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Specify colors used for ripple effect for navigation icons. Note that for
     * performance reasons this call will not affect anything but Overflow button after calling
     * {@link #setMenuResId(int)} method so make sure to call it before.
     */
    public void setBackgroundColor(ColorStateList backgroundColor) {
        this.backgroundColor = backgroundColor;
        navigationView.setBackgroundColor(backgroundColor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        setPadding(getPaddingLeft() + actionBarWidth, getPaddingTop(),
                getPaddingRight(), getPaddingBottom());
        super.onLayout(changed, left, top, right, bottom);
        setPadding(getPaddingLeft() - actionBarWidth, getPaddingTop(),
                getPaddingRight(), getPaddingBottom());
        navigationView.layout(left, top, left + actionBarWidth, bottom);

        if (shadowDrawable != null) {
            int localShadowWidth = shadowWidth;

            if (localShadowWidth == 0) {
                Rect bounds = shadowDrawable.getBounds();
                localShadowWidth = bounds.width();
            }

            shadowDrawable.setBounds(0, 0, localShadowWidth, bottom - top);
        }
    }

    /**
     * Called in constructors. Initialize the component
     */
    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setWillNotDraw(false);

        navigationView = new NavigationHolder(getContext());
        addView(navigationView);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.NavigationToolbar, defStyleAttr, defStyleRes);

        actionBarWidth = typedArray.getDimensionPixelSize(
                R.styleable.NavigationToolbar_navigationToolbarWidth, 0);
        shadowWidth = typedArray.getDimensionPixelSize(
                R.styleable.NavigationToolbar_navigationToolbarShadowWidth, 0);
        Drawable shadowDrawable = typedArray.getDrawable(
                R.styleable.NavigationToolbar_navigationToolbarShadowDrawable);
        int primaryColorDark = typedArray.getColor(
                R.styleable.NavigationToolbar_navigationToolbarColorPrimaryDark, 0);
        int primaryColor = typedArray.getColor(
                R.styleable.NavigationToolbar_navigationToolbarColorPrimary, 0);
        int accentColor = typedArray.getColor(
                R.styleable.NavigationToolbar_navigationToolbarColorAccent, 0);
        int menuResId = typedArray.getResourceId(
                R.styleable.NavigationToolbar_navigationToolbarMenu, 0);
        typedArray.recycle();

        int[][] states = new int[][] {{ android.R.attr.state_pressed }, {}};
        int[] iconColors = new int[] { primaryColorDark, primaryColor };
        int[] backgroundColors = new int[] { accentColor, accentColor & TRANSPARENT_COLOR_MASK };
        iconColor = new ColorStateList(states, iconColors);
        backgroundColor = new ColorStateList(states, backgroundColors);
        navigationView.setIconColor(iconColor);
        navigationView.setBackgroundColor(backgroundColor);

        setShadowDrawable(shadowDrawable);
        setNavigationToolbarListener(
                new OptionsMenuNavigationToolbarListener((Activity) getContext()));

        if (menuResId != 0) {
            setMenuResId(menuResId);
        }
    }

    /**
     * Inflate a menu from resource
     */
    private void inflateMenu(int menuResId) {
        PopupMenu popupMenu = new PopupMenu(getContext(), null);
        Menu menu = popupMenu.getMenu();

        MenuInflater menuInflater = new MenuInflater(getContext());
        menuInflater.inflate(menuResId, menu);

        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        NavigationItemInflater navigationItemInflater = new NavigationItemInflater(layoutInflater,
                navigationView, R.layout.navigation_item, navigationClickListener,
                iconColor, backgroundColor);

        int itemsCount = menu.size();

        for (int i = 0; i < itemsCount; i++) {
            navigationItemInflater.inflateView(menu.getItem(i));
        }
    }

}

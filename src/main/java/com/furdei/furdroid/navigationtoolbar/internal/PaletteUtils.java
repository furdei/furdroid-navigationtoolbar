package com.furdei.furdroid.navigationtoolbar.internal;

import android.content.res.ColorStateList;

/**
 * Helper class to work with color palette
 *
 * @author Stepan Furdey
 */
public class PaletteUtils {

    private static final int TRANSPARENT_COLOR_MASK = 0x00FFFFFF;

    public static ColorStateList getIconColorState(int primaryColor, int primaryColorDark,
                                                   int accentColor) {
        int states[][] = new int[][] {
                {android.R.attr.state_pressed},
                {android.R.attr.state_focused},
                {}};
        int colors[] = new int[] {primaryColorDark, accentColor, primaryColor};
        return new ColorStateList(states, colors);
    }

    public static ColorStateList getRippleColorState(int accentColor) {
        int states[][] = new int[][] {
                {android.R.attr.state_pressed},
                {}};
        int colors[] = new int[] {accentColor, accentColor & TRANSPARENT_COLOR_MASK};
        return new ColorStateList(states, colors);
    }

}

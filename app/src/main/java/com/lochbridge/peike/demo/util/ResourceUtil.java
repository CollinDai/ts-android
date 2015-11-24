package com.lochbridge.peike.demo.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by PDai on 11/24/2015.
 */
public class ResourceUtil {
    public static int getCountryFlagResId(Context context,String iso639) {
        String resourceName = Constants.PREFIX_RES_FLAG + iso639;
        return getDrawableIdByName(context, resourceName);
    }
    public static int getDrawableIdByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }
}

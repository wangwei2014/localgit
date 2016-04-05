package com.ww.optimize;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

public class DisplayUtils {
	
	private static DisplayMetrics mDisplayMetrics;
    private static final int THUMBNAIL_SIZE = 320;
    private static final int MAX_THUMBNAIL_SIZE = 640;
    private static int mThumbnailSize;

	public static void init(Context context) {
		mDisplayMetrics = context.getResources().getDisplayMetrics();
        mThumbnailSize = dp2px(THUMBNAIL_SIZE);
        if (mThumbnailSize > MAX_THUMBNAIL_SIZE) {
            mThumbnailSize = MAX_THUMBNAIL_SIZE;
        }
	}

    public static int sp2px(int spValue) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, mDisplayMetrics);
    }

    public static int getThumbnailSize() {
        return mThumbnailSize;
    }

	public static int getDisplayWidth() {
		return mDisplayMetrics.widthPixels;
	}

	public static int getDisplayHeight() {
		return mDisplayMetrics.heightPixels;
	}

	public static float getDisplayDensity() {
		return mDisplayMetrics.density;
	}

    public static int getDpi() {
        return mDisplayMetrics.densityDpi;
    }

	public static int dp2px(float dp) {
		return (int)(mDisplayMetrics.density * dp + 0.5f);
	}

    public static int px2dip(int px) {
        return (int) (px / mDisplayMetrics.density + 0.5f);
    }

    private static int mAppWidth;
    private static int mAppHeight;
    private static float mAppRatio;

    public static void setAppWindowWidth(int width) {
        mAppWidth = width;
    }

    public static void setAppWindowHeight(int height) {
        mAppHeight = height;
    }

    public static void setAppWindowRatio(float ratio) {
        mAppRatio = ratio;
    }

    public static int getAppWindowWidth() {
        return mAppWidth;
    }

    public static int getAppWindowHeight() {
        return mAppHeight;
    }

    public static float getAppWindowRatio() {
        return mAppRatio;
    }

    private static int mNotificationHeight;

    /**
     * 必要条件：1.显示通知栏，2.没有显示输入法
     */
    public static void setActivity(Activity activity) {
        if (mNotificationHeight == 0) {
            View view = activity.getWindow().getDecorView();
            mAppWidth = view.getWidth();
            mAppHeight = view.getHeight();
            mAppRatio = (float)mAppWidth / mAppHeight;
            mNotificationHeight = mAppHeight - getWindowVisibleDisplayHeight(view);
        }
    }

    public static int getNotificationHeight() {
        return mNotificationHeight;
    }

    public static int getWindowVisibleDisplayHeight(View view) {
        Rect r = new Rect();
        view.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    public static int getRootViewHeight(View view) {
        return view.getRootView().getHeight();
    }

}

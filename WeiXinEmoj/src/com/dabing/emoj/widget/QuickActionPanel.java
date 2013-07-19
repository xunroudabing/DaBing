package com.dabing.emoj.widget;

import java.util.List;

import com.dabing.emoj.R;



import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

public class QuickActionPanel extends PopupWindow {

	private static final int MEASURE_AND_LAYOUT_DONE = 1 << 1;

    private final int[] mLocation = new int[2];
    private final Rect mRect = new Rect();

    private int mPrivateFlags;

    private Context mContext;

    private boolean mDismissOnClick;
    private int mArrowOffsetY;

    private int mPopupY;
    private boolean mIsOnTop;

    private int mScreenHeight;
    private int mScreenWidth;
    private boolean mIsDirty;
    private FrameLayout mContainer;
    public QuickActionPanel(Context c){
    	super(c);
    	this.mContext=c;
    	initializeDefault();

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        final WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = windowManager.getDefaultDisplay().getWidth();
        mScreenHeight = windowManager.getDefaultDisplay().getHeight();
        
        setContentView(R.layout.gd_quick_action_panel);
        final View view = getContentView();      
        mContainer = (FrameLayout)view.findViewById(R.id.gdPanel_container);
    }
    public void setContainerView(int layoutId){
    	if(mContainer != null){
    	View childView=LayoutInflater.from(mContext).inflate(layoutId, null);
    	mContainer.addView(childView);
    	}
    }
    public void setContainerView(View view) {
		// TODO Auto-generated method stub
    	if(mContainer != null){
    		mContainer.addView(view);
    	}
	} 
    public View getContainerView(){
    	return this.mContainer;
    }
    public void setContentView(int layoutId) {
        setContentView(LayoutInflater.from(mContext).inflate(layoutId, null));
    }

    private void initializeDefault() {
        mDismissOnClick = true;
        mArrowOffsetY = mContext.getResources().getDimensionPixelSize(R.dimen.gd_arrow_offset);
    }
    
    public int getArrowOffsetY() {
        return mArrowOffsetY;
    }
    
    public void setArrowOffsetY(int offsetY) {
        mArrowOffsetY = offsetY;
    }
    
    protected int getScreenWidth() {
        return mScreenWidth;
    }
    
    protected int getScreenHeight() {
        return mScreenHeight;
    }
    
    public void setDismissOnClick(boolean dismissOnClick) {
        mDismissOnClick = dismissOnClick;
    }

    public boolean getDismissOnClick() {
        return mDismissOnClick;
    }
    
    public void show(View anchor) {

        final View contentView = getContentView();

        if (contentView == null) {
            throw new IllegalStateException("You need to set the content view using the setContentView method");
        }

        // Replaces the background of the popup with a cleared background
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final int[] loc = mLocation;
        anchor.getLocationOnScreen(loc);
        mRect.set(loc[0], loc[1], loc[0] + anchor.getWidth(), loc[1] + anchor.getHeight());

        if (mIsDirty) {
//            clearQuickActions();
//            populateQuickActions(mQuickActions);
        }

        onMeasureAndLayout(mRect, contentView);

        if ((mPrivateFlags & MEASURE_AND_LAYOUT_DONE) != MEASURE_AND_LAYOUT_DONE) {
            throw new IllegalStateException("onMeasureAndLayout() did not set the widget specification by calling"
                    + " setWidgetSpecs()");
        }

        showArrow();
        prepareAnimationStyle();
        showAtLocation(anchor, Gravity.NO_GRAVITY, 0, mPopupY);
    }
    
    protected void onMeasureAndLayout(Rect anchorRect, View contentView) {

        contentView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        contentView.measure(MeasureSpec.makeMeasureSpec(getScreenWidth(), MeasureSpec.EXACTLY),
                LayoutParams.WRAP_CONTENT);

        int rootHeight = contentView.getMeasuredHeight();

        int offsetY = getArrowOffsetY();
        int dyTop = anchorRect.top;
        int dyBottom = getScreenHeight() - anchorRect.bottom;

        boolean onTop = (dyTop > dyBottom);
        int popupY = (onTop) ? anchorRect.top - rootHeight + offsetY : anchorRect.bottom - offsetY;

        setWidgetSpecs(popupY, onTop);
    }

    protected void setWidgetSpecs(int popupY, boolean isOnTop) {
        mPopupY = popupY;
        mIsOnTop = isOnTop;

        mPrivateFlags |= MEASURE_AND_LAYOUT_DONE;
    }

    private void showArrow() {

//        final View contentView = getContentView();
//        final int arrowId = mIsOnTop ? R.id.gdPanel_arrow_down : R.id.gdPanel_arrow_up;
//        final View arrow = contentView.findViewById(arrowId);
//        final View arrowUp = contentView.findViewById(R.id.gdPanel_arrow_up);
//        final View arrowDown = contentView.findViewById(R.id.gdPanel_arrow_down);
//
//        if (arrowId == R.id.gdPanel_arrow_up) {
//            arrowUp.setVisibility(View.VISIBLE);
//            arrowDown.setVisibility(View.INVISIBLE);
//        } else if (arrowId == R.id.gdPanel_arrow_down) {
//            arrowUp.setVisibility(View.INVISIBLE);
//            arrowDown.setVisibility(View.VISIBLE);
//        }

//        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) arrow.getLayoutParams();
//        param.leftMargin = mRect.centerX() - (arrow.getMeasuredWidth()) / 2;
    }

    private void prepareAnimationStyle() {

        final int screenWidth = mScreenWidth;
        final boolean onTop = mIsOnTop;
        final int arrowPointX = mRect.centerX();

        if (arrowPointX <= screenWidth / 4) {
            setAnimationStyle(onTop ? R.style.GreenDroid_Animation_PopUp_Left
                    : R.style.GreenDroid_Animation_PopDown_Left);
        } else if (arrowPointX >= 3 * screenWidth / 4) {
            setAnimationStyle(onTop ? R.style.GreenDroid_Animation_PopUp_Right
                    : R.style.GreenDroid_Animation_PopDown_Right);
        } else {
            setAnimationStyle(onTop ? R.style.GreenDroid_Animation_PopUp_Center
                    : R.style.GreenDroid_Animation_PopDown_Center);
        }
    }

    protected Context getContext() {
        return mContext;
    }

}

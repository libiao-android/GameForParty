package com.nubia.lijia;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SoundEffectConstants;
import android.view.View;

public class WheelView extends TosGallery {
 
    private Rect mSelectorBound = new Rect();

    public WheelView(Context context) {
        super(context);

        initialize(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initialize(context);
    }

    private void initialize(Context context) {
        this.setVerticalScrollBarEnabled(false);
        this.setSlotInCenter(true);
        this.setOrientation(TosGallery.VERTICAL);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
        this.setUnselectedAlpha(1.0f);
        this.setWillNotDraw(false);

        this.setSoundEffectsEnabled(true);
    }

    /**
     * Called by draw to draw the child views. This may be overridden by derived classes to gain
     * control just before its children are drawn (but after its own view has been drawn).
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

    }

    /**
     * setOrientation
     */
    @Override
    public void setOrientation(int orientation) {
        if (TosGallery.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("The orientation must be VERTICAL");
        }

        super.setOrientation(orientation);
    }

    /**
     * Call when the ViewGroup is layout.
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        int galleryCenter = getCenterOfGallery();
        View v = this.getChildAt(0);

        int height = (null != v) ? v.getMeasuredHeight() : 50;
        int top = galleryCenter - height / 2;
        int bottom = top + height;

        mSelectorBound.set(getPaddingLeft(), top, getWidth() - getPaddingRight(), bottom);
    }
    @Override
    protected void selectionChanged() {
        super.selectionChanged();

        playSoundEffect(SoundEffectConstants.CLICK);
    }
  
}

package com.demo.ms.dragdismisscontroldemo;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * ==============================================
 * 类名：拖拽消失View
 *
 * 作者：M-Liu
 *
 * 时间：2016/9/27
 *
 * 邮箱：ms_liu163@163.com
 * ==============================================
 */

public class DragDismissView extends FrameLayout  {
    private static final int DEFAULT_DISMISS_DRAG_HEIGHT = 400;
    private static final float DEFAULT_DRAG_DAMP = .8f;
    private static final float DEFAULT_SCALE_DISMISS = .9f;
    private static final boolean DEFAULT_NEED_SCALE = true;
    private static final boolean DEFAULT_DRAG_V = true;
    private static final int VERTICAL = LinearLayout.VERTICAL;
    private static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    private final int mTouchSlop;
    private Context mCtx;
    private boolean dragDown = false;
    private boolean dragUp = false;
    private float totalDrag;
    private int maxDismissDragHeight = DEFAULT_DISMISS_DRAG_HEIGHT;
    private float dragDamp = DEFAULT_DRAG_DAMP;
    private float scaleDismiss = DEFAULT_SCALE_DISMISS;

    private List<DragDismissCallback> callbacks;
    private boolean needScale = DEFAULT_NEED_SCALE;
    private int dragOrientation = VERTICAL;
    private boolean dragRight = false;
    private boolean dragLeft = false;

    public DragDismissView(Context context) {
        this(context, null);
    }

    public DragDismissView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragDismissView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mCtx = context;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    //开始滑动
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if ((nestedScrollAxes & View.SCROLL_AXIS_HORIZONTAL)!= 0){
            return true;
        }else if ((nestedScrollAxes & View.SCROLL_AXIS_VERTICAL) != 0){
            return true;
        }
        return super.onStartNestedScroll(child,target,nestedScrollAxes);
    }

    /**
     * 添加回调
     * @param callback
     */
    public void addCallback(DragDismissCallback callback){
        if (callbacks == null){
            callbacks = new ArrayList<>();
        }
        callbacks.add(callback);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//        当有拖拽手势的时候 会调用
//        if (checkDragVertical(dy)){
//            if (dragOrientation == VERTICAL){
//                scaleAndTranslateWithDrag(dx, dy);
//            }else if (dragOrientation == HORIZONTAL){
//                scaleAndTranslateWithDrag(dxUnconsumed, dx);
//            }
//        }
    }

    private boolean checkDragVertical(int y) {
        //垂直滑动 返回true
        return dragDown && (y > 0) || dragUp && (y < 0);
    }

    //滑动过程中
    @Override
    public void onNestedScroll(View target,
                               int dxConsumed,
                               int dyConsumed,
                               int dxUnconsumed,
                               int dyUnconsumed) {
            scaleAndTranslateWithDrag(dxConsumed,dyUnconsumed);
    }

    private void scaleAndTranslateWithDrag(int dx, int dy) {
        int drag = 0;
        if (dragOrientation == VERTICAL){
            drag = dy;
        }else if (dragOrientation == HORIZONTAL){
            drag = dx;
        }
        //未拖拽
        if (drag == 0) return;
        //记录当前拖拽总距离
        totalDrag += drag;
        //判断 拖拽方向
        if (dragOrientation == VERTICAL) {
            if (drag < 0 && !dragDown && !dragUp){
                dragDown = true;
                if (needScale) setPivotY(getHeight());
            } else if (drag> 0&& !dragUp&& !dragDown){
                dragUp = true;
                if (needScale) setPivotY(0f);
            }
        }else if (dragOrientation == HORIZONTAL){
            if (drag< 0&& Math.abs(drag)> View.SCROLL_AXIS_HORIZONTAL&& !dragRight&& !dragLeft){
                dragRight = true;
            }else if(drag> 0&& Math.abs(drag)> View.SCROLL_AXIS_HORIZONTAL&& !dragLeft&& !dragRight){
                dragLeft = true;
            }
        }

        //计算拖拽比例
        // 使用Log10为了 确保数值在0 - 1 并且是增量 具体参照看函数曲线
        float dragFraction = (float) Math.log10(1+(Math.abs(totalDrag) / maxDismissDragHeight));
        //计算位移距离 = 拖拽比例 * 最大允许拖拽距离 * 阻尼系数
        float translate = dragFraction * maxDismissDragHeight * dragDamp;
        //判断是否做缩放
        if (needScale) doScale(dragFraction);
        //做位移
        doTranslate(translate);
        //方向改变时 重置方向
        if ((dragDown && totalDrag >= 0)
                || (dragUp && totalDrag <= 0)) {
            totalDrag = translate = dragFraction = 0;
            dragDown = dragUp = false;
            dragRight = dragLeft = false;
            setTranslationY(0f);
            if (needScale) doScale(-1);
        }
        dispatchDraggingCallback(dragFraction,translate, totalDrag);
    }

    private void doScale(float dragFraction) {
        float scale = (float) (1 - ((1-scaleDismiss)* dragFraction * dragDamp));
        setScaleX(scale);
        setScaleY(scale);
    }

    private void doTranslate(float translate) {
        if (dragOrientation == HORIZONTAL) {
            if (dragRight) {
                translate *= -1;
            }
            setTranslationX(200);
        }else if (dragOrientation == VERTICAL){
            if (dragUp) {
                translate *= -1;
            }
            setTranslationY(translate);
        }
    }

    @Override
    public void onStopNestedScroll(View child) {
        if (Math.abs(totalDrag) >= maxDismissDragHeight){//结束
            dispatchDismissedCallback();
        }else {//恢复
           recover();
        }
    }

    //传递 拖拽过程中回调
    private void dispatchDraggingCallback(float dragFraction, float translateY, float totalDragY) {
        if (callbacks != null && !callbacks.isEmpty()){
            for (DragDismissCallback callback : callbacks){
                callback.onDraggingCallback(dragFraction,translateY,totalDragY);
            }
        }
    }

    //传递 拖拽结束回调
    private void dispatchDismissedCallback() {
        if (callbacks != null && !callbacks.isEmpty()){
            for (DragDismissCallback callback : callbacks){
                callback.onDragDismissed();
            }
        }else {
            if (mCtx instanceof Activity){
                Activity activity = (Activity) mCtx;
                activity.onBackPressed();
            }
        }
    }

    /**
     * 恢复
     */
    public void recover() {
        animate()
                .translationY(0f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(200L)
                .setListener(null)
                .start();
        totalDrag = 0;
        dragDown = dragUp = false;
    }

    public interface DragDismissCallback {
        /**
         * 拖拽过程中
         * @param dragFraction
         * @param translateY
         * @param totalDragY
         */
        void onDraggingCallback(float dragFraction, float translateY, float totalDragY);

        /**
         * 拖拽结束
         */
        void onDragDismissed();
    }


}

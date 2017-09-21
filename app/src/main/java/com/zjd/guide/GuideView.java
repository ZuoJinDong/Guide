package com.zjd.guide;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by 左金栋 on 2017/9/19.
 */

public class GuideView extends View{
    private Context context;
    private int width;//设置高
    private int height;//设置高

    private Paint paint_background,paint_Button;
    private TextPaint textPaint;
    private List<ViewBean> views;
    private int position=0;
    private OnFinishListener mOnFinishListener;

    public GuideView(Context context) {
        super(context);
        this.context=context;
        initPaint();
    }

    public GuideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initPaint();
    }

    public GuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initPaint();
    }

    private void initPaint() {
        paint_background=new Paint();
        paint_background.setAntiAlias(true);
        paint_background.setStyle(Paint.Style.FILL_AND_STROKE);
        paint_background.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        paint_Button=new Paint();
        paint_Button.setAntiAlias(true);
        paint_Button.setStrokeWidth(3);
        paint_Button.setStyle(Paint.Style.STROKE);
        paint_Button.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        textPaint=new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(100, 0, 0, 0);
        drawBackground(canvas);
        drawText(canvas);
    }

    private void drawBackground(Canvas canvas) {
        View view=views.get(position).getView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(view.getX()+left*value,view.getY()+top*value,view.getX()+view.getWidth()+right*value,view.getY()+view.getHeight()+bottom*value,10,10,paint_background);
        }else {
            canvas.drawRect(view.getX(),view.getY(),view.getX()+view.getWidth(),view.getY()+view.getHeight(),paint_background);
        }
    }

    private void drawText(Canvas canvas) {
        canvas.save();
        textPaint.setTextSize(width/20);
        StaticLayout myStaticLayout = new StaticLayout(views.get(position).getContent(), textPaint, canvas.getWidth()*3/4, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        if(views.get(position).getView().getY()<height/2){
            canvas.translate(width/2,views.get(position).getView().getY()+views.get(position).getView().getHeight()+width/10+bottom*value);
        }else {
            canvas.translate(width/2,views.get(position).getView().getY()-width/10+bottom*value-myStaticLayout.getHeight());
        }
        myStaticLayout.draw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                if(valueAnimator==null||(valueAnimator!=null&&!valueAnimator.isRunning())){
                    position++;
                    if(position==views.size()){
                        if(mOnFinishListener!=null){
                            mOnFinishListener.isFinish(true);
                        }
                    }else {
                        SwitchAmin();
                    }
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private float value=0;
    private float left=0,top=0,right=0,bottom=0;
    private ValueAnimator valueAnimator;
    private void SwitchAmin() {
        View viewFormer;
        if(views.size()==0){
            return;
        }
        View view=views.get(position).getView();

        if(views.size()==1){
            left=0-view.getX();
            top=0-view.getY();
            right=width-view.getX()-view.getWidth();
            bottom=height-view.getY()-view.getHeight();
        }else {
            if(position==0){
                left=0-view.getX();
                top=0-view.getY();
                right=width-view.getX()-view.getWidth();
                bottom=height-view.getY()-view.getHeight();
            }else {
                viewFormer=views.get(position-1).getView();
                left=viewFormer.getX()-view.getX();
                top=viewFormer.getY()-view.getY();
                right=viewFormer.getX()+viewFormer.getWidth()-view.getX()-view.getWidth();
                bottom=viewFormer.getY()+viewFormer.getHeight()-view.getY()-view.getHeight();
            }
        }

        valueAnimator= ValueAnimator.ofFloat(1f,0f);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value= (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getMeasuredWidth(), widthMeasureSpec);// 获得控件的宽度
        height = getDefaultSize(getMeasuredHeight(), heightMeasureSpec);//获得控件的高度
        setMeasuredDimension(width , height);//设置宽和高
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 获取基线中间点
     * @param paint
     * @return
     */
    public int getBaseLineY(Paint paint){
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        return (int) (width/16 - top/2 - bottom/2);//基线中间点的y轴计算公式
    }

    public void setViews(List<ViewBean> views){
        this.views=views;
        invalidate();
    }

    public void setReStart(){
        position=0;
        SwitchAmin();
    }

    /**
     * 点击结束
     */
    public interface OnFinishListener{
        void isFinish(boolean isFinish);
    }

    public void setOnFinishListener(OnFinishListener listener){
        this.mOnFinishListener=listener;
    }
}

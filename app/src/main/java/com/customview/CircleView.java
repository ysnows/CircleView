package com.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.ArrayList;

/**
 * Created by xianguangjin on 16/8/31.
 * <p>
 * 我的GitHub: https://github.com/ysnows
 * <p>
 * 加油,做一个真的汉子
 */

public class CircleView extends View {
    private Paint _paint;
    private int centerX, centerY;
    private Path path;
    private int width;
    private int height;

    private int circleNum = 2;//原点的数量
    private float radius = 60;//圆的半径
    private float gap = 60;//圆之间的间隔

    private PointF p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, center;

    private ArrayList<PointF> centers = new ArrayList<>();


    private float blackMagic = 0.551915024494f;
    private float c = 0;
    private float offset = 0;
    private float mInterpolatedTime;


    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(3);

        path = new Path();

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        /** 说明:
         *宽高
         */
        width = w;
        height = h;

        /** 说明:
         *中心点
         */
        centerX = w / 2;
        centerY = h / 2;
        center = new PointF(centerX, centerY);


        for (int i = 0; i < circleNum; i++) {
            PointF pointF = new PointF();
            pointF.y = centerY;
            pointF.x = radius + gap + (radius * 2 + gap) * i;
            centers.add(pointF);
        }
    }


    /**
     * @param canvas
     * @param center
     * @param radius     用三次贝塞尔曲线画一个圆形,可以直接用
     * @param percentage
     */
    private void drawCircle(Canvas canvas, PointF center, float radius, Paint paint, float percentage) {
        c = radius * blackMagic;

        float offsetRight = 0;
        float offsetLeft = 0;


        /** 说明:
         *计算两边边点的移动偏差
         */
        if (percentage <= 0.2f) {//0f->0.2f
            offsetRight = radius * 2 * percentage;//0f->0.2f
            offsetLeft = 0f;//0f
        } else if (percentage <= 0.4f) {//0.2f->0.4f
            offsetRight = radius * 2 * percentage;//0.2f->0.4f
            offsetLeft = radius * 2 * (percentage - 0.2f);//0f->0.2f
        } else if (percentage <= 0.6f) {//0.4f->0.6f
            offsetRight = radius * 2 * (0.4f - (percentage - 0.4f));//0.4f->0.2f
            offsetLeft = radius * 2 * (percentage - 0.2f);//0.2f->0.4f
        } else if (percentage <= 0.8f) {//0.6f->0.8f
            offsetRight = radius * 2 * (0.2f - (percentage - 0.6f));//0.2f->0f
            offsetLeft = radius * 2 * (0.6f - (percentage - 0.4f));//0.4f->0.2f
        } else if (percentage <= 1f) {//0.8f->1f
            offsetRight = 0;//0f
            offsetLeft = radius * 2 * (0.2f - (percentage - 0.8f));//0.2f->0f
        }

        /** 说明:
         *只有在0.2之后才开始移动
         */
        if (percentage > 0.2f) {
            float length = (percentage - 0.2f) * 450;
            center.x += length;
        }


        /** 说明:
         *计算贝塞尔曲线用到的点
         */

        p0 = new

                PointF(center.x, center.y + radius);

        p1 = new

                PointF(center.x + c, center.y + radius);

        p2 = new

                PointF(center.x + radius + offsetRight, center.y + c);

        p3 = new

                PointF(center.x + radius + offsetRight, center.y);

        p4 = new

                PointF(center.x + radius + offsetRight, center.y - c);

        p5 = new

                PointF(center.x + c, center.y - radius);

        p6 = new

                PointF(center.x, center.y - radius);

        p7 = new

                PointF(center.x - c, center.y - radius);

        p8 = new

                PointF(center.x - radius - offsetLeft, center.y - c);

        p9 = new

                PointF(center.x - radius - offsetLeft, center.y);

        p10 = new

                PointF(center.x - radius - offsetLeft, center.y + c);

        p11 = new

                PointF(center.x - c, center.y + radius);

        path.reset();

        path.moveTo(p0.x, p0.y);
        path.cubicTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
        path.cubicTo(p4.x, p4.y, p5.x, p5.y, p6.x, p6.y);
        path.cubicTo(p7.x, p7.y, p8.x, p8.y, p9.x, p9.y);
        path.cubicTo(p10.x, p10.y, p11.x, p11.y, p0.x, p0.y);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        drawCoordinate(canvas);

//        for (PointF pointF : centers) {
//            drawCircle(canvas, pointF, radius, _paint);
//        }

        _paint.setColor(Color.BLUE);
        _paint.setStyle(Paint.Style.FILL);


        float v = mInterpolatedTime * 600;//滑动的多少距离

        center.x = radius;
        center.y = centerY;
        drawCircle(canvas, center, radius, _paint, mInterpolatedTime);
    }

    /**
     * 绘制坐标系
     *
     * @param canvas
     */
    private void drawCoordinate(Canvas canvas) {
        _paint.setColor(Color.LTGRAY);
        _paint.setStyle(Paint.Style.FILL);
        _paint.setStrokeWidth(3);
        canvas.drawLine(0, centerY, width, centerY, _paint);
        canvas.drawLine(centerX, 0, centerX, height, _paint);

    }


    private class MoveAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
            invalidate();
        }

    }


    public void startAnimation() {
        path.reset();
        mInterpolatedTime = 0;
        MoveAnimation move = new MoveAnimation();
        move.setDuration(500);
        move.setFillAfter(true);
        startAnimation(move);
    }

}

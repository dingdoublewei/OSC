package com.xianjitong.administrator.osc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2018/9/16.
 * 创建类继承surfaceview
 */
public class SensorScopeDisplay extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder=null;
    private MyThread myThread;
    //定义绘图所用的变量
    final int  X_OFFSET = 0; //起始端点的X坐标
    int i;
    int surfacewidth;
    int surfaceheight;
    /*
     *由于默认的XML文件解析方法是:
     * 调用View的View(Context , AttributeSet )构造函数构造View
     */
    public SensorScopeDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        //获取holder
        holder = this.getHolder();
        //添加回调
        holder.addCallback(this);
//        myThread = new MyThread(holder);//创建一个绘图线程
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        //获取surfaceview的宽
        surfacewidth = width;
        //获取surfaceview的高
        surfaceheight = height;

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        //在 View 中系统不允许主线程外的线程控制 UI .但是 SurfaceView 却可以
        myThread = new MyThread(holder);//创建一个绘图线程
        myThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }
    //如果CH1按下，则开始绘图
//    public boolean onTouchEvent(MotionEvent event) {
//        // TODO Auto-generated method stub
//        if(event.getAction()==MotionEvent.ACTION_DOWN){
//            String a=this.getTag().toString();
//            if(a.equals("true")){
//              //  xs.add(event.getX());
//              //  ys.add(event.getY());
//            }
//        }
//        return true;
//    }

    //线程内部类（进行示波器绘图）
    class MyThread extends Thread implements Runnable {
        private SurfaceHolder holder;
        public MyThread(SurfaceHolder holder) {
            drawBack(holder);
            this.holder = holder;
        }
        @Override
        public void run() {
            int[] dData = new int[1024];
            float x = 0;
            int X = 0;//真x，实际画在图上的x
            int oldX = X;//真oldx,实际画在图上的oldx,上一个X的坐标
            float[] y = new float[360];//上一个纵轴坐标
            float y0 = (46 * surfaceheight) / 56f;//纵轴0点位置
            int[] dFlag = new int[360];
            int j = 0;
            //将数据转换为实际点纵坐标
            for (int i = 0; i < 160; i++)
            {
                y[i] = ((40 - dData[i]) * 41f *surfaceheight) / (40f * 56f) + 5 * surfaceheight / 56f;
            }

            while (true) {
                Canvas c = null;
                try {
                    synchronized (holder) {
                        if (x >= surfacewidth)
                        {
                            // 清除画布
                            //clear();
                            c = holder.lockCanvas(new Rect((int) oldX, 0, (int) X + 3, surfaceheight));
                            c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                            holder.unlockCanvasAndPost(c);
                            x = 0;
                            X = 0;
                            oldX = X;
                        }
                        else
                        {
                            c = holder.lockCanvas(new Rect((int) oldX, 0, (int) X + 3, surfaceheight));
                            c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);// 清除画布
                            Paint p = new Paint();
                            p.setColor(Color.YELLOW);
                            p.setStrokeWidth(3);
                            c.drawLine(oldX, y[j - 1], X, y[j++], p);
                            oldX = X;
                            if (j == 360) j = 0;
                            //需要按照坐标画点，若不省略点，则一共需要画：8秒/20毫秒=400个点
                            //首先算出精确的x坐标，是浮点数，然后四舍五入成整数画在画布上，保证点数正确
                            x = x +surfacewidth/ 360f;
                            X = Math.round(x);
                            holder.unlockCanvasAndPost(c);
                        }
                        Thread.sleep(20);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }
        }
    }

    //绘制坐标轴
    private void drawBack(SurfaceHolder holder){
        Canvas canvas = holder.lockCanvas();
        //绘制蓝色背景
        canvas.drawColor(Color.GREEN);
        //绘制实线效果
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStrokeWidth(6);
        //设置虚线效果
        Paint p1 = new Paint();
        p1.setColor(Color.YELLOW);
        p1.setStrokeWidth(2);
        p1.setPathEffect ( new DashPathEffect( new float [ ] { 3, 3 }, 0 ) ) ;
        //画点
        //绘制实线效果
        Paint p2 = new Paint();
        p2.setColor(Color.RED);
        p2.setStrokeWidth(2);
        /*
         *  绘制坐标轴
         *  drawLine (float startX, float startY, float stopX, float stopY, Paint paint)
         *  startX：起始端点的X坐标。
         *  startY：起始端点的Y坐标。
         *  stopX：终止端点的X坐标。
         *  stopY：终止端点的Y坐标。
         *  paint：绘制直线所使用的画笔。
         */

        //绘制横坐标
        for(i=1;i<8;i++){
            canvas.drawLine(X_OFFSET, surfaceheight*i/8 , surfacewidth,surfaceheight*i/8, p1);
        }
        canvas.drawLine(X_OFFSET, 0 , surfacewidth,0, p);
        canvas.drawLine(X_OFFSET, surfaceheight , surfacewidth,surfaceheight, p);
        for(i=1;i<40;i++){
            if(i/5!=0||i/5!=1||i/5!=2||i/5!=3||i/5!=4||i/5!=5||i/5!=6||i/5!=7||i/5!=8) {
                canvas.drawLine(X_OFFSET, surfaceheight * i / 40, 10, surfaceheight * i / 40, p2);
            }
        }
        //绘制纵坐标
        for(i=1;i<10;i++){
            canvas.drawLine(surfacewidth*i/10,0,surfacewidth*i/10, surfaceheight, p1);
        }
        canvas.drawLine(X_OFFSET,0, X_OFFSET, surfaceheight, p);
        canvas.drawLine(surfacewidth,0,surfacewidth, surfaceheight, p);
        for(i=1;i<50;i++){
            if(i/5!=0||i/5!=1||i/5!=2||i/5!=3||i/5!=4||i/5!=5||i/5!=6||i/5!=7||i/5!=8||i/5!=9||i/5!=10) {
                canvas.drawLine(surfacewidth*i/50,surfaceheight,surfacewidth*i/50, surfaceheight-10, p2);
            }
        }
        holder.unlockCanvasAndPost(canvas);
        holder.lockCanvas(new Rect(0,0,0,0));
        holder.unlockCanvasAndPost(canvas);

    }
}
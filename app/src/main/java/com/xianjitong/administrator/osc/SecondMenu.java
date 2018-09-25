package com.xianjitong.administrator.osc;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2018/4/26.
 * 该程序实现示波器一级菜单中MENU按键的功能
 */


public class SecondMenu extends LoginActivity {

    Button CurrentButton, AdaptButton, ProbeselectButton, ReverseselecetButton, RestrainBandButton;//定义各个通道的按钮
    private LinearLayout secondlayout;
    TextView ReceiveEditText;//定义信息输入框
    //Button ReceiveButton;//定义接收按钮
    // EditText MsgEditText;//定义信息输出框
    private InputStream inputStream = null;//定义输入流
    private OutputStream outputStream = null;//定义输出流

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondmenu);

        //当登陆界面的登陆按钮登陆以后，启动连接线程
        // Receive_Thread receive_Thread = new Receive_Thread();
        // receive_Thread.start();

        CurrentButton = (Button) findViewById(R.id.current_bt);//获得发送按钮对象
        RestrainBandButton = (Button) findViewById(R.id.restrainBand_bt);//获得带宽抑制按钮对象
        AdaptButton = (Button) findViewById(R.id.adapt_bt);//获得伏/格对象
        ProbeselectButton = (Button) findViewById(R.id.probeselect_bt);//获得探头按钮对象
        ReverseselecetButton = (Button) findViewById(R.id.reverseselecet_bt);//获得反向按钮对象
        secondlayout = (LinearLayout) findViewById(R.id.secondlayout);

        //MsgEditText = (EditText) findViewById(R.id.adapt_bt);//获得发送消息文本框对象
        ReceiveEditText = (TextView) findViewById(R.id.Receive_ET);//获得接收消息文本框对象

        CurrentButton.setOnClickListener(CurrentButton_Listener);//耦合按钮监听
        // ReceiveButton.setOnClickListener(ReceiveButton_Listener);
        RestrainBandButton.setOnClickListener(RestrainBandButton_Listener);//带宽抑制按钮监听
        AdaptButton.setOnClickListener(AdaptButton_Listener);//伏/格按钮监听
        ProbeselectButton.setOnClickListener(ProbeselectButton_Listener);//探头按钮监听
        ReverseselecetButton.setOnClickListener(ReverseselecetButton_Listener);//反向按钮监听

        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        secondlayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    /*
     * 点击耦合按钮时所执行的动作
     */
    private View.OnClickListener CurrentButton_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                //获取输出流
                outputStream = socket.getOutputStream();
                //发送数据
                outputStream.write("CurrentButton_Openning".getBytes());
                //outputStream.write(MsgEditText.getText().toString().getBytes());
                Toast.makeText(SecondMenu.this, "已发送打开耦合通道命令", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    /*
     * 点击带宽抑制按钮时所执行的动作
     */
    private View.OnClickListener RestrainBandButton_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                //获取输出流
                outputStream = socket.getOutputStream();
                //发送数据
                outputStream.write(" RestrainBandButton_Openning".getBytes());
                //outputStream.write(MsgEditText.getText().toString().getBytes());
                Toast.makeText(SecondMenu.this, "已发送打开带宽抑制通道命令", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    /*
     * 点击伏/格按钮时所执行的动作
     */
    private View.OnClickListener AdaptButton_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                //获取输出流
                outputStream = socket.getOutputStream();
                //发送数据
                outputStream.write(" AdaptButton_Openning".getBytes());
                //outputStream.write(MsgEditText.getText().toString().getBytes());
                Toast.makeText(SecondMenu.this, "已发送打开伏/格通道命令", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    /*
     * 点击探头按钮时所执行的动作
     */
    private View.OnClickListener ProbeselectButton_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                //获取输出流
                outputStream = socket.getOutputStream();
                //发送数据
                outputStream.write("ReverseselecetButton_Openning".getBytes());
                //outputStream.write(MsgEditText.getText().toString().getBytes());
                Toast.makeText(SecondMenu.this, "已发送打开探头通道命令", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    /*
     * 点击反向按钮时所执行的动作
     */
    private View.OnClickListener ReverseselecetButton_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                //获取输出流
                outputStream = socket.getOutputStream();
                //发送数据
                outputStream.write("CH1_Openning".getBytes());
                //outputStream.write(MsgEditText.getText().toString().getBytes());
                Toast.makeText(SecondMenu.this, "已发送打开反向通道命令", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    //接收线程
    /*
    class Receive_Thread extends Thread {
        public void run()//重写run方法
        {
            try {
                while (true) {
                    final byte[] buffer = new byte[1024];//创建接收缓冲区
                    inputStream = socket.getInputStream();
                    final int len = inputStream.read(buffer);//数据读出来，并且返回数据的长度
                    runOnUiThread(new Runnable()//不允许其他线程直接操作组件，用提供的此方法可以
                    {
                        public void run() {
// TODO Auto-generated method stub
                        ReceiveEditText.setText(new String(buffer, 0, len));
                        }
                    });
                }
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
*/
}

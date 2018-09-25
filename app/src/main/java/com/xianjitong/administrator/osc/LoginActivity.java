package com.xianjitong.administrator.osc;
/*
 * import Timer与TimerTask是为了实现正弦波的数据。
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xianjitong.administrator.osc.view.EcgView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class LoginActivity extends MainActivity {
    Button MenuButton, CH2Button, CH1Button;//定义菜单按钮
    private EcgView sensorScopeDisplay;

    private InputStream inputStream = null;//定义输入流
    private OutputStream outputStream = null;//定义输出流
    private final float SUM_QUANTITY = 65535f;
    private final float DS_VOLTAGE = 3.3f;
    private final float DS_MULTIPLE = 0.0275f;
    private final float averageValue = 24968;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawline_main);

        //当登陆界面的登陆按钮登陆以后，启动连接线程
        Receive_Thread receive_Thread = new Receive_Thread();
//        receive_Thread.start();

        // 获取各个控件
        CH1Button = (Button) findViewById(R.id.ch1);//获得主界面CH1按钮对象
        CH2Button = (Button) findViewById(R.id.ch2);//获得主界面CH2按钮对象
        MenuButton = (Button) findViewById(R.id.mu);//获得主界面菜单按钮对象
        //布局文件中继承SurfaceView类的view的id,主要是为了找到这个View,避免为空
        sensorScopeDisplay = (EcgView) findViewById(R.id.myView);

        //给各控件添加监听事件
        CH1Button.setOnClickListener(CH1Button_Listener);//通道CH1的监听事件
        CH2Button.setOnClickListener(CH2Button_Listener);//通道CH2的监听事件
        MenuButton.setOnClickListener(MenuButton_Listener);//菜单按钮的监听事件

    }

    //接收线程
    /*
     * InputStream中read(byte[] b):从输入流中读取一定数量的字节，并将其存储在缓冲区数组b中；
     * 以整数形式返回实际读取的字节数
     */
    class Receive_Thread extends Thread {
        public void run()//重写run方法
        {
            try {
                while (true) {
                    final byte[] buffer = new byte[1024];//创建接收缓冲区
                    final int len;
                    inputStream = socket.getInputStream();
                    len = inputStream.read(buffer);//数据读出来，并且返回数据的长度
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*
     * 点击CH1按钮时所执行的动作
     */
    private View.OnClickListener CH1Button_Listener = new View.OnClickListener() {
//        public void onClick(View v) {
//            try {
//                //获取输出流
//                outputStream = socket.getOutputStream();
//                //发送数据
//                outputStream.write("CH1Button_Openning".getBytes());
//                sensorScopeDisplay.setTag(true);//按下发送命令的同时，开始绘图
//                //outputStream.write(MsgEditText.getText().toString().getBytes());
//                Toast.makeText(LoginActivity.this, "已发送打开耦合通道命令", Toast.LENGTH_SHORT).show();
//
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }


        @Override
        public void onClick(View v) {
            // 测试代码
            InputStream is = null;
            try {
//                is = getAssets().open("text.txt");
                is = socket.getInputStream();
                int length = is.available();
                byte[] buffer = new byte[length];
                is.read(buffer);
                String result = new String(buffer, "utf8").replaceAll(" ", "");
                String[] reads = result.split(",");
                for (String readBean : reads) {  // Float.parseFloat(readBean)
                    int readHigt = Integer.parseInt(readBean.substring(0, 8), 2);
                    int readlow = Integer.parseInt(readBean.substring(8, 16), 2);
                    float readdd = (readHigt << 8) | readlow;
                    Log.d("readdd =", readdd + " 1");
                    readdd = (((readdd - averageValue) / SUM_QUANTITY * DS_VOLTAGE) / DS_MULTIPLE);
                    Log.d("readdd =", readdd + " 2");
                    EcgView.addEcgData0(readdd);
                }
                sensorScopeDisplay.startThread();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    /*
     * 点击CH2按钮时所执行的动作
     */
    private View.OnClickListener CH2Button_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                //获取输出流
                outputStream = socket.getOutputStream();
                //发送数据
                outputStream.write("CH2Button_Openning".getBytes());
                //outputStream.write(MsgEditText.getText().toString().getBytes());
                Toast.makeText(LoginActivity.this, "已发送打开耦合通道命令", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    //当点击菜单按钮的时候，弹出一级菜单栏
    private View.OnClickListener MenuButton_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(LoginActivity.this, SelectPicPopupWindow.class));
        }
    };

}





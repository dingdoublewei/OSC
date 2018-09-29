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
    //接收数据线程
    private Receive_Thread receive_thread;
    //接收数据线程是否开启
    private boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawline_main);

        //当登陆界面的登陆按钮登陆以后，启动连接线程
        receive_thread = new Receive_Thread();

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
        @Override
        public void run() {
            // 测试代码
            InputStream is = null;
            try {
                //从本地文件获取数据
//                is = getAssets().open("text.txt");
                //从socket中获取流数据
                is = socket.getInputStream();
                int length = 1024 * 4;
                byte[] buffer = new byte[length];
                while (is.read() != -1) {
                    is.read(buffer);
                    String result = new String(buffer, "utf8").replaceAll(" ", "");
                    String[] reads = result.split(",");
                    for (String readBean : reads) {  // Float.parseFloat(readBean)
                        Log.d("readBean.length() =", readBean.length() + "");
                        if (readBean.length() == 16) {
                            int readHeight = Integer.parseInt(readBean.substring(0, 8), 2);
                            int readLow = Integer.parseInt(readBean.substring(8, 16), 2);
                            float finalResult = (readHeight << 8) | readLow;
                            Log.d("finalResult =", finalResult + " 1");
                            finalResult = (((finalResult - averageValue) / SUM_QUANTITY * DS_VOLTAGE) / DS_MULTIPLE);
                            Log.d("finalResult =", finalResult + " 2");
                            EcgView.addEcgData0(finalResult);
                        }
                    }
                    buffer = new byte[length];
                    sensorScopeDisplay.startThread();
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 点击CH1按钮时所执行的动作
     */
    private View.OnClickListener CH1Button_Listener = new View.OnClickListener() {
        //        @Override
//        public void onClick(View v) {
//            // 测试代码
//            InputStream is = null;
//            try {
//                //从本地文件获取数据
////                is = getAssets().open("text.txt");
//                //从socket中获取流数据
//                is = socket.getInputStream();
//                int length = is.available();
//                byte[] buffer = new byte[length];
//                is.read(buffer);
//                String result = new String(buffer, "utf8").replaceAll(" ", "");
//                String[] reads = result.split(",");
//                for (String readBean : reads) {  // Float.parseFloat(readBean)
//                    int readHigt = Integer.parseInt(readBean.substring(0, 8), 2);
//                    int readlow = Integer.parseInt(readBean.substring(8, 16), 2);
//                    float readdd = (readHigt << 8) | readlow;
//                    Log.d("readdd =", readdd + " 1");
//                    readdd = (((readdd - averageValue) / SUM_QUANTITY * DS_VOLTAGE) / DS_MULTIPLE);
//                    Log.d("readdd =", readdd + " 2");
//                    EcgView.addEcgData0(readdd);
//                }
//                sensorScopeDisplay.startThread();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        @Override
        public void onClick(View v) {
            if (!isStart && receive_thread != null) {
                isStart = true;
                receive_thread.start();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!receive_thread.isInterrupted()) {
            receive_thread.interrupt();
            receive_thread = null;
            isStart = false;
        }
    }
}





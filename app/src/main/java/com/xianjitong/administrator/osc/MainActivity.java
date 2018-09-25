package com.xianjitong.administrator.osc;

/**
 * Created by Administrator on 2018/3/28.
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    String imei;//手机imei号
    long long_mima;
    String string_mima;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberpass;
    static Socket socket = null;//定义socket
    Button shenqing_button;//申请密钥
    Button LandingButton;//登陆按钮
    Button NearButton;//进程连接按钮
    EditText PasswordEditText;//密码输入框
    EditText NearIpEditText;//近程ip输入框
    EditText PortText;//定义端口输入框
    static InputStream inputStream = null;//定义输入流

    //static OutputStream outputStream=null;//定义输出流
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        rememberpass = (CheckBox) findViewById(R.id.cb_mima);

        shenqing_button = (Button) findViewById(R.id.shenqingButton);//申请密钥
        LandingButton = (Button) findViewById(R.id.dengluButton);//登陆
        NearButton = (Button) findViewById(R.id.jinButton);//进程按钮

        NearIpEditText = (EditText) findViewById(R.id.jinIPEditText);//进程ip输入框
        PortText = (EditText) findViewById(R.id.PORTEditText);//通信端口
        PasswordEditText = (EditText) findViewById(R.id.mimaEditText);//登陆密码

        boolean isRemember = pref.getBoolean("cb_mima", false);//得到cb_mima文件存的值，得不到会返回false
        if (isRemember) //如果上次选择了保存密码
        {
            String password = pref.getString("password", "");//取出密码
            PasswordEditText.setText(password);//把密码输入到密码输入框
            rememberpass.setChecked(true);//选中记住密码
        }
        shenqing_button.setOnClickListener(shenqing_buttonListener);
        NearButton.setOnClickListener(NearButtonListener);//进程监听
        LandingButton.setOnClickListener(LandingButtonListener);//登陆监听

        /**
         * 获取IMEI号，IESI号，手机型号
         */
        getInfo();

        /**
         * 为完全退出应用程序而加的代码
         */
        ExitApplication.getInstance().addActivity(this);
    }

    /**
     * 申请密钥监听
     */
    private OnClickListener shenqing_buttonListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Toast.makeText(MainActivity.this, "请把" + imei + "提供给管理员", Toast.LENGTH_LONG).show();
        }
    };
    /**
     * //近程按钮连接监听
     */
    private OnClickListener NearButtonListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            NearConnect_Thread nearconnect_Thread = new NearConnect_Thread();
            nearconnect_Thread.start();
        }
    };

    /**
     * 近程连接线程
     */
    class NearConnect_Thread extends Thread//继承Thread
    {
        public void run()//重写run方法
        {
            try {
                if (true)
                {
                    //用InetAddress方法获取ip地址
                    InetAddress ipAddress = InetAddress.getByName(NearIpEditText.getText().toString());
                    int port = Integer.valueOf(PortText.getText().toString());//获取端口号
                    socket = new Socket(ipAddress, port);//创建连接地址和端口
                    if (socket != null) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // TODO Auto-generated method stub
                                Toast.makeText(MainActivity.this, "已成功连接！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 登陆监听，启动控制Activity（活动）
     */
    private OnClickListener LandingButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //if(true)
            String password = PasswordEditText.getText().toString();//得到输入框输入的密码

            if (socket != null)//连接成功
            {
                if (PasswordEditText.getText().toString().equals(string_mima))//密码正确
                {
                    editor = pref.edit();
                    if (rememberpass.isChecked()) {
                        editor.putBoolean("cb_mima", true);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.commit();
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(intent);

                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            // TODO Auto-generated method stub
                            Toast.makeText(MainActivity.this, "已连接，请输入正确密码！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(MainActivity.this, "请先连接！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    /**
     * 退出提示框
     */
    protected void dialog() {
        AlertDialog.Builder builder = new Builder(MainActivity.this);
        builder.setMessage("亲，确定要退出吗");
        builder.setTitle("提示");
        builder.setPositiveButton("确定",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MainActivity.this.finish();
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        builder.create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog();
            return false;
        }
        return false;
    }

    /**
     * 获取IMEI号，IESI号，手机型号
     */
    private void getInfo() {
        TelephonyManager mTm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        imei = mTm.getDeviceId();

        String aaaa = imei.substring(0, 6);//获取本手机的IESI号的前六位，本机的IESE的前六位为867489

        long_mima = Integer.parseInt(aaaa);//将IESI的前六位付给long_mima
        long_mima = long_mima / 3 + 666;//做运算当作登陆密码
        string_mima = "" + long_mima;

        //String imsi = mTm.getSubscriberId();
        //String mtype = android.os.Build.MODEL; // 手机型号
        //String mtyb= android.os.Build.BRAND;//手机品牌
        //String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得

        Log.i("text", "手机IMEI号：" + imei);
        Log.i("456", string_mima);
    }
}

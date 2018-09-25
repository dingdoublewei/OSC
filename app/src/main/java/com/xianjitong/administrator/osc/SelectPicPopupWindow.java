package com.xianjitong.administrator.osc;

/**
 * Created by Administrator on 2018/4/24.
 * 该程序实现点击主界面中Menu按钮弹出菜单的功能
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SelectPicPopupWindow extends Activity implements OnClickListener {

    private Button MEASURE_bt, ACQUIRE_bt, CH_bt, MATH_bt, MENU_bt;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstmenu);
        MEASURE_bt = (Button) this.findViewById(R.id.measure);
        ACQUIRE_bt = (Button) this.findViewById(R.id.acquire);
        CH_bt = (Button) this.findViewById(R.id.ch);
        MATH_bt = (Button) this.findViewById(R.id.math);
        MENU_bt = (Button) this.findViewById(R.id.menu);


        layout = (LinearLayout) findViewById(R.id.pop_layout);

        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        layout.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //添加按钮监听
        MEASURE_bt.setOnClickListener(this);
        ACQUIRE_bt.setOnClickListener(this);
        CH_bt.setOnClickListener(this);
        MATH_bt.setOnClickListener(this);
        MENU_bt.setOnClickListener(this);
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.measure:
                break;
            case R.id.acquire:
                break;
            case R.id.ch:
                break;
            case R.id.math:
                break;
            case R.id.menu:
                startActivity(new Intent(SelectPicPopupWindow.this, SecondMenu.class));
            default:
                break;
        }
        finish();
    }


}

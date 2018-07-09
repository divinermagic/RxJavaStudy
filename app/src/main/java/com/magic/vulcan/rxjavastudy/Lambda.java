package com.magic.vulcan.rxjavastudy;

import android.util.Log;

public class Lambda {

    public void test01(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG","jikexueyuan");
            }
        }).start();
    }

    public void testLambda(){
       new Thread(() ->Log.d("TAG","jikexueyuan")).start();
    }
}

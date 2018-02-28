package com.aderayanbima31.masigit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.aderayanbima31.masigit.preference.PrefManager;



public class SplashScreen extends Activity {
    protected void onCreate(Bundle savedInstanceState) {

        //private PrefManager prefManager;

        super.onCreate(savedInstanceState);
    setContentView(R.layout.splashscreen_activity);

    Thread timer = new Thread(){
        public void run(){
            try{
                sleep(2000);


            }
            catch(InterruptedException e)
            {
                e.printStackTrace();


            }
            finally{
                //prefManager.setFirstTimeLaunch(false);
                Intent startAct = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(startAct);

            }

        }

    };
    timer.start();



}

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }



}

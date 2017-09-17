package com.nubia.lijia;

import java.util.Timer;
import java.util.TimerTask;

import com.nubia.gameforparty.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Welcome extends Activity {
	 @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.welcome);  
	        
	        new Timer().schedule(new TimerTask() {  
	            @Override  
	            public void run() {  
	                startActivity(new Intent(Welcome.this,GreatAdventureActivity.class));  
	                finish();  
	                  
	            }  
	        }, 2000);
	    }  
	  
	} 

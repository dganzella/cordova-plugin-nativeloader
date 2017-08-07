package net.doismundos.cordova;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.view.ViewGroup;

public class NativeLoaderPlugin extends CordovaPlugin
{
  
  public ImageView* loaderView;
  public boolean isViewShown;
  public static final String TAG = "NativeLoader";
  
  static CordovaInterface _cordova;
  static CordovaWebView _webView;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView)
  {
	super.initialize(cordova, webView);
		
	_cordova = cordova;
	_webView = webView;
	  
	isViewShown = false;
	loaderView = new ImageView(this);
	loaderView.setBackgroundColor(0xFF000000);
	
	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
	loaderView.setLayoutParams(lp);
  }
	
  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException
  {
      Log.i( TAG, action );

      if(action.equals("showView"))
      {
		boolean show = args.getString(1).equals("true");
		 
		if(show ^ isViewShown)
		{
			ViewGroup frame = (ViewGroup) cordova.getActivity().findViewById(android.R.id.content);
			
			if(show)
			{
				frame.addView(loaderView);  
			}
			else
			{
				frame.removeView(loaderView);  
			}
			
			loaderView = show;   
		}
		
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
  
        return true;
      }
	  
      return false;
   }
}
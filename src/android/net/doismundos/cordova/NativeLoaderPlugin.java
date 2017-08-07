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
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.graphics.drawable.AnimationDrawable;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;

public class NativeLoaderPlugin extends CordovaPlugin
{
  
  public ImageView loaderView;
  public AnimationDrawable spinnerAnimation;
  public static final String TAG = "NativeLoader";
  
  static CordovaInterface _cordova;
  static CordovaWebView _webView;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView)
  {
	Log.i( TAG, "initialize Native Loader" );

	super.initialize(cordova, webView);
		
	_cordova = cordova;
	_webView = webView;
	
	FrameLayout layout = (FrameLayout) _webView.getView().getParent();

	loaderView = layout.findViewWithTag("nativeLoaderView");
	
	if(loaderView == null)
	{
		loaderView = new ImageView(cordova.getActivity().getApplicationContext());
		loaderView.setTag("nativeLoaderView");
		loaderView.setBackgroundColor(0x00FFFFFF);

		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams( FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT );
		lp.gravity = Gravity.CENTER;
		loaderView.setLayoutParams(lp);
		loaderView.setBackgroundResource(getAppResource("preloader","drawable"));	
	}

  	spinnerAnimation = (AnimationDrawable) loaderView.getBackground();
  }

  private int getAppResource(String name, String type) {
    return cordova.getActivity().getResources().getIdentifier(name, type, cordova.getActivity().getPackageName());
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException
  {
      Log.i( TAG, action );

      if(action.equals("showView"))
      {
		boolean show = args.getString(0).equals("true");

		if(show)
		{
			this.cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					FrameLayout layout = (FrameLayout) _webView.getView().getParent();
					layout.addView(loaderView);
					loaderView.bringToFront();
					spinnerAnimation.start();
				}
			}); 
		}
		else
		{
			this.cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					FrameLayout layout = (FrameLayout) _webView.getView().getParent();
					layout.removeView(loaderView);
					spinnerAnimation.stop();
				}
			}); 
		}
		
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
  
        return true;
      }
	  
      return false;
   }
}
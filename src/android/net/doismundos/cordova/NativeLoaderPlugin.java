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

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Interpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.BounceInterpolator;

public class NativeLoaderPlugin extends CordovaPlugin
{
  
  public ImageView loaderView, bgView;
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

	loaderView = (ImageView)layout.findViewWithTag("nativeLoaderView");
	bgView = (ImageView)layout.findViewWithTag("bgView");

	if(loaderView == null)
	{
		loaderView = new ImageView(cordova.getActivity().getApplicationContext());
		loaderView.setTag("nativeLoaderView");
		loaderView.setBackgroundColor(0x00FFFFFF);

		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams( FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT );
		lp.gravity = Gravity.CENTER;
		loaderView.setLayoutParams(lp);
		loaderView.setBackgroundResource(getAppResource("preloader","drawable"));	

		bgView = new ImageView(cordova.getActivity().getApplicationContext());
		bgView.setTag("bgView");
		bgView.setBackgroundColor(0xFFFFFFFF);
		bgView.setLayoutParams(layout.getLayoutParams());
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
					layout.addView(bgView);
					layout.addView(loaderView);
					loaderView.bringToFront();
					spinnerAnimation.start();

					Animation scaleIn = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
					
					Animation fadeIn = new AlphaAnimation(0, 1);

					AnimationSet animation = new AnimationSet(true);

					animation.addAnimation(scaleIn);
					animation.addAnimation(fadeIn);
					animation.setDuration(600);

					animation.setInterpolator(new BounceInterpolator());

					loaderView.startAnimation(animation);	
				}
			}); 
		}
		else
		{
			this.cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {

					Animation scaleOut = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
					
					Animation fadeOut = new AlphaAnimation(1, 0);

					AnimationSet animation = new AnimationSet(true);

					animation.addAnimation(scaleOut);
					animation.addAnimation(fadeOut);
					animation.setDuration(600);

					animation.setInterpolator(new BounceInterpolator());
					loaderView.startAnimation(animation);

					new android.os.Handler().postDelayed(
					new Runnable() {
						public void run() {
							FrameLayout layout = (FrameLayout) _webView.getView().getParent();
							layout.removeView(loaderView);
							spinnerAnimation.stop();
						}
						}, 
					600);

					AnimationSet animationbg = new AnimationSet(true);
					Animation fadeOutDelayed = new AlphaAnimation(1, 0);
					fadeOutDelayed.setStartOffset(600);
					animationbg.addAnimation(fadeOutDelayed);
					animationbg.setDuration(1200);

					bgView.startAnimation(animationbg);

					new android.os.Handler().postDelayed(
					new Runnable() {
						public void run() {
							FrameLayout layout = (FrameLayout) _webView.getView().getParent();
							layout.removeView(bgView);
						}
						}, 
					1200);
				}
			}); 
		}
		
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
  
        return true;
      }
	  
      return false;
   }
}
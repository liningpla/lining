package com.fission.recordupload;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger.LogLevel;
import com.google.android.gms.analytics.Tracker;

import java.util.Map;

import gradlebuild.lining.com.recorduplod.R;

public class GoogleAnalyticsUtils {
	private static Tracker tracker;
	
	private static final String CATEGORY_BASE = "base";
	private static final String CATEGORY_USER = "user";
	private static final String CATEGORY_TASK = "task";
	private static final String CATEGORY_ENGINE = "engine";

	private static final String ACTION_SIGN_IN = "sign_in";
	private static final String ACTION_CHANGE_HEADER = "change_header";
//	private static final String ACTION_ENTER_TASK_SYSTEM = "enter_task_system";

	public static void init(Context context){
		if(tracker == null){
			GoogleAnalytics.getInstance(context).getLogger().setLogLevel(LogLevel.VERBOSE);
			tracker = GoogleAnalytics.getInstance(context).newTracker(R.xml.app_tracker);
			tracker.enableAutoActivityTracking(true);
		}
	}
	
	public static void setUserId(String userId){
		tracker.set("&uid", userId);
	}
	
	public static void onSignIn(){
		tracker.send(new HitBuilders.EventBuilder(CATEGORY_BASE, ACTION_SIGN_IN).build());
	}
	
	public static void onChangeHeader(){
		tracker.send(new HitBuilders.EventBuilder(CATEGORY_USER, ACTION_CHANGE_HEADER).build());
	}

	public static void onAnalyticEvent(BaseEvent event)
	{
		Map map = new HitBuilders.EventBuilder(event.getCategory(), event.getAction_type()).build();
		map.putAll(event.obtainMap());
		tracker.send(map);
	}



	/**
	 * <li/>error  0
	 * <li/>resume 1
	 * <li/>pause  2
	 * <li/>stop   3
	 * */
	public static void sendEngineStatus(int status)
	{
		String action = "";
		switch(status)
		{
			case 0:
				action = "engine_error";
				break;
			case 1:
				action = "engine_resume";
				break;
			case 2:
				action = "engine_pasue";
				break;
			case 3:
				action = "engine_stop";
				break;
		}
		tracker.send(new HitBuilders.EventBuilder(CATEGORY_ENGINE, action).build());
	}




}

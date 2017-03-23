package com.mrd.shopsup.controllers;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;


public abstract class Controller {
	
	public static final int MESSAGE_STOP_CURRENT_PROCESS = 1000;
	public static final int MESSAGE_ERROR = 1001;
	public static final int MESSAGE_SUCCESS = 1002;
	public static final int MESSAGE_NO_NETWORK = 1003;
	public static final int MESSAGE_NO_DATA = 1004;

	@SuppressWarnings("unused")
	private static final String TAG = Controller.class.getSimpleName();
	private final List<Handler> outboxHandlers = new ArrayList<Handler>();
	
	public abstract boolean handleMessage(int what,Object data);
	public boolean handleMessage(int what)
	{
		return handleMessage(what, null);
	}
	public void dispose() {}

	public final void addOutboxHandler(Handler handler) {
		outboxHandlers.add(handler);
	}

	public final void removeOutboxHandler(Handler handler) {
		outboxHandlers.remove(handler);
	}
	
	protected final void notifyOutboxHandlers(int what, int arg1, int arg2, Object obj) {
		if (!outboxHandlers.isEmpty()) {
			for (Handler handler : outboxHandlers) {
				Message msg = Message.obtain(handler, what, arg1, arg2, obj);
				msg.sendToTarget();
			}
		}
	
	}
	
	

}

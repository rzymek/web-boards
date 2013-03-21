package com.google.gwt.core.client;

import com.google.gwt.user.client.impl.WindowImpl;

public class GWTBridge extends com.google.gwt.core.shared.GWTBridge {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T create(Class<?> type) {
		if(WindowImpl.class.equals(type)) {
			return (T) new WindowImpl();
		}else{
			return null;
		}
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isClient() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void log(String arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}

}

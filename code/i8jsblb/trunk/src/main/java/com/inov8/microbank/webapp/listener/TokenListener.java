package com.inov8.microbank.webapp.listener;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.TOKEN_KEY;
import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.nextToken;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class TokenListener implements HttpSessionListener {
	
    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
    	
        HttpSession session = sessionEvent.getSession();
        String nextToken = nextToken();        
        session.setAttribute(TOKEN_KEY, nextToken);
    }

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		//todo
	}		
}

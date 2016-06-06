package com.devtence.will.dev.servlets;


import com.devtence.will.Constants;
import com.devtence.will.dev.notificators.Notificator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This servlet is used by GAE Push Queue to process all the notifications that are to be processed by the service.
 * The request must contain the notificator to be used and the parameters the notificator requires
 * Created by plessmann on 11/03/16.
 */
public class Notify extends HttpServlet {

	private static final Logger log = Logger.getLogger(Notify.class.getName());
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		try {
			String notificatorName = request.getParameter(Constants.NOTIFICATOR_KEY);
			if (notificatorName != null) {
				Class notificatorClass = Class.forName(notificatorName);
				if (notificatorClass != null) {
					Notificator notificator = (Notificator) notificatorClass.newInstance();
					if (notificator != null) {
						notificator.notify(request.getParameterMap());
					}
				}
			}
		} catch (Exception e) {
			log.log(Level.WARNING, Constants.ERROR, e);
		}
	}

}

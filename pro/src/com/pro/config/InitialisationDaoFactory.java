package com.pro.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.pro.dao.DAOFactory;

public class InitialisationDaoFactory implements ServletContextListener {

	private static final String ATT_DAO_FACTORY	= "daofactory";
	
	private DAOFactory daofactory;
	
	public void contextInitialized(ServletContextEvent event) {
		
		ServletContext servletContext = event.getServletContext();
		this.daofactory = DAOFactory.getInstance();
		servletContext.setAttribute(ATT_DAO_FACTORY, this.daofactory);
	}	

	public void contextDestroyed(ServletContextEvent arg0) {

	}

}

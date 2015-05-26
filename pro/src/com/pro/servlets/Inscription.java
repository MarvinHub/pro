package com.pro.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pro.beans.Utilisateur;
import com.pro.dao.DAOFactory;
import com.pro.dao.UtilisateurDAO;
import com.pro.forms.InscriptionForm;

public class Inscription extends HttpServlet {

	private static final long serialVersionUID 	= 1L;
	private static final String VUE 			= "/WEB-INF/inscription.jsp";
	private static final String ATT_FORM		= "form";
	private static final String ATT_USER		= "user";
	private static final String CONF_DAO_FACTORY= "daofactory";
	
	private UtilisateurDAO utilisateurDao;
	
	public void init() throws ServletException {
		
		this.utilisateurDao = ((DAOFactory)this.getServletContext().getAttribute(CONF_DAO_FACTORY)).getUtilisateurDao();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		this.getServletContext().getRequestDispatcher(VUE).forward(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		InscriptionForm form = new InscriptionForm(this.utilisateurDao);
		Utilisateur user = form.inscrireUtilisateur(req);
		
		req.setAttribute(ATT_FORM, form);
		req.setAttribute(ATT_USER, user);
		
		this.getServletContext().getRequestDispatcher(VUE).forward(req, resp);
	}
}

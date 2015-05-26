package com.pro.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pro.beans.Utilisateur;
import com.pro.cookies.LastConnectionCookie;
import com.pro.dao.DAOFactory;
import com.pro.dao.UtilisateurDAO;
import com.pro.forms.ConnexionForm;

public class Connexion extends HttpServlet {

	private static final long 	serialVersionUID 	= 1L;
	private static final String ATT_USER			= "utilisateur";
	private static final String ATT_FORM			= "form";
	private static final String ATT_SESSION			= "sessionUtilisateur";
	private static final String ATT_HOUR			= "horaire";
	private static final String VUE					= "/WEB-INF/connexion.jsp";
	private static final String CONF_DAO_FACTORY	= "daofactory";

	private LastConnectionCookie gestionSauvegardeConnexion;
	private UtilisateurDAO utilisateurDao;
	
	public void init() throws ServletException {
		this.gestionSauvegardeConnexion = new LastConnectionCookie();
		utilisateurDao = ((DAOFactory)getServletContext().getAttribute(CONF_DAO_FACTORY)).getUtilisateurDao();
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// Récupération du cookie de dernière connexion
		Cookie hourCookie = gestionSauvegardeConnexion.recupererCookie(req);
		
		// Transmission de la date de connexion à la vue
		if(hourCookie != null){
			req.setAttribute(ATT_HOUR, hourCookie.getValue());
		}
		
		this.getServletContext().getRequestDispatcher(VUE).forward(req, resp);
	}
	
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// Validation des données de connexion 
		ConnexionForm form = new ConnexionForm();
		Utilisateur user = form.connecterUtilisateur(req, utilisateurDao);
		HttpSession session = req.getSession();
		
		// Création de la session utilisateur si connexion réussie
		if(form.getErreurs().isEmpty()){
			session.setAttribute(ATT_SESSION, user);
			gestionSauvegardeConnexion.enregistrerCookie(req, resp);			
		} else {
			// Echec de la connexion
			session.setAttribute(ATT_SESSION, null);
		}
		
		req.setAttribute(ATT_USER, user);
		req.setAttribute(ATT_FORM, form);
		
		this.getServletContext().getRequestDispatcher(VUE).forward(req, resp);
	}

}

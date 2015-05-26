package com.pro.cookies;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

public class LastConnectionCookie {

	private static final String COOKIE_LAST_TIME	= "heureConnexion";
	private static final int	COOKIE_MAX_AGE		= 60*60*24*365;
	private static final String CHAMP_SAVE			= "lastconnect";
	
	// Récupération du cookie contenant la dernière date de connexion si il existe
	public Cookie recupererCookie(HttpServletRequest req){
		Cookie[] reqCookies = req.getCookies();
		Cookie hourCookie = null;
		
		for(Cookie cookie : reqCookies){
			if(cookie.getName().equals(COOKIE_LAST_TIME))
				hourCookie = cookie;
		}
		
		return hourCookie;
	}
	
	// Création d'un cookie d'enregistrement de la dernière heure de connexion si l'utilisateur à coché la case SAVE
	public void enregistrerCookie(HttpServletRequest req, HttpServletResponse resp){
		if(req.getParameter(CHAMP_SAVE) != null){
			DateTime heureConnexion = new DateTime();
			Cookie cookie = new Cookie(COOKIE_LAST_TIME, heureConnexion.toString());
			cookie.setMaxAge(COOKIE_MAX_AGE);
			resp.addCookie(cookie);
		} else {
			// Si case SAVE non cochée invalidation du cookie
			Cookie cookie = new Cookie(COOKIE_LAST_TIME, "");
			cookie.setMaxAge(0);
			resp.addCookie(cookie);
		}
	}
}

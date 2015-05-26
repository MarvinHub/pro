package com.pro.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RestrictionFilter implements Filter {

	public static final String VUE_CONNEXION	= "/connexion";
	public static final String ATT_SESSION_USER	= "sessionUtilisateur";

	
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession();
		
		// Non-filtrage des ressources statiques et de la page d'inscription
        String chemin = request.getRequestURI().substring( request.getContextPath().length() );
        if ( chemin.startsWith( "/inc" ) || chemin.startsWith("/inscription")) {
            chain.doFilter( request, response );
            return;
        }
		
        // Restriction d'accès à la ressource si l'utilisateur n'a pas d'objet en session
		if(session.getAttribute(ATT_SESSION_USER) == null){
			request.getRequestDispatcher(VUE_CONNEXION).forward(request, response);
		} else {
			chain.doFilter(req, resp);
		}
	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
}

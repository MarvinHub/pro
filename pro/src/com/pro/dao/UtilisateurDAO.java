package com.pro.dao;

import com.pro.beans.Utilisateur;

public interface UtilisateurDAO {
	
	void creer(Utilisateur utilisateur) throws DAOException;
	Utilisateur trouver(String email) throws DAOException;
}

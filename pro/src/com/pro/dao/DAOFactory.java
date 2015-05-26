package com.pro.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DAOFactory {

    private static final String FICHIER_PROPERTIES       = "/com/pro/dao/dao.properties";
    private static final String PROPERTY_URL             = "url";
    private static final String PROPERTY_DRIVER          = "driver";
    private static final String PROPERTY_NOM_UTILISATEUR = "nomutilisateur";
    private static final String PROPERTY_MOT_DE_PASSE    = "motdepasse";
    
    private String url;
    private String username;
    private String password;
    
    DAOFactory( String url, String username, String password){  	
    	this.url = url;
    	this.username = username;
    	this.password = password;
    }
    
    // Initialise un DAOFactory avant de le retourner
    public static DAOFactory getInstance() throws DAOConfigurationException {
    	
    	Properties properties = new Properties();
    	String url, driver, nomUtilisateur, mdp;
    	
    	/* Lecture des propriétés de connexion à la BDD dans le FICHIER_PROPERTIES */
    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    	InputStream fichierProperties = classLoader.getResourceAsStream(FICHIER_PROPERTIES);
    	
    	if(fichierProperties == null){
    		throw new DAOConfigurationException("Le fichier properties '" + FICHIER_PROPERTIES + "' n'a pas pu être chargé.");
    	}
    	
		try {
			properties.load(fichierProperties);
			url = properties.getProperty(PROPERTY_URL);
			driver = properties.getProperty(PROPERTY_DRIVER);
			nomUtilisateur = properties.getProperty(PROPERTY_NOM_UTILISATEUR);
			mdp = properties.getProperty(PROPERTY_MOT_DE_PASSE);
		} catch (IOException e) {
			throw new DAOConfigurationException("Fichier properties illisible.", e);
		}
		
		// Chargement du driver JDBC
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new DAOConfigurationException("Driver JDBC introuvable dans le classpath.", e);
		}
    
		DAOFactory instance = new DAOFactory(url, nomUtilisateur, mdp);
		return instance;
    }
    
    
    // Récupération d'une connexion active à la BDD
    Connection getConnection() throws SQLException{
    	return DriverManager.getConnection(url, username, password);
    }
    
    public UtilisateurDAO getUtilisateurDao(){
    	return new UtilisateurDaoImpl(this);
    }

}

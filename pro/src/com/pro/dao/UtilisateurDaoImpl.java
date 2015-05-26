package com.pro.dao;

import static com.pro.dao.DAOUtilitaire.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.pro.beans.Utilisateur;

public class UtilisateurDaoImpl implements UtilisateurDAO {

	
	private final static String SQL_CREATE			= "INSERT INTO Utilisateur(email, mot_de_passe, nom, date_inscription) VALUES(?, ?, ?, NOW())";
	private final static String SQL_READ_BY_EMAIL	= "SELECT * FROM Utilisateur WHERE email = ?";
			
	DAOFactory daoFactory;
	
	public UtilisateurDaoImpl(DAOFactory daofactory){
		this.daoFactory = daofactory;
	}
	
	public void creer(Utilisateur utilisateur) throws DAOException {
		
		Connection connexion = null;
		PreparedStatement ppStmt = null;
		ResultSet cles = null;
		
		try {
			// Initialisation de la requête préparée avec les données du bean Utilisateur
			connexion = daoFactory.getConnection();
			ppStmt = connexion.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
			ppStmt.setString(1, utilisateur.getEmail());
			ppStmt.setString(2, utilisateur.getMotDePasse());
			ppStmt.setString(3, utilisateur.getNom());
			
			// Exécution de la requête et vérification du succès
			int nbInsert = ppStmt.executeUpdate();	
			if(nbInsert == 0){
				throw new DAOException("Impossible d'insérer l'utilisateur dans la BDD.");
			}
			
			// Récupération de l'ID de l'utilisateur et maj du bean
			cles = ppStmt.getGeneratedKeys();
			if(cles.next()){
				utilisateur.setId(cles.getLong(1));
			} else {
				throw new DAOException("Echec de la création de l'utilisateur en base, aucun ID auto-généré retourné.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(cles, ppStmt, connexion);
		}
		
	
	}

	
	public Utilisateur trouver(String email) throws DAOException {
		
		Connection connexion = null;
		PreparedStatement ppStmt = null;
		ResultSet result = null;
		Utilisateur user = null;
		
		try{
			// Préparation et exécution de la requête
			connexion = daoFactory.getConnection();
			ppStmt = connexion.prepareStatement(SQL_READ_BY_EMAIL);
			ppStmt.setString(1, email);
			result = ppStmt.executeQuery();
			
			// Mapping du résultat dans l'objet Utilisateur ou renvoie null
			if(result.next()){
				user = map(result);
			}
		} catch(SQLException e){
			throw new DAOException(e);
		} finally{
			fermeturesSilencieuses(result, ppStmt, connexion);
		}
		
		return user;
	}

	// Mapping d'un objet Utilisateur avec un resultSet
	private static Utilisateur map( ResultSet resultSet ) throws SQLException {
	    Utilisateur utilisateur = new Utilisateur();
	    utilisateur.setId( resultSet.getLong( "id" ) );
	    utilisateur.setEmail( resultSet.getString( "email" ) );
	    utilisateur.setMotDePasse( resultSet.getString( "mot_de_passe" ) );
	    utilisateur.setNom( resultSet.getString( "nom" ) );
	    utilisateur.setDateInscription( resultSet.getTimestamp( "date_inscription" ) );
	    return utilisateur;
	}
}

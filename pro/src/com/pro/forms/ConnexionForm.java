package com.pro.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import com.pro.beans.Utilisateur;
import com.pro.dao.UtilisateurDAO;

public class ConnexionForm {

	private static final String CHAMP_EMAIL		= "email";
	private static final String CHAMP_PASS		= "motdepasse";
	private static final String ALGO_CHIFFREMENT = "SHA-256";
	
	private String resultat;
	private Map<String, String> erreurs 		= new HashMap<String,String>();
	
	public Utilisateur connecterUtilisateur(HttpServletRequest req, UtilisateurDAO utilisateurDao){
		
		Utilisateur user = new Utilisateur();
		String email = getValeurChamp(req, CHAMP_EMAIL);
		String pass = getValeurChamp(req, CHAMP_PASS);
		
		
		/* Validation champs d'entrées */
		try{
			validationEmail(email);
		} catch(FormValidationException e){
			setErreur(CHAMP_EMAIL, e.getMessage());
		}
		user.setEmail(email);
		
		try{
			
			validationMotDePasse(pass);
		} catch(FormValidationException e){
			setErreur(CHAMP_PASS, e.getMessage());
		}
		user.setMotDePasse(pass);
		
		/* Validation des données par rapport à l'utilisateur de référence en base */
		Utilisateur userReference = null;
		if(!erreurs.containsKey(CHAMP_EMAIL)){
			userReference = utilisateurDao.trouver(email);
		}
		
		if(userReference == null){
			setErreur(CHAMP_EMAIL, "Utilisateur introuvable.");
		} else if(!comparerMotDePasse(pass, userReference.getMotDePasse())){
			setErreur(CHAMP_PASS, "Mot de passe incorrect");
		}
		
		if(erreurs.isEmpty()){
			resultat="Connexion réussie.";
		} else {
			resultat="Echec de la connexion.";
		}
		
		return user;
	}
	
	/* Getters and Setters */
	public String getResultat(){
		return resultat;
	}
	
	public Map<String, String> getErreurs() {
		return erreurs;
	}
	
	public void setErreur(String champ, String msg){
		erreurs.put(champ, msg);
	}
	
	/* Méthodes de validation */
	private void validationEmail(String email) throws FormValidationException{
		if(email != null){
			if (!email.matches("([^.@]+)(\\.[^.@])*@([^.@]+\\.)+([^.@]+)")){
				throw new FormValidationException("Merci de saisir une adresse mail valide.");
			}
		} else {
			throw new FormValidationException("Merci de saisir une adresse mail");
		}
	}
	
    private void validationMotDePasse( String motDePasse ) throws FormValidationException {
        if ( motDePasse != null ) {
            if ( motDePasse.length() < 3 ) {
                throw new FormValidationException( "Le mot de passe doit contenir au moins 3 caractères." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir votre mot de passe." );
        }
    }
	
	private static String getValeurChamp(HttpServletRequest req, String champ){
		String value = req.getParameter(champ);
		if(value == null || value.trim().length() == 0){
			return null;
		} else {
			return value.trim();
		}
	}
	
	
	// Méthode de vérification de MDP avec Jasypt
	private boolean comparerMotDePasse(String password, String encryptedPassword){
		ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
	    passwordEncryptor.setAlgorithm(ALGO_CHIFFREMENT);
	    passwordEncryptor.setPlainDigest(false);
	    
	    return passwordEncryptor.checkPassword(password, encryptedPassword);
	}
}

package com.pro.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import com.pro.beans.Utilisateur;
import com.pro.dao.DAOException;
import com.pro.dao.UtilisateurDAO;

public class InscriptionForm {

	private static final String CHAMP_EMAIL		= "email";
	private static final String CHAMP_PASS		= "motdepasse";
	private static final String CHAMP_CONF		= "confirmation";
	private static final String CHAMP_NOM		= "nom";
	private static final String ALGO_CHIFFREMENT = "SHA-256";
	
	private String resultat;
	private Map<String, String> erreurs = new HashMap<String, String>();
	private UtilisateurDAO utilisateurDao;
	
	public InscriptionForm(UtilisateurDAO utilisateurDao){
		this.utilisateurDao = utilisateurDao;
	}
	
	public Utilisateur inscrireUtilisateur(HttpServletRequest req){
		
		String email = getValeurChamp(req, CHAMP_EMAIL);
		String mdp = getValeurChamp(req, CHAMP_PASS);
		String confirmation = getValeurChamp(req, CHAMP_CONF);
		String nom = getValeurChamp(req, CHAMP_NOM);
		Utilisateur utilisateur = new Utilisateur();
	
		/* Validation des données envoyées */
		try{
			validationEmail(email);
		} catch(FormValidationException e){
			setErreur(CHAMP_EMAIL, e.getMessage());
		}
		utilisateur.setEmail(email);
	
		try{
			validationMotsDePasse(mdp, confirmation);
		} catch(FormValidationException e){
			setErreur(CHAMP_PASS, e.getMessage());
			setErreur(CHAMP_CONF, null);
		}
		utilisateur.setMotDePasse(encrypterMotDePasse(mdp));
		
		try{
			validationNom(nom);
		} catch(FormValidationException e){
			setErreur(CHAMP_NOM, e.getMessage());
		}
		utilisateur.setNom(nom);
		
		try{
			if(erreurs.isEmpty()){
				// Stockage de l'utilisateur
				utilisateurDao.creer(utilisateur);
				resultat = "Inscription réussie.";
			} else {
				resultat = "Echec de l'inscription.";
			} 
		} catch (DAOException e){
			resultat = "Echec de l'inscription : merci de réessayer dans quelques instants.";
			e.printStackTrace();
		}
		
		return utilisateur;
	}
	
	
	/* Getters and Setters */
	private void setErreur(String champ, String msg){
		erreurs.put(champ, msg);
	}
	
	public Map<String, String> getErreurs(){
		return erreurs;
	}
	
	public String getResultat(){
		return resultat;
	}
	
	
	/* Méthode de récupération */
	private String getValeurChamp(HttpServletRequest req, String nomChamp){
		
		String valeur = req.getParameter(nomChamp);
		if(valeur == null || valeur.trim().length() == 0){
			return null;
		} else {
			return valeur.trim();
		}
	}
	
	
	/* Méthodes de validation */
	private void validationEmail(String email) throws FormValidationException {
		
		if(email != null){
			if (!email.matches("([^.@]+)(\\.[^.@])*@([^.@]+\\.)+([^.@]+)")){
				throw new FormValidationException("Merci de saisir une adresse mail valide.");
			} else if (utilisateurDao.trouver(email) != null) {
				throw new FormValidationException("Cette adresse mail est déjà utilisée, merci d'en saisir une autre");
			}
		} else {
			throw new FormValidationException("Merci de saisir une adresse mail valide.");
		}
		
	}
	
	private void validationMotsDePasse(String mdp, String confirmation) throws FormValidationException {
		
		if(mdp != null && confirmation != null){
			if(!mdp.equals(confirmation)){
				throw new FormValidationException("Les mots de passe sont différents, merci de les saisir à nouveau.");
			} else if (mdp.trim().length() < 3){
				throw new FormValidationException("Les mots de passe doivent contenir au moins 3 caractères.");
			}
		} else {
			throw new FormValidationException("Merci de saisir et confirmer votre mot de passe");
		}
	}
	
	private void validationNom(String nom) throws FormValidationException {
		
		if ( nom != null && nom.length() < 3 ) {
	        throw new FormValidationException( "Le nom d'utilisateur doit contenir au moins 3 caractères." );
	    }
	}
	
	
	// Cryptage du MDP avec la bibliothèque Jasypt en SHA-256
	private String encrypterMotDePasse(String password){		
		ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
	    passwordEncryptor.setAlgorithm(ALGO_CHIFFREMENT);
	    passwordEncryptor.setPlainDigest(false);
	    String motDePasseChiffre = passwordEncryptor.encryptPassword(password);

	    return motDePasseChiffre;
	}
}

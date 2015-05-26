# pro
Mise en oeuvre des mécanismes de connexion sur un site Web
- Connexion/Deconnexion avec création/suppression de la session utilisateur
- Validation des données envoyées par formulaire par des objets métier 
- Validation de la cohérence des données avec la BDD (inscription/connexion)
- Gestion des accès en base par des Data Object Access
- Restriction d'accès au site(si non connecté) grâce à l'utilisation d'un filtre
- Enregistrement de la date de dernière connexion dans un Cookie

Utilisation d'une base SQL dont le schéma est le suivant :
CREATE DATABASE bdd_sdzee DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE USER 'java'@'localhost' IDENTIFIED BY 'SdZ_eE';
GRANT ALL ON bdd_sdzee.* TO 'java'@'localhost' IDENTIFIED BY 'SdZ_eE';

CREATE TABLE `utilisateur` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(60) NOT NULL,
  `mot_de_passe` char(56) NOT NULL,
  `nom` varchar(20) NOT NULL,
  `date_inscription` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

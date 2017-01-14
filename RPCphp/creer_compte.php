<?php
	/* ETAPE 0 : TINCLUDE DE FONCTIONS ET PARAMETRAGE */
	$GLOBALS['json']=1;
	include('inc/erreurs.inc');



	/* ETAPE 1 : TEST DES PARAMETRES */
	if (!isset($_POST['pseudo']) || empty($_POST['pseudo'])){
		RetournerErreur(100);
	}
	if (!isset($_POST['mdp']) || empty($_POST['mdp'])){
		RetournerErreur(110);
	}
	$pseudo = $_POST['pseudo'];
	$mdp = $_POST['mdp'];



	/* ETAPE 2 : CONNEXION A LA BASE DE DONNEES */
	include('inc/db.inc');



	/* ETAPE 3 : VERIF LOGIN DEJA EXISTANT */
	try{
		$requete="SELECT count(*) AS nbr FROM utilisateurs
				  WHERE pseudo=?";
		$stm= $bdd->prepare($requete);
		$stm->execute(array($pseudo));
		$row = $stm->fetch();
	}catch(Exception $e){
		RetournerErreur(2001);
	}
	if($row['nbr'] != 0){
		RetournerErreur(200);
	}



	/* ETAPE 4 : SAUVEGARDE DANS LA DB */
	try{
		$requete="INSERT INTO utilisateurs (pseudo, mdp) VALUES (?, ?)";
		$stm= $bdd->prepare($requete);
		$stm->execute(array($pseudo, $mdp));
	}catch(Exception $e){
		RetournerErreur(2002);
	}

	/* ETAPE 5 : PRENDRE L'ID DE L'UTILISATEUR CREE DANS LA DB */
	try {
		$requete="SELECT id_utilisateur FROM utilisateurs
					WHERE pseudo = ? AND mdp = ?";
		$stm= $bdd->prepare($requete);
		$stm->execute(array($pseudo, $mdp));
		$row = $stm->fetch();
	} catch (Exception $e) {
		RetournerErreur(2003);
	}

	/* ETAPE 6 : SI ON EST ARRIVE JUSQU'ICI, C'EST QUE TOUT EST CORRECT */
	$resultat = '{"code": 0, "id": ' . $row['id_utilisateur'] . '}';
	echo $resultat;

	/* Valeurs de retour en format JSON
	 * 00 : OK
	 * 100 : problème $_POST['pseudo']
	 * 110 : problème $_POST['mdp']
	 * 200 : pseudo déjà existant
	 * 1000 : problème de connexion à la DB
	 * 20XX : autre problème
	 */
?>

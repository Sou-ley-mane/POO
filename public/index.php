<?php
//Composer : outil de gestion de dependance (Gsetionnaire de dépendance)
//Gestionnaire===>
//Dependance==>Toute classe externe qu'on peut ajouter dans notre projet
//hub ==> site regroupant les dependance les dependance du laguages (Exemple en php packagist)
 //autoLaording==>Chargement automatique des classes
//nameSpace==>Package
//Afficer les erreurs sur le navigateur

use App\Model\Attache;
use App\Model\Personne;
use App\Model\Professeur;

require_once("../vendor/autoload.php");
require_once("../core/fonctions.php");
// dd(Professeur::findAll());
afficheErreur();
require_once("../routes/routes.web.php");












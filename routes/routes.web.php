<?php

use App\Controller\AttacheController;
use App\Controller\ProfesseurController;
use App\Controller\EtudiantController;
use App\Controller\ClasseController;
use App\Controller\ResponsablePedagoController;
use App\Controller\SecurityController;
use App\Core\Router;
use App\Exception\RoueNotFound;

$router=new Router();
//Definition des routes
$router->route('/login',[SecurityController::class,"authentification"]);
$router->route('/logout',[SecurityController::class,"deconnexion"]);
// *************************************************************
$router->route('/classes',[ClasseController::class,"ListeClasse"]);
// **************************************************************
$router->route('/etudiant',[EtudiantController::class,"formulerDemande"]);
$router->route('/etudiants',[EtudiantController::class,"liste"]);
$router->route('/inscrire',[EtudiantController::class,"inscrire"]);
// ***************************************************************
// $router->route('/etudiants',[EtudiantController::class,"liste"]);
$router->route('/professeur',[ProfesseurController::class,"affecterClasse"]);
$router->route('/professeur',[ProfesseurController::class,"ListerProfesseur"]);
// **********************************************************************
$router->route('/attache',[AttacheController::class,"listeAttache"]);
$router->route('/ajout',[ResponsablePedagoController::class,"ajouterAtt"]);


//Resolution des routes
try {
    $router->resolve();
} catch (RoueNotFound $e) {
  echo  $e->message;
}


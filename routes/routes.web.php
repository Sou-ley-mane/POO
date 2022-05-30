<?php

use App\Controller\AttacheController;
use App\Controller\ProfesseurController;
use App\Controller\EtudiantController;
use App\Controller\ClasseController;
use App\Controller\ResponsablePedagoController;
use App\Controller\SecurityController;
use App\Controller\DemandeController;
use App\Controller\ModuleController;

use App\Core\Router;
use App\Exception\RoueNotFound;

$router=new Router();
// $router->route('/ins',[InscriptionController::class,"valide"]);

// $router->route('/ins',[InscriptionController::class,"inscrire"]);
//Definition des routes
$router->route('/ins',[SecurityController::class,"inscrire"]);

$router->route('/login',[SecurityController::class,"authentification"]);
$router->route('/deconnexion',[SecurityController::class,"deconnexion"]);
$router->route('/',[SecurityController::class,"pageLogin"]);

// *************************************************************
$router->route('/classes',[ClasseController::class,"ListeClasse"]);
// **************************************************************
$router->route('/etudiant',[EtudiantController::class,"formulerDemande"]);
$router->route('/etudiants',[EtudiantController::class,"liste"]);
// ***************************************************************
// $router->route('/etudiants',[EtudiantController::class,"liste"]);
$router->route('/professeur',[ProfesseurController::class,"affecterClasse"]);
$router->route('/professeur',[ProfesseurController::class,"ListerProfesseur"]);
$router->route('/detail',[ProfesseurController::class,"detail"]);
$router->route('/affecter',[ProfesseurController::class,"AffecterClasseModules"]);
$router->route('/ajoutModule',[ModuleController::class,"ajouterModule"]);
$router->route('/demande',[DemandeController::class,"demande"]);
$router->route('/listDemande',[DemandeController::class,"demandesEtudiants"]);



// **********************************************************************
$router->route('/attache',[AttacheController::class,"listeAttache"]);
$router->route('/Supprimer',[AttacheController::class,"delete"]);

$router->route('/ajout',[ResponsablePedagoController::class,"ajouterAtt"]);


//Resolution des routes
try {
    $router->resolve();
} catch (RoueNotFound $e) {
  echo  $e->message;
}

// public function inscrire(){
    // 
    // }


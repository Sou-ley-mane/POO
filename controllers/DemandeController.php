<?php
namespace App\Controller;

use App\Core\Controller;
use App\Model\Demande;
use App\Model\Personne;

class DemandeController extends Controller{

public function demande(){
    if ($this->request->isGet()) {
        $nom=$_SESSION["user-connect"]->nom_complet;
        $login=$_SESSION["user-connect"]->login;

        $this->render("etudiant/demande.html.php",[
            "nom"=>$nom,
            "login"=>$login
        ]);
     
   }else{

    if ($_POST ["objet"]=="" || $_POST["date"]=="" || $_POST["message"]=="") {
       dd("Merci de remplir les champs");
    }
  //    dd($_POST);
    //    extract($_POST);
    $demande =new Demande($_POST ["objet"],$_POST["date"],$_SESSION["user-connect"]->id,$_POST["message"]);
    $demande->insert();
    $this->redirectToRoute("demande");
 //    echo "Traitement demande"
   }

}
  
public function demandesEtudiants(){
  $lesDemandes=Demande::findAll();
  // dd($lesDemandes);
  $i=0;
  $this->render("demande/demandelist.html.php",[
      "lesDemandes"=> $lesDemandes,
      "titre"=>"LES DEMANDES DES ETUDIANTS",
      "num"=>$i
  ]);
  // echo "Lister les demandes des etudiants";
}
}






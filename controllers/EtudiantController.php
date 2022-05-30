<?php
namespace App\Controller;
use App\Core\Controller;
use App\Model\Classe;
use App\Model\Etudiant;

class EtudiantController extends Controller{

public function formulerDemande(){
 
}



public function liste(){
    // if ($this->role->isConnect()) {
        $etudiant=Etudiant::findAll();
       

        $i=0;
        $this->render("etudiant/etudiant.html.php",[
            "etudiant"=> $etudiant,
            "titre"=>"LISTE DES ETUDIANTS",
            "num"=>$i
        ]);
    // }
    // $this->render("security/login.html.php");
    // dd("affichage du formulaire");
    // echo "VOUS AVEZ ICI LA LISTE DES ETUDIANTS";
    }


    
}
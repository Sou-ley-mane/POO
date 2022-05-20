<?php
namespace App\Controller;

use App\Core\Controller;
use App\Model\Professeur;

class ProfesseurController extends Controller{

    public function affecterClasse(){

        echo "affecter une classe a un prof";
    }

    public function ListerProfesseur(){
       
            $listeProfs=Professeur::findAll();
            $titre="LISTE DES PROFESSEURS";
            $numero=0;
            $data=[
                "titre"=>$titre,
                "listeProf"=>$listeProfs,
                "numero"=>$numero

            ];
            $this->render("professeur/professeur.html.php",$data);
        // dd("affichage du formulaire");
       }
       
    }


<?php
namespace App\Controller;

use App\Core\Controller;
use App\Model\Professeur;
use App\Model\Classe;
use App\Model\Modules;

class ProfesseurController extends Controller{

    public function affecterClasse(){

        echo "affecter une classe a un prof";
    }

    public function ListerProfesseur(){
       
            $listeProfs=Professeur::findAll();
            // dd($listeProfs);
            $titre="PROFESSEUR";
            $numero=0;
            $data=[
                "titre"=>$titre,
                "listeProf"=>$listeProfs,
                "numero"=>$numero

            ];
            $this->render("professeur/professeur.html.php",$data);
        // dd("affichage du formulaire");
       }


       public function detail($id){
        //    dd($id);
        $info=Professeur::findById($id);
        $classe=Classe::findAll();
        $module=Modules::findAll();
        // dd($module);
        // dd($info);
        $titre="INFORMATION DE ".strtoupper($info->nom_complet);
        $login="Adresse Mail : " .$info->login;
        $grade="Grade : " .$info->grade;
        $id=$info->id;


        $data=[
            "titre"=>$titre,
            "login"=>$login,
            "grade"=>$grade,
            "id"=>$id,
            "classes"=>$classe,
            "modules"=>$module
        ];
        $this->render("professeur/detail.html.php",$data);

        // dd($id);
       }

       public function AffecterClasseModules(){
// dd($_POST);
extract($_POST);
$moduleProf=new Modules($module,1);
$moduleProf->moduleProf($module,1);
// dd("Affecter des classes et des modules aux professeurs");
       }
       
    }


<?php
namespace App\Controller;
use App\Core\Controller;
use App\Model\Classe;
use App\Model\Etudiant;

class EtudiantController extends Controller{

public function formulerDemande(){
echo "je formle une demande";

}

public function inscrire(){

    if ($this->request->isGet()) {
        $this->render("etudiant/inscrireEtudiant.html.php");
        // $this->render("security/login.html.php");
    // dd("affichage du formulaire");
   }else{
// die("dans le post");
// var_dump($_POST);
// die;
       (extract($_POST));
       $etudInscrit=new Etudiant();
    //    $classe=new Classe();
       $etudInscrit->setNomComplet($nomcomplet);
       $etudInscrit->setLogin($login);
       $etudInscrit->setMatricule($matricule);
       $etudInscrit->setAdresse($adresse);
       $etudInscrit->setSexe($sexe);
       $etudInscrit->setPassword($password);


    //    $etudInscrit-> $classe->setFiliere($filiere);
    //    $etudInscrit-> $classe->setNiveau($niveau);
    //    $etudInscrit-> $classe->getId($classe);
$etudInscrit->insert();
$this->render("etudiant/inscrireEtudiant.html.php");
    //    $etudInscrit->setNomComplet($nomcomplet);
    //    $etudInscrit->setNomComplet($nomcomplet);
    //    $etudInscrit->setNomComplet($nomcomplet);

     
       

    // echo "je formle une demande";
    
    }

}

public function liste(){
    
        $etudiant=Etudiant::findAll();
        $i=0;
        $this->render("etudiant/etudiant.html.php",[
            "etudiant"=> $etudiant,
            "titre"=>"LISTE DES ETUDIANTS",
            "num"=>$i
        ]);
    // dd("affichage du formulaire");
    // echo "VOUS AVEZ ICI LA LISTE DES ETUDIANTS";
    }


    
}
<?php
namespace App\Controller;

use App\Core\Controller;
use App\Core\Role;
use App\Core\Session;
use App\Model\Annee;
use App\Model\Attache;
use App\Model\Classe;
use App\Model\Etudiant;
use App\Model\Inscription;
use App\Model\User;

class SecurityController extends Controller{

    public function deconnexion(){
        unset($_SESSION["user-connect"]);
        session_destroy();
        $this->render("security/login.html.php");
   }
   

    public function authentification(){
          // 1-Affichage du formulaire ==>GET
         if ($this->request->isGet()) {
            //  dd("merci");
                $this->render("security/login.html.php");
                //   dd("affichage du formulaire");
            }else{
                // dd($_POST);
                (extract($_POST));
                if ($login!="" && $password!="") {
                    $user=User::findUserByLoginAndPassword($login,$password);
                    if ($user) {
                        $this->session->set("user-connect",$user);
                        // dd(Session::getRoleUserSession()); 
                        // dd( Session::getRoleUserSession());

                        //  dd($_SESSION);
                         $this->redirectToRoute("etudiants");   
                          
                 }else{
                     
                $this->redirectToRoute("login");

                 }
                 
             }else{
                $this->redirectToRoute("login");

                //  echo "Veillez remplir les champs";
             }
          

         }

            
    }

  

   public function pageLogin(){
    $this->render("security/login.html.php");

   }

  public function inscrire(){
    //   echo "inscrire";

    //   dd("affichage du formulaire");

          if ($this->request->isGet()) {
              $classe=Classe::findAll();
              $annees=Annee::findAll();
              $attache=Attache::findAll();
              extract($annees);
              extract($classe);
              extract($attache);
            //   dd($classe[0]->libelle);
              $this->render("inscription/inscrireEtudiant.html.php",[
                  "classes"=>$classe,
                  "annees"=>$annees,
                  "attache"=>$attache
                ]);
              // $this->render("security/login.html.php");
         }else{
    //  dd($_POST);
             (extract($_POST));
             $etudInscrit=new Etudiant();
             $etudInscrit->setNomComplet($nomcomplet);
             $etudInscrit->setLogin($login);
             $etudInscrit->setMatricule($matricule);
             $etudInscrit->setPassword($password);
             $etudInscrit->setAdresse($adresse);
             $etudInscrit->setSexe($sexe);
            //  $etudInscrit->se($sexe);

            $id=$etudInscrit->insert();
            
            // $id=lastInsertId();
             if ($id>0) {
                 $inscrition=new Inscription($annee, $classe,$_SESSION["user-connect"]->id,$id);
                 $inscrition->insert();
                 $this->redirectToRoute("ins");
                //  $this->render("etudiant/inscrireEtudiant.html.php");
             }else{
                 die("Une erreur est survenue lors de l'inscription de l'etudiant");
             }
            //  Inscription
          
         
          }
  }
}
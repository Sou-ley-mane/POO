<?php
namespace App\Controller;

use App\Core\Controller;
use App\Core\Session;
use App\Model\Etudiant;
use App\Model\User;

class SecurityController extends Controller{



    public function authentification(){
          // 1-Affichage du formulaire ==>GET
         if ($this->request->isGet()) {
            $this->render("security/login.html.php");

             // dd("affichage du formulaire");
            }else{
                (extract($_POST));
                if ($login!="" && $password!="") {
                    $user=User::findUserByLoginAndPassword($login,$password);
                    if (isset($user)) {
                        
                         $this->session->setUser($user);
                         dd($this->session);
                        $this->redirectToRoute("Etudiants");
                        // $this->render("etudiant/etudiant.html.php");
                        
                    //  dd($user);
                 }else{
                     echo "user n'existe pas";
                 }
                 
             }else{
                 echo "Veillez remplir les champs";
             }
          

         }

         // 2-Traitement du formulaire apres soumission ==>POST
        //  if ($this->request->isPost()) {

            //   dd("Traitement du formulaire apres soumission");
        //  }

     //     echo "JE VEUX ME CONNECTER";
    }

    public function deconnexion(){
     $this->redirectToRoute("login");
   }
}
<?php
namespace App\Controller;
use App\Core\Controller;

use App\Model\Attache;

class ResponsablePedagoController extends Controller{

    public function ajouterAtt(){

      if ($this->request->isGet()) {
        $this->render("Responsable/formInscrip.html.php");
    // dd("affichage du formulaire");
   }else{
    (extract($_POST));
    if ($nomComplet!="" && $loginAtt!="" && $mdp!=""  ) {

      $att=new Attache($nomComplet,$loginAtt,$mdp);
      $att->insert();
      $this->render("Responsable/formInscrip.html.php");
      
   }
   $this->render("Responsable/formInscrip.html.php");
  
       
  }
}

}
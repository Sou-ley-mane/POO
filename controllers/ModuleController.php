<?php
namespace App\Controller;

use App\Core\Controller;
use App\Model\Modules;
// use App\Controller\ProfesseurController;

class ModuleController extends Controller{

  public function ajouterModule(){
    if ($this->request->isPost()) {
      // dd($_POST);
      extract($_POST);
      if ($libelle!=""&& $module!="") {
        # code...
        $module=new Modules($libelle,$module);
     $module->insert();
     $this->redirectToRoute("professeur");
      }
      dd("les champs du formulaire module sont vides");
    }

    
   
  }  

}

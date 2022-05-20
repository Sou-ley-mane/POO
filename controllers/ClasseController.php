<?php
namespace App\Controller;
use App\Core\Controller;
use App\Model\Classe;

class ClasseController extends Controller{

//Les useCases sont definis dans les controllers
public function CreerClasse(){

}

    public function ListeClasse(){

        dd(Classe::findAll());
        if ($this->request->isGet()) {
            $this->render("classe/listeClasse.html.php");
        // dd("affichage du formulaire");
       }

        // echo "LA LISTE DES CLASSES";
    }

}






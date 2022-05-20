<?php
namespace App\Core;
use App\Core\Request;
use App\Model\Etudiant;
use App\Model\Professeur;

class Controller{
    
    protected Session $session;
    protected Request $request;
    //constructeur
    //injection de la request dans le controleur
    public function __construct(Request $request,Session $session)
    {
        $this->session=$session;
        $this->request=$request;
    }

    //CHARGEMENT DES VUES PAR UN CONTROLEUR
//$path ==> chemin de la vue 
//$data ==> Données de la vue
    public function render(string $path,array $data=[]){
        $data["Constantes"]=Constantes::class;
        // $data["Professeur"]=Professeur::class;
        // $data["Etudiant"]=Etudiant::class;
        $data["request"]=$this->request;
        //Recuperation de la contenue d'une page
        ob_start();
        //Acces aux constantes
        extract($data);
        require_once(Constantes:: ROOT()."templates/".$path);
        $content_for_views=ob_get_clean();
        require_once(Constantes:: ROOT()."templates/layout/base.html.php");

    }

    //Fonction de redirection
    public function redirectToRoute($uri){
        header("location:".Constantes:: WEB_ROOT.$uri);
       
            }
}

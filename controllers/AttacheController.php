<?php
namespace App\Controller;
use App\Core\Controller;
use App\Model\Attache;

class AttacheController extends Controller{

public function listeAttache(){
$Att=Attache::findAll();
 $numero=0;
$this->render("attache/attache.html.php",[
    "numero"=>$numero,
    "attaches"=>$Att,
    "titre"=>"LISTE DES ATTACHES"
]);
    }


    public function delete(){
        Attache::delete(1);
        echo "supprimer";
    }
}

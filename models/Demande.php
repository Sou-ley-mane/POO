<?php
namespace App\Model;
use App\Core\Model;

class Demande extends Model{
    private int $idDemande;
    private string $motif;
    private string $dateDemande;
//constructeur
public function __construct()
{
    // self::$nomTable="demande";
}
    //FONCTIONS NAVIGATIONNELLES
    //manyToone=>Etudiant
    public function etudiant():Etudiant{
        return new Etudiant();
    }
//manyToone==>RP
public function responsablePedagogique():ResponsablePedago{
    return new ResponsablePedago();
}



}
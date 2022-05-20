<?php
//PACKAGE DE LA CLASSE
namespace App\Model;
use App\Core\Model;
// use App\Core\Model;
class Annee extends Model{
    private int $idAnnee;
    private string $libelleAnnee;
    //constructeur 
    public function __construct()
    {
        // self::nomTable()="annee";
    }

    //MERTHODE   NAVIGATIONNELLLES
    public function inscriptions():array{
        //REQUETE SQL
        return[];
    } 
}
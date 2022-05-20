<?php
namespace App\Model;
use App\Core\Model;

class Modules extends Model{
    private int $idModule;
    private string $nomModule;
    private string $libelleModule;
    //constructeur
    public function __construct()
    {
        // self::$nomTable="module";
    }
    //FONCTIONS NAVIGATIONNELLES
        //manyTomany==>professeurs
        public function professeurs():array{
            return[];
        }
}
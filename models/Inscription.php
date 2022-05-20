<?php
//PACKAGE DE LA CLASSE
namespace App\Model;
use App\Core\Model;

class Inscription extends Model{
    private int $id;
    //constructeur
    public function __construct()
    {
        // self::nomTable()="inscription";
    }
    //les attribut navigationnels  ==>issus des associations

    public function Attache():Attache{
         $sql="SELECT p.* FROM inscription i,personne p 
         WHERE p.id=i.attache_id
          AND p.role LIKE 'ATTACHE'
          AND i.id=".$this->id;
        return new Attache($this->nomComplet,$this->login,$this->password);
    }

    public function Annee():Annee{ 
        $sql="SELECT a.* FROM inscription i,annee a
         WHERE a.id=i.annee_id
          AND i.id=".$this->id;
        return new Annee();
    }
//oneToone==>Etudiant
    public function etudiant():Etudiant{
        return new Etudiant();
    }

    //manyToone==>cmasses
    // public function classes():Classe{
    //     return new Classe();
    // }


   
}
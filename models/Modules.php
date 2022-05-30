<?php
namespace App\Model;
use App\Core\Model;

class Modules extends Model{
    // private int $idModule;
    private string $nomModule;
    private string $libelleModule;
    //constructeur
    public function __construct(string $libelle,string $module)
    {
        $this->libelleModule=$libelle;
        $this->nomModule=$module;
        // self::$nomTable="module";
    }
    //FONCTIONS NAVIGATIONNELLES
        //manyTomany==>professeurs
        public function professeurs():array{
            return[];
        }

        public function insert():int{
            $sql="INSERT INTO `module` ( `libelle_module`, `nom_module`) VALUES (?,?)";
            // dd( $sql);
            // $sql="INSERT INTO  module (`nom_complet`, `role`, `login`,password) VALUES (?,?,?,?)";
            $bdd=parent::database();
            $bdd->connexionBDD();
            $result=$bdd->executeUpdate($sql,[$this->libelleModule,$this->nomModule]);
            $bdd->closeConnexion();
            return $result;  
        }

        public static function findAll():array{
            $sql="SELECT `id`,`nom_module` FROM module";
            // $sql="SELECT nom_complet, role, grade FROM ".parent::nomTable()." WHERE role  LIKE ? ";
            return parent::findBy($sql,[]);
        }

        public function moduleProf($id,$id_professeur):int{
            $sql="INSERT INTO module_professeur ( id,id_professeur) VALUES (?,?)";
            $bdd=parent::database();
            $bdd->connexionBDD();
            $result=$bdd->executeUpdate($sql,[$id,$id_professeur]);
            $bdd->closeConnexion();
            return $result;  
        }

}
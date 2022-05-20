<?php
namespace App\Model;


class Attache extends User{
    //les attributs nvigationnels
    //oneTomany
    //Approche utiliser en java(1ere Approche)
    private array $inscription;
  
    //Le constructeur
    //on peux initialiser les attribut d'un objet dans le constructeur
    public function __construct(string $nomComplet,string $login,string $password)
    {
        $this->nomComplet=$nomComplet;
        $this->login=$login;
        $this->password=$password;
        $this->inscription=[];
        parent::gestionDesRoles();
    }
    //2ieme Approche avec les methodes navigationnelles
    public function inscriptions():array{
        //REQUETE SQL
        return[];
    }  
    
    public static function findAll():array{
        $sql="SELECT `nom_complet`, `role`, `login`,`password`  FROM ".parent::nomTable()." WHERE role  LIKE ? ";

        // $sql="SELECT nom_complet, role, grade FROM ".parent::nomTable()." WHERE role  LIKE ? ";
        return parent::findBy($sql,[self::gestionDesRoles()]);
    }
    //REDEFINITION
    // public static function findAll():array{
    //     $bdd=parent::database();
    //     $bdd->connexionBDD();
    //     $sql="SELECT `nom_complet`, `role`, `login`,`password`  FROM ".parent::nomTable()." WHERE role  LIKE '".self::gestionDesRoles()."'";
    //     $result=$bdd->executeSelect($sql);
    //     $bdd->closeConnexion();
    //     return $result;
      
    // }

    public function insert():int{
        $sql="INSERT INTO ".parent::nomTable()." (`nom_complet`, `role`, `login`,password) VALUES (?,?,?,?)";
        $bdd=parent::database();
        $bdd->connexionBDD();
        $result=$bdd->executeUpdate($sql,[$this->nomComplet,self::gestionDesRoles(),$this->login,$this->password]);
        $bdd->closeConnexion();
        return $result;
      
    }

    // public static function findById(int $id):object|null{
    //     $sql="SELECT*FROM '".parent::nomTable()."'WHERE id=$id";
    //     echo $sql;
    //     return null;
    // }

}
<?php
namespace App\Model;
class ResponsablePedago extends User{
    
    public function __construct()
    {   
        parent::gestionDesRoles();  
    }
    //FONCTION NAVIGATIONNELLE
    //oneTomany==>demandes
    public function demandes():array{
        return [];
    }
//oneTomany==>Classes
    public function classe():array{
        return [];
    }
     
    public static function findAll():array{
        $sql="SELECT `nom_complet`, `role`, `login`, `password` FROM ".parent::nomTable()." WHERE role  LIKE ? ";
       return parent::findBy($sql,[self::gestionDesRoles()]);
    }

    
    public function insert():int{
        $sql="INSERT INTO ".parent::nomTable()." (`nom_complet`, `role`, `login`, `password`) VALUES (?,?,?,?)";
        //Creation d'un objet de type database
        $bdd=parent::database();
        $bdd->connexionBDD();
        $result=$bdd->executeUpdate($sql,[$this->nomComplet,self::gestionDesRoles(),$this->login,$this->password]);
        $bdd->closeConnexion();
        return $result;
    }

  
}
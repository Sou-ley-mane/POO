<?php
namespace App\Model;
class Professeur extends Personne{
    private string $grade;

    public function __construct($grade)
    {
        $this->grade=$grade;
        // parent::$role="PROFESSEUR";
        parent::gestionDesRoles();
       
    }

    //FONCTIONS    NAVIGATIONNELLES
    //manyTomany==>Classes
    public function classes():array{
        return [];
    }
//manyTomany==>Modules
public function modules():array{
    return []; 
}

    /**
     * Get the value of grade
     */ 
    public function getGrade()
    {
        return $this->grade;
    }

    /**
     * Set the value of grade
     *
     * @return  self
     */ 
    public function setGrade($grade)
    {
        $this->grade = $grade;

        return $this;
    }

    //Rdefinition
    public static function findAll():array{
        $sql="SELECT id, nom_complet, role, grade FROM ".parent::nomTable()." WHERE role  LIKE ? ";
        return parent::findBy($sql,[self::gestionDesRoles()]);
    }


    public function insert():int{
        $sql="INSERT INTO ".parent::nomTable()." (nom_complet, role, grade) VALUES (?,?,?)";
        //Creation d'un objet de type database
        $bdd=parent::database();
        //Application de la methode connexionBDD
        $bdd->connexionBDD();
        //Execution de requete a partir de la base de données
        $result=$bdd->executeUpdate($sql,[$this->nomComplet,self::gestionDesRoles(),$this->grade]);
        //fermeture de la connexion a la base de données
        $bdd->closeConnexion();
        return $result;
    }
}
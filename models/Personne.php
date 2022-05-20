<?php

namespace App\Model;

//Importation du package Core pour l'utilisation de la classe model
use App\Core\Model;

abstract class Personne extends Model
{
    //Les variables d'instance
    protected  int $id;
    protected string $nomComplet;
    protected static string $role;

    //le constructeur par defaut
    public function __construct()
    {
        // parent::$nomTable="personne"; 
    }

    //Gestion des roles
    public static function gestionDesRoles()
    {
            return self::$role = "ROLE_" . strtoupper(str_replace("App\Model\\", "",get_called_class())) ;
    }

    // ******************************************************************
    public static function findAll():array{
        $bdd=parent::database();
        $bdd->connexionBDD();
        $sql="SELECT `nom_complet`, `role`, `login`,`password`  FROM ".parent::nomTable()." WHERE role  LIKE '".self::gestionDesRoles()."'";
        $result=$bdd->executeSelect($sql);
        $bdd->closeConnexion();
        return $result;
      
    }

    // ******************************************
    //les getters ou Accesseurs
    public function getId(): int
    {
        return $this->id;
    }


    /**
     * Set the value of role
     *
     * @return  self
     */
    public function setRole($role)
    {
        self::$role = $role;

        return $this;
    }


    /**
     * Get the value of role
     */
    public function getRole()
    {
        return $this->role;
    }

    /**
     * Get the value of nomComplet
     */
    public function getNomComplet()
    {
        return $this->nomComplet;
    }

    /**
     * Set the value of nomComplet
     *
     * @return  self
     */
    public function setNomComplet($nomComplet)
    {
        $this->nomComplet = $nomComplet;

        return $this;
    }
}

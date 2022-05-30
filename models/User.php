<?php 
namespace App\Model;
abstract class User extends Personne{
    //les elements caracteristiques
    protected string $login;
    protected string $password="";
  
//LES GETTERS ET LES SETTERS
    /**
     * Get the value of login
     * 
     * 
     */ 

     
    public function getLogin()
    {
        return $this->login;
    }

    /**
     * Set the value of login
     *
     * @return  self
     */ 
    public function setLogin($login)
    {
        $this->login = $login;

        return $this;
    }

    /**
     * Get the value of password
     */ 
    public function getPassword()
    {
        return $this->password;
    }

    /**
     * Set the value of password
     *
     * @return  self
     */ 
    public function setPassword($password)
    {
        $this->password = $password;

        return $this;
    }

        //Requete de connexion
        public static function findUserByLoginAndPassword(string $login,string $password):object|null{ 
          return  parent::findBy("SELECT*FROM ".parent::nomTable()." WHERE login=? and password=?",[$login,$password],true);
        }

        public static function findAll():array{
            $sql="SELECT*FROM ".parent::nomTable()." WHERE role NOT LIKE 'ROLE_PROFESSEUR'";
            return  parent::findBy($sql);
        }
}
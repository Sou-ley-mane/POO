<?php
namespace App\Model;
use App\Model\classe;
class Etudiant extends User{
    //les variables d'instance
    private string $matricule;
    private string $adresse;
    private string $sexe;
    protected string $passwoed;
    protected string $login;
    // private string $sexe;

    private Classe $classe;
//Constructeur
    public function __construct(){
    // $this->classe = new classe();
    }

  // parent::$role="ETUDIANT";
    
    //FONCTION NAVIGATIONNELLES
    //oneTomany=>Demande
    public function demandes():array{
        return [];
    }
    //manyToone==>Classe
    public function classe():Classe{
        return new Classe();
    }

    //oneTomany==>Inscription
    public function inscription():array{
        return [];
    }

    //les getters et setters
    /**
     * Get the value of matricule
     */ 
    public function getMatricule()
    {
        return $this->matricule;
    }

    /**
     * Set the value of matricule
     *
     * @return  self
     */ 
    public function setMatricule($matricule)
    {
        $this->matricule = $matricule;

        return $this;
    }

    

    /**
     * Get the value of adresse
     */ 
    public function getAdresse()
    {
        return $this->adresse;
    }

    /**
     * Set the value of adresse
     *
     * @return  self
     */ 
    public function setAdresse($adresse)
    {
        $this->adresse = $adresse;

        return $this;
    }

    /**
     * Get the value of sexe
     */ 
    public function getSexe()
    {
        return $this->sexe;
    }

    /**
     * Set the value of sexe
     *
     * @return  self
     */ 
    public function setSexe($sexe)
    {
        $this->sexe = $sexe;

        return $this;
    }

    public function insert():int{
        $sql="INSERT INTO " .parent::nomTable()." (nom_complet, role,login,adresse,sexe,matricule,password) VALUES (?,?,?,?,?,?,?)";
        //Creation d'un objet de type database
        $bdd=parent::database();
        //Application de la methode connexionBDD
        $bdd->connexionBDD();
        //Execution de requete a partir de la base de données
        $result=$bdd->executeUpdate($sql,[$this->nomComplet,self::gestionDesRoles(),$this->login,$this->adresse,$this->sexe,$this->matricule,$this->password]);
        //fermeture de la connexion a la base de données
        $bdd->closeConnexion();
        return $result;
    }
    public static function findAll():array{
        $sql="SELECT nom_complet,matricule,adresse FROM ".parent::nomTable()." WHERE role  LIKE ? ";
        return parent::findBy($sql,[self::gestionDesRoles()]);
    }
    // public static function findAll():array{
    //     $sql="SELECT*FROM ".parent::nomTable()." WHERE role  LIKE ? ";
    //     return parent::findBy($sql,[self::gestionDesRoles()]);
        
        
    // }

    // public static function findById(int $id):object|null{
    //     $sql="SELECT*FROM '".parent::nomTable()."'WHERE id=$id";
    //     echo $sql;
    //     return null;
    // }

    // public function  delete():int{
    //     echo "je veux supprimer un étudiant";
    //     return 0;
    // }

    public function update():int{
        echo "Je souhaite mettre a jour les information d'un etudiant";
        return 0;
    }

    /**
     * Set the value of classe
     *
     * @return  self
     */ 
    public function setClasse($classe)
    {
        $this->classe = $classe;

        return $this;
    }
}


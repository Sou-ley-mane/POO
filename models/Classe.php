<?php
namespace App\Model;
use App\Core\Model;

class Classe extends Model{
    private int $id;
    private string $libelle;
    private string $filiere;
    private string $niveau;
    
    //constructeur de la classe
    public function __construct()
    {
        // self::$nomTable="classe";
    }

    public function ListerClasse(){
        //sql
    }

    //FONCTION NAVIGATIONNELLES
    //manyToone==>Responsable Pedagogique
    public function responsablePedagogique():ResponsablePedago{
        return new ResponsablePedago();
    }
    //manyTomany==>professeurs
    public function professeurs():array|null{
        $sql="select............;";
       $result= parent::findBy($sql,[$this->id]);
        return $result;
    }
//********************************************** */
//oneTomany==>Etudiants
    public function etudiants():array{
        return[];
    }

    //oneTomany==>Inscriptions
    public function inscriptions():array{
        return [];
    }

    //Redefinition des methodes
    public function update():int{
         echo "Je souhaite mettre a jour";
         return 0;
     }
    //  public function  delete():int{
    //      echo "je veux supprimer";
    //      return 0;
    //  }
     //Requete de données
     public static function findAll():array{
        $sql="SELECT * FROM classe ";
        return parent::findBy($sql,[]);
        // $sql="SELECT nom_complet, role, grade FROM ".parent::nomTable()." WHERE role  LIKE ? ";
        // echo "classe";
        // echo $sql;
        // return [];
    }
  
     public static function findById(int $id):object|null{
         $sql="SELECT*FROM '".self::nomTable()."'WHERE id=$id";
         echo $sql;
         return null;
     }
     //Autres  requetes
     //single =true on a un resultat 
     public static function findBy(string $requeteSql,array $data=null,$single=false):object|null|array{
         return null;
     }

    /**
     * Get the value of filiere
     */ 
    public function getFiliere()
    {
        return $this->filiere;
    }

    /**
     * Set the value of filiere
     *
     * @return  self
     */ 
    public function setFiliere($filiere)
    {
        $this->filiere = $filiere;

        return $this;
    }

    /**
     * Get the value of niveau
     */ 
    public function getNiveau()
    {
        return $this->niveau;
    }

    /**
     * Set the value of niveau
     *
     * @return  self
     */ 
    public function setNiveau($niveau)
    {
        $this->niveau = $niveau;

        return $this;
    }

    /**
     * Get the value of libelle
     */ 
    public function getLibelle()
    {
        return $this->libelle;
    }

    /**
     * Set the value of libelle
     *
     * @return  self
     */ 
    public function setLibelle($libelle)
    {
        $this->libelle = $libelle;

        return $this;
    }

    /**
     * Get the value of id
     */ 
    public function getId()
    {
        return $this->id;
    }

    public function insert():int{
        $sql="INSERT INTO ".parent::nomTable()." (filiere,niveau) VALUES (?,?)";
        $bdd=parent::database();
        $bdd->connexionBDD();
        $result=$bdd->executeUpdate($sql,[$this->filiere,$this->niveau]);
        $bdd->closeConnexion();
        return $result;
      
    }
    

}
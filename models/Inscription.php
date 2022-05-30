<?php
//PACKAGE DE LA CLASSE
namespace App\Model;
use App\Core\Model;



class Inscription extends Model{
    private int $id;
    private  int $id_annee;
    private int $id_classe;
    private int $id_attache;
    private  int $id_etudiant;

    //constructeur
    public function __construct(int $id_annee,int $id_classe,int $id_attache,int $id_etudiant)
    {
      
        
        $this->id_annee=$id_annee;
        $this->id_classe=$id_classe;
        $this->id_attache=$id_attache;
        $this->id_etudiant=$id_etudiant;
       
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

 
    public function insert():int{
        $sql="INSERT INTO inscription (id_annee,id_classe,id_attache,id_etudiant) VALUES (?,?,?,?)";
        $bdd=parent::database();
        $bdd->connexionBDD();
        $result=$bdd->executeUpdate($sql,[$this->id_annee,$this->id_classe,$this->id_attache,$this->id_etudiant]);
        $bdd->closeConnexion();
        return $result;
      
    }

    public function inserer($annee,$classe,$attache):int{
        $sql="INSERT INTO inscription (id_annee,id_classe,id_personne_attache_inscription ) VALUES ($annee,$classe,$attache)";
        $bdd=parent::database();
        $bdd->connexionBDD();
        $result=$bdd->executeUpdate($sql);
        $bdd->closeConnexion();
        return $result;
      
    }

   

    /**
     * Get the value of id
     */ 
    public function getId()
    {
        return $this->id;
    }

    /**
     * Set the value of id
     *
     * @return  self
     */ 
    public function setId($id)
    {
        $this->id = $id;

        return $this;
    }

    /**
     * Get the value of id_annee
     */ 
    public function getId_annee()
    {
        return $this->id_annee;
    }

    /**
     * Set the value of id_annee
     *
     * @return  self
     */ 
    public function setId_annee($id_annee)
    {
        $this->id_annee = $id_annee;

        return $this;
    }

    /**
     * Get the value of id_classe
     */ 
    public function getId_classe()
    {
        return $this->id_classe;
    }

    /**
     * Set the value of id_classe
     *
     * @return  self
     */ 
    public function setId_classe($id_classe)
    {
        $this->id_classe = $id_classe;

        return $this;
    }

    /**
     * Get the value of id_attache
     */ 
    public function getId_attache()
    {
        return $this->id_attache;
    }

    /**
     * Set the value of id_attache
     *
     * @return  self
     */ 
    public function setId_attache($id_attache)
    {
        $this->id_attache = $id_attache;

        return $this;
    }

    /**
     * Get the value of id_etudiant
     */ 
    public function getId_etudiant()
    {
        return $this->id_etudiant;
    }

    /**
     * Set the value of id_etudiant
     *
     * @return  self
     */ 
    public function setId_etudiant($id_etudiant)
    {
        $this->id_etudiant = $id_etudiant;

        return $this;
    }
}
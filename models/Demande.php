<?php
namespace App\Model;
use App\Core\Model;

class Demande extends Model{
    // private int $idDemande;
    private string $motif;
    private string $dateDemande;
    private int $id_etudiant;
    private string $message;

//constructeur
public function __construct(string $motif,string $dateDemande,int $id_etudiant,string $message)
{
    $this->motif=$motif;
    $this->dateDemande=$dateDemande;
    $this->id_etudiant=$id_etudiant;
    $this->message=$message;
    // self::$nomTable="demande";
}
    //FONCTIONS NAVIGATIONNELLES
    //manyToone=>Etudiant
    public function etudiant():Etudiant{
        return new Etudiant();
    }
//manyToone==>RP
public function responsablePedagogique():ResponsablePedago{
    return new ResponsablePedago();
}


//

public function insert():int{
    $sql=" INSERT INTO demande ( motif, date_demande, id_etudiant, `message`) VALUES (?,?,?,?)";
    //Creation d'un objet de type database
    $bdd=parent::database();
    //Application de la methode connexionBDD
    $bdd->connexionBDD();
    //Execution de requete a partir de la base de données
    $result=$bdd->executeUpdate($sql,[$this->motif,$this->dateDemande,$this->id_etudiant,$this->message]);
    //fermeture de la connexion a la base de données
    $bdd->closeConnexion();
    return $result;
}

public static function findAll():array{
    $sql="SELECT * FROM demande ";
    // $sql="SELECT nom_complet, role, grade FROM ".parent::nomTable()." WHERE role  LIKE ? ";
    return parent::findBy($sql,[]);
}

}
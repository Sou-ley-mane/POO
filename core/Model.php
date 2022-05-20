<?php
namespace App\Core;
//CLASSE D'IMPLEMENTATION
abstract class Model implements IModel{

//Liaison(Dependance) du model avec la base de données
    protected static function database():Database{
return new Database();
    } 


    //Fonction pour génerer automatiquement le nom de la table
    public static function  nomTable():string{
 $nomClass=get_called_class();
 return $nomTable=($nomClass=="Etudiant" or $nomClass=="Attache" or $nomClass="ResponsablePedago" or $nomClass=="User" or $nomClass=="Professeur ")?'personne':strtolower($nomClass);
    }

//Redefinition des methodes de l'interface Imodel 
     //Requetes  de mis a jour
     public function insert():int{
         return 0;
     }
     public function update():int{
         echo "Je souhaite mettre a jour";
         return 0;
     }
     public static function  delete(int $id):int{
         //Creation d'un objet de type database
         $bdd=self::database();
         //Application de la methode connexionBDD
         //objet connecter a la base de données
         $bdd->connexionBDD();
         //Execution de requete a partir de la base de données
         $result=$bdd->executeUpdate("DELETE FROM".self::nomTable()."WHERE id=?",[$id]);
         //fermeture de la connexion a la base de données
         $bdd->closeConnexion();
         return $result;
     }


     
     //Requete de données
     public static function findAll():array{
         //Creation d'un objet de type database
         $bdd=self::database();
         //Application de la methode connexionBDD
         $bdd->connexionBDD();
        $result= $bdd->executeSelect("SELECT*FROM '".self::nomTable()."'",[],false);
        //  $sql="SELECT*FROM '".self::nomTable()."'";
        //  echo $sql;
          //fermeture de la connexion a la base de données
          $bdd->closeConnexion();
         return $result;
     }
     public static function findById(int $id):object|null{
                 //Creation d'un objet de type database
                 $bdd=self::database();
                 //Application de la methode connexionBDD
                 $bdd->connexionBDD();
                $result= $bdd->executeSelect("SELECT*FROM '".self::nomTable()."'WHERE id=?",[$id],true);
        //  $sql="SELECT*FROM '".self::nomTable()."'WHERE id=$id";
        //  echo $sql;
          //fermeture de la connexion a la base de données
          $bdd->closeConnexion();
         return $result;
     }
     //Autres  requetes
     //single =true on a un resultat 
     public static function findBy(string $requeteSql,array $data=null,$single=false):object|null|array{
           $bdd=self::database();
           $bdd->connexionBDD();
          $result= $bdd->executeSelect($requeteSql,$data,$single);
          $bdd->closeConnexion();
   return $result;
       
     }

}


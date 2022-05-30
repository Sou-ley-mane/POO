<?php
namespace App\Core;

use App\Exception\ConnecteNotFound;
use PDO;

class Database{

    private \PDO|null $pdo=null; 
//Pour la connexion a la base de données
    public function connexionBDD():void{
        try {
            $this->pdo=new \PDO("mysql:dbname=Gestion_scolaire;host=localhost:3306","souleymane","diallo");
        } catch (\Exception $th) {
            throw new ConnecteNotFound();
                // $th->getMessage();
        }
    }

        //Fermer la connexion de la BDD
        public function closeConnexion():void{
            $this->pdo=null;
        }

//pour executyer les requete Select
    public function executeSelect(string $sql,array $datas=[],$single=false):array|object|null{
    $query=$this->pdo->prepare($sql);  
       $query->execute($datas);
       //recuperation des resultats
       if ($single) {
          $result=$query->fetch(\PDO::FETCH_OBJ);
          if ($query->rowCount()==0) {
             return null;
          }
       }else{
           //si $single=false
$result=$query->fetchAll(\PDO::FETCH_OBJ);
}
return $result;

    }

//pour executyer les requete Update ou delete ou insert
    public function executeUpdate(string $sql,array $datas=[]):int{
        $query=$this->pdo->prepare($sql);  
        $query->execute($datas);
        return ($query->rowCount()>=1)?  $this->pdo->lastInsertId():$query->rowCount();
        // $result=$query->rowCount();
        // return $result;
        //NB: Insert==>On return le dernier id (dans la BDD)
    }

   


}
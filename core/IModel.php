<?php
namespace App\Core;
//CLASSE 100% ABSTRAITE
//Methode static ==>Methode de classe(on n'a pas besoin de l'etat de l'objet)
//Acces: Classe::Methode
// Methode d'inst ance(besoin de l'etat de l'objet)
//Acces:$o bjet->Methode

interface IModel{
    //METHODES ABSTRAITES
    //Requete de mis a jour 
    //Methodes d'instances
    public function update():int;
    public function insert():int;
     //Methodes static
     public static function  delete(int $id):int;
    public static function findAll():array;
    public static function findById(int $id):object|null;
    public static function findBy(string $requeteSql,array $data=null,$single=false):object|null|array;
   
    
}

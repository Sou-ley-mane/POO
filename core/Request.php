<?php

namespace App\Core;
class Request{

    //Recuperation de l'URL
//  public function filtreUri(){
//    return $uri=str_replace("/public/","",$_SERVER["REQUEST_URI"]) ;

//  }

public function __construct()
{
//     echo "URL REQUEST";
}

    public function getUri(){
        $uri=explode("/",$_SERVER["REQUEST_URI"]) ;
        unset($uri[0]);
       return array_values($uri);
    }

    public function isGet():bool{
        return $_SERVER["REQUEST_METHOD"]=="GET";
    }

    public function isPost():bool{
        return $_SERVER["REQUEST_METHOD"]=="POST";
    }

    public function  getPost(){
        
         return $_POST;
    }

}
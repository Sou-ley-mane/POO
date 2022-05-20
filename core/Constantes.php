<?php
namespace   App\Core;

class Constantes{

   public const WEB_ROOT="http://localhost:8002/";
   
public static function ROOT(){
  return str_replace("public","",$_SERVER["DOCUMENT_ROOT"]);
}
//    public const ROOT="http://localhost:8002/";
//    public const ROOT="http://localhost:8002/";
//    public const ROOT="http://localhost:8002/";



}
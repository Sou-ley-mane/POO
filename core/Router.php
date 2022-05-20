<?php
namespace App\Core;

use App\Exception\RoueNotFound;

class Router{
//traduction de la reception de la request par le router
    private Request $request;
    private Session $session;
private array $routes=[];

public function __construct()
{
    $this->request=new Request(); 
    $this->session=new Session();
}

//Tableau associatif
public function route(string $uri,array $action){
 $this->routes[$uri]=$action;
}

//Verification de l'existence de l'url
public function resolve(){
    $uri="/". $this->request ->getUri()[0];
    if (isset($this->routes[$uri])) {
        //SELECTION DE CONTROLLER ET VERIFICATION DE L'EXISTANCE DE L'ACTION
        $route=$this->routes[$uri];
        [$ctrClass,$action]=$route;
        if (class_exists($ctrClass)&& method_exists($ctrClass,$action)) {
            $ctrl=new $ctrClass($this->request,$this->session);
            $ctrl->$action();
            // call_user_func(array($ctrl,$action));
    }
      else{ 
        //throw==>souleve une exception
        dd("Classe n'existe pas ou l'action existe pas");;
    }
  }else{ 
    //throw==>souleve une exception
    throw new RoueNotFound();
  }
}

}
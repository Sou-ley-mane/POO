<?php
namespace App\Core;

use App\Exception\RoueNotFound;

class Router{
//traduction de la reception de la request par le router
    private Request $request;
    private Session $session;
    private Role $role;
private array $routes=[];

public function __construct()
{
    $this->request=new Request(); 
    $this->session=new Session();
    $this->role=new Role();
}

//Tableau associatif
public function route(string $uri,array $action){
 $this->routes[$uri]=$action;
}

//Verification de l'existence de l'url
public function resolve(){
    $uri="/". $this->request ->getUri()[0];

    $params= $this->request ->getUri();
    unset($params[0]);
    // dd(array_values($params));
    $params=count($params)>=1?array_values($params):[];

    // dd($params);

    if (isset($this->routes[$uri])) {
        //SELECTION DE CONTROLLER ET VERIFICATION DE L'EXISTANCE DE L'ACTION
        $route=$this->routes[$uri];
        [$ctrClass,$action]=$route;
        if (class_exists($ctrClass) && method_exists($ctrClass,$action)) {
          $ctrl=new $ctrClass($this->request,$this->session,$this->role);
          //les routes accessibles sans connexion
          $freeTest=explode("\\",$ctrl::class)[2]."/".$action;
          $free=["SecurityController/authentification","SecurityController/deconnexion","*"];
          if (in_array("*",$free)|| in_array($freeTest,$free)) {
            call_user_func_array([$ctrl,$action],$params);
            // $ctrl->$action();
         
            
          }elseif($this->role->isConnect())
          {
            call_user_func_array([$ctrl,$action],$params);

              // $ctrl->$action();
          } else{
        // header("location:".Constantes:: ROOT()."security/login.html.php");

            // header("Location:","security/login.html.php");
            dd("uri non table and you are not connect");
            throw new RoueNotFound();
          }
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
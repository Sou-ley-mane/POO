<?php
namespace App\Core;

use App\Model\User;

class Session{

    private User $user;

public function __construct()
{
    
    if (session_status()==PHP_SESSION_NONE) {
        session_start();
    }
}

//Ajout dans la session
public function set(string $key,$data){
      $_SESSION[$key]=$data;
}

//Recuperer la valeur dans la session
// public function get(string $key){
//      return  $_SESSION[$key];
//     }


    /**
     * Get the value of user
     */ 
    public function getUser()
    {
        return $this->user;
    }

    /**
     * Set the value of user
     *
     * @return  self
     */ 
    public function setUser($user)
    {
        $this->user = $user;
        $_SESSION["user_connect"]=$user;
        return $this;
    }

    public function distroySession(){
        //pour fermer la session
    }
}
<?php

namespace App\Core;



// use App\Core\Session;
class Role{
// private Session $session;


    // public function __construct(Session $session )
    // {$this->session=$session; 
        
    // }

    public static function isConnect():bool{
        return isset($_SESSION['user-connect']);
    }

    public function isResponsable(){
    return (self::isConnect() && $_SESSION ["user-connect"]->role=="ROLE_RESPONSABLEPEDAGO") ;
        // return self::isConnect() && $_SESSION ["user-connect"]->role="ROLE_RESPONSABLEPEDAGO";
        // return self::isConnect() && $_SESSION['user-connect']['role']="";
        
    }
    
    public static function isEtudiant(){
        // echo "etudiant";
        return (self::isConnect() && $_SESSION ["user-connect"]->role=="ROLE_ETUDIANT") ;
        // return self::isConnect() && $_SESSION ["user-connect"]->role="ROLE_ETUDIANT";

}
public static function isAc(){
    // echo "etudiant";
    return (self::isConnect() && $_SESSION ["user-connect"]->role=="ROLE_ATTACHE") ;
    // return self::isConnect() && $_SESSION ["user-connect"]->role="ROLE_ETUDIANT";

}





// public static function etudiant(){
//     // echo "etudiant";
//   return  self::isConnect() && $_SESSION ["user-connect"]->role=="ROLE_ETUDIANT";
       

// }

}
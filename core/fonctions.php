<?php

use App\Core\Session;

function dd($data){
echo '<pre>';
var_dump($data);
echo '</pre>';
die();
}


function afficheErreur(){
 ini_set('display_errors',1);
ini_set('display_startup_errors',1);
error_reporting(E_ALL);

}


                     

// Fonction pour deactiver les boutons
function hidden(array $except,$hidden=true){
    // dd(Session::getRoleUserSession());
    foreach($except as $persoConnect){
        // dd($persoConnect);
        if (Session::getRoleUserSession()==$persoConnect) {
            return $hidden ? "disabled":"hidden";
        }
return "";
    }
}

// function hidden(array $except){
//     foreach($except as $persoConnect){
//         if (Session::getRoleUserSession()==$persoConnect) {
//             return "disabled=true";
//         }
        

//     }
// }

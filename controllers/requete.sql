if ($this->role->isEtudiant()) {?>
     
    <?php 
    $hidden="";
} ?>  

<nav >

      <ul class="menu">
 

          <li><a href="<?= $Constantes ::WEB_ROOT."etudiants"?>" <?=$hidden?>   >Liste etudiant</a></li>
          <li><a href="<?= $Constantes ::WEB_ROOT."demande"?>" <?=$hidden?>   >Demande Etudiant</a></li>
          <li><a href="<?= $Constantes ::WEB_ROOT."inscrire"?> " <?=$hidden?>  >Inscrire etudiant</a></li>
          <li><a href="<?= $Constantes ::WEB_ROOT."attache"?>" >liste attache</a></li>
          <li><a href="<?= $Constantes ::WEB_ROOT."deconnexion"?>">DECONNEXION</a></li>
      </ul>
  </nav>  

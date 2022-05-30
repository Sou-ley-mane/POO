

<?php

//  /dd($_SESSION);

// dd($_SESSION["user-connect"]->role);

  if ($session::getRoleUserSession() == "ROLE_ETUDIANT") {?>
<h1 class="bg-dark text-center text-white"><?= $titre ?></h1>
<table class="table mt-2">
   <tr class="text-center bg-danger text-white font-weight-bold">
      <td>N°</td>
      <td>Nom Complet</td>
      <td>matricule</td>
      <td>adresse</td>
      <td>Informations de l'etudiant</td>

      <!-- <td>Actions</td> -->
   </tr>
   <?php
   foreach ($etudiant as $etudiant) {
   ?>
      <tr class="text-center">
         <td> <?= ++$num; ?></td >
         <td> <?= $etudiant->nom_complet; ?></td>
         <td><?= $etudiant->matricule; ?></td>
         <td><?= $etudiant->adresse; ?></td>
         <td class="text-center">
         <!-- <button class="bg-primary">Archiver</button> -->
         <a class="text-white" href="#" ><button class="bg-dark text-white font-weight-bold" >Detail Etudiant</button></a>
         <!-- <button class="bg-danger text-white " >Supprimer</button> -->
      </td>
      </tr>
   <?php } ?>
</table>
   <?php } else{?>

<h1 style="color:brown">VOUS NE POUVEZ PAS ACCEDEZ DANS CETTE PAGE</h1>
<button type="button" class="btn btn-outline-primary ">DEMANDE D'ACCES</button>
  <?php }
   ?>
  
 

<div class="">
<h1 class="bg-primary text-center text-white"><?= $titre; ?></h1>
<table class="table">
<tr class="bg-primary text-white text-center font-weight-bold">
      <td>N°</td>
      <td>Nom Complet</td>
      <td>Login</td>
      <!-- <td>Role</td> -->
      <td>Actions</td>
   </tr>
<?php
foreach ($attaches as $attache) {
?>
   <tr class="text-center">
      <td><?= ++$numero?></td>
      <td><?= $attache->nom_complet;?></td>
      <td><?= $attache->login;?></td>
      <!-- <td><?= $attache->role ;?></td> -->
      <td class="text-center">
         <button class="bg-primary text-white">Archiver</button>
         <button class="bg-warning">Modifier</button>
         <button class="bg-danger "><a class="text-white" href="Supprimer">Supprimer</a></button>

      </td>
   </tr>

   <?php } ?>
</table>
</div>

<!-- ********************************************** -->

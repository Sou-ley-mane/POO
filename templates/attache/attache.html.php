<div class="">
<h1 class="bg-secondary"><?= $titre; ?></h1>
<table class="table">
<tr class="text-center">
      <td>N°</td>
      <td>Nom Complet</td>
      <td>Login</td>
      <td>Role</td>
      <td>Actions</td>
   </tr>
<?php
foreach ($attaches as $attache) {
?>
   <tr class="text-center">
      <td><?= ++$numero?></td>
      <td><?= $attache->nom_complet;?></td>
      <td><?= $attache->login;?></td>
      <td><?= $attache->role ;?></td>
      <td class="text-center">
         <button class="bg-primary">Archiver</button>
         <button class="bg-warning">Modifier</button>
         <button class="bg-danger">Supprimer</button>

      </td>
   </tr>

   <?php } ?>
</table>
</div>
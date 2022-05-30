<div class="">
<h1 class="text-center"><?= $titre; ?></h1>
<table class="table">
<tr class=" bg-dark">
      <td">N°</td>
      <td>Nom Complet</td>
      <td>Grade</td>
      <td>Role</td>
      <td>Actions</td>
   </tr>
<?php
foreach ($listeProf as $prof) {
?>
   <tr class="text-center">
      <td><?= ++$numero?></td>
      <td><?= $prof->nom_complet;?></td>
      <td><?= $prof->grade;?></td>
      <td><?= $prof->role ;?></td>
      <td class="text-center">
         <button class="bg-primary">Archiver</button>
         <button class="bg-warning">Modifier</button>
         <button class="bg-danger">Supprimer</button>

      </td>
   </tr>

   <?php } ?>
</table>
</div>
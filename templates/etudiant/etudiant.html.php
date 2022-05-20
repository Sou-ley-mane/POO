
<h1 class="bg-info"><?= $titre ?></h1>
<table class="table">
   <tr class="text-center">
      <td>N°</td>
      <td>Nom Complet</td>
      <td>matricule</td>
      <td>adresse</td>
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
      </tr>
   <?php } ?>
</table>

<h1 class="bg-dark text-center text-white"><?= $titre; ?></h1>
<table class="table text-center">
  <thead class="thead-dark ">
    <tr >
      <th scope="col">Numéro</th>
      <th scope="col">Nom Complet</th>
      <th scope="col" class="m-4">Grade</th>
      <!-- <th scope="col">Role</th> -->
      <th scope="col">Actions</th>
    </tr>
  </thead>

  <?php

                   

foreach ($listeProf as $prof) {
?>
    <tr>
      <!-- <th scope="row">1</th> -->
      <td><?= ++$numero?></td>
      <td><?= $prof->nom_complet;?></td>
      <td><?= $prof->grade;?></td>
    

      <td class="text-center">
         <button class="bg-primary">Archiver</button>
         <a class="text-white " href="/detail/<?=$prof->id?>" > <button class="bg-secondary" >Information</button></a>
         <button class="bg-danger " >Supprimer</button>
      </td>
    </tr>
    <?php } ?>
</table>

<!-- <table class="table">
  <thead class="thead-light"> -->
    <!-- <tr>
      <th scope="col">#</th>
      <th scope="col">First</th>
      <th scope="col">Last</th>
      <th scope="col">Handle</th>
    </tr>
  </thead> -->
  <!-- <tbody> -->
  


    
    <!-- <tr>
      <th scope="row">2</th>
      <td>Jacob</td>
      <td>Thornton</td>
      <td>@fat</td>
    </tr>
    <tr>
      <th scope="row">3</th>
      <td>Larry</td>
      <td>the Bird</td>
      <td>@twitter</td>
    </tr> -->
  <!-- </tbody>
</table> -->

<!-- ***************************************************************** -->

<!-- <div class="">
<h1 class="bg-info"><?$titre; ?></h1>
<table class="table">
<tr class="text-center">
      <td>N°</td>
      <td>Nom Complet</td>
      <td>Grade</td>
      <td>Role</td>
      <td>Actions</td>
   </tr>
<?php
foreach ($listeProf as $prof) {
?>
   <tr class="text-center">
      <td><? ++$numero?></td>
      <td><? $prof->nom_complet;?></td>
      <td><? $prof->grade;?></td>
      <td><? $prof->role ;?></td>
      <td class="text-center">
         <button class="bg-primary">Archiver</button>
         <button class="bg-warning">Modifier</button>
         <button class="bg-danger">Supprimer</button>

      </td>
   </tr>

   <?php } ?>
</table>
</div> -->



<h1 class="bg-dark text-center text-white"><?= $titre; ?></h1>
<table class="table text-center">
  <thead class="thead-dark ">
    <tr >
      <!-- <th scope="col">Numéro</th> -->
      <th scope="col">Motif</th>
      <th scope="col">Contenu</th>
      <th scope="col" class="m-4">Date</th>
      <th scope="col">Actions</th>
    </tr>
  </thead>

  <?php

                   

foreach ($lesDemandes as $demande) {
?>
    <tr>
      <!-- <th scope="row">1</th> -->
      <!-- <td><?= ++$numero?></td> -->
      <td><?= $demande->motif;?></td>
      <td><?= $demande->message;?></td>
      <td><?= $demande->date_demande;?></td>
    
      <td class="text-center ">
         <button class="bg-success p-1 font-weight-bold">Accepter</button>
         <!-- <a class="text-white " href="/detail/<?=$prof->id?>" > <button class="bg-secondary" >Information</button></a> -->
         <button class="bg-danger p-1 text-white font-weight-bold pl-2 pr-2" >Refuser</button>
      </td>
    </tr>
    <?php } ?>
</table>


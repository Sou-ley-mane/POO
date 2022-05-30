
<!-- 
<h1><?= $titre ?></h1>
<h2><?= $login ?></h2>
<h2><?= $grade ?></h2> -->
<!-- ****************************************************** -->
<div class="card ">
  <h5 class="card-header bg-dark text-white"><?=$titre ?></h5>
  <div class="card-body">
    <h5 class="card-title"><?= $login ?></h5>
    <h5 class="card-title"><?= $grade ?></h5>
  </div>
</div>
<!-- ************************************************************************* -->
<div class=" card mt-5 " style="min-height: 20vw;">
  <h5 class="card-header bg-dark text-white">AFFECTATION DE CLASSE ET DE MODULE AU PROFESSEUR<button id="btnModule" class="p-2   bg-info text-white" style="cursor: pointer; margin-left: 15vw;">New module</button></h5>
<!-- ****************************Modal module****************************************** -->
<form action="<?= $Constantes ::WEB_ROOT."ajoutModule"?>" method="POST" class="col-3 border border-danger bg-info p-2 m-2 modalModul ">
  <div class=" col-3">
 <div class="col-3">
 <label for="" class="font-weight-bold">libelle</label>
 <input name="libelle" type="text">
</div>
<div class="col-3">
  <label for="" class="font-weight-bold">modules</label>
  <input name="module" type="text">
  </div>
</div>
<div class="d-flex">
  <input type="submit" value="Ajouter" class="m-3 bg-success text-white">
  <input type="button" value="Quitter" class="m-3 bg-danger text-white annuler">
</div>
</form>
<!-- ******************************************************************* -->
  <div  class="  d-flex mx-2  justify-content-between  mt-4" >
    <form  action="<?= $Constantes ::WEB_ROOT."affecter"?>" method="POST" class="row">
    <div class="col-2 ">
  <input class="bg-primary text-white p-2" type="submit" value="Affecter" style="width: 15vh ;" >
  </div>
  <div class="col-3 ">
    
  <!-- <label class="text-white font-weight-bold" for="">Classe</label> -->
                <select name="classe" id=""  class="form-control">
                <option disabled selected>--Veillez choisir une classe --</option>
                <?php
                foreach($classes as $classe){
                    ?>
                    <option value=<?= $classe->id?>><?= $classe->libelle?></option>
                    <?php } ?>
                </select>   
  </div>
<!-- ******************************************************************************** -->
  <div class="col-7  mt-3 ">
  <h5 class="card-header bg-info text-center font-weight-bold text-white">AFFECTATION DE MODULES</h5>

    <div class="mb-3 text-center h5 text-danger">Toutes les modules ont été affecter au professeur</div>
  <div class="row  text-center mt-1">
    <?php
       foreach($modules as $module){
        ?>
        <div class=" col-3">
         <input type="checkbox" value="<?= $module->id?>" name="module" id="<?= $module->id?>">
        <label for="<?= $module->id?>"><?= $module->nom_module?></label>
        </div>            <!-- <option value=<?= $classe->id?>><?= $classe->libelle?></option> -->
        <?php } ?> 
  </div>

 

  </form>
  </div>

</div>
</div>
<!-- ************************************************************** -->
<table class="table mt-5">
  <thead>
    <tr class="text-center bg-dark text-white">
      <!-- <th scope="col">#</th> -->
      <th scope="col">CLASSE DU PROFESSEUR</th>
      <th scope="col">MODULES DU PROFESSEUR</th>
      <!-- <th scope="col"></th> -->
    </tr>
  </thead>
  <tbody class="table-group-divider text-center">
    <tr>
      <td>Sonatel</td>
      <td>Developpement</td>
    </tr>
    <tr>
      <td>Sonatel</td>
      <td>Developpement</td>
    </tr>
    <tr>
      <td>Sonatel</td>
      <td>Developpement</td>
    </tr>
    <tr>
      <td>Sonatel</td>
      <td>Developpement</td>
    </tr>
  </tbody>
</table>
<!-- **************************Modale pour ajouter des modules**************************** -->

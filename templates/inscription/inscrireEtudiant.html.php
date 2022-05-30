
<div class="container-fluid vh-100 mt-5 d-flex align-items-center">
    <div class="container mt-3 w-70 bg-dark p-4 ">
        <h1 class="bg-white text-center">GESTION DES INSCRIPTIONS</h1>
        <div class="mb-3 d-flex mt-5 justify-content-around justify-content-center align-items-center ">
               <h4 class="text-white" >Attache en service :</h4>
               <?php
               if ($_SESSION["user-connect"]->sexe == 'f') {
                ?>
                <h4 class="text-white">Madame <?=$_SESSION["user-connect"]->nom_complet?></h4>
                <?php } else {
                ?>
                <h4 class="text-white">Monsieur <?=$_SESSION["user-connect"]->nom_complet?></h4>

                <?php } ?>
            </div>
        <form action="ins" method="POST">
        <div class="mb-3">
                <label class="text-white font-weight-bold" for="">Année universitaire</label>
                <select name="annee" id="" class="form-control">
                <?php
                foreach($annees as $annees){
                    ?>
                    <option value=<?=$annees->id_annee?>><?= $annees->libelle?></option>
                    <?php } ?>
                </select> 
            </div>
            <div class="mb-3">
                <label class="text-white font-weight-bold" for="">Nom Complet</label>
                <input name="nomcomplet" type="text" class="form-control">
            </div>
           
            <div class="mb-3">
                <label class="text-white font-weight-bold" for="">Email</label>
                <input name="login" type="text" class="form-control">
            </div>
            <div class="mb-3">
                <label class="text-white font-weight-bold" for="">Matricule</label>
                <input  readonly  name="matricule" type="text" value="<?=date("ymdhms")?>" class="form-control">
            </div>
            <div class="mb-3">
                <label  class="text-white font-weight-bold" for="">Mot de passe</label>
                <input readonly name="password" type="password" value="sanatel<?= date("hms")?>" class="form-control">
            </div> 
            <div class="mb-3">
                <label class="text-white font-weight-bold" for="">Adresse</label>
                <input name="adresse" type="text" class="form-control">
            </div>     <div class="mb-3">
                <label class="text-white font-weight-bold" for="">Sexe</label>
                <select  name="sexe" id="">
                    <option value="m">Homme</option>
                    <option value="f">Femme</option>
                </select>
                <!-- <input name="sexe" type="text" class="form-control"> -->
            </div>
        
         
           
            <div class="mb-3">
                <label class="text-white font-weight-bold" for="">Classe</label>
                <select name="classe" id=""  class="form-control">
                <option disabled selected>--Veillez choisir une classe --</option>
                <?php
                foreach($classes as $classe){
                    ?>
                    <option value=<?= $classe->id?>><?= $classe->libelle?></option>
                    <?php } ?>
                </select> 
            </div>
            <button type="submit" class="btn btn-success bg-dark  w-25">inscrire Etudiant</button>
        </form>
</div>
</div>
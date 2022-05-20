
<div class="container-fluid vh-100 mt-5 d-flex align-items-center">
    <div class="container mt-3 w-70 bg-primary p-4">
        <h1 class="bg-white">GESTION DES INSCRIPTIONS</h1>
        <form action="inscrire" method="POST">
            <div class="mb-3">
                <label for="">Nom Complet</label>
                <input name="nomcomplet" type="text" class="form-control">
            </div>
            <div class="mb-3">
                <label for="">Matricule</label>
                <input name="matricule" type="text" class="form-control">
            </div>
            <div class="mb-3">
                <label for="">Email</label>
                <input name="login" type="text" class="form-control">
            </div>
            <div class="mb-3">
                <label for="">Mot de passe</label>
                <input name="password" type="password" class="form-control">
            </div>
            <div class="mb-3">
                <label for="">Adresse</label>
                <input name="adresse" type="text" class="form-control">
            </div>     <div class="mb-3">
                <label for="">Sexe</label>
                <select  name="sexe" id="">
                    <option value="m">Homme</option>
                    <option value="f">Femme</option>
                </select>
                <!-- <input name="sexe" type="text" class="form-control"> -->
            </div>
            <div class="mb-3">
                <label for="">Filiere</label>
                <input name="filiere" type="text" class="form-control">
            </div>
            <div class="mb-3">
                <label for="">Niveau</label>
                <input name="niveau" type="text" class="form-control">
            </div>
            <div class="mb-3">
                <label for="">Classe</label>
                <select name="classe" id=""  class="form-control">
                <?php
                use App\Model\Classe;
                $idClasse=Classe::findAll();
                // dd(Classe::findAll());
                foreach( $idClasse as $classe){
                    ?>
                    <option value=<?= $classe->id?>> <?php echo  $classe->id ?> </option>
                    <?php } ?>
                </select> 
            </div>
            <button type="submit" class="btn btn-success bg-dark  w-25">S 'inscrire</button>
        </form>
</div>
</div>
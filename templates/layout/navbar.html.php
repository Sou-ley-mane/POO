



<?php

// ******************************************************
// ******************************************************

$hidden="hidden";
//PROBLEMEME DE ROLE DANS LA SESSION

// var_dump("ok");
if ($this->role->isResponsable()) {?>
     
    <?php 
    $hidden="";
} ?>  

<nav class="navbar navbar-expand-sm navbar-dark bg-dark" aria-label="Third navbar example">
    <div class="container-fluid">
      <!-- <a class="navbar-brand" href="#">Expand at sm</a> -->
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarsExample03" aria-controls="navbarsExample03" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarsExample03">
        <ul class="navbar-nav me-auto mb-2 mb-sm-0">
         
          <li class="nav-item">
            <a class="nav-link active font-weight-bold h4 text-white" aria-current="page" href="<?= $Constantes ::WEB_ROOT."ins"?>">Inscription</a>
          </li>
          <li class="nav-item">
            <a class="nav-link font-weight-bold h4 text-white" href="<?= $Constantes ::WEB_ROOT."etudiants"?>">ListeEtudiant</a>
          </li>
          <li class="nav-item">
            <a class="nav-link font-weight-bold h4 text-white" href="<?= $Constantes ::WEB_ROOT."demande"?>">Nouvelle Demandes</a>
          </li>
          <li class="nav-item">
            <a class="nav-link font-weight-bold h4 text-white" href="<?= $Constantes ::WEB_ROOT."listDemande"?>" >Demandes Etudiants</a>
          </li>
          <li class="nav-item">
            <a class="nav-link font-weight-bold h4 text-white" href="<?= $Constantes ::WEB_ROOT."attache"?>">ListeAttaches</a>
          </li>
          <li class="nav-item">
            <a class="nav-link font-weight-bold h4 text-white" href="<?= $Constantes ::WEB_ROOT."professeur"?>" >Liste Professeur</a>
          </li>
        
          <li class="nav-item">
            <a class="nav-link font-weight-bold h4 text-white" href="<?= $Constantes ::WEB_ROOT."ajout"?>" >Ajouter Attache</a>
          </li>
          


          <!-- <li class="nav-item ">
            <a class="nav-link font-weight-bold h4 text-white " href="<?= $Constantes ::WEB_ROOT."deconnexion"?>">Déconnexion</a>
          </li> -->
          
          <!-- <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="dropdown03" data-bs-toggle="dropdown" aria-expanded="false">Dropdown</a>
            <ul class="dropdown-menu" aria-labelledby="dropdown03">
              <li><a class="dropdown-item" href="#">Action</a></li>
              <li><a class="dropdown-item" href="#">Another action</a></li>
              <li><a class="dropdown-item" href="#">Something else here</a></li>
            </ul>
          </li> -->
        </ul>
        <!-- <li class="nav-item ml-5 bg-dark" > -->

        
          <button><a class="nav-link font-weight-bold h5 text-black " href="<?= $Constantes ::WEB_ROOT."deconnexion"?>">Déconnexion</a></button>  
         
            
            <!-- <span class="bg-white p-2 text-primary" ><?=$_SESSION["user-connect"]->nom_complet?></span> -->
          <!-- </li> -->
     
        <!-- <form>
          <input class="form-control" type="text" placeholder="Search" aria-label="Search">
        </form> -->
      </div>
    </div>
  </nav>
 
 <!-- ********************************************************************* -->
 <?php


$hidden="hidden";
//PROBLEMEME DE ROLE DANS LA SESSION

// var_dump("ok");


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<?= $Constantes ::WEB_ROOT."css/style.css"?>">
    <link rel="stylesheet" href="<?= $Constantes ::WEB_ROOT."Bootstrap/css/bootstrap.min.css"?>">
    
    <title>ODC</title>
</head>
<body>
  <nav >
      <ul class="menu">
          <li><a href="<?= $Constantes ::WEB_ROOT."etudiants"?>">Liste etudiant</a></li>
          <li><a href="<?= $Constantes ::WEB_ROOT."inscrire"?>">Inscrire etudiant</a></li>
          <li><a href="<?= $Constantes ::WEB_ROOT."attache"?>">liste attache</a></li>
          
      </ul>
  </nav>
<!-- CHARGEMENT DES VUES -->
<div class="container mt-5">

    <?=$content_for_views?>
</div>


    <script src="<?= $Constantes ::WEB_ROOT."js/script.js"?>"></script>
    <script src="<?= $Constantes ::WEB_ROOT."Bootstrap/js/bootstrap.min.js"?>"></script>

</body>
</html>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<?= $Constantes ::WEB_ROOT."css/style.css"?>">
    <link rel="stylesheet" href="<?= $Constantes ::WEB_ROOT."Bootstrap/css/bootstrap.min.css"?>">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    <script defer src="/resources/fontawesome/js/all.js"></script>
    
    <title>ODC</title>
</head>
<body>

<!-- CHARGEMENT DES VUES -->
<!-- <div class="container mt-5"> -->
<?php
 


use App\Core\Role;
if ($this->role-> isConnect()) {
   // dd($_SESSION["user-connect"]->role);
    //dd($_SESSION);
    require_once ($Constantes::ROOT()."templates/layout/navbar.html.php");
}
?>
<div  class="container mt-5">
    <?=$content_for_views?>
    </div>
<!-- </div> -->


    <script src="<?= $Constantes ::WEB_ROOT."js/script.js"?>"></script>
    <script src="<?= $Constantes ::WEB_ROOT."Bootstrap/js/bootstrap.min.js"?>"></script>
    <script src="<?= $Constantes ::WEB_ROOT."Bootstrap/js/bootstrap.bundle.min.js"?>"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>


</body>
</html>
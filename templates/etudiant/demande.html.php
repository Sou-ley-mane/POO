<div  class="bg-primary p-4 mb-3">
  <h2 class="text- text-center text-white">DEMANDE ETUDIANT</h2>
</div>

<div class="d-flex justify-content-around  align-items-center  ">

<div class="border border-dark p-3 bg-dark w-50 ">
<div class="form-outline mb-4">
    <span class="text-white ml-5">Votre nom est : <?=$nom?></span>
  <!-- <label class="form-label  text-white" for="form4Example1">Votre nom est :</label>
  <input type="text" id="form4Example1" class="form-control" value=""  name="nomEtudiant"/> -->
  </div>                                                        
<form method="POST" action="demande" >
  <!-- Name input -->
  <div class="form-outline mb-4">
  <label class="form-label text-white " for="form4Example1">Motif de votre demande</label>
  <input type="text" id="form4Example1" class="form-control" placeholder="Ecrivez ici le motif de votre demande"  name="objet"/>
  </div>
   <!-- Email input -->
   <div class="form-outline mb-4">
    <label class="form-label  text-white" for="form4Example2">Date demande</label>
    <input type="date" id="form4Example2" class="form-control" name="date" value=""/>
  </div>
  <!-- ***************************************************************** -->

  <!-- Message input -->
  <div class="form-outline mb-4">
  <label class="form-label  text-white" for="form4Example3">Message</label>
  <textarea class="form-control" id="form4Example3" rows="4" placeholder="Rédigez votre demande" name="message"></textarea>
  </div>

  <!-- Checkbox -->
  <!-- <div class="form-check d-flex justify-content-center mb-4">
  <label class="form-check-label" for="form4Example4">
      Send me a copy of this message
    </label>
  <input class="form-check-input me-2" type="checkbox" value="" id="form4Example4" checked />
   
  </div> -->

  <!-- Submit button -->
  <button type="submit" class="btn btn-primary btn-block mb-4 font-weight-bold">Envoyer</button>
</form>
</div>

<div class="h-50 ml-5 mt-4  ">
 <img width="600" rounded-4 height="600" src="<?= $Constantes ::WEB_ROOT."/images/demande.jpg"?>" alt="souleymane">
</div>

</div>
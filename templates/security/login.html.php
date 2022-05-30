
<div class="container-fluid  mt-5">
  <div class="container border-5   py-5  bg-dark " >
    <div class="row d-flex justify-content-center align-items-center">
      <div class="col col-xl-10">
        <div class="card" style="border-radius: 1rem;">
          <div class="row g-0">
            <div class="col-md-6 col-lg-5 d-none d-md-block">
                <img src="<?= $Constantes ::WEB_ROOT."/images/SA.png"?>"
                    alt="login form" class="img-fluid mt-5" style="border-radius: 1rem 0 0 1rem; " />
                </div>
            <div class="col-md-6 col-lg-7 d-flex align-items-center">
              <div class="card-body p-4 p-lg-5 text-black">

                        <form action="login" method="POST">

                        <div class="d-flex align-items-center mb-3 pb-1">
                            <i class="fas fa-cubes fa-2x me-3" style="color: #ff6219;"></i>
                            <span class="h1 fw-bold mb-0 ml-5">ODC SCHOOL</span>
                        </div>

                        <h5 class="fw-normal mb-3 pb-3" style="letter-spacing: 1px;">AUTHENTIFICATION</h5>

                        <div class="form-outline mb-4">
                            <label class="form-label" for="form2Example17">Email address</label>
                            <input name="login" type="text" id="form2Example17" class="form-control form-control-lg" />
                        </div>

                        <div class="form-outline mb-4">
                            <label class="form-label" for="form2Example27">Password</label>
                            <input name="password" type="password" id="form2Example27" class="form-control form-control-lg" />
                        </div>

                        <div class="pt-1 mb-4">
                            <button class="btn btn-dark btn-lg btn-block" type="submit">Login</button>
                        </div>

                        <!-- <a class="small text-muted" href="#!">mot de passe oublié?</a>
                        <p class="mb-5 pb-lg-2" style="color: #393f81;">Don't have an account? <a href="#!"
                            style="color: #393f81;">Register here</a></p>
                        <a href="#!" class="small text-muted">Terms of use.</a>
                        <a href="#!" class="small text-muted">Privacy policy</a> -->
                        </form>

              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
<!-- </section> -->
</div>

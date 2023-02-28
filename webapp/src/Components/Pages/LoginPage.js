import {clearPage} from "../../utils/render";

const LoginPage = () => {
  clearPage();
  renderLoginForm();
};

function renderLoginForm() {
  const main = document.querySelector('main');
  const loginForm = document.createElement('div');
  loginForm.className='loginpage'
  loginForm.innerHTML =  ` 
  
      <div class="container py-5 h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
          <div class="col-xl-10">
            <div class="card rounded-3 text-black">
              <div class="row g-0">
                <div class="col-lg-12">
                  <div class="card-body p-md-5 mx-md-4">
    
                    <div class="text-center">
                      <img src="" style="width: 185px;" alt="logo">
                      <h4 class="mt-1 mb-5 pb-1">Identifiez-vous </h4>
                    </div>
    
                    <form>
                  
                      <div class='pos2'>
                      <div class="form-outline mb-4">
                        <input type="email" id="form2Example11" class="form-control" size="40"  placeholder="Email address" />
                        <label class="form-label" for="form2Example11">E-mail*</label>
                      </div>
                      </div>
    
                      <div class='pos2'>
                      <div class="form-outline mb-4">
                        <input type="password" id="form2Example22" class="form-control" size="90" />
                        <label class="form-label" for="form2Example22">Mot de passe*</label>
                      </div>
                      </div>
              
                      <div class="text-center pt-1 mb-5 pb-1">
                        <button class="btn btn-secondary btn-block fa-lg gradient-custom-2 mb-3" type="button">Se connecter</button>
                      </div>
    
                    </form>
    
                  </div>
                </div>
                
              </div>
            </div>
          </div>
        </div>
      </div>
    `
    main.appendChild(loginForm);


}

export default LoginPage;
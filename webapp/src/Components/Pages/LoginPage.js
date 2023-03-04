import { clearPage } from '../../utils/render';
import Navigate from '../Router/Navigate';

const LoginPage = () => {
  clearPage();
  renderLoginForm();
};

function renderLoginForm() {
  const main = document.querySelector('main');

  const loginForm = document.createElement('div');
  loginForm.className = 'container p-5';

  loginForm.innerHTML = `
    <h1 class="text-center mb-5">Identifiez-vous</h1>
    <div class="row justify-content-center">
      <form class="col-12 col-lg-6 col-xl-4">
        <div class="mb-3">
          <label for="input-email" class="form-label">E-mail</label>
          <input type="email" class="form-control" id="input-email" placeholder="nom@exemple.com" required />
        </div>
        <div class="mb-3">
          <label for="input-password" class="form-label">Mot de passe</label>
          <input type="password" class="form-control" id="input-password" placeholder="********" required />
        </div>
        <div class="mb-3 form-check">
          <input class="form-check-input" type="checkbox" value="" id="input-remember" />
          <label class="form-check-label" for="input-remember">Se souvenir de moi</label>
        </div>
        <input type="submit" value="S'identifier" class="btn btn-primary w-100" />
        <div class="hstack gap-2 mt-3 justify-content-between">
          <span>Pas de compte ?</span>
          <a href="#" id="register-link">Inscrivez-vous ici</a>
        </div>
      </form>
    </div>
  `;

  main.appendChild(loginForm);

  document.querySelector('#register-link').addEventListener('click', (e) => {
    e.preventDefault();
    Navigate('/register');
  });
}

export default LoginPage;

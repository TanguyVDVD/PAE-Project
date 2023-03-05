import { clearPage } from '../../utils/render';
import { isAuthenticated, setAuthenticatedUser } from '../../utils/auths';
import Navigate from '../Router/Navigate';

const LoginPage = () => {
  if (isAuthenticated()) {
    Navigate('/');
    return;
  }

  clearPage();
  renderLoginForm();
};

function renderLoginForm() {
  let isSubmitting = false;

  const main = document.querySelector('main');

  const loginForm = document.createElement('div');
  loginForm.className = 'container p-5';

  loginForm.innerHTML = `
    <h1 class="text-center mb-5">Identifiez-vous</h1>
    <div class="row justify-content-center">
      <form class="col-12 col-lg-6 col-xl-4">
        <div class="mb-3">
          <label for="input-email" class="form-label">E-mail</label>
          <input
            type="email"
            class="form-control"
            id="input-email"
            placeholder="nom@exemple.com"
            required
          />
        </div>
        <div class="mb-3">
          <label for="input-password" class="form-label">Mot de passe</label>
          <input
            type="password"
            class="form-control"
            id="input-password"
            placeholder="********"
            required
          />
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

  loginForm.querySelector('#register-link').addEventListener('click', (e) => {
    e.preventDefault();
    Navigate('/register');
  });

  loginForm.querySelector('form').addEventListener('submit', (e) => {
    e.preventDefault();

    if (isSubmitting) return;
    isSubmitting = true;

    renderError();

    const email = document.querySelector('#input-email').value;
    const password = document.querySelector('#input-password').value;
    const remember = document.querySelector('#input-remember').checked;

    fetch('/api/users/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password }),
    })
      .then((res) => res.json())
      .then((data) => {
        if (data.error) throw new Error(data.error);

        setAuthenticatedUser(data, remember);
        Navigate('/');
      })
      .catch((err) => {
        renderError(err.message);
      })
      .finally(() => {
        isSubmitting = false;
      });
  });

  main.appendChild(loginForm);
}

function renderError(error) {
  const container = document.querySelector('main > .container');

  if (container.querySelector('.alert')) {
    container.querySelector('.alert').remove();
  }

  if (!error) return;

  const alert = document.createElement('div');
  alert.className = 'alert alert-danger d-flex align-items-center';
  alert.setAttribute('role', 'alert');
  alert.innerHTML = `
    <i class="bi bi-exclamation-triangle-fill me-2"></i>
    <span>${error}</span>
  `;

  container.prepend(alert);
}

export default LoginPage;

import { clearPage } from '../../utils/render';
import { isAuthenticated, setAuthenticatedUser } from '../../utils/auths';
import Navigate from '../Router/Navigate';
import API from '../../utils/api';

const RegisterPage = () => {
  if (isAuthenticated()) {
    Navigate('/');
    return;
  }

  clearPage();
  renderRegisterForm();
};

function renderRegisterForm() {
  let isSubmitting = false;

  const main = document.querySelector('main');

  const registerForm = document.createElement('div');
  registerForm.className = 'container p-5';

  registerForm.innerHTML = `
    <h1 class="text-center mb-5">Inscrivez-vous</h1>
    <div class="row justify-content-center">
      <form class="col-12 col-xl-8">
        <div class="row row-cols-1 row-cols-md-2">
          <div class="col mb-3">
            <label for="input-lastname" class="form-label">Nom*</label>
            <input
              type="text"
              class="form-control"
              id="input-lastname"
              placeholder="Nom"
              required
            />
          </div>
          <div class="col mb-3">
            <label for="input-firstname" class="form-label">Prénom*</label>
            <input
              type="text"
              class="form-control"
              id="input-firstname"
              placeholder="Prénom"
              required
            />
          </div>
        </div>
        <div class="row row-cols-1 row-cols-md-2">
          <div class="col mb-3">
            <label for="input-email" class="form-label">E-mail*</label>
            <input
              type="email"
              class="form-control"
              id="input-email"
              placeholder="nom@exemple.com"
              required
            />
          </div>
          <div class="col mb-3">
            <label for="input-phone" class="form-label">Numéro de GSM*</label>
            <input
              type="tel"
              class="form-control"
              id="input-phone"
              placeholder="0xxxxxxxx"
              required
            />
          </div>
        </div>
        <div class="row row-cols-1 row-cols-md-2">
          <div class="col mb-3">
            <label for="input-password" class="form-label">Mot de passe*</label>
            <input
              type="password"
              class="form-control"
              id="input-password"
              placeholder="********"
              required
            />
          </div>
          <div class="col mb-3">
            <label for="input-password-confirm" class="form-label">
              Confirmer le mot de passe*
            </label>
            <input
              type="password"
              class="form-control"
              id="input-password-confirm"
              placeholder="********"
              required
            />
          </div>
        </div>
        <div class="row row-cols-1 row-cols-md-2">
          <div class="col mb-3">
            <label for="input-photo" class="form-label">Photo de profil</label>
            <input
              type="file"
              accept="image/png, image/jpeg"
              class="form-control"
              id="input-photo"
              placeholder=""
            />
          </div>
          <div class="col mb-3 align-self-end">
            <button type="submit" class="btn btn-primary w-100">S'inscrire</button>
          </div>
        </div>
        <div>
          <span class="text-muted">* Champs obligatoires</span>
        </div>
      </form>
    </div>
  `;

  // Check if the password and the password confirmation are the same
  const passwordInput = registerForm.querySelector('#input-password');
  const passwordConfirmInput = registerForm.querySelector('#input-password-confirm');
  passwordConfirmInput.addEventListener('input', () => {
    if (passwordInput.value !== passwordConfirmInput.value) {
      passwordConfirmInput.setCustomValidity(
        'La confirmation du mot de passe doit être identique au mot de passe.',
      );
    } else {
      passwordConfirmInput.setCustomValidity('');
    }
  });

  registerForm.querySelector('form').addEventListener('submit', (e) => {
    e.preventDefault();

    if (isSubmitting) return;
    isSubmitting = true;
    registerForm.classList.add('loading');

    renderError();

    const formData = new FormData();

    ['lastname', 'firstname', 'email', 'phone', 'password', 'photo'].forEach((key) => {
      const input = registerForm.querySelector(`#input-${key}`);

      formData.append(key, input.type === 'file' ? input.files[0] : input.value);
    });

    API.post('users/register', { body: formData })
      .then((data) => {
        setAuthenticatedUser(data);
        Navigate('/');
      })
      .catch((err) => {
        renderError(err.message);
      })
      .finally(() => {
        isSubmitting = false;
        registerForm.classList.remove('loading');
      });
  });

  main.appendChild(registerForm);
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

export default RegisterPage;

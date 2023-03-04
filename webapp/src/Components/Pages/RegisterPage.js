import { clearPage } from '../../utils/render';

const RegisterPage = () => {
  clearPage();
  renderRegisterForm();
};

function renderRegisterForm() {
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

  main.appendChild(registerForm);

  // Check if the password and the password confirmation are the same
  const passwordInput = document.querySelector('#input-password');
  const passwordConfirmInput = document.querySelector('#input-password-confirm');
  passwordConfirmInput.addEventListener('input', () => {
    if (passwordInput.value !== passwordConfirmInput.value) {
      passwordConfirmInput.setCustomValidity(
        'La confirmation du mot de passe doit être identique au mot de passe.',
      );
    } else {
      passwordConfirmInput.setCustomValidity('');
    }
  });
}

export default RegisterPage;

import { Modal } from 'bootstrap';

import Navigate from '../Router/Navigate';
import API from '../../utils/api';
import { getAuthenticatedUser, setAuthenticatedUser } from '../../utils/auths';
import { clearPage, renderError } from '../../utils/render';
import { formatDate, formatPhoneNumber } from '../../utils/format';

import noProfilePicture from '../../img/no_profile_picture.svg';
import noFurniturePhoto from '../../img/no_furniture_photo.svg';

const objects = [];

const UserPage = (params) => {
  objects.splice(0, objects.length);

  const id = parseInt(params.id, 10);

  clearPage();
  const main = document.querySelector('main');

  const authenticatedUser = getAuthenticatedUser();

  if (!authenticatedUser) {
    Navigate('/login');
    return;
  }

  if (authenticatedUser.role === null && authenticatedUser.id !== id) {
    Navigate('/');
    return;
  }

  main.innerHTML = `
    <div class="text-center my-5">
      <div class="spinner-border" role="status"></div>
    </div>
  `;

  // Use the my endpoint if the user is looking at their own profile
  API.get(`users/${authenticatedUser.id === id ? 'my' : id}`)
    .then((user) => {
      renderUserPage(user);
    })
    .catch((error) => {
      renderError(error.message);
    });
};

function renderUserPage(user) {
  const authenticatedUser = getAuthenticatedUser();

  const main = document.querySelector('main');

  const userPage = document.createElement('div');
  userPage.className = 'container p-5';

  userPage.innerHTML = `
    <div class="d-flex gap-3 flex-column flex-md-row align-items-center">
      <div>
        <img
          src="${user.photo ? API.getEndpoint(`users/${user.id}/photo`) : noProfilePicture}"
          onerror="this.src='${noProfilePicture}'"
          alt="user avatar"
          class="rounded-circle object-fit-cover"
          width="128"
          height="128"
        />
      </div>
      <div class="d-flex gap-3 flex-column align-items-center align-items-md-start">
        <div class="d-flex gap-2 justify-content-center align-items-baseline flex-wrap">
          <h1 class="m-0">${user.firstName} ${user.lastName}</h1>

          ${
            authenticatedUser.id === user.id
              ? `
                <a href="#" id="edit-profile-btn">
                  Modifier mes données
                </a>
              `
              : ''
          }
        </div>
        <div class="d-flex gap-3 flex-wrap justify-content-center justify-content-md-start">
          <div><a href="mailto:${user.email}">${user.email}</a></div>
          <div>${formatDate(user.registerDate)}</div>
          <div>
            <a href="tel:+32${user.phoneNumber.substring(1)}"
              >${formatPhoneNumber(user.phoneNumber)}</a
            >
          </div>
        </div>
        ${
          authenticatedUser.role === 'responsable' && authenticatedUser.id !== user.id
            ? `
              <div>
                <div class="form-check form-switch">
                  <input
                    class="form-check-input"
                    type="checkbox"
                    role="switch"
                    id="helper-switch"
                    ${user.role === 'aidant' ? 'checked' : ''}
                  />
                  <label class="form-check-label" for="helper-switch">Aidant</label>
                </div>
              </div>
            `
            : ''
        }
      </div>
    </div>

    <h2 class="my-5">Objets proposés</h2>

    <div id="objects">
      <div class="text-center my-5">
        <div class="spinner-border" role="status"></div>
      </div>
    </div>
  `;

  const editProfileBtn = userPage.querySelector('#edit-profile-btn');
  if (editProfileBtn) {
    editProfileBtn.addEventListener('click', (e) => {
      e.preventDefault();
      renderEditProfile(authenticatedUser);
    });
  }

  const helperSwitch = userPage.querySelector('#helper-switch');
  if (helperSwitch) {
    helperSwitch.addEventListener('change', (e) => {
      e.target.disabled = true;
      e.target.checked = !e.target.checked;

      API.patch(`users/${user.id}`, { body: { role: e.target.checked ? null : 'aidant' } })
        .then((updatedUser) => {
          e.target.checked = updatedUser.role === 'aidant';
        })
        .catch((error) => {
          renderError(error.message);
        })
        .finally(() => {
          e.target.disabled = false;
        });
    });
  }

  main.replaceChildren(userPage);

  fetchObjects(user);
}

async function fetchObjects(user) {
  if (objects.length === 0)
    API.get(`objects/user/${user.id}`)
      .then((result) => {
        objects.push(...result);

        const obj = document.querySelector('#objects');

        if (objects.length === 0) {
          obj.innerHTML = `
            <div class="text-center text-muted my-5">
              <p>Aucun objet proposé</p>
            </div>
          `;
          return;
        }

        obj.className = 'row row-cols-1 row-cols-md-2 row-cols-lg-4 g-5 text-center';

        obj.innerHTML = objects
          .map(
            (object) => `
              <div class="col">
                <div class="card card-object" data-id="${object.id}" role="button">
                  <img
                    src="${
                      object.photo
                        ? API.getEndpoint(`objects/${object.id}/photo`)
                        : noFurniturePhoto
                    }"
                    onerror="this.src='${noFurniturePhoto}'"
                    class="card-img-top"
                    alt="Photo : ${object.description}"
                  />
                  <div class="card-title fw-bold m-0">${object.objectType}</div>
                  <div class="card-body pt-0">
                    ${object.description}
                  </div>
                </div>
              </div>
            `,
          )
          .join('');

        const cards = document.querySelectorAll('.card-object');
        cards.forEach((card) => {
          card.addEventListener('click', (e) => {
            const { id } = e.currentTarget.dataset;
            Navigate(`/object/${id}`);
          });
        });
      })
      .catch((error) => {
        renderError(error.message);
      });
}

function renderEditProfile(user) {
  const main = document.querySelector('main');

  const editProfile = document.createElement('div');
  editProfile.className = 'modal fade';
  editProfile.tabIndex = -1;

  const html = String.raw;
  editProfile.innerHTML = html`
    <div class="modal-dialog modal-dialog-centered modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Modifier mes données</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <div class="justify-content-center">
            <form id="edit-profile-form">
              <div class="row row-cols-1 row-cols-md-2">
                <div class="col mb-3">
                  <label for="input-lastName" class="form-label">Nom</label>
                  <input
                    type="text"
                    class="form-control"
                    id="input-lastName"
                    name="lastName"
                    placeholder="Nom"
                  />
                </div>
                <div class="col mb-3">
                  <label for="input-firstName" class="form-label">Prénom</label>
                  <input
                    type="text"
                    class="form-control"
                    id="input-firstName"
                    name="firstName"
                    placeholder="Prénom"
                  />
                </div>
              </div>
              <div class="row row-cols-1 row-cols-md-2">
                <div class="col mb-3">
                  <label for="input-email" class="form-label">E-mail</label>
                  <input
                    type="email"
                    class="form-control"
                    id="input-email"
                    name="email"
                    placeholder="nom@exemple.com"
                  />
                </div>
                <div class="col mb-3">
                  <label for="input-phoneNumber" class="form-label">Numéro de GSM</label>
                  <input
                    type="tel"
                    class="form-control"
                    id="input-phoneNumber"
                    name="phoneNumber"
                    placeholder="0xxxxxxxx"
                  />
                </div>
              </div>
              <div class="row row-cols-1 row-cols-md-2">
                <div class="col mb-3">
                  <label for="input-password" class="form-label">Nouveau mot de passe</label>
                  <input
                    type="password"
                    class="form-control"
                    id="input-password"
                    name="password"
                    placeholder="********"
                  />
                </div>
                <div class="col mb-3">
                  <label for="input-passwordConfirm" class="form-label">
                    Confirmer nouveau mot de passe
                  </label>
                  <input
                    type="password"
                    class="form-control"
                    id="input-passwordConfirm"
                    name="passwordConfirm"
                    placeholder="********"
                  />
                </div>
              </div>
              <div class="row row-cols-1 row-cols-md-2">
                <div class="col mb-3">
                  <div class="hstack gap-2 justify-content-between">
                    <label for="input-photo" class="form-label">Photo de profil</label>
                    ${user.photo
                      ? html`
                          <div class="form-check form-check-reverse form-switch">
                            <label class="form-check-label" for="input-removePhoto"
                              >Supprimer</label
                            >
                            <input
                              class="form-check-input"
                              type="checkbox"
                              role="switch"
                              id="input-removePhoto"
                              name="removePhoto"
                            />
                          </div>
                        `
                      : ''}
                  </div>
                  <input
                    type="file"
                    accept="image/png, image/jpeg"
                    class="form-control"
                    id="input-photo"
                    name="photo"
                    placeholder=""
                  />
                </div>
                <div class="col mb-3">
                  <img
                    src="${user.photo
                      ? API.getEndpoint(`users/${user.id}/photo`)
                      : noProfilePicture}"
                    onerror="this.src='${noProfilePicture}'"
                    class="rounded-circle object-fit-cover"
                    width="70"
                    height="70"
                    id="profile-picture-preview"
                  />
                </div>
              </div>
              <br />
              <div class="row row-cols-1 row-cols-md-2">
                <div class="col mb-3">
                  <label for="input-currentPassword" class="form-label">
                    Mot de passe actuel
                  </label>
                  <input
                    type="password"
                    class="form-control"
                    id="input-currentPassword"
                    name="currentPassword"
                    placeholder="********"
                    required
                  />
                </div>
                <div class="col mb-3 align-self-end">
                  <button type="submit" class="btn btn-primary w-100">Sauvegarder</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  `;

  const editProfileModal = new Modal(editProfile);

  // Fill the form with the current user data
  ['firstName', 'lastName', 'email'].forEach((field) => {
    editProfile.querySelector(`#input-${field}`).value = user[field];
  });
  editProfile.querySelector(`#input-phoneNumber`).value = formatPhoneNumber(user.phoneNumber);

  // Handle the photo preview
  const currentProfilePicture = user.photo
    ? API.getEndpoint(`users/${user.id}/photo`)
    : noProfilePicture;
  const photoInput = editProfile.querySelector('#input-photo');
  const profilePicturePreview = editProfile.querySelector('#profile-picture-preview');
  const removePhoto = editProfile.querySelector('#input-removePhoto');
  if (removePhoto)
    removePhoto.addEventListener('change', (e) => {
      profilePicturePreview.src = e.target.checked ? noProfilePicture : currentProfilePicture;

      if (e.target.checked) {
        photoInput.value = '';
      }

      photoInput.disabled = e.target.checked;
    });

  photoInput.addEventListener('change', (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (ev) => {
        profilePicturePreview.src = ev.target.result;
      };
      reader.readAsDataURL(file);
    } else {
      profilePicturePreview.src = currentProfilePicture;
    }
  });

  const editProfileForm = editProfile.querySelector('#edit-profile-form');
  let formIsSubmitting = false;
  editProfileForm.addEventListener('submit', (e) => {
    e.preventDefault();

    if (formIsSubmitting) return;

    renderError(null, editProfileForm);

    const form = Object.fromEntries(new FormData(e.target));

    if (form.password !== form.passwordConfirm) {
      renderError(
        'Le nouveau mot de passe et sa confirmation ne correspondent pas.',
        editProfileForm,
      );
      return;
    }

    const body = {};

    ['firstName', 'lastName', 'email', 'password'].forEach((field) => {
      if (form[field] && form[field] !== user[field]) {
        body[field] = form[field];
      }
    });

    // Special case for the phone number
    if (form.phoneNumber !== formatPhoneNumber(user.phoneNumber)) {
      body.phoneNumber = form.phoneNumber;
    }

    const promises = [];

    if (Object.keys(body).length > 0) {
      body.currentPassword = form.currentPassword;
      promises.push(() => API.patch(`users/${user.id}`, { body }));
    }

    // HTTP DELETE requests cannot have a body, so we use the same endpoint as the PUT request
    // but with an empty photo
    if (form.removePhoto || (form.photo && form.photo.size > 0)) {
      const formData = new FormData();
      formData.append('photo', form.removePhoto ? null : form.photo);
      formData.append('password', form.currentPassword);
      promises.push(() => API.put(`users/${user.id}/photo`, { body: formData }));
    }

    if (promises.length === 0) {
      renderError("Aucune modification n'a été apportée.", editProfileForm);
      return;
    }

    formIsSubmitting = true;
    promises
      .reduce((p, next) => p.then(next), Promise.resolve())
      .then((res) => {
        if (res) {
          setAuthenticatedUser(res);
          renderUserPage(res);
        }

        editProfileModal.hide();
      })
      .catch((err) => {
        renderError(err.message, editProfileForm);
      })
      .finally(() => {
        formIsSubmitting = false;
      });
  });

  main.appendChild(editProfile);

  editProfileModal.show();
}

export default UserPage;

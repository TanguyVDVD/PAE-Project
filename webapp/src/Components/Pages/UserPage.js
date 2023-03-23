import Navigate from '../Router/Navigate';
import API from '../../utils/api';
import {getAuthenticatedUser} from '../../utils/auths';
import {clearPage, renderError} from '../../utils/render';
import {formatDate, formatPhoneNumber} from '../../utils/format';

import noProfilePicture from '../../img/no_profile_picture.webp';

const UserPage = (params) => {
  const id = parseInt(params.id, 10);

  clearPage();
  const main = document.querySelector('main');

  const authenticatedUser = getAuthenticatedUser();

  if (!authenticatedUser) {
    Navigate('/login');
    return;
  }

  main.innerHTML = html`
    <div class="text-center my-5">
      <div class="spinner-border" role="status"></div>
    </div>
  `;

  // Use the my endpoint if the user is looking at their own profile
  API.get(`users/${authenticatedUser.id === id ? 'my' : id}`)
  .then((user) => {
    renderUserPage(user);
    fetchObjects(user);
  })
  .catch((error) => {
    renderError(error.message);
  });
};

// TODO: remove this
function html(strings, ...values) {
  let str = '';
  strings.forEach((string, i) => {
    str += string + (values[i] || '');
  });
  return str;
}

function renderUserPage(user) {
  const authenticatedUser = getAuthenticatedUser();

  const main = document.querySelector('main');

  const userPage = document.createElement('div');
  userPage.className = 'container p-5';

  userPage.innerHTML = html`
    <div class="d-flex gap-3 flex-column flex-md-row align-items-center">
      <div>
        <img
            src="${user.photo ? API.getEndpoint(`users/${user.id}/photo`)
                : noProfilePicture}"
            onerror="this.src='${noProfilePicture}'"
            alt="user avatar"
            class="rounded-circle object-fit-cover"
        />
      </div>
      <div
          class="d-flex gap-3 flex-column align-items-center align-items-md-start">
        <div
            class="d-flex gap-2 justify-content-center align-items-baseline flex-wrap">
          <h1 class="m-0">${user.firstName} ${user.lastName}</h1>

          ${authenticatedUser.id === user.id
              ? html`
                <a href="#">
                  Modifier mes données
                </a>
              `
              : ''}
        </div>
        <div
            class="d-flex gap-3 flex-wrap justify-content-center justify-content-md-start">
          <div><a href="mailto:${user.email}">${user.email}</a></div>
          <div>${formatDate(user.registerDate)}</div>
          <div>
            <a href="tel:+32${user.phoneNumber.substring(1)}"
            >${formatPhoneNumber(user.phoneNumber)}</a
            >
          </div>
        </div>
        ${authenticatedUser.id === 1 && authenticatedUser.id !== user.id
            ? html`
              <div>
                <div class="form-check form-switch">
                  <input
                      class="form-check-input"
                      type="checkbox"
                      role="switch"
                      id="helper-switch"
                      ${user.helper ? 'checked' : ''}
                  />
                  <label class="form-check-label"
                         for="helper-switch">Aidant</label>
                </div>
              </div>
            `
            : ''}
      </div>
    </div>

    <h2 class="my-5">Objets proposés</h2>

    <div id="objects">
      <div class="text-center my-5">
        <div class="spinner-border" role="status"></div>
      </div>
    </div>
  `;

  const helperSwitch = userPage.querySelector('#helper-switch');
  if (helperSwitch) {
    helperSwitch.addEventListener('change', (e) => {
      e.target.disabled = true;
      e.target.checked = !e.target.checked;

      API.patch(`users/${user.id}`, {body: {helper: !e.target.checked}})
      .then((updatedUser) => {
        e.target.checked = updatedUser.helper;
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
}

async function fetchObjects() {
  const placeholderObjects = [
    {
      id: 1,
      type: 'Fauteuil',
      name: 'fauteuil tapissé',
      image: '',
    },
    {
      id: 2,
      type: 'Matériel de cuisine',
      name: '3 casseroles',
      image: '',
    },
    {
      id: 3,
      type: 'Matériel de cuisine',
      name: '4 poêles',
      image: '',
    },
    {
      id: 4,
      type: 'Couvertures',
      name: 'couvertures en bon état',
      image: '',
    },
    {
      id: 5,
      type: 'Meuble',
      name: 'anciens tableaux',
      image: '',
    },
    {
      id: 6,
      type: 'Meuble',
      name: 'décoration de cactus',
      image: '',
    },
    {
      id: 7,
      type: 'Matelas',
      name: 'matelas blanc',
      image: '',
    },
    {
      id: 8,
      type: 'Matériel de cuisine',
      name: '4 poêles',
      image: '',
    },
  ];

  setTimeout(() => {
    const objects = document.querySelector('#objects');
    objects.className = 'row row-cols-1 row-cols-md-2 row-cols-lg-4 g-5 text-center';

    objects.innerHTML = placeholderObjects
    .map(
        (object) => html`
          <div class="col">
            <div class="card card-object" data-id="${object.id}" role="button">
              <img src="${object.image}" class="card-img-top"
                   alt="Photo : ${object.name}"/>
              <div class="card-title fw-bold m-0">${object.type}</div>
              <div class="card-body pt-0">
                ${object.name}
              </div>
            </div>
          </div>
        `,
    )
    .join('');
  }, 3000);
}

export default UserPage;

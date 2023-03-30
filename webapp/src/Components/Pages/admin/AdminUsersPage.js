import Navigate from '../../Router/Navigate';
import { getAuthenticatedUser } from '../../../utils/auths';
import { clearPage } from '../../../utils/render';
import { formatPhoneNumber } from '../../../utils/format';
import { dateStringtoGoodFormat } from '../../../utils/dates';
import API from '../../../utils/api';

import noProfilePicture from '../../../img/no_profile_picture.svg';

const AdminUsersPage = () => {
  const user = getAuthenticatedUser();

  if (!user || !user.isHelper) {
    Navigate('/');
    return;
  }

  clearPage();
  renderAdminUsersPage();
  searchUsers();
};

function renderAdminUsersPage() {
  const main = document.querySelector('main');
  const div = document.createElement('div');
  div.className = 'container my-5';

  div.innerHTML = `
    <h2>Utilisateurs</h2>
    <form class="input-group">
      <input type="text" class="form-control border-end-0" placeholder="Rechercher..." />
      <button class="btn border" type="submit">
        <i class="bi bi-search"></i>
      </button>
    </form>
    <div id="users-table"></div>
  `;

  div.querySelector('form').addEventListener('keyup', (e) => {
    e.preventDefault();

    const search = e.target.value;
    searchUsers(search);
  });

  main.appendChild(div);
}

async function searchUsers(query = '') {
  const table = document.getElementById('users-table');

  table.innerHTML = `
    <div class="text-center my-5">
      <div class="spinner-border" role="status"></div>
    </div>
  `;

  API.get(`users?query=${encodeURIComponent(query)}`).then((users) => {
    document.getElementById('users-table').innerHTML = `
        <table class="table table-striped table-hover mt-3 border border-1">
          <thead>
            <tr>
              <th scope="col">ID</th>
              <th scope="col">Photo</th>
              <th scope="col">Nom</th>
              <th scope="col">Prénom</th>
              <th scope="col">Numéro de GSM</th>
              <th scope="col">Adresse mail</th>
              <th scope="col">Date d'inscription</th>
              <th scope="col">Rôle</th>
              <th scope="col">&nbsp;</th>
            </tr>
          </thead>
          <tbody>
            ${users
              .map(
                (user) => `
                  <tr>
                    <th scope="row" class="font-monospace">${user.id}</th>
                    <td>
                      <a href="#" role="button" data-id="${user.id}">
                        <img
                          src="${
                            user.photo
                              ? API.getEndpoint(`users/${user.id}/photo`)
                              : noProfilePicture
                          }"
                          onerror="this.src='${noProfilePicture}'"
                          alt="avatar"
                          width="64"
                          height="64"
                          class="rounded-circle object-fit-cover"
                          alt="Photo de ${user.firstName} ${user.lastName}"
                          loading="lazy"
                        />
                      </a>
                    </td>
                    <td>${user.lastName}</td>
                    <td>${user.firstName}</td>
                    <td class="font-monospace">${formatPhoneNumber(user.phoneNumber)}</td>
                    <td>${user.email}</td>
                    <td class="font-monospace">${dateStringtoGoodFormat(user.registerDate)}</td>
                    <td>
                      ${
                        user.isHelper
                          ? `<span class="badge bg-primary">Aidant</span>`
                          : `<span class="badge bg-secondary">Utilisateur</span>`
                      }
                    </td>

                    <td>
                      <a href="#" class="btn btn-link" role="button" data-id="${user.id}">
                        Voir plus
                      </a>
                    </td>
                  </tr>
                `,
              )
              .join('')}
          </tbody>
        </table>
      `;

    table.querySelectorAll('a[data-id]').forEach((link) => {
      link.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate(`/user/${e.currentTarget.dataset.id}`);
      });
    });
  });
}

export default AdminUsersPage;

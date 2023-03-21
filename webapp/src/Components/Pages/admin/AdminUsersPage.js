import Navigate from '../../Router/Navigate';
import { getAuthenticatedUser } from '../../../utils/auths';
import { clearPage } from '../../../utils/render';
import API from '../../../utils/api';

const AdminUsersPage = () => {
  const user = getAuthenticatedUser();

  if (!user || !user.helper) {
    Navigate('/');
    return;
  }

  clearPage();
  renderAdminUsersPage();
  fetchUsers();
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
    fetchUsers(search);
  });

  main.appendChild(div);
}

async function fetchUsers(query = '') {
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
              <th scope="col">Nom</th>
              <th scope="col">Prénom</th>
              <th scope="col">Rôle</th>
              <th scope="col">&nbsp;</th>
            </tr>
          </thead>
          <tbody>
            ${users
              .map(
                (user) => `
                  <tr>
                    <th scope="row">${user.id}</th>
                    <td>${user.lastName}</td>
                    <td>${user.firstName}</td>
                    <td>${user.helper ? 'Aideur' : 'Utilisateur'}</td>
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
        Navigate(`/user/${e.target.dataset.id}`);
      });
    });
  });
}

export default AdminUsersPage;

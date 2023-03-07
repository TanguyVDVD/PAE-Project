import Navigate from '../../Router/Navigate';
import { getAuthenticatedUser } from '../../../utils/auths';
import { clearPage } from '../../../utils/render';

const AdminUsersPage = () => {
  const user = getAuthenticatedUser();

  if (!user || !user.isHelper) {
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
    <div class="input-group">
      <input type="text" class="form-control border-end-0" placeholder="Rechercher..." />
      <button class="btn border" type="submit">
        <i class="bi bi-search"></i>
      </button>
    </div>
    <table class="table table-striped table-hover mt-3 border border-1" id="users-table">
      <thead>
        <tr>
          <th scope="col">ID</th>
          <th scope="col">Nom</th>
          <th scope="col">Prénom</th>
          <th scope="col">Rôle</th>
          <th scope="col">&nbsp;</th>
        </tr>
      </thead>
      <tbody></tbody>
    </table>
  `;

  main.appendChild(div);
}

function fetchUsers() {
  // TODO: fetch
  const users = [
    {
      id: 1,
      lastName: 'Riez',
      firstName: 'Christophe',
      isHelper: true,
    },
    {
      id: 2,
      lastName: 'Vdv',
      firstName: 'Constantine',
      isHelper: true,
    },
    {
      id: 3,
      lastName: 'lsh',
      firstName: 'Bernard',
      isHelper: true,
    },
    {
      id: 4,
      lastName: 'bdh',
      firstName: 'Lise',
      isHelper: false,
    },
  ];

  document.getElementById('users-table').querySelector('tbody').innerHTML = users
    .map(
      (user) => `
        <tr>
          <th scope="row">${user.id}</th>
          <td>${user.lastName}</td>
          <td>${user.firstName}</td>
          <td>${user.isHelper ? 'Aideur' : 'Utilisateur'}</td>
          <td>
            <a href="#" class="btn btn-link" role="button">
              Voir plus
            </a>
          </td>
        </tr>
      `,
    )
    .join('');
}

export default AdminUsersPage;

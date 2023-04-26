import Autocomplete from 'bootstrap5-autocomplete';
import Navigate from '../../Router/Navigate';
import {getAuthenticatedUser} from '../../../utils/auths';
import {clearPage, renderError} from '../../../utils/render';
import {formatPhoneNumber} from '../../../utils/format';
import {dateStringtoGoodFormat} from '../../../utils/dates';
import API from '../../../utils/api';
import noProfilePicture from '../../../img/no_profile_picture.svg';

let searchQuery = '';

const AdminUsersPage = () => {

  searchQuery = '';

  const user = getAuthenticatedUser();

  if (!user || user.role === 'utilisateur') {
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
      <input type="text" class="form-control autocomplete border-end-0" placeholder="Rechercher..." />
      <button class="btn border" type="submit">
        <i class="bi bi-search"></i>
      </button>
    </form>
    <div id="users-table" class="table-responsive"></div>
  `;

  const fullNames = [];

  API.get(`users?query=${encodeURIComponent('')}`)
  .then((users) => {
    if (users !== null) {
      users.forEach((user) => {
        fullNames.push(user.firstName.concat(' ', user.lastName));
      });
    }

    Autocomplete.init('input.autocomplete', {
      items: fullNames,
      fullWidth: true,
      fixed: true,
      autoselectFirst: false,
      updateOnSelect: true,
    });
  })
  .catch((err) => {
    renderError(err.message);
  });

  div.querySelector('form').addEventListener('submit', (e) => {
    e.preventDefault();

    const search = e.target.querySelector('input').value;
    if (search === searchQuery) {
      return;
    }
    searchQuery = search;

    searchUsers(searchQuery);
  });

  div.querySelector('form').addEventListener('change', (e) => {
    e.preventDefault();

    e.currentTarget.dispatchEvent(new Event('submit'));
  });

  main.appendChild(div);
}

async function searchUsers(query = '') {
  const table = document.getElementById('users-table');

  table.innerHTML = `
    <div class="position-absolute top-50 start-50 translate-middle">
      <div class="spinner-border" role="status"></div>
    </div>
  `;

  API.get(`users?query=${encodeURIComponent(query)}`).then((users) => {
    renderUsersTable(table, users);
  });
}

const columns = [
  {
    name: 'id',
    label: 'ID',
    sortable: true,
    monospace: true,
  },
  {
    name: 'photo',
    label: 'Photo',
    render: (user) => `
      <a href="#" role="button" data-id="${user.id}">
        <img
          src="${user.photo ? API.getEndpoint(`users/${user.id}/photo`)
        : noProfilePicture}"
          onerror="this.src='${noProfilePicture}'"
          width="64"
          height="64"
          class="rounded-circle object-fit-cover"
          alt="Photo de ${user.firstName} ${user.lastName}"
          loading="lazy"
        />
      </a>
    `,
  },
  {
    name: 'lastName',
    label: 'Nom',
    sortable: true,
  },
  {
    name: 'firstName',
    label: 'Prénom',
    sortable: true,
  },
  {
    name: 'phoneNumber',
    label: 'Numéro de GSM',
    sortable: true,
    monospace: true,
    render: (user) => formatPhoneNumber(user.phoneNumber),
  },
  {
    name: 'email',
    label: 'Adresse mail',
    sortable: true,
  },
  {
    name: 'registerDate',
    label: "Date d'inscription",
    sortable: true,
    monospace: true,
    render: (user) => dateStringtoGoodFormat(user.registerDate),
  },
  {
    name: 'role',
    label: 'Rôle',
    sortable: true,
    render: (user) => {
      const roleColors = {
        responsable: 'danger',
        aidant: 'success',
        utilisateur: 'primary',
      };

      return `<div class="badge bg-${roleColors[user.role]}">${user.role
      .charAt(0)
      .toUpperCase()
      .concat(user.role.slice(1))}</div>`;
    },
  },
  {
    name: 'actions',
    label: '&nbsp;',
    render: (user) => `
      <a href="#" class="btn btn-link" role="button" data-id="${user.id}">
        Voir plus
      </a>
    `,
  },
];
let sortingBy = null;
let sortingDirection = 'asc';

function renderUsersTable(table, users) {
  document.getElementById('users-table').innerHTML = `
    <table class="table table-striped table-hover mt-3 border border-1">
      <thead>
        <tr>
          ${columns
  .map(
      (column, i) => `
                <th
                  scope="col"
                  class="
                    text-nowrap
                    ${column.sortable ? 'cursor-pointer' : ''}
                    ${sortingBy === i ? `sorting-${sortingDirection}` : ''}
                  "
                >
                  ${column.label}
                </th>
              `,
  )
  .join('')}
        </tr>
      </thead>
      <tbody>
        ${users
  .map(
      (user) => `
              <tr>
                ${columns
      .map(
          (column) => `
                      <td class="${column.monospace ? 'font-monospace' : ''}">
                        ${column.render ? column.render(user)
              : user[column.name]}
                      </td>
                    `,
      )
      .join('')}
              </tr>
            `,
  )
  .join('')}
      </tbody>
    </table>
  `;

  // Add event listeners to the links
  table.querySelectorAll('a[data-id]').forEach((link) => {
    link.addEventListener('click', (e) => {
      e.preventDefault();
      Navigate(`/user/${e.currentTarget.dataset.id}`);
    });
  });

  // Column sorting
  table.querySelectorAll('th').forEach((th) => {
    const index = th.cellIndex;

    if (columns[index].sortable) {
      th.addEventListener('click', () => {
        // Reset sorting if the column is already sorted by
        if (sortingBy === index) {
          if (sortingDirection === 'desc') {
            sortingBy = null;
            sortingDirection = 'asc';
          } else {
            sortingDirection = 'desc';
          }
        } else {
          sortingBy = index;
          sortingDirection = 'asc';
        }

        const column = columns[sortingBy] ?? columns[0];

        // use localeCompare to sort strings
        const sortedUsers = users.sort(
            (a, b) =>
                (typeof a[column.name] === 'string'
                    ? a[column.name].localeCompare(b[column.name])
                    : a[column.name] - b[column.name]) * (sortingDirection
                === 'asc' ? 1 : -1),
        );

        renderUsersTable(table, sortedUsers);
      });
    }
  });
}

export default AdminUsersPage;

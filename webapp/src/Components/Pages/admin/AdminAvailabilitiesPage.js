import {getAuthenticatedUser} from "../../../utils/auths";
import Navigate from "../../Router/Navigate";
import {clearPage} from "../../../utils/render";

const AdminAvailabilitiesPage = () => {
  const authenticatedUser = getAuthenticatedUser();

  if (!authenticatedUser || authenticatedUser.role === null) {
    Navigate('/');
    return;
  }

  clearPage();
  renderAdminAvailabilitiesPage();
  renderDatePicker();
};

function renderAdminAvailabilitiesPage() {
  const main = document.querySelector('main');
  const div = document.createElement('div');
  div.className = 'container my-5';

  div.innerHTML = `
    <h2>Disponibilités</h2>
    <div id="div-date-picker"></div>
  `;

  main.appendChild(div);
}

function renderDatePicker() {
  const main = document.querySelector('main');

  const authenticatedUser = getAuthenticatedUser();

  const div = document.createElement('div');

  div.className = 'container p-5';

  div.innerHTML = ``;
}

export default AdminAvailabilitiesPage;
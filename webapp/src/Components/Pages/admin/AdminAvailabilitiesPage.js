import flatpickr from 'flatpickr';
import {getAuthenticatedUser} from "../../../utils/auths";
import Navigate from "../../Router/Navigate";
import {clearPage} from "../../../utils/render";
import API from "../../../utils/api";
import {dateStringtoGoodFormat} from "../../../utils/dates";

const AdminAvailabilitiesPage = () => {
  const authenticatedUser = getAuthenticatedUser();

  if (!authenticatedUser || authenticatedUser.role === null) {
    Navigate('/');
    return;
  }

  clearPage();
  renderAdminAvailabilitiesPage();
};

function renderAdminAvailabilitiesPage() {
  const main = document.querySelector('main');
  const div = document.createElement('div');
  div.className = 'container my-5';


  div.innerHTML = `
    <h2>Disponibilités</h2>
    <div id="div-date-picker" class="text-center">
      <input type="text" id="date-picker" class="text-center">
    </div>
  `;

  main.appendChild(div);

  renderDatePicker("#date-picker");
}

function renderDatePicker(datePickerId) {
  flatpickr(datePickerId);

  API.get('/availabilities').then((availabilities) => {
    const defaultAvailabilities = [];

    availabilities.forEach((item) => {
      defaultAvailabilities.push(dateStringtoGoodFormat(item.date));
    })

    flatpickr("#date-picker", {
      static: true,
      position: "center",
      inline: true,
      mode: "multiple",
      dateFormat: "d-m-Y",
      defaultDate: defaultAvailabilities,
      minDate: "today",
    });
  });
}

export default AdminAvailabilitiesPage;
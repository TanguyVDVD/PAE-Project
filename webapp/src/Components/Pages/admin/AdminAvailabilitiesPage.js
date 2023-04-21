import flatpickr from 'flatpickr';
import "flatpickr/dist/l10n/fr";
import {getAuthenticatedUser} from "../../../utils/auths";
import Navigate from "../../Router/Navigate";
import {clearPage, renderError} from "../../../utils/render";
import API from "../../../utils/api";
import {invertDateFormat} from "../../../utils/dates";

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
  const divDatePicker = document.createElement('divDatePicker');
  div.className = 'container my-5';

  div.innerHTML = `
    <h2>Disponibilités</h2>
  `;

  divDatePicker.innerHTML = `
    <div class="text-center my-5">
      <div class="spinner-border" role="status"></div>
    </div>
  `;

  div.append(divDatePicker)

  main.append(div);

  let defaultAvailabilities = [];

  API.get('/availabilities').then((availabilities) => {
    availabilities.forEach((item) => {
      defaultAvailabilities.push(invertDateFormat(item.date));
    });

    divDatePicker.innerHTML = `
      <div id="div-date-picker" class="text-center">
        <input type="text" id="date-picker" class="text-center">
      </div>
    `;

    const datePicker = document.getElementById("date-picker");

    renderDatePicker("#date-picker", defaultAvailabilities);

    defaultAvailabilities = datePicker.value.split(", ");

    datePicker.addEventListener("change", (event) => {
      const datesAfterChange = event.target.value.split(", ");

      if (defaultAvailabilities.length < datesAfterChange.length){
        const lastDate = invertDateFormat(datesAfterChange[datesAfterChange.length-1]);
        API.post('/availabilities', { body: { date: lastDate } })
        .catch((err) => {
          renderError(err.message);
        })
        .finally(AdminAvailabilitiesPage);
      }
      else if (defaultAvailabilities.length > datesAfterChange.length || datesAfterChange[0] === ""){
        defaultAvailabilities.forEach((availability) => {
          // Finding missing date to delete
          if (!datesAfterChange.includes(availability)){
            availabilities.forEach((item) => {
              const date = invertDateFormat(item.date);
              // Finding the id to delete
              if (date === availability){
                API.delete(`/availabilities/${item.id}`)
                .then(AdminAvailabilitiesPage)
                .catch((err) => {
                  AdminAvailabilitiesPage();
                  renderError(err.message);
                });
              }
            })
          }
        })
      }
    });
  });
}

function renderDatePicker(datePickerId, defaultAvailabilities) {
  flatpickr(datePickerId, {
    altInput: true,
    altInputClass : "invisible",
    static: true,
    position: "center",
    inline: true,
    mode: "multiple",
    locale: "fr",
    dateFormat: "d-m-Y",
    defaultDate: defaultAvailabilities,
    minDate: "today",
  });
}

export default AdminAvailabilitiesPage;
import Navigate from '../../Router/Navigate';
import { getAuthenticatedUser } from '../../../utils/auths';
import { clearPage } from '../../../utils/render';
import API from '../../../utils/api';
import { dateStringtoGoodFormat, subtractDates } from '../../../utils/dates';
import { setUserOrPhoneNumber } from '../../../utils/objects';

import noFurniturePhoto from '../../../img/no_furniture_photo.svg';

let searchQuery = '';

const AdminOffersPage = () => {
  searchQuery = '';

  const authenticatedUser = getAuthenticatedUser();

  if (!authenticatedUser || authenticatedUser.role === null) {
    Navigate('/');
    return;
  }

  clearPage();
  renderAdminOffersPage();
  renderOffers();
};

function renderAdminOffersPage() {
  const main = document.querySelector('main');
  const div = document.createElement('div');
  div.className = 'container my-5';

  div.innerHTML = `
    <h2>Propositions</h2>
    <form class="input-group">
      <input type="text" class="form-control border-end-0" placeholder="Rechercher..." />
      <button class="btn border" type="submit">
        <i class="bi bi-search"></i>
      </button>
    </form>
    <div id="offers-list"></div>
  `;

  div.querySelector('form').addEventListener('submit', (e) => {
    e.preventDefault();

    const search = e.target.querySelector('input').value;
    if (search === searchQuery) return;
    searchQuery = search;

    renderOffers(searchQuery);
  });

  div.querySelector('form').addEventListener('keyup', (e) => {
    e.preventDefault();

    e.currentTarget.dispatchEvent(new Event('submit'));
  });

  main.appendChild(div);
}

async function renderOffers(query = '') {
  const offersList = document.getElementById('offers-list');

  offersList.innerHTML = `
    <div class="text-center my-5">
      <div class="spinner-border" role="status"></div>
    </div>
  `;

  API.get(`objects/offers?query=${encodeURIComponent(query)}`).then((offers) => {
    document.getElementById('offers-list').innerHTML = `
      <div class="container mt-5 mb-5">
        <div class="d-flex justify-content-center row">
          <div class="col-md-10">
            ${offers
              .map(
                (offer) => `
                  <div class="row p-2 bg-white border rounded">
                    <div class="col-md-3 mt-1">
                      <img
                        class="object-fit-cover rounded product-image"
                        src="${API.getEndpoint(`objects/${offer.id}/photo`)}"
                        onerror="this.src='${noFurniturePhoto}'"
                        width="180"
                        height="180"
                        alt="${offer.objectType}"
                      />
                    </div>
                    <div class="col-md-6 mt-1">
                      <h5>${offer.objectType}</h5>

                      <div class="mt-1 mb-1 spec-1">
                        <h6>${offer.description}</h6>
                      </div>
                      <br />
                      <p>
                        À récupérer le ${dateStringtoGoodFormat(offer.receiptDate)}
                        ${
                          offer.timeSlot === 'matin'
                            ? ' au '.concat(offer.timeSlot)
                            : " l'".concat(offer.timeSlot)
                        }
                      </p>

                      <div class="div-user"></div>
                    </div>

                    <div
                      class="col-md-3 border-left mt-1 d-flex flex-column align-content-center justify-content-between"
                    >
                      <div class="div-remaining-time"></div>

                      <div class="d-flex flex-column mb-4 div-button">
                        <button
                          class="btn btn-outline-primary btn-sm button-respond"
                          type="button"
                          data-id="${offer.id}"
                        >
                          Répondre
                        </button>
                      </div>
                    </div>
                  </div>
                `,
              )
              .join('')}
          </div>
        </div>
      </div>
    `;

    setUserOrPhoneNumber(document, 'div-user', offers);
    setRemainingTime('div-remaining-time', offers);

    offersList.querySelectorAll('a[data-id]').forEach((link) => {
      link.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate(`/user/${e.target.dataset.id}`);
      });
    });

    offersList.querySelectorAll('button[data-id]').forEach((link) => {
      link.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate(`/object/${e.target.dataset.id}`);
      });
    });
  });
}

function setRemainingTime(className, offers) {
  const elements = document.getElementsByClassName(className);
  for (let i = 0; i < elements.length; i += 1) {
    const offer = offers[i];
    const element = elements.item(i);

    const receiptDate = new Date(offer.receiptDate);
    const todaySDate = new Date();

    const timeRemaining = subtractDates(receiptDate, todaySDate);

    if (timeRemaining <= 3) {
      element.innerHTML = `
        <h6 class="text-danger">${timeRemaining} jours restants pour répondre !</h6>
      `;
    } else {
      element.innerHTML = `
        <h6 class="text-primary">${timeRemaining} jours restants pour répondre</h6>
      `;
    }
  }
}

export default AdminOffersPage;

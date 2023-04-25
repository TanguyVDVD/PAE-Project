import flatpickr from 'flatpickr';
import "flatpickr/dist/l10n/fr";
import Navigate from '../../Router/Navigate';
import {getAuthenticatedUser} from '../../../utils/auths';
import {clearPage, renderError} from '../../../utils/render';
import API from '../../../utils/api';
import {invertDateFormat,dateStringtoGoodFormat, subtractDates} from '../../../utils/dates';
import {encodingHelp, setUserOrPhoneNumber, filterObjects} from '../../../utils/objects';

import noFurniturePhoto from '../../../img/no_furniture_photo.svg';

import Navbar from "../../Navbar/Navbar";

const AdminOffersPage = () => {
  Navbar();
  const authenticatedUser = getAuthenticatedUser();

  if (!authenticatedUser || authenticatedUser.role === 'utilisateur') {
    Navigate('/');
    return;
  }

  clearPage();
  renderAdminOffersPage();
};

function renderAdminOffersPage() {
  // let searchQuery = '';
  const main = document.querySelector('main');
  const div = document.createElement('div');
  div.className = 'container my-5';

  div.innerHTML = `
    <h2>Propositions</h2>
    <form class="input-group">
      <div class="row g-3 justify-content-md-center">
        <div class="col-md-12">
          <input type="text" class="form-control autocomplete" id="search-bar" placeholder="Rechercher..." />
        </div>
        <div class="col-md-3">
          <div class="input-group">
            <span class="input-group-text bg-white">Prix minimum</span>
            <input type="number" class="form-control form-filter" id="input-minPrice" placeholder="" />
          </div>
        </div>
        <div class="col-md-3">
          <div class="input-group">
            <span class="input-group-text bg-white">Prix maximum</span>
            <input type="number" class="form-control form-filter" id="input-maxPrice" placeholder="" />
          </div>
        </div>
        <div class="col-md-3">
          <div class="input-group">
            <span class="input-group-text bg-white">Date de réception</span>
            <input type="text" class="form-control form-filter" id="input-receipt-date" placeholder="Date de réception"/>
          </div>
        </div>
        <div class="col-md-2">
          <div class="mx-0 dropdown">
            <button class="btn btn-secondary dropdown-toggle" type="button" id="type-dropdown" data-bs-toggle="dropdown" aria-expanded="false">
              Types d'objets
            </button>
            <ul class="dropdown-menu" aria-labelledby="type-dropdown">
              <li><label class="dropdown-item"><input type="checkbox" value="Meuble" class="form-filter"> Meuble</label></li>
              <li><label class="dropdown-item"><input type="checkbox" value="Table" class="form-filter"> Table</label></li>
              <li><label class="dropdown-item"><input type="checkbox" value="Chaise" class="form-filter"> Chaise</label></li>
              <li><label class="dropdown-item"><input type="checkbox" value="Fauteuil" class="form-filter"> Fauteuil</label></li>
              <li><label class="dropdown-item"><input type="checkbox" value="Lit/sommier" class="form-filter"> Lit/sommier</label></li>
              <li><label class="dropdown-item"><input type="checkbox" value="Matelas" class="form-filter"> Matelas</label></li>
              <li><label class="dropdown-item"><input type="checkbox" value="Couverture" class="form-filter"> Couverture</label></li>
              <li><label class="dropdown-item"><input type="checkbox" value="Materiel de cuisine" class="form-filter"> Materiel de cuisine</label></li>
              <li><label class="dropdown-item"><input type="checkbox" value="Vaisselle" class="form-filter"> Vaisselle</label></li>
            </ul>
          </div>
        </div>
      </div>
    </form>
    <div id="offers-list"></div>
  `;

  main.appendChild(div);

  const offersList = document.getElementById('offers-list');

  offersList.innerHTML = `
    <div class="text-center my-5">
      <div class="spinner-border" role="status"></div>
    </div>
  `;

  const enableDates = [];

  API.get('/availabilities').then((availabilities) => {
    availabilities.forEach((item) => {
      enableDates.push(invertDateFormat(item.date));
    });
    renderDatePicker("#input-receipt-date",enableDates);
  }).catch((err) => {
    renderError(err.message);
  });

  const descriptions = [];

  API.get(`objects/offers?query=${encodeURIComponent("")}`)
  .then((offers) => {
    if(offers !== null){
      renderOffers(offers);

      offers.forEach((offer) => {
        descriptions.push(offer.description);
      });
    }

    encodingHelp(descriptions);
  })
  .catch((err) => {
    renderError(err.message);
  });

  div.querySelector('form').addEventListener('keyup', (e) => {
    e.preventDefault();

    const search = document.getElementById('search-bar').value;
    const minPrice = document.getElementById('input-minPrice').value;
    const maxPrice = document.getElementById('input-maxPrice').value;
    const date = document.getElementById('input-receipt-date').value;
    const type = [...document.querySelectorAll('.form-filter:checked')].map((cb) => cb.value);

    API.get(`objects/offers?query=${encodeURIComponent(search)}`)
    .then((offers) => {
      if(offers !== null){
        renderOffers(filterObjects(offers, minPrice, maxPrice, date, type));
      }
    })
    .catch((err) => {
      renderError(err.message);
    });
  });

  div.querySelectorAll('.form-filter').forEach((e) => {
    e.addEventListener('change', () => {

      const search = document.getElementById('search-bar').value;
      const minPrice = document.getElementById('input-minPrice').value;
      const maxPrice = document.getElementById('input-maxPrice').value;
      const date = document.getElementById('input-receipt-date').value;
      const type = [...document.querySelectorAll('.form-filter:checked')].map((cb) => cb.value);

      API.get(`objects/offers?query=${encodeURIComponent(search)}`)
      .then((offers) => {
        if(offers !== null){
          renderOffers(filterObjects(offers, minPrice, maxPrice, date, type));
        }
      })
      .catch((err) => {
        renderError(err.message);
      });
    });
  });

}

async function renderOffers(offersFiltered) {
  const offersList = document.getElementById('offers-list');

  offersList.innerHTML = `
      <div class="container mt-5 mb-5">
          <div class="d-flex justify-content-center row">
              <div class="col-md-10">
                  ${offersFiltered
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
                                  alt="${offer.objectType}">
                          </div>
                          <div class="col-md-6 mt-1">
                              <h5>${offer.objectType}</h5>
                             
                              <div class="mt-1 mb-1 spec-1">
                                  <h6>${offer.description}</h6>
                              </div>
                              <br>
                              <p>
                                  À récupérer le ${dateStringtoGoodFormat(offer.receiptDate)} ${
                                    offer.timeSlot === 'matin'
                                      ? ' au '.concat(offer.timeSlot)
                                      : " l'".concat(offer.timeSlot)
                                  }
                              </p>
                              
                              <div class="div-user">
                              </div>
                          </div>
                          
                          <div class="col-md-3 border-left mt-1 d-flex flex-column align-content-center justify-content-between">                                
                              <div class="div-remaining-time pt-1">
                              </div>
                                                              
                              <div class="d-flex flex-column mb-4 div-button">
                                  <button class="btn btn-primary text-secondary btn-sm button-respond" type="button" data-id="${
                                    offer.id
                                  }">Répondre</button>
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

  setUserOrPhoneNumber(document, 'div-user', offersFiltered);
  setRemainingTime('div-remaining-time', offersFiltered);

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
}

function setRemainingTime(className, offers) {
  const elements = document.getElementsByClassName(className);
  for (let i = 0; i < elements.length; i += 1) {
    const offer = offers[i];
    const element = elements.item(i);

    const receiptDate = new Date(offer.receiptDate);
    const todaySDate = new Date();

    const timeRemaining = subtractDates(todaySDate, receiptDate);

    if (timeRemaining < 0) {
      element.innerHTML = `
        <h6 class="text-danger">Date pour récupérer l'objet dépassée !</h6>
      `;
    } else if (timeRemaining <= 3) {
      element.innerHTML = `
        <h6 class="text-danger">${timeRemaining} jours restants pour répondre !</h6>
      `;
    } else {
      element.innerHTML = `
        <h6>${timeRemaining} jours restants pour répondre</h6>
      `;
    }
  }
}

function renderDatePicker(datePickerId, availabilities) {
  flatpickr(datePickerId, {
    locale: "fr",
    dateFormat: "d-m-Y",
    minDate: "today",
    enable: availabilities,
  });
}


export default AdminOffersPage;

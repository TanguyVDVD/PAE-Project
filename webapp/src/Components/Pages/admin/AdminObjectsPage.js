import flatpickr from 'flatpickr';
import "flatpickr/dist/l10n/fr";
import Navigate from '../../Router/Navigate';
import {getAuthenticatedUser} from '../../../utils/auths';
import {clearPage, renderError} from '../../../utils/render';
import API from '../../../utils/api';
import {invertDateFormat, subtractDates} from '../../../utils/dates';
import {encodingHelp, setReceiptDate, setUserOrPhoneNumber} from '../../../utils/objects';

import noFurniturePhoto from '../../../img/no_furniture_photo.svg';
import Navbar from "../../Navbar/Navbar";

const AdminObjectsPage = () => {
  Navbar();
  const authenticatedUser = getAuthenticatedUser();

  if (!authenticatedUser || authenticatedUser.role === 'utilisateur') {
    Navigate('/');
    return;
  }

  clearPage();
  renderAdminObjectsPage();
};

function renderAdminObjectsPage() {
  // let searchQuery = '';
  const main = document.querySelector('main');
  const div = document.createElement('div');
  div.className = 'container my-5';

  div.innerHTML = `
    <h2>Objets</h2>
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
    <div id="objects-list"></div>
  `;

  main.appendChild(div);

  const objectslist = document.getElementById('objects-list');

  objectslist.innerHTML = `
    <div class="position-absolute top-50 start-50 translate-middle">
      <div class="spinner-border" role="status"></div>
    </div>
  `;

  let enableDates = [];

  API.get('/availabilities')
    .then((availabilities) => {
      enableDates = availabilities.map((item) => invertDateFormat(item.date));
      renderDatePicker("#input-receipt-date",enableDates);
  }).catch((err) => {
    renderError(err.message);
  });

  let descriptions = [];

  API.get(`objects?query=${encodeURIComponent("")}`)
  .then((objects) => {
    if (objects !== null) {
      renderObjects(objects);
      descriptions = objects.map((object) => object.description);
      encodingHelp(descriptions);
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

    API.get(`objects?query=${encodeURIComponent(search)}`)
    .then((objects) => {
      if(objects !== null){
        renderObjects(filterObjects(objects, minPrice, maxPrice, date, type));
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

    API.get(`objects?query=${encodeURIComponent(search)}`)
      .then((objects) => {
        if(objects !== null){
          renderObjects(filterObjects(objects, minPrice, maxPrice, date, type));
        }
      })
      .catch((err) => {
        renderError(err.message);
      });
    });
  });
}

async function renderObjects(objectsFiltered) {
  const objectslist = document.getElementById('objects-list');

  objectslist.innerHTML = `
      <div class="container mt-5 mb-5">
          <div class="d-flex justify-content-center row">
              <div class="col-md-10">
                  ${objectsFiltered.map((object) => `
                      <div class="row p-2 bg-white border rounded">
                          <div class="col-md-3 mt-1">
                              <img 
                                  class="rounded product-image object-fit-cover" 
                                  src="${API.getEndpoint(`objects/${object.id}/photo`)}"
                                  width="180" height="180"
                                  onerror="this.src='${noFurniturePhoto}'"
                                  alt="${object.objectType}">
                          </div>
                          <div class="col-md-6 mt-1">
                              <h5>${object.objectType}</h5>
                             
                              <div class="mt-1 mb-1 spec-1">
                                  <h6>${object.description}</h6>
                              </div>
                              <br>
                              <div class="div-receipt-date">
                              </div>
                              
                              <div class="div-user">
                              </div>
                          </div>
                          
                          <div class="col-md-3 border-left mt-1 d-flex flex-column align-content-center justify-content-between">
                              <div>
                                <div class="div-state">
                                </div>
                                
                                <div class="d-flex flex-row align-items-center div-price-time-remaining">
                                </div>
                              </div>
                                                   
                              <div class="d-flex flex-column mb-4 div-button">
                              </div>
                          </div>
                      </div>
                      `
                  ,).join('')}
              </div>                     
          </div>
      </div>
    `;

  setReceiptDate(document, 'div-receipt-date', objectsFiltered);
  setUserOrPhoneNumber(document, 'div-user', objectsFiltered);
  setPriceOrTimeRemaining('div-price-time-remaining', objectsFiltered);
  setStateColor('div-state', objectsFiltered);
  setButton('div-button', objectsFiltered);

  objectslist.querySelectorAll('a[data-id]').forEach((link) => {
    link.addEventListener('click', (e) => {
      e.preventDefault();
      Navigate(`/user/${e.target.dataset.id}`);
    });
  });

  objectslist.querySelectorAll('button[data-id]').forEach((link) => {
    link.addEventListener('click', (e) => {
      e.preventDefault();
      Navigate(`/object/${e.target.dataset.id}`);
    });
  });
}

function setPriceOrTimeRemaining(className, objects) {
  const elements = document.getElementsByClassName(className);
  for (let i = 0; i < elements.length; i += 1) {
    const object = objects[i];
    const element = elements.item(i);

    if (object.state === 'proposé') {
      const receiptDate = new Date(object.receiptDate);
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
    } else if (object.status === 'refusé') {
      element.innerHTML = ``;
    } else {
      element.innerHTML = `
        <h4 class="mr-1">${object.price} €</h4>
      `;
    }
  }
}

function setStateColor(className, objects) {
  const elements = document.getElementsByClassName(className);
  for (let i = 0; i < elements.length; i += 1) {
    const object = objects[i];
    const element = elements.item(i);

    if (object.state === 'refusé') {
      element.innerHTML = `
        <h6 class="text-danger">${object.state}</h6>
      `;
    } else if (object.state === 'proposé') {
      element.innerHTML = `
        <h6 class="text-info">${object.state}</h6>
      `;
    } else {
      element.innerHTML = `
        <h6 class="text-success">${object.state}</h6>
      `;
    }
  }
}

function setButton(className, objects) {
  const elements = document.getElementsByClassName(className);
  for (let i = 0; i < elements.length; i += 1) {
    const object = objects[i];
    const element = elements.item(i);
    if (object.state === 'refusé') {
      element.innerHTML = `
        <button class="btn btn-primary btn-sm button-see" type="button" data-id="${object.id}">
          Voir
        </button>
      `;
    } else if (object.state === 'proposé') {
      element.innerHTML = `
        <button
          class="btn btn-primary text-secondary btn-sm button-respond"
          type="button"
          data-id="${object.id}"
        >
          Répondre
        </button>
      `;
    } else {
      element.innerHTML = `
        <button class="btn btn-primary text-secondary btn-sm button-modify" type="button" data-id="${object.id}">
          Modifier
        </button>
      `;
    }
  }
}

function renderDatePicker(datePickerId, availabilities) {
  flatpickr(datePickerId, {
    locale: "fr",
    dateFormat: "d-m-Y",
    enable: availabilities,
  });
}

function filterObjects(objects, minPrice,maxPrice, date, types){
  return objects.filter((object) => {
    if (minPrice && object.price < minPrice) {
      return false;
    }

    // Filter by maxPrice if provided
    if (maxPrice && object.price > maxPrice) {
      return false;
    }

    // Filter by date if provided
    if (date && invertDateFormat(object.receiptDate) !== date) {
      return false;
    }

    // Filter by type if provided
    if (types.length > 0 && !types.includes(object.objectType)) {
      return false;
    }

    return true;
  });
}

export default AdminObjectsPage;

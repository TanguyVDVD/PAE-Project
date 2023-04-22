import Navigate from '../../Router/Navigate';
import { getAuthenticatedUser } from '../../../utils/auths';
import { clearPage } from '../../../utils/render';
import API from '../../../utils/api';
import {subtractDates} from '../../../utils/dates';
import {setReceiptDate, setUserOrPhoneNumber} from '../../../utils/objects';

import noFurniturePhoto from '../../../img/no_furniture_photo.svg';

const AdminObjectsPage = () => {
  const authenticatedUser = getAuthenticatedUser();

  if (!authenticatedUser || authenticatedUser.role === null) {
    Navigate('/');
    return;
  }

  clearPage();
  renderAdminObjectsPage();
  renderObjects();
};

function renderAdminObjectsPage() {
  const main = document.querySelector('main');
  const div = document.createElement('div');
  div.className = 'container my-5';

  div.innerHTML = `
    <h2>Objets</h2>
    <form class="input-group">
      <input type="text" class="form-control border-end-0" id="input-text" placeholder="Rechercher..." />
      <input type="number" class="form-control mx-2" id="input-minPrice" placeholder="Prix minimum" />
      <input type="number" class="form-control mx-2" id="input-maxPrice" placeholder="Prix maximum" />
      <div class="form-check dropdown">
        <button class="btn btn-secondary dropdown-toggle" type="button" id="type-dropdown" data-bs-toggle="dropdown" aria-expanded="false">
          Tous les types
        </button>
        <ul class="dropdown-menu" aria-labelledby="type-dropdown">
          <li><label class="dropdown-item"><input type="checkbox" value="Meuble" class="type-filter"> Meuble</label></li>
          <li><label class="dropdown-item"><input type="checkbox" value="Table" class="type-filter"> Table</label></li>
          <li><label class="dropdown-item"><input type="checkbox" value="Chaise" class="type-filter"> Chaise</label></li>
          <li><label class="dropdown-item"><input type="checkbox" value="Fauteuil" class="type-filter"> Fauteuil</label></li>
          <li><label class="dropdown-item"><input type="checkbox" value="Lit/sommier" class="type-filter"> Lit/sommier</label></li>
          <li><label class="dropdown-item"><input type="checkbox" value="Matelas" class="type-filter"> Matelas</label></li>
          <li><label class="dropdown-item"><input type="checkbox" value="Couverture" class="type-filter"> Couverture</label></li>
          <li><label class="dropdown-item"><input type="checkbox" value="Materiel de cuisine" class="type-filter"> Materiel de cuisine</label></li>
          <li><label class="dropdown-item"><input type="checkbox" value="Vaisselle" class="type-filter"> Vaisselle</label></li>
        </ul>
      </div>
      <button class="btn border" type="submit">
        <i class="bi bi-search"></i>
      </button>
    </form>
    <div id="objects-list"></div>
  `;

  div.querySelector('form').addEventListener('keyup', (e) => {
    e.preventDefault();

    const search = document.getElementById('input-text').value; // Get search filter value
    const minPrice = document.getElementById('input-minPrice').value; // Get min price filter value
    const maxPrice = document.getElementById('input-maxPrice').value; // Get max price filter value
    const typeFilters = [...document.querySelectorAll('.type-filter:checked')].map((cb) => cb.value); // Get type filter values

    renderObjects(minPrice, maxPrice, search, typeFilters);
  });

  const checkboxes = div.querySelectorAll('.type-filter');
  checkboxes.forEach((checkbox) => {
    checkbox.addEventListener('change', () => {
      const search = document.getElementById('input-text').value; // Get search filter value
      const minPrice = document.getElementById('input-minPrice').value; // Get min price filter value
      const maxPrice = document.getElementById('input-maxPrice').value; // Get max price filter value
      const typeFilters = [...document.querySelectorAll('.type-filter:checked')].map((cb) => cb.value); // Get type filter values

      renderObjects(minPrice, maxPrice, search, typeFilters);
    });
  });

  main.appendChild(div);
}

async function renderObjects(minPrice , maxPrice,query = '', typeFilter = []) {
  const objectslist = document.getElementById('objects-list');

  objectslist.innerHTML = `
    <div class="text-center my-5">
      <div class="spinner-border" role="status"></div>
    </div>
  `;

  console.log("min", minPrice);
  console.log("max", maxPrice);
  console.log("type", typeFilter);

  API.get(`objects?query=${encodeURIComponent(query)}`).then((objects) => {
    const objectsF = objects.filter((object) => {
      if (minPrice && object.price <= minPrice) {
        return false;
      }

      // Filter by maxPrice if provided
      if (maxPrice && object.price >= maxPrice) {
        return false;
      }

      // Filter by type if provided
      if (typeFilter.length > 0 && !typeFilter.includes(object.objectType)) {
        return false;
      }

      return true;
    });
    document.getElementById('objects-list').innerHTML = `
        <div class="container mt-5 mb-5">
            <div class="d-flex justify-content-center row">
                <div class="col-md-10">
                    ${objectsF
                      .map(
                        (object) => `
                        <div class="row p-2 bg-white border rounded">
                            <div class="col-md-3 mt-1">
                                <img 
                                    class="rounded product-image object-fit-cover" 
                                    src="${
                                      object.photo
                                        ? API.getEndpoint(`objects/${object.id}/photo`)
                                        : noFurniturePhoto
                                    }"
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
                `,
                      )
                      .join('')}
                </div>                     
            </div>
        </div>
      `;

    setReceiptDate(document, 'div-receipt-date', objectsF);
    setUserOrPhoneNumber(document, 'div-user', objectsF);
    setPriceOrTimeRemaining('div-price-time-remaining', objectsF);
    setStateColor('div-state', objectsF);
    setButton('div-button', objectsF);

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

      const timeRemaining = subtractDates(receiptDate, todaySDate);

      if (timeRemaining <= 3) {
        element.innerHTML = `
          <p class="text-danger">${timeRemaining} jours restants pour répondre !</p>
      `;
      } else {
        element.innerHTML = `
          <p class="text-primary">${timeRemaining} jours restants pour répondre</p>
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
          <h6 class="text-primary">${object.state}</h6>
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
          <button class="btn btn-primary btn-sm button-see" type="button" data-id="${object.id}">Voir</button>
      `;
    } else if (object.state === 'proposé') {
      element.innerHTML = `
          <button class="btn btn-outline-primary btn-sm button-respond" type="button" data-id="${object.id}">Répondre</button>
      `;
    } else {
      element.innerHTML = `
          <button class="btn btn-primary btn-sm button-modify" type="button" data-id="${object.id}">Modifier</button>
      `;
    }
  }
}

export default AdminObjectsPage;

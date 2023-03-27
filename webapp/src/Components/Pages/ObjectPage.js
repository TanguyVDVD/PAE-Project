import { clearPage, renderError } from '../../utils/render';
import { getAuthenticatedUser } from '../../utils/auths';
import API from '../../utils/api';
import { dateStringtoGoodFormat, getTodaySDate } from '../../utils/dates';
import Navigate from '../Router/Navigate';
import AdminOffersPage from './admin/AdminOffersPage';
import AdminObjectsPage from './admin/AdminObjectsPage';
import setUserOrPhoneNumber from '../../utils/objects';

import noFurniturePhoto from '../../img/no_furniture_photo.svg';

const ObjectPage = (params) => {
  const id = parseInt(params.id, 10);

  clearPage();

  API.get(`/objects/${id}`).then((object) => {
    API.get('/objectsTypes').then((objectTypes) => {
      renderObjectPage(object, objectTypes);
    });
  });
};

function renderObjectPage(object, objectTypes) {
  const main = document.querySelector('main');

  const authenticatedUser = getAuthenticatedUser();

  const div = document.createElement('div');

  div.className = 'container p-5';

  div.innerHTML = `
    <section>
      <div>
          <div class="row gx-6 gx-lg-6 align-items-top">
              <div class="col-md-4">
                <img class="card-img-top mb-5 mb-md-0 object-fit-cover" 
                src="${
                  object.photo ? API.getEndpoint(`objects/${object.id}/photo`) : noFurniturePhoto
                }"
                onerror="this.src='${noFurniturePhoto}'"
                width="400"
                height="400"
                 />
                
                ${
                  authenticatedUser && authenticatedUser.isHelper && object.state !== 'proposé'
                    ? `
                    <form>
                      <div class="row row-cols-1 row-cols-md-2 m-1">
                        <input
                          type="file"
                          accept="image/png, image/jpeg"
                          class="form-control"
                          id="input-photo"
                          name="photo"
                          placeholder=""
                        />
                      </div>
                    </form>
                  `
                    : ''
                }
              </div>
              

              <div class="col-md-8">
                  
                  ${
                    authenticatedUser && authenticatedUser.isHelper && object.state !== 'proposé'
                      ? `
                      <form id="object-form">
                        <div class="form-group" id="object-type-form">
                          <label>Type</label>
                          <select class="form-control" id="object-type-select">
                            ${objectTypes.map(
                              (objectType) =>
                                `
                                <option>${objectType.label}</option>
                              `,
                            )}
                          </select>
                        </div>
                        <br>
                        <div class="form-group" id="object-description-form">
                          <label>Description</label>
                          <textarea class="form-control" id="object-description-textarea" rows="2">${
                            object.description
                          }</textarea>
                        </div>
                        <br>
                        <div class="form-group div-user" id="object-user-form">
                        </div>
                        
                        <div class="form-group" id="object-receipt-date-form">
                          <p>Récupéré le ${dateStringtoGoodFormat(object.receiptDate)}</p>
                        </div>
                        
                        <div class="form-group" id="object-time-slot-form">
                          <p>Créneau choisi : ${object.timeSlot}</p>
                        </div>
                        
                        <div class="form-group" id="object-state-form">
                          <label>État</label>
                          <select class="form-control" id="object-state-select">
                            <option>accepté</option>
                            <option>à l'atelier</option>
                            <option>en magasin</option>
                            <option>en vente</option>
                            <option>vendu</option>
                            <option>retiré</option>
                          </select>
                        </div>
                        
                        <div class="form-group" id="object-state-date-form">
                          <br>
                          <label>Date du nouvel état : </label>
                          <input type="date" id="object-state-date-input">
                        </div>
                        
                        <div class="form-group" id="object-price-form">
                          <br>
                          <label for="object-price-input">Prix</label>
                          <input type="number" id="object-price-input" min="0" max="10">
                        </div>
                        <br>
                        <div class="form-check form-switch">
                          <input
                            class="form-check-input"
                            type="checkbox"
                            role="switch"
                            id="visible-switch"
                            ${object.isVisible ? 'checked' : ''}
                          />
                          <label class="form-check-label" for="helper-switch">Visible sur le site</label>
                        </div>
                        <br>
                        <button type="submit" class="btn btn-primary" id="save-btn">Sauvegarder</button>
                        <div class="bordure_verticale"></div>
                        <button type="submit" class="btn btn-outline-primary" id="cancel-btn">Annuler</button>
                      </form> 
                    `
                      : `
                      <h2>${object.objectType}</h2>
                      <p>Description : ${object.description}</p>
                    `
                  }
                  
                  ${authenticatedUser && authenticatedUser.isHelper && object.state === "proposé" ?
                    `
                      <div class="div-user" id="object-user-offer"></div>
                      
                      <div class="form-group" id="object-receipt-date-offer">
                        <p>À récupérer le ${dateStringtoGoodFormat(object.receiptDate)}</p>
                      </div>
                      
                      <div id="object-time-slot-offer">
                        <p>Créneau choisi : ${object.timeSlot}</p>
                      </div>
                      
                      <div id="reason-for-refusal-div">
                        <label>Raison du refus</label>
                        <textarea class="form-control" id="reason-for-refusal" rows="2"></textarea>
                      </div>   
                      
                      <a href="#" class="accept" id="accept-btn">Accepter l'objet<span class="fa fa-check"></span></a>
                      <div class="bordure_verticale"></div>
                      <a href="#" class="deny" id="deny-btn">Refuser l'objet<span class="fa fa-close"></span></a>
                    `
                      : ''
                  }
                  
                  ${
                    authenticatedUser && !authenticatedUser.isHelper
                      ? `
                      <div id="object-state-non-helper">
                        <p>État : ${object.state}</p>
                      </div>
                      
                      <div id="object-price-non-helper">
                        <p>Prix : ${object.price}€</p>
                      </div>
                    ` : ''
                  }
              </div>
          </div>
      </section>
    `;

  main.appendChild(div);

    if (authenticatedUser && authenticatedUser.isHelper && object.state === "proposé"){
      const acceptBtn = document.getElementById("accept-btn");
      const denyBtn = document.getElementById("deny-btn");

      if (authenticatedUser.id !== 1){
        acceptBtn.disabled = true;
        denyBtn.disabled = true;
      }

      acceptBtn.addEventListener('click', () => {
        const status = "accepté";

      API.patch(`objects/status/${object.id}`, { body: { status } });
      AdminOffersPage();
      Navigate('/admin/offers');
    });

      denyBtn.addEventListener('click', () => {
        const status = "refusé";
        const reasonForRefusal = document.getElementById("reason-for-refusal").value;

      API.patch(`objects/status/${object.id}`, { body: { status, reasonForRefusal } });
      AdminOffersPage();
      Navigate('/admin/offers');
    });
  }

    if (authenticatedUser && authenticatedUser.isHelper && object.state !== "proposé"){
      setUserOrPhoneNumber(document, "div-user", [object]);
      setDefaultValues(object);

    const stateForm = document.getElementById('object-state-select');
    const priceInput = document.getElementById('object-price-input');

    stateForm.addEventListener('change', () => {
      if (
        stateForm.value === 'accepté' ||
        stateForm.value === "à l'atelier" ||
        stateForm.value === 'en magasin'
      ) {
        priceInput.value = null;
        priceInput.disabled = true;
      } else if (stateForm.value === 'retiré') {
        priceInput.value = object.price;
        priceInput.disabled = true;
      } else {
        priceInput.value = object.price;
        priceInput.disabled = false;
      }
    });

    document.getElementById('object-form').addEventListener('submit', async (e) => {
      e.preventDefault();

      const description = document.getElementById('object-description-textarea').value;
      const type = document.getElementById('object-type-select').value;
      const state = document.getElementById('object-state-select').value;
      const date = document.getElementById('object-state-date-input').value;
      const price = document.getElementById('object-price-input').value;
      const isVisible = document.getElementById('visible-switch').checked;

      try {
        await API.put(`objects/${object.id}`, {
          body: { description, type, state, date, price, isVisible },
        });

        const photo = document.getElementById('input-photo');
        if (photo.files.length > 0) {
          const formData = new FormData();
          formData.append('photo', photo.files[0]);
          await API.put(`objects/${object.id}/photo`, { body: formData });
        }

        AdminObjectsPage();
        Navigate('/admin/objects');
      } catch (error) {
        renderError(error);
      }
    });

    document.getElementById('cancel-btn').addEventListener('click', () => {
      AdminObjectsPage();
      Navigate('/admin/objects');
    });
  }
}

function setDefaultValues(object) {
  document.getElementById('object-type-select').value = object.objectType;
  document.getElementById('object-state-select').value = object.state;
  document.getElementById('object-state-date-input').value = getTodaySDate();

  const priceInput = document.getElementById('object-price-input');

  if (
    object.state === 'accepté' ||
    object.state === "à l'atelier" ||
    object.state === 'en magasin'
  ) {
    priceInput.value = null;
    priceInput.disabled = true;
  } else if (object.state === 'retiré') {
    priceInput.value = object.price;
    priceInput.disabled = true;
  } else {
    priceInput.value = object.price;
    priceInput.disabled = false;
  }

  const visibleSwitch = document.getElementById('visible-switch');

  visibleSwitch.checked = !!object.isVisible;
  visibleSwitch.checked = !!object.isVisible;
}

export default ObjectPage;

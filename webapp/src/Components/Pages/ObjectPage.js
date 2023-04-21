import { clearPage, renderError } from '../../utils/render';
import { getAuthenticatedUser } from '../../utils/auths';
import API from '../../utils/api';
import { dateStringtoGoodFormat, getTodaySDate } from '../../utils/dates';
import Navigate from '../Router/Navigate';
import AdminOffersPage from './admin/AdminOffersPage';
import AdminObjectsPage from './admin/AdminObjectsPage';
import {setUserOrPhoneNumber, setReceiptDate} from '../../utils/objects';

import noFurniturePhoto from '../../img/no_furniture_photo.svg';

const ObjectPage = (params) => {
  const id = parseInt(params.id, 10);

  clearPage();

  const main = document.querySelector('main');

  main.innerHTML = `
    <div class="text-center my-5">
      <div class="spinner-border" role="status"></div>
    </div>
  `;

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
            
            
            ${authenticatedUser && authenticatedUser.role !== null
              && object.state !== 'proposé' && object.state !== "refusé"
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
            ${authenticatedUser && authenticatedUser.role !== null
              && object.state !== 'proposé' && object.state !== "refusé"
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
                
                <div class="form-group div-receipt-date" id="object-receipt-date-form">
                </div>
                
                <div class="form-group" id="object-time-slot-form">
                  <p>Créneau choisi : ${object.timeSlot}</p>
                </div>
                
                <div class="form-group" id="object-state-form">
                  <label>État</label>
                  <select class="form-control" id="object-state-select">
                    ${getAvailableStates(object).map(
                    (state) => `
                        <option>${state}</option>
                      `,
                    )}
                  </select>
                </div>
                
                <div class="form-group" id="object-state-date-form">
                  <br>
                  <label id="object-state-date-label"></label>
                  <input type="date" id="object-state-date-input">
                </div>
                
                <div class="form-group" id="object-price-form">
                  <br>
                  <label for="object-price-input" id="object-price-label">Prix</label>
                  <input type="number" id="object-price-input" min="0" max="10" step=".01">
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
            
            ${authenticatedUser && authenticatedUser.role !== null
              && object.state === "proposé" ?
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
            
            ${authenticatedUser && authenticatedUser.role !== null
              && object.state === "refusé" ? 
            `
              <div id="object-state-refused">
                <h6 class="text-danger">Refusé</h6>
              </div>
              
              <div id="object-refused-reason-for-refusal">
                <p>Raison du refus : ${object.reasonForRefusal}</p>
              </div>
              
              <div class="form-group div-receipt-date" id="object-receipt-date-form">
              </div>
              
              <div class="form-group div-user" id="object-user-form">
              </div>
              
              
              <div class="form-group" id="object-time-slot-form">
                <p>Créneau choisi : ${object.timeSlot}</p>
              </div>
            ` : ''
            }
            
            ${authenticatedUser && authenticatedUser.role === null
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

  main.replaceChildren(div);

  /**
   * Réponse à une proposition
   */
  if (authenticatedUser && authenticatedUser.role !== null
      && object.state === "proposé"){
    const acceptBtn = document.getElementById("accept-btn");
    const denyBtn = document.getElementById("deny-btn");

    if (authenticatedUser.role !== "responsable"){
      acceptBtn.disabled = true;
      denyBtn.disabled = true;
    }

    acceptBtn.addEventListener('click', () => {
      const status = "accepté";
      const versionNbr = object.versionNumber;

      API.patch(`objects/status/${object.id}`, { body: { status, versionNbr } });
      AdminOffersPage();
      Navigate('/admin/offers');
    });

    denyBtn.addEventListener('click', () => {
      const status = "refusé";
      const reasonForRefusal = document.getElementById("reason-for-refusal").value;
      const versionNbr = object.versionNumber;

      API.patch(`objects/status/${object.id}`, { body: { status, reasonForRefusal, versionNbr } });
      AdminOffersPage();
      Navigate('/admin/offers');
    });
  }

  /**
   * Page objet si aidant
   */
  if (
      authenticatedUser &&
      authenticatedUser.role !== null &&
      object.state !== "proposé" &&
      object.state !== "refusé"
  ){
    setUserOrPhoneNumber(document, "div-user", [object]);
    setReceiptDate(document, "div-receipt-date", [object]);
    setDefaultValues(object);

    const stateForm = document.getElementById('object-state-select');

    stateForm.addEventListener("change", () => {
      const state = stateForm.value;

      setStateDate(state, object);
      setPrice(state, object);
      setSwitch(state, object);
    });

    document.getElementById('object-form').addEventListener('submit', async (e) => {
      e.preventDefault();

      const description = document.getElementById('object-description-textarea').value;
      const type = document.getElementById('object-type-select').value;
      const state = document.getElementById('object-state-select').value;
      const date = document.getElementById('object-state-date-input').value;
      const price = document.getElementById('object-price-input').value;
      const isVisible = document.getElementById('visible-switch').checked;
      const versionNbr = object.versionNumber;

      try {
        await API.put(`objects/${object.id}`, {
          body: { description, type, state, date, price, isVisible, versionNbr },
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

  /**
   * Page objet si objet refusé
   */
  if (authenticatedUser && authenticatedUser.role !== null
      && object.state === "refusé"){
    setUserOrPhoneNumber(document, "div-user", [object]);
    setReceiptDate(document, "div-receipt-date", [object]);
  }
}

function setDefaultValues(object) {
  document.getElementById('object-type-select').value = object.objectType;
  document.getElementById('object-state-select').value = object.state;

  setStateDate(object.state, object);
  setPrice(object.state, object);
  setSwitch(object.state, object);
}

function setStateDate(state, object){
  const dateInput = document.getElementById('object-state-date-input');
  const label = document.getElementById('object-state-date-label');

  if (state === "accepté") {
    if (state === object.state){
      dateInput.value = object.acceptanceDate;
    } else {
      dateInput.value = getTodaySDate();
    }
    label.innerHTML = "Accepté le : ";
  }
  else if (state === "à l'atelier") {
    if (state === object.state){
      dateInput.value = object.workshopDate;
    } else {
      dateInput.value = getTodaySDate();
    }
    label.innerHTML = "Mis à l'atelier le : ";
  }
  else if (state === "en magasin") {
    if (state === object.state){
      dateInput.value = object.depositDate;
    } else {
      dateInput.value = getTodaySDate();
    }
    label.innerHTML = "Déposé en magasin le : ";
  }
  else if (state === "en vente") {
    if (state === object.state){
      dateInput.value = object.onSaleDate;
    } else {
      dateInput.value = getTodaySDate();
    }
    label.innerHTML = "Mis en vente le : ";
  }
  else if (state === "vendu") {
    if (state === object.state){
      dateInput.value = object.sellingDate;
    } else {
      dateInput.value = getTodaySDate();
    }
    label.innerHTML = "Vendu le : ";
  }
  else if (state === "retiré") {
    if (state === object.state){
      dateInput.value = object.withdrawalDate;
    } else {
      dateInput.value = getTodaySDate();
    }
    label.innerHTML = "Retiré le : ";
  }
}

function getAvailableStates(object){
  if (object.state === "accepté"){
    return ["accepté", "à l'atelier", "en magasin"];
  }
  if (object.state === "à l'atelier") {
    return ["à l'atelier", "en magasin"];
  }
  if (object.state === "en magasin") {
    return ["en magasin", "en vente", "retiré"];
  }
  if (object.state === "en vente") {
    return ["en vente", "vendu", "retiré"];
  }
  if (object.state === "vendu") {
    return ["vendu"];
  }
  if (object.state === "retiré") {
    return ["retiré"];
  }
  return null;
}

function setPrice(state, object){
  const priceInput = document.getElementById('object-price-input');
  const priceLabel = document.getElementById('object-price-label');

  if (state === "accepté" || state === "à l'atelier" || state === "en magasin") {
    priceInput.value = null;
    priceInput.disabled = true;
    priceLabel.style.color = "#909294";
  } else if (state === "en vente") {
    priceInput.value = object.price;
    priceInput.disabled = false;
    priceLabel.style.color = "text-primary";
  } else {
    priceInput.value = object.price;
    priceInput.disabled = true;
    priceLabel.style.color = "#909294";
  }
}

function setSwitch(state, object){
  const visibleSwitch = document.getElementById('visible-switch');

  if (state === "en magasin") {
    visibleSwitch.checked = true;
    visibleSwitch.disabled = false;
  } else if (state === "en vente" || state === "vendu") {
    visibleSwitch.checked = !!object.isVisible;
    visibleSwitch.disabled = false;
  } else {
    visibleSwitch.checked = false;
    visibleSwitch.disabled = true;
  }
}

export default ObjectPage;

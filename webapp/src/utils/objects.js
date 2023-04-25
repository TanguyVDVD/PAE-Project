import Autocomplete from 'bootstrap5-autocomplete';
import Navigate from '../Components/Router/Navigate';
import {dateStringtoGoodFormat, invertDateFormat} from './dates';
import { formatPhoneNumber } from './format';
import API from './api';
import { renderError } from './render';

import noFurniturePhoto from '../img/no_furniture_photo.svg';

function setUserOrPhoneNumber(document, className, objects) {
  const elements = document.getElementsByClassName(className);
  for (let i = 0; i < elements.length; i += 1) {
    const object = objects[i];
    const element = elements.item(i);
    if (object.user === null) {
      element.innerHTML = `
          <p>Proposé au ${formatPhoneNumber(
          object.phoneNumber,
      )} le ${dateStringtoGoodFormat(object.offerDate)}</p>
      `;
    } else {
      element.innerHTML = `
          <p>
          Proposé par
              <a href="#" class="btn-link link-primary" role="button" data-id="${object.user.id}">${
          object.user.firstName
      } ${object.user.lastName}</a>
          le ${dateStringtoGoodFormat(object.offerDate)}
          </p>
      `;

      element.querySelectorAll('a[data-id]').forEach((link) => {
        link.addEventListener('click', (e) => {
          e.preventDefault();
          Navigate(`/user/${e.target.dataset.id}`);
        });
      });
    }
  }
}

function setReceiptDate(document, className, objects) {
  const elements = document.getElementsByClassName(className);
  for (let i = 0; i < elements.length; i += 1) {
    const object = objects[i];
    const element = elements.item(i);
    if (object.state === 'proposé' || object.state === 'accepté') {
      element.innerHTML = `
          <p>
              À récupérer le ${dateStringtoGoodFormat(object.receiptDate)} ${
          object.timeSlot === 'matin' ? ' au '.concat(object.timeSlot) : " l'".concat(object.timeSlot)
      }
          </p>
      `;
    } else if (object.state === 'refusé') {
      element.innerHTML = `
          <p>
              Refusé le ${dateStringtoGoodFormat(object.refusalDate)}
          </p>
      `;
    } else {
      element.innerHTML = `
          <p>
              Récupéré le ${dateStringtoGoodFormat(object.receiptDate)} ${
          object.timeSlot === 'matin' ? ' au '.concat(object.timeSlot) : " l'".concat(object.timeSlot)
      }
          </p>
      `;
    }
  }
}

function encodingHelp(descriptions) {
  const objectTypes = [];

  API.get('objectTypes')
  .then((types) => {
    types.forEach((item) => {
      objectTypes.push(item.label);
    });

    const src = descriptions.concat(objectTypes);

    Autocomplete.init('input.autocomplete', {
      items: src,
      fullWidth: true,
      fixed: true,
      autoselectFirst: false,
      updateOnSelect: true,
    });
  })
  .catch((err) => {
    renderError(err.message);
  });
}

function createObjectCard(_object) {
  const randomPlaceHolder = () => {
    const random = Math.floor(Math.random() * 3);
    return `
      <span class="placeholder-glow"><span class="placeholder col-${random + 3}"></span></span>
    `;
  };

  const object = _object || {
    id: undefined,
    objectType: `${randomPlaceHolder()}`,
    description: `${randomPlaceHolder()} ${randomPlaceHolder()} ${randomPlaceHolder()}`,
  };

  return `
    <div
      class="object-card ${object.state === 'vendu' ? 'sold' : ''}"
      data-id="${object.id}"
      role="button"
    >
      <div class="object-card-img">
        ${
      object.id !== undefined
          ? `
              <img
                src="${API.getEndpoint(`objects/${object.id}/photo`)}"
                onerror="this.src='${noFurniturePhoto}'"
                alt="${object.description ? `Photo : ${object.description}` : ''}"
              />
            `
          : ''
  }
      </div>
      <div>
        <div class="object-card-title">${object.objectType}</div>
        <div class="object-card-description">
          ${object.description}
        </div>
      </div>
    </div>
  `;
}

// Because object types are used in multiple places, we cache them
const objectTypes = [];

async function getObjectTypes() {
  if (objectTypes.length !== 0) return objectTypes;

  const types = await API.get('objectTypes');

  objectTypes.splice(0, objectTypes.length, ...types);

  return objectTypes;
}

function filterObjects(objects, minPrice, maxPrice, date, type){
  return objects.filter((object) => {
    if (minPrice && object.price < minPrice) {
      return false;
    }

    // Filter by maxPrice if provided
    if (maxPrice && object.price > maxPrice) {
      return false;
    }

    // Filter by date if provided
    if (date && date.includes(invertDateFormat(object.receiptDate)) === false) {
      return false;
    }

    // Filter by type if provided
    if (type.length > 0 && !type.includes(object.objectType)) {
      return false;
    }

    return true;
  });
}

export { setUserOrPhoneNumber, setReceiptDate, createObjectCard, encodingHelp, getObjectTypes, filterObjects };

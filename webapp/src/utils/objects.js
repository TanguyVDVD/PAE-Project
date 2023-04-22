import Autocomplete from "bootstrap5-autocomplete";
import Navigate from '../Components/Router/Navigate';
import API from './api';
import { dateStringtoGoodFormat } from './dates';
import { formatPhoneNumber } from './format';
import API from "./api";
import {renderError} from "./render";

import noFurniturePhoto from '../img/no_furniture_photo.svg';

function setUserOrPhoneNumber(document, className, objects) {
  const elements = document.getElementsByClassName(className);
  for (let i = 0; i < elements.length; i += 1) {
    const object = objects[i];
    const element = elements.item(i);
    if (object.user === null) {
      element.innerHTML = `
          <p>Proposé anonymement au ${formatPhoneNumber(
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

function encodingHelp(descriptions){
  const objectTypes = [];

  API.get('objectTypes').then((types) => {
    types.forEach((item) => {
      objectTypes.push(item.label);
    });

    const src = descriptions.concat(objectTypes);

    Autocomplete.init("input.autocomplete", {
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

export {
    setUserOrPhoneNumber,
    setReceiptDate,
    encodingHelp,
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
        <div>
          ${object.description}
        </div>
      </div>
    </div>
  `;
}

export { setUserOrPhoneNumber, setReceiptDate, createObjectCard, encodingHelp };

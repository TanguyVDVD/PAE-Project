import Navigate from '../Components/Router/Navigate';
import { dateStringtoGoodFormat } from './dates';
import { formatPhoneNumber } from './format';

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

export default setUserOrPhoneNumber;

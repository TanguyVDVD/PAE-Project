import {dateStringtoGoodFormat} from "./dates";

function setUserOrPhoneNumber(document, className, objects) {
  const elements = document.getElementsByClassName(className);
  for (let i = 0; i < elements.length; i += 1) {
    const object = objects[i];
    const element = elements.item(i);
    if (object.user === null) {
      element.innerHTML = `
          <p>Proposé anonymement au ${phoneNumberToGoodFormat(object.phoneNumber)} le ${dateStringtoGoodFormat(object.offerDate)}</p>
      `;
    } else {
      element.innerHTML = `
          <p>
          Proposé par
              <a href="#" class="btn-link link-primary" role="button" data-id="${object.user.id}">${object.user.firstName} ${object.user.lastName}</a>
          le ${dateStringtoGoodFormat(object.offerDate)}
          </p>
      `;
    }
  }
}

function phoneNumberToGoodFormat (phoneNumber){
  return phoneNumber.substring(0, 4).concat("/", phoneNumber.substring(4, 6), ".", phoneNumber.substring(6, 8), ".", phoneNumber.substring(8, 10));
}

export default setUserOrPhoneNumber;
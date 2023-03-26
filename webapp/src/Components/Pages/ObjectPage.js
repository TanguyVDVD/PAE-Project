import {clearPage} from '../../utils/render';
import {getAuthenticatedUser} from '../../utils/auths';
import API from "../../utils/api";
import {dateStringtoGoodFormat, getTodaySDate} from "../../utils/dates";
import Navigate from "../Router/Navigate";


const ObjectPage = (params) => {
  const id = parseInt(params.id, 10);

  clearPage();

  API.get(`/objects/${id}`).then((object) => {
    API.get("/objectsTypes").then((objectTypes) => {
      renderObjectPage(object, objectTypes);
    })
  })
};

function renderObjectPage(object, objectTypes) {
    const main = document.querySelector('main');

    const authenticatedUser = getAuthenticatedUser();

    const div = document.createElement('div');

    div.className='container p-5'

    div.innerHTML=`
    <section>
      <div>
          <div class="row gx-6 gx-lg-6 align-items-top">
          
              <div class="col-md-4">
                <img class="card-img-top mb-5 mb-md-0" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRqsL6QorN-b6YhpcfTl9YJEzWB2xSkhFkN4Q&usqp=CAU" alt="..." />
                
                ${authenticatedUser && authenticatedUser.isHelper && object.state !== "proposé" ? 
                  `
                    <form>
                      <br>
                      <div class="custom-file">
                        <input type="file" class="custom-file-input" id="customFile">
                      </div>
                    </form>
                  ` : ''
                }
              </div>
              

              <div class="col-md-8">
                  
                  ${authenticatedUser && authenticatedUser.isHelper && object.state !== "proposé" ?
                    `
                      <form id="object-form">
                        <div class="form-group" id="object-type-form">
                          <label>Type</label>
                          <select class="form-control" id="object-type-select">
                            ${objectTypes.map((objectType) =>
                              `
                                <option>${objectType.label}</option>
                              `
                            )}
                          </select>
                        </div>
                        <br>
                        <div class="form-group" id="object-description-form">
                          <label>Description</label>
                          <textarea class="form-control" id="object-description-textarea" rows="2">${object.description}</textarea>
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
                        
                        <div class="form-group" id="object-state-date-form">
                          <br>
                          <label for="object-price-input">Prix</label>
                          <input type="number" id="object-price-input" min="0" max="10">
                        </div>
                        <br>
                        <label>Visible : </label>
                        <div class="form-check form-check-inline">
                          <input class="form-check-input" type="radio" name="exampleRadios" id="object-isVisible-input">
                          <label class="form-check-label">
                            Oui
                          </label>
                        </div>
                        <div class="form-check form-check-inline">
                          <input class="form-check-input" type="radio" name="exampleRadios" id="object-isNotVisible-input">
                          <label class="form-check-label">
                            Non
                          </label>
                        </div>
                        <br>
                        <button type="submit" class="btn btn-primary" id="save-btn">Sauvegarder</button>
                        <div class="bordure_verticale"></div>
                        <button type="submit" class="btn btn-outline-primary" id="cancel-btn">Annuler</button>
                      </form> 
                    ` :
                    `
                      <h2>${object.objectType}</h2>
                      <p>Description : ${object.description}</p>
                    `
                  }
                  
                  ${authenticatedUser && authenticatedUser.id === 1 && object.state === "proposé" ?
                    `
                      <div class="div-user" id="object-user-offer">
                      </div>
                      
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
                    ` : ''
                  }
                  
                  ${authenticatedUser && !authenticatedUser.isHelper ?
                    `
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
      setUserOrPhoneNumber("div-user", object, div);

      document.getElementById("accept-btn").addEventListener('click', () => {
        const status = "accepté";

        API.patch(`objects/status/${object.id}`, {body: {status}})
        .then(() => {
          Navigate('/admin/offers');
        })
      });

      document.getElementById("deny-btn").addEventListener('click', () => {
        const status = "refusé";
        const reasonForRefusal = document.getElementById("reason-for-refusal").value;

        API.patch(`objects/status/${object.id}`, {body: {status, reasonForRefusal}})
        .then(() => {
          Navigate('/admin/offers');
        })
      });

    }

    if (authenticatedUser && authenticatedUser.id === 1 && object.state !== "proposé"){
      setUserOrPhoneNumber("div-user", object, div);
      setDefaultValues(object);

      document.getElementById("save-btn").addEventListener('click', () => {
        const description = document.getElementById("object-description-textarea").value;
        const type = document.getElementById("object-type-select").value;
        const state = document.getElementById("object-state-select").value;
        const date = document.getElementById("object-state-date-input").value;
        const price = document.getElementById("object-price-input").value;
        const isVisible = document.getElementById("object-isVisible-input").defaultChecked;

        API.put(`objects/${object.id}`, {body: {description, type, state, date, price, isVisible}})
        .then(() => {
          Navigate('/admin/objects');
        })
      });

      document.getElementById("cancel-btn").addEventListener('click', () => {
        Navigate('/admin/objects');
      });
    }

}

function setDefaultValues(object){
  document.getElementById("object-type-select").value = object.objectType;
  document.getElementById("object-state-select").value = object.state;
  document.getElementById("object-state-date-input").value = getTodaySDate();
  document.getElementById("object-price-input").value = object.price;
  if (object.isVisible){
    document.getElementById("object-isNotVisible-input").defaultChecked = false;
    document.getElementById("object-isVisible-input").defaultChecked = true;
  }
  else{
    document.getElementById("object-isVisible-input").defaultChecked = false;
    document.getElementById("object-isNotVisible-input").defaultChecked = true;
  }
}

function setUserOrPhoneNumber(className, object, div){
  const element = div.getElementsByClassName(className)[0];
  if (object.user === null){
    element.innerHTML = `<p>Proposé anonymement au ${object.phoneNumber} le ${dateStringtoGoodFormat(object.offerDate)}</p>`;
  }
  else {
    element.innerHTML = `
      <p>
        Proposé par
          <a href="#" class="btn-link" role="button" data-id="${object.user.id}">${object.user.firstName} ${object.user.lastName}</a>
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

export default ObjectPage;
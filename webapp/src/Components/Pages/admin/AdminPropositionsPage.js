import Navigate from '../../Router/Navigate';
import { getAuthenticatedUser } from '../../../utils/auths';
import { clearPage } from '../../../utils/render';
import API from '../../../utils/api';

const AdminPropositionsPage = () => {
  const user = getAuthenticatedUser();

  if (!user || !user.helper) {
    Navigate('/');
    return;
  }

  clearPage();
  renderAdminObjectsPage();
  fetchPropositions();
};

function renderAdminObjectsPage() {
  const main = document.querySelector('main');
  const div = document.createElement('div');
  div.className = 'container my-5';

  div.innerHTML = `
    <h2>Propositions</h2>
    <form class="input-group">
      <input type="text" class="form-control border-end-0" placeholder="Rechercher..." />
      <button class="btn border" type="submit">
        <i class="bi bi-search"></i>
      </button>
    </form>
    <div id="propositions-list"></div>
  `;

  div.querySelector('form').addEventListener('keyup', (e) => {
    e.preventDefault();

    const search = e.target.value;
    fetchPropositions(search);
  });

  main.appendChild(div);
}

async function fetchPropositions(query = '') {
  const list = document.getElementById('propositions-list');

  API.get(`objects?query=${encodeURIComponent(query)}`).then((propositions) => {
    document.getElementById('propositions-list').innerHTML = `
        <div class="container mt-5 mb-5">
            <div class="d-flex justify-content-center row">
                <div class="col-md-10">
                    ${propositions.map((proposition) => `
                        <div class="row p-2 bg-white border rounded">
                            <div class="col-md-3 mt-1">
                                <img 
                                    class="img-fluid img-responsive rounded product-image" 
                                    src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRqsL6QorN-b6YhpcfTl9YJEzWB2xSkhFkN4Q&usqp=CAU"
                                    alt="${proposition.objectType}">
                            </div>
                            <div class="col-md-6 mt-1">
                                <h5>${proposition.objectType}</h5>
                               
                                <div class="mt-1 mb-1 spec-1">
                                    <span>${proposition.description}</span>
                                </div>
                                
                                <p>À récupérer le ${proposition.pickupDate} ${proposition.timeSlot}</p>
                                
                                <div class="div-user">
                                </div>
                            </div>
                            
                            <div class="align-items-center align-content-center col-md-3 border-left mt-1">                                
                                <div class="div-remaining-time">
                                </div>
                                                                
                                <div class="d-flex flex-column mt-4 div-button">
                                    <button class="btn btn-outline-primary btn-sm button-respond" type="button" data-id="${proposition.id}">Répondre</button>
                                </div>
                            </div>
                        </div>
                `,).join('')}
                </div>                     
            </div>
        </div>
      `;

    setUserOrPhoneNumber("div-user", propositions);
    setRemainingTime("div-remaining-time", propositions);

    list.querySelectorAll('a[data-id]').forEach((link) => {
      link.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate(`/user/${e.target.dataset.id}`);
      });
    });

    list.querySelectorAll('button[data-id]').forEach((link) => {
      link.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate(`/object/${e.target.dataset.id}`);
      });
    });
  });
}

function setUserOrPhoneNumber(className, propositions){
  const elements = document.getElementsByClassName(className);
  for (let i = 0; i < elements.length; i += 1) {
    const proposition = propositions[i];
    const element = elements.item(i);
    if (proposition.user === null){
      element.innerHTML = `
          <p>Proposé anonymement par ${proposition.phoneNumber}</p>
      `
    }
    else {
      element.innerHTML = `
          <p>
          Proposé par
              <a href="#" class="btn-link" role="button" data-id="${proposition.user.id}">
                  ${proposition.user.firstName} ${proposition.user.lastName}
              </a>
          </p>
      `
    }
  }
}

function setRemainingTime(className, propositions){
  const elements = document.getElementsByClassName(className);
  for (let i = 0; i < elements.length; i += 1) {
    const proposition = propositions[i];
    const element = elements.item(i);

    const pickupDate = new Date(proposition.pickupDate);
    const todaySDate = new Date();

    const timeRemaining = subtractDates(pickupDate, todaySDate);

    if (timeRemaining <= 3){
      element.innerHTML = `
          <h6 class="text-danger">${timeRemaining} jours restants pour répondre !</h6>
      `
    }
    else {
      element.innerHTML = `
          <h6 class="text-primary">${timeRemaining} jours restants pour répondre</h6>
      `
    }
  }
}

function subtractDates(date1, date2){
  date1.setHours(12,0,0,0)
  date2.setHours(12,0,0,0)
  const diffTime = Math.abs(date2.getTime() - date1.getTime());
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
}

export default AdminPropositionsPage;

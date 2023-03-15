import Navigate from '../../Router/Navigate';
import { getAuthenticatedUser } from '../../../utils/auths';
import { clearPage } from '../../../utils/render';
import API from '../../../utils/api';

const AdminObjectsPage = () => {
  const user = getAuthenticatedUser();

  if (!user || !user.helper) {
    Navigate('/');
    return;
  }

  clearPage();
  renderAdminObjectsPage();
  fetchObjects();
};

function renderAdminObjectsPage() {
  const main = document.querySelector('main');
  const div = document.createElement('div');
  div.className = 'container my-5';

  div.innerHTML = `
    <h2>Objets</h2>
    <form class="input-group">
      <input type="text" class="form-control border-end-0" placeholder="Rechercher..." />
      <button class="btn border" type="submit">
        <i class="bi bi-search"></i>
      </button>
    </form>
    <div id="objects-list"></div>
  `;

  div.querySelector('form').addEventListener('keyup', (e) => {
    e.preventDefault();

    const search = e.target.value;
    fetchObjects(search);
  });

  main.appendChild(div);
}

async function fetchObjects(query = '') {
  const list = document.getElementById('objects-list');

  API.get(`objects?query=${encodeURIComponent(query)}`).then((objects) => {
    document.getElementById('objects-list').innerHTML = `
        <div class="container mt-5 mb-5">
            <div class="d-flex justify-content-center row">
                <div class="col-md-10">
                    ${objects.map((object) => `
                        <div class="row p-2 bg-white border rounded">
                            <div class="col-md-3 mt-1">
                                <img 
                                    class="img-fluid img-responsive rounded product-image" 
                                    src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRqsL6QorN-b6YhpcfTl9YJEzWB2xSkhFkN4Q&usqp=CAU"
                                    alt="${object.objectType}">
                            </div>
                            <div class="col-md-6 mt-1">
                                <h5>${object.objectType}</h5>
                               
                                <div class="mt-1 mb-1 spec-1">
                                    <span>${object.description}</span>
                                </div>
                                
                                <p class="text-justify text-truncate para mb-0">Récupéré le ${object.pickupDate} ${object.timeSlot}</p>
                                
                                <p class="text-justify text-truncate para mb-0">
                                    Proposé par 
                                    ${object.user === null ? object.phoneNumber : 
                                    object.user.lastName.concat(" ", object.user.firstName)}
                                </p>
                            </div>
                            
                            <div class="align-items-center align-content-center col-md-3 border-left mt-1">
                                <div class="d-flex flex-row align-items-center">
                                    <h4 class="mr-1">${object.price} €</h4>
                                </div>
                                
                                <h6 class="text-success">${object.state}</h6>
                                
                                <div class="d-flex flex-column mt-4">
                                    <button class="btn btn-primary btn-sm" type="button">Modifier</button>
                                </div>
                            </div>
                        </div>
                `,).join('')}
                </div>                     
            </div>
        </div>
      `;

    list.querySelectorAll('a[data-id]').forEach((link) => {
      link.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate(`/object/${e.target.dataset.id}`);
      });
    });
  });
}

export default AdminObjectsPage;

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

  div.querySelector('form').addEventListener('submit', (e) => {
    e.preventDefault();

    const search = e.target.querySelector('input').value;
    fetchObjects(search);
  });

  main.appendChild(div);
}

async function fetchObjects(query = '') {
  const list = document.getElementById('objects-list');

  list.innerHTML = `
    <div class="text-center my-5">
      <div class="spinner-border" role="status"></div>
    </div>
  `;

  API.get(`objects?query=${encodeURIComponent(query)}`).then((objects) => {
    document.getElementById('objects-list').innerHTML = `
        <div class="container d-flex justify-content-center mt-50 mb-50">
            <div class="row">
                <div class="col-md-10">
                    ${objects.map((object) => `
                    <div class="card card-body mt-3">
                  
                        <div class="media align-items-center align-items-lg-start text-center text-lg-left flex-column flex-lg-row">
                    
                            <div class="mr-2 mb-3 mb-lg-0">
                                <img src="https://images.pexels.com/photos/1866149/pexels-photo-1866149.jpeg?cs=srgb&dl=pexels-martin-p%C3%A9chy-1866149.jpg&fm=jpg" width="150" height="150" alt="">
                            </div>

                            <div class="media-body">
                                <h6 class="media-title font-weight-semibold">
                                    <a href="#" data-abc="true">${object.description}</a>
                                </h6>
        
                                <h4 class="media-title font-weight-semibold">
                                    <a href="#" data-abc="true">${object.objectType}</a>
                                </h4>
        
                                <p class="mb-3">256 GB ROM | 15.49 cm (6.1 inch) Display 12MP Rear Camera | 15MP Front Camera A12 Bionic Chip Processor | Gorilla Glass with high quality display </p>
        
                                <ul class="list-inline list-inline-dotted mb-0">
                                    <li class="list-inline-item">All items from <a href="#" data-abc="true">Mobile junction</a></li>
                                    <li class="list-inline-item">Add to <a href="#" data-abc="true">wishlist</a></li>
                                </ul>
                            </div>

                            <div class="mt-3 mt-lg-0 ml-lg-3 text-center">
                                <h3 class="mb-0 font-weight-semibold">$612.99</h3>
    
                                <div>
                                    <i class="fa fa-star"></i>
                                    <i class="fa fa-star"></i>
                                    <i class="fa fa-star"></i>
                                    <i class="fa fa-star"></i>
                                    <i class="fa fa-star"></i>
                                </div>
    
                                <div class="text-muted">2349 reviews</div>
    
                                <button type="button" class="btn btn-warning mt-4 text-white"><i class="icon-cart-add mr-2"></i> Add to cart</button>
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

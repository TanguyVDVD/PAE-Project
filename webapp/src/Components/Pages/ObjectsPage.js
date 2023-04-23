import Navigate from '../Router/Navigate';
import { clearPage, renderError } from '../../utils/render';
import { createObjectCard, getObjectTypes } from '../../utils/objects';
// import { getAuthenticatedUser } from '../../utils/auths';
import API from '../../utils/api';

const objects = [];
let params = null;

const ObjectsPage = () => {
  objects.splice(0, objects.length);
  params = new URLSearchParams(window.location.search);

  clearPage();
  renderObjectsPage();
};

function renderObjectsPage() {
  const main = document.querySelector('main');
  const div = document.createElement('div');
  div.className = 'container my-5';

  div.innerHTML = `
    <div class="my-3 hstack gap-3 justify-content-between flex-column flex-md-row">
      <h2>Objets</h2>
      <form class="hstack flex-column flex-md-row gap-3" id="objects-filter-form">
        <div class="input-group">
          <input type="text" class="form-control border-end-0" placeholder="Rechercher..." />
          <button class="btn border" type="submit">
            <i class="bi bi-search"></i>
          </button>
        </div>
        <div class="input-group">
          <label class="input-group-text bg-white" for="filter-select">
            <i class="bi bi-funnel"></i>
          </label>
          <select class="form-select" id="filter-select">
            <option selected value="">Tous les types</option>
          </select>
        </div>
      </form>
    </div>
    <div id="objects" class="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-5 text-center"></div>
  `;

  main.appendChild(div);

  const objectsFilterForm = document.getElementById('objects-filter-form');
  const searchInput = objectsFilterForm.querySelector('input');
  const filterSelect = objectsFilterForm.querySelector('select');

  searchInput.value = params.get('query') || '';

  filterSelect.addEventListener('change', (e) => {
    e.target.form.dispatchEvent(new Event('submit'));
  });

  getObjectTypes().then((types) => {
    filterSelect.innerHTML += `
      ${types
        .map(
          (objectType) => `
          <option value="${objectType.id}">${objectType.label}</option>
        `,
        )
        .join('')}
    `;

    filterSelect.value = params.get('type') || '';
  });

  objectsFilterForm.addEventListener('submit', (e) => {
    e.preventDefault();

    const query = searchInput.value;
    const type = filterSelect.value;

    params.set('query', query);
    params.set('type', type);

    window.history.pushState({}, '', `${window.location.pathname}?${params.toString()}`);

    document.getElementById('objects').classList.add('opacity-25');
    getObjects();
  });

  renderObjects(Array(12).fill(null));
  getObjects();
}

function getObjects() {
  API.get(`objects/public?${params.toString()}`)
    .then((response) => {
      objects.splice(0, objects.length, ...response);
      renderObjects();
    })
    .catch((err) => {
      renderError(err);
    })

    .finally(() => {
      document.getElementById('objects').classList.remove('opacity-25');
    });
}

function renderObjects(_objects = objects) {
  const div = document.getElementById('objects');

  if (_objects.length === 0) {
    div.innerHTML = `
      <div class="text-center text-muted py-5 w-100">
        <p>Aucun objet ne correspond à votre recherche.</p>
      </div>
    `;

    return;
  }

  div.innerHTML = _objects
    .map(
      (object) => `
        <div class="col">
          ${createObjectCard(object)}
        </div>
      `,
    )
    .join('');

  div.querySelectorAll('.object-card').forEach((card) => {
    card.addEventListener('click', (e) => {
      e.preventDefault();

      if (card.dataset.id !== 'undefined') Navigate(`/object/${card.dataset.id}`);
    });
  });
}

export default ObjectsPage;

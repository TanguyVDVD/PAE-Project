import Navigate from '../Router/Navigate';
import { clearPage } from '../../utils/render';
import { createObjectCard } from '../../utils/objects';
import API from '../../utils/api';

import RessourceRieBrand from '../../img/ressourcerie_brand.svg';

const HomePage = () => {
  clearPage();
  renderHomePage();
};

function renderHomePage() {
  const main = document.querySelector('main');

  const hero = document.createElement('div');
  hero.className = 'bg-dark text-white mb-5 hero';

  hero.innerHTML = `
    <div class="container">
      <h1 class="display-5 fw-bold">Arrêtons de jeter nos objets&nbsp;!</h1>
      <p class="lead">
        Nous vous proposons de venir les déposer au parc à conteneurs de Blégny le samedi. Nous les
        récupérerons et les vendrons à bas prix dans notre ressourcerie. Vous pouvez également venir
        acheter des objets à bas prix dans notre ressourcerie.
      </p>
      <div class="hstack gap-3 justify-content-between flex-column flex-md-row">
        <p class="mb-0">
          <img src="${RessourceRieBrand}" alt="RessourceRie" class="ressourcerie-brand" /> se situe
          <a
            href="http://maps.apple.com/?address=Rue+de+Heuseux+77ter,+4671+Blégny,+Belgium&t=m"
            target="_blank"
            class="link-light text-decoration-underline"
          >
          Rue de Heuseux 77ter, 4671 Blégny</a>.
        </p>
        <a class="btn btn-light btn-lg" href="#" role="button" id="propose-btn">
          Proposer un objet
        </a>
      </div>
    </div>
  `;

  main.appendChild(hero);

  document.getElementById('propose-btn').addEventListener('click', (e) => {
    e.preventDefault();

    Navigate('/propose');
  });

  const objects = document.createElement('div');
  objects.className = 'container';

  objects.innerHTML = `
    <div class="my-3 hstack gap-3 justify-content-between flex-column flex-md-row">
      <h2>Objets proposés</h2>
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
            <option selected value="">Filtrer par</option>
          </select>
        </div>
      </form>
    </div>
    <div id="objects-carousel" class="carousel carousel-dark slide"></div>
    <div class="text-center">
      <a href="#" class="btn btn-link mt-3" role="button" id="more-btn">
        Afficher plus
      </a>
    </div>
  `;

  main.appendChild(objects);

  const objectsFilterForm = document.getElementById('objects-filter-form');
  const searchInput = objectsFilterForm.querySelector('input');
  const filterSelect = objectsFilterForm.querySelector('select');

  filterSelect.addEventListener('change', (e) => {
    e.target.form.dispatchEvent(new Event('submit'));
  });

  API.get('/objectsTypes').then((response) => {
    filterSelect.innerHTML += `
      ${response
        .map(
          (objectType) => `
          <option value="${objectType.id}">${objectType.label}</option>
        `,
        )
        .join('')}
    `;
  });

  objectsFilterForm.addEventListener('submit', (e) => {
    e.preventDefault();

    const query = searchInput.value;
    const filter = filterSelect.value;

    const params = new URLSearchParams();
    if (query) params.set('search', query);
    if (filter) params.set('filter', filter);

    if (params.toString()) Navigate(`/objects?${params}`);
  });

  const objectsCarousel = document.getElementById('objects-carousel');
  objectsCarousel.replaceChildren(renderObjectsCarousel([null, null, null, null]));
  API.get('/objects/public').then((response) => {
    objectsCarousel.replaceChildren(renderObjectsCarousel(response));
  });

  document.getElementById('more-btn').addEventListener('click', (e) => {
    e.preventDefault();

    Navigate('/objects');
  });
}

function renderObjectsCarousel(objects = []) {
  const div = document.createElement('div');
  div.id = 'objects-carousel';
  div.className = 'carousel carousel-dark slide';

  if (objects.length === 0) {
    div.innerHTML = `
      <div class="text-center text-muted my-5 w-100">
        <p>Aucun objet proposé</p>
      </div>
    `;

    return div;
  }

  // Split objects into groups of 4
  const objectsGroups = [];
  for (let i = 0; i < objects.length; i += 4) {
    objectsGroups.push(objects.slice(i, i + 4));
  }

  div.innerHTML = `
    <div class="carousel-inner">
      ${
        objectsGroups.length > 0
          ? `
            ${objectsGroups
              .map(
                (objectsGroup, index) => `
                <div class="carousel-item ${index === 0 ? 'active' : ''}">
                  <div class="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-5 text-center">
                    ${objectsGroup
                      .map(
                        (object) => `
                        <div class="col">
                          ${createObjectCard(object)}
                        </div>
                      `,
                      )
                      .join('')}
                  </div>
                </div>
              `,
              )
              .join('')}
          `
          : ''
      }
    </div>
    <button
      class="carousel-control-prev w-auto"
      type="button"
      data-bs-target="#objects-carousel"
      data-bs-slide="prev"
    >
      <span class="carousel-control-prev-icon" aria-hidden="true"></span>
      <span class="visually-hidden">Précédent</span>
    </button>
    <button
      class="carousel-control-next w-auto"
      type="button"
      data-bs-target="#objects-carousel"
      data-bs-slide="next"
    >
      <span class="carousel-control-next-icon" aria-hidden="true"></span>
      <span class="visually-hidden">Suivant</span>
    </button>
  `;

  div.querySelectorAll('.object-card').forEach((card) => {
    card.addEventListener('click', (e) => {
      e.preventDefault();

      if (card.dataset.id !== 'undefined') Navigate(`/object/${card.dataset.id}`);
    });
  });

  return div;
}

export default HomePage;

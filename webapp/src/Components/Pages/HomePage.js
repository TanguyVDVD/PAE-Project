import Navigate from '../Router/Navigate';
import { clearPage } from '../../utils/render';

const HomePage = () => {
  clearPage();
  renderHomePage();
};

function renderHomePage() {
  const main = document.querySelector('main');
  const hero = document.createElement('div');
  hero.className = 'p-4 bg-dark-subtle text-black mb-5';

  hero.innerHTML = `
    <div class="container">
      <h1 class="display-5 fw-bold">Arrêtons de jeter nos objets&nbsp;!</h1>
      <p class="lead">
        Nous vous proposons de venir les déposer au parc à conteneurs de Blégny le samedi. Nous les
        récupérerons et les vendrons à bas prix dans notre ressourcerie. Vous pouvez également venir
        acheter des objets à bas prix dans notre ressourcerie.
      </p>
      <div class="hstack gap-3 justify-content-between flex-column flex-md-row">
        <p class="mb-0">RessourceRie se situe Rue de Heuseux 77ter, 4671 Blégny.</p>
        <a class="btn btn-primary btn-lg" href="#" role="button" id="propose-btn">
          Proposer un objet
        </a>
      </div>
    </div>
  `;

  main.appendChild(hero);

  document.getElementById('propose-btn').addEventListener('click', () => {
    Navigate('/propose');
  });

  const objects = document.createElement('div');
  objects.className = 'container';

  objects.innerHTML = `
    <div class="my-3 hstack gap-3 justify-content-between flex-column flex-md-row">
      <h2>Objets proposés</h2>
      <form class="hstack flex-column flex-md-row gap-3">
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
            <option selected>Filtrer par</option>
            <option value="1">TODO</option>
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

  const objectsCarousel = document.getElementById('objects-carousel');
  objectsCarousel.appendChild(
    renderObjectsCarousel([
      {
        id: 1,
        type: 'Fauteuil',
        name: 'fauteuil tapissé',
        image: '',
      },
      {
        id: 2,
        type: 'Matériel de cuisine',
        name: '3 casseroles',
        image: '',
      },
      {
        id: 3,
        type: 'Matériel de cuisine',
        name: '4 poêles',
        image: '',
      },
      {
        id: 4,
        type: 'Couvertures',
        name: 'couvertures en bon état',
        image: '',
      },
      {
        id: 5,
        type: 'Meuble',
        name: 'anciens tableaux',
        image: '',
      },
      {
        id: 6,
        type: 'Meuble',
        name: 'décoration de cactus',
        image: '',
      },
      {
        id: 7,
        type: 'Matelas',
        name: 'matelas blanc',
        image: '',
      },
      {
        id: 8,
        type: 'Matériel de cuisine',
        name: '4 poêles',
        image: '',
      },
    ]),
  );

  document.getElementById('more-btn').addEventListener('click', () => {
    Navigate('/objects');
  });
}

function renderObjectsCarousel(objects = []) {
  const div = document.createElement('div');
  div.id = 'objects-carousel';
  div.className = 'carousel carousel-dark slide';

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
                          <div class="card card-object" data-id="${object.id}" role="button">
                            <img
                              src="${object.image}"
                              class="card-img-top"
                              alt="Photo : ${object.name}"
                            />
                            <div class="card-title fw-bold m-0">${object.type}</div>
                            <div class="card-body pt-0">
                              ${object.name}
                            </div>
                          </div>
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

  div.querySelectorAll('.card-object').forEach((card) => {
    card.addEventListener('click', () => {
      Navigate(`/object/${card.dataset.id}`);
    });
  });

  return div;
}

export default HomePage;

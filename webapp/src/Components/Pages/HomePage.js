import Navigate from '../Router/Navigate';
import { clearPage } from '../../utils/render';

const HomePage = () => {
  clearPage();
  renderHomePage();
};

function renderHomePage() {
  const main = document.querySelector('main');
  const hero = document.createElement('div');
  hero.className = 'p-4 bg-dark-subtle text-black';

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
    Navigate('/proposer');
  });
}

export default HomePage;

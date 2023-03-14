import Navigate from '../Router/Navigate';
import { clearPage } from '../../utils/render';

const PageNotFoundPage = () => {
  clearPage();

  document.querySelector('main').innerHTML = `
    <div class="container my-5 py-5 text-center">
      <h2 class="display-5 fw-bold mb-4">Page introuvable</h2>
      <p>
        La page que vous recherchez n'existe pas.<br />Elle pourrait avoir été supprimée, renommée
        ou pourrait n'avoir jamais existé.
      </p>
      <a class="btn btn-primary" href="#" role="button" id="home-btn">
        Retour à l'accueil
      </a>
    </div>
  `;

  document.getElementById('home-btn').addEventListener('click', () => {
    Navigate('/');
  });
};

export default PageNotFoundPage;

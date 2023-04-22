import RessourceRieBrand from '../../img/ressourcerie_brand.svg';

const Footer = () => {
  const footer = document.querySelector('footer');

  footer.innerHTML = `
    <div class="container text-center py-3 my-4 border-top">
      <span class="mb-3 mb-md-0 text-body-secondary">
        © 2023
        <img src="${RessourceRieBrand}" alt="RessourceRie" class="ressourcerie-brand" />
      </span>
    </div>
  `;
};

export default Footer;

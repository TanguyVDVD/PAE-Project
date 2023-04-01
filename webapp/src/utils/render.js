const clearPage = () => {
  const main = document.querySelector('main');
  main.innerHTML = '';
};

const renderPageTitle = (title) => {
  if (!title) return;
  const main = document.querySelector('main');
  const pageTitle = document.createElement('h4');
  pageTitle.innerText = title;
  main.appendChild(pageTitle);
};

const renderError = (error, container = document.querySelector('main')) => {
  if (container.querySelector('.alert')) container.querySelector('.alert').remove();

  if (!error) return;

  const alert = document.createElement('div');
  alert.className = 'alert alert-danger alert-dismissible d-flex align-items-center';
  alert.setAttribute('role', 'alert');
  alert.innerHTML = `
    <i class="bi bi-exclamation-triangle-fill me-2"></i>
    <span>${error}</span>
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
  `;

  alert.querySelector('.btn-close').addEventListener('click', () => {
    alert.remove();
  });

  container.prepend(alert);
};

export { clearPage, renderPageTitle, renderError };

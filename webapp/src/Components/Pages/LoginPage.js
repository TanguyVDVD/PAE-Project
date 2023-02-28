const LoginPage = () => {
  renderRegisterForm();
};

function renderRegisterForm() {
  const main = document.querySelector('main');
  const div = document.createElement('div');
  div.className = 'container';
  const form = document.createElement('form');
  form.className = 'p-5';
  const username = document.createElement('input');
  username.type = 'text';
  username.id = 'username';
  username.placeholder = 'username';
  username.required = true;
  username.className = 'form-control mb-3';
  const password = document.createElement('input');
  password.type = 'password';
  password.id = 'password';
  password.required = true;
  password.placeholder = 'password';
  password.className = 'form-control mb-3';
  const submit = document.createElement('input');
  submit.value = 'Login';
  submit.type = 'submit';
  submit.className = 'btn btn-danger';
  form.appendChild(username);
  form.appendChild(password);
  form.appendChild(submit);
  div.appendChild(form);
  main.appendChild(div);
}

export default LoginPage;
/* eslint-disable import/no-cycle */
import { getAuthenticatedUser } from './auths';

const API_URL = '/api';

export default class API {
  static getEndpoint(endpoint) {
    return `${API_URL}/${endpoint}`;
  }

  static async #call(endpoint, options = {}) {
    const currentUser = getAuthenticatedUser();

    const opt = {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        Authorization: currentUser ? currentUser.token : '',
        ...options.headers,
      },
    };

    // If the body is a FormData object, we don't want to set the Content-Type header
    // so the browser will automatically set the correct boundary.
    if (opt.body instanceof FormData) {
      delete opt.headers['Content-Type'];
    }

    const response = await fetch(API.getEndpoint(endpoint), opt);

    const json = await response.json().catch(() => ({ error: 'Une erreur est survenue' }));

    if (json.error) throw new Error(json.error);
    if (!response.ok) throw new Error(`Une erreur est survenue`);

    return json;
  }

  static get(endpoint, options = {}) {
    return API.#call(endpoint, {
      ...options,
      method: 'GET',
    });
  }

  static post(endpoint, options = {}) {
    const body =
      options.body && typeof options.body === 'object' && !(options.body instanceof FormData)
        ? JSON.stringify(options.body)
        : options.body;

    return API.#call(endpoint, {
      ...options,
      body,
      method: 'POST',
    });
  }

  static put(endpoint, options = {}) {
    const body =
      options.body && typeof options.body === 'object' && !(options.body instanceof FormData)
        ? JSON.stringify(options.body)
        : options.body;

    return API.#call(endpoint, {
      ...options,
      body,
      method: 'PUT',
    });
  }

  static patch(endpoint, options = {}) {
    const body =
      options.body && typeof options.body === 'object' && !(options.body instanceof FormData)
        ? JSON.stringify(options.body)
        : options.body;

    return API.#call(endpoint, {
      ...options,
      body,
      method: 'PATCH',
    });
  }

  static delete(endpoint, options = {}) {
    return API.#call(endpoint, {
      ...options,
      method: 'DELETE',
    });
  }
}

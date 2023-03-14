/* eslint-disable import/no-cycle */
import { getAuthenticatedUser } from './auths';

const API_URL = '/api';

export default class API {
  static async #call(endpoint, options = {}) {
    const currentUser = getAuthenticatedUser();

    const response = await fetch(`${API_URL}/${endpoint}`, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        Authorization: currentUser ? currentUser.token : '',
        ...options.headers,
      },
    });

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
}

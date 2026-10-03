/**
 * api.js — Central API Communication Hub
 * ========================================
 * ALL calls to the Spring Boot backend go through this file.
 * Base URL: http://localhost:8080
 *
 * Other JS files use these functions like:
 *   const cars = await api.get('/api/cars');
 *   const booking = await api.post('/api/bookings', bookingData);
 */

const API_BASE_URL = 'http://localhost:8080';

// ----------------------------------------------------------------
// TOKEN STORAGE HELPERS
// We store the JWT token in sessionStorage (cleared when browser closes)
// NEVER store passwords here — only the token issued by the backend
// ----------------------------------------------------------------

const auth = {
  /**
   * Save the JWT token and user info after a successful login
   * Called from: auth.js (after login/register)
   */
  saveSession(token, user) {
    sessionStorage.setItem('km_token', token);
    sessionStorage.setItem('km_user', JSON.stringify(user));
  },

  /** Get the stored JWT token */
  getToken() {
    return sessionStorage.getItem('km_token');
  },

  /** Get the stored user object (id, name, email, role) */
  getUser() {
    const raw = sessionStorage.getItem('km_user');
    return raw ? JSON.parse(raw) : null;
  },

  /** Check if a customer is currently logged in */
  isLoggedIn() {
    return !!this.getToken();
  },

  /** Check if the logged-in user is an admin */
  isAdmin() {
    const user = this.getUser();
    return user && user.role === 'ROLE_ADMIN';
  },

  /**
   * Clear all stored session data (logout)
   * Called from: main.js (logout button)
   */
  clearSession() {
    sessionStorage.removeItem('km_token');
    sessionStorage.removeItem('km_user');
  }
};

// ----------------------------------------------------------------
// CORE API FUNCTIONS
// ----------------------------------------------------------------

/**
 * Build request headers.
 * Automatically adds Authorization: Bearer <token> if logged in.
 */
function buildHeaders(includeAuth = true) {
  const headers = {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  };

  if (includeAuth) {
    const token = auth.getToken();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
  }

  return headers;
}

/**
 * Handle the HTTP response.
 * - If 401 (Unauthorized) → session expired → redirect to login
 * - If 403 (Forbidden) → access denied
 * - If response has JSON body, parse it
 * - Otherwise throw a descriptive error
 */
async function handleResponse(response, endpoint) {
  // 401 Unauthorized
  if (response.status === 401) {
    if (endpoint.includes('/api/auth/login')) {
      throw new Error('Invalid email or password. Please check your credentials.');
    }
    auth.clearSession();
    showToast('Your session has expired. Please log in again.', 'error');
    setTimeout(() => {
      // FIX BUG-018: Use absolute paths so redirects work from any subdirectory (e.g. /admin/)
      const isAdmin = window.location.pathname.includes('/admin/');
      const frontendRoot = window.location.origin;
      window.location.href = isAdmin
        ? `${frontendRoot}/admin/admin-login.html`
        : `${frontendRoot}/login.html`;
    }, 1500);
    throw new Error('Session expired');
  }

  // 403 = Logged in but not allowed
  if (response.status === 403) {
    throw new Error('You do not have permission to perform this action.');
  }

  // 404 = Resource not found
  if (response.status === 404) {
    throw new Error('The requested resource was not found.');
  }

  // Try to parse JSON body (backend always sends JSON)
  let data;
  try {
    data = await response.json();
  } catch {
    // Response had no JSON body
    data = null;
  }

  // If request failed (4xx or 5xx), throw with backend's error message
  if (!response.ok) {
    const message = data?.message || data?.error || `Server error (${response.status})`;
    throw new Error(message);
  }

  return data;
}

/**
 * The main API object — use these in all other JS files
 */
const api = {

  /**
   * GET request — Fetch data from backend
   * @param {string} endpoint - e.g. '/api/cars'
   * @param {boolean} requireAuth - true if token needed (default: false for public)
   *
   * Usage: const cars = await api.get('/api/cars');
   */
  async get(endpoint, requireAuth = false) {
    try {
      const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        method: 'GET',
        headers: buildHeaders(requireAuth || auth.isLoggedIn())
      });
      return await handleResponse(response, endpoint);
    } catch (error) {
      if (error.message === 'Session expired') throw error;
      if (error.message === 'Failed to fetch') {
        throw new Error('Unable to connect to the server. Is the backend running?');
      }
      throw error;
    }
  },

  /**
   * POST request — Send data to backend (create new resource)
   * @param {string} endpoint - e.g. '/api/bookings'
   * @param {object} body - The data to send (will be JSON-stringified)
   * @param {boolean} requireAuth - true if token needed
   *
   * Usage: const booking = await api.post('/api/bookings', { carId: 1, ... });
   */
  async post(endpoint, body, requireAuth = true) {
    try {
      const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        method: 'POST',
        headers: buildHeaders(requireAuth),
        body: JSON.stringify(body)
      });
      return await handleResponse(response, endpoint);
    } catch (error) {
      if (error.message === 'Session expired') throw error;
      if (error.message === 'Failed to fetch') {
        throw new Error('Unable to connect to the server. Is the backend running?');
      }
      throw error;
    }
  },

  /**
   * PUT request — Update existing data
   * @param {string} endpoint - e.g. '/api/cars/1'
   * @param {object} body - Updated data
   *
   * Usage: await api.put('/api/cars/1', { isAvailable: false });
   */
  async put(endpoint, body, requireAuth = true) {
    try {
      const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        method: 'PUT',
        headers: buildHeaders(requireAuth),
        body: JSON.stringify(body)
      });
      return await handleResponse(response, endpoint);
    } catch (error) {
      if (error.message === 'Session expired') throw error;
      if (error.message === 'Failed to fetch') {
        throw new Error('Unable to connect to the server. Is the backend running?');
      }
      throw error;
    }
  },

  /**
   * DELETE request — Remove a resource
   * @param {string} endpoint - e.g. '/api/cars/1'
   *
   * Usage: await api.delete('/api/cars/1');
   */
  async delete(endpoint, requireAuth = true) {
    try {
      const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        method: 'DELETE',
        headers: buildHeaders(requireAuth)
      });
      // Some DELETE endpoints return 204 (no content) — handle that
      if (response.status === 204) return { success: true };
      return await handleResponse(response, endpoint);
    } catch (error) {
      if (error.message === 'Session expired') throw error;
      if (error.message === 'Failed to fetch') {
        throw new Error('Unable to connect to the server. Is the backend running?');
      }
      throw error;
    }
  },

  /**
   * POST without auth — specifically for login and register
   * These endpoints don't need a JWT token
   */
  async postPublic(endpoint, body) {
    return this.post(endpoint, body, false);
  }
};

// ----------------------------------------------------------------
// TOAST NOTIFICATION HELPER
// Used across all pages to show success/error messages
// ----------------------------------------------------------------

/**
 * Show a pop-up toast notification
 * @param {string} message - The text to show
 * @param {'success'|'error'|'info'} type - Controls color
 * @param {number} duration - Auto-dismiss after ms (default: 3500)
 *
 * Usage: showToast('Booking created!', 'success');
 *        showToast('Car not available.', 'error');
 */
function showToast(message, type = 'info', duration = 3500) {
  // Create container if it doesn't exist
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.textContent = message;

  container.appendChild(toast);

  // Auto remove after duration
  setTimeout(() => {
    toast.style.animation = 'slideInRight 0.3s ease reverse forwards';
    setTimeout(() => toast.remove(), 300);
  }, duration);
}

// Make them globally available
window.api  = api;
window.auth = auth;
window.showToast = showToast;

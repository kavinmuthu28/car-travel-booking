/**
 * auth.js — Login & Registration with JWT token storage
 */

async function handleLoginSubmit(e) {
  e.preventDefault();
  const btn = document.getElementById('btn-login-submit');
  const email = document.getElementById('login-email').value.trim();
  const password = document.getElementById('login-password').value;

  if (!email || !password) {
    showToast('Please enter both email and password.', 'error');
    return false;
  }

  btn.disabled = true;
  btn.textContent = 'Signing in...';

  try {
    const data = await api.postPublic('/api/auth/login', { email, password });
    
    // Save JWT token & user info in session
    auth.saveSession(data.token, {
      id: data.id,
      name: data.name,
      email: data.email,
      role: data.role
    });

    showToast(`Welcome back, ${data.name || 'User'}!`, 'success');

    // Check redirect parameter (e.g., if user was on booking.html)
    const params = new URLSearchParams(window.location.search);
    const redirect = params.get('redirect');

    setTimeout(() => {
      if ((data.role === 'ROLE_ADMIN' || data.role === 'role_admin')) {
        window.location.href = 'admin/dashboard.html';
      } else if (redirect) {
        window.location.href = redirect;
      } else {
        window.location.href = 'index.html';
      }
    }, 1000);
  } catch (err) {
    showToast(err.message || 'Login failed. Please check your credentials.', 'error');
    btn.disabled = false;
    btn.textContent = 'Sign In';
  }

  return false;
}

async function handleRegisterSubmit(e) {
  e.preventDefault();
  const btn = document.getElementById('btn-reg-submit');
  const name = document.getElementById('reg-name').value.trim();
  const email = document.getElementById('reg-email').value.trim();
  const phone = document.getElementById('reg-phone').value.trim();
  const password = document.getElementById('reg-password').value;
  const confirmPassword = document.getElementById('reg-confirm-password').value;

  if (password !== confirmPassword) {
    showToast('Passwords do not match.', 'error');
    return false;
  }

  if (password.length < 6) {
    showToast('Password must be at least 6 characters.', 'error');
    return false;
  }

  btn.disabled = true;
  btn.textContent = 'Creating account...';

  try {
    const data = await api.postPublic('/api/auth/register', { name, email, phone, password });
    
    // Automatically login with received token
    auth.saveSession(data.token, {
      id: data.id,
      name: data.name,
      email: data.email,
      role: data.role
    });

    showToast('Account created successfully!', 'success');

    const params = new URLSearchParams(window.location.search);
    const redirect = params.get('redirect');

    setTimeout(() => {
      if (redirect) {
        window.location.href = redirect;
      } else {
        window.location.href = 'index.html';
      }
    }, 1000);
  } catch (err) {
    showToast(err.message || 'Registration failed. Email might already exist.', 'error');
    btn.disabled = false;
    btn.textContent = 'Create Account';
  }

  return false;
}

window.handleLoginSubmit = handleLoginSubmit;
window.handleRegisterSubmit = handleRegisterSubmit;

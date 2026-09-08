/**
 * main.js — Shared Logic for ALL Pages
 * ======================================
 * Every HTML page loads this file.
 * Handles:
 *  - Navbar active link highlighting
 *  - Mobile hamburger menu toggle
 *  - Navbar scroll effect
 *  - Login/logout state in navbar
 *  - Logout button behavior
 */

document.addEventListener('DOMContentLoaded', () => {
  initNavbar();
  updateNavbarAuthState();
  highlightActiveNavLink();
});

// ----------------------------------------------------------------
// NAVBAR: Scroll Effect
// Adds .scrolled class when user scrolls down — increases shadow
// ----------------------------------------------------------------
function initNavbar() {
  const navbar = document.querySelector('.navbar');
  if (!navbar) return;

  window.addEventListener('scroll', () => {
    if (window.scrollY > 20) {
      navbar.classList.add('scrolled');
    } else {
      navbar.classList.remove('scrolled');
    }
  });

  // Mobile hamburger toggle
  const toggle = document.querySelector('.nav-toggle');
  const mobileMenu = document.querySelector('.nav-mobile-menu');

  if (toggle && mobileMenu) {
    toggle.addEventListener('click', () => {
      const isOpen = toggle.classList.toggle('open');
      mobileMenu.classList.toggle('open', isOpen);
      // Prevent body scroll when menu is open
      document.body.style.overflow = isOpen ? 'hidden' : '';
    });

    // Close mobile menu when a link is clicked
    mobileMenu.querySelectorAll('a').forEach(link => {
      link.addEventListener('click', () => {
        toggle.classList.remove('open');
        mobileMenu.classList.remove('open');
        document.body.style.overflow = '';
      });
    });

    // Close menu if user clicks outside it
    document.addEventListener('click', (e) => {
      if (!navbar.contains(e.target) && !mobileMenu.contains(e.target)) {
        toggle.classList.remove('open');
        mobileMenu.classList.remove('open');
        document.body.style.overflow = '';
      }
    });
  }
}

// ----------------------------------------------------------------
// NAVBAR: Show correct links based on login state
//
// Not logged in:
//   Shows: [Login] [Register]
//
// Logged in as CUSTOMER:
//   Shows: Hello, Kavin | [My Bookings] [Logout]
//
// Logged in as ADMIN:
//   Shows: [Admin Dashboard] [Logout]
// ----------------------------------------------------------------
function updateNavbarAuthState() {
  // These elements are defined in the navbar HTML
  const guestLinks    = document.getElementById('nav-guest');
  const customerLinks = document.getElementById('nav-customer');
  const adminLinks    = document.getElementById('nav-admin');
  const userNameEl    = document.getElementById('nav-user-name');
  const mobileGuestLinks    = document.getElementById('mobile-nav-guest');
  const mobileCustomerLinks = document.getElementById('mobile-nav-customer');
  const mobileAdminLinks    = document.getElementById('mobile-nav-admin');
  const mobileUserNameEl    = document.getElementById('mobile-nav-user-name');

  const user = auth.getUser();

  if (!user) {
    // Not logged in — show Login / Register
    show(guestLinks);
    hide(customerLinks);
    hide(adminLinks);
    show(mobileGuestLinks);
    hide(mobileCustomerLinks);
    hide(mobileAdminLinks);
    return;
  }

  // Logged in — set user name greeting
  const name = user.name || user.email.split('@')[0];
  if (userNameEl) userNameEl.textContent = `Hello, ${name}`;
  if (mobileUserNameEl) mobileUserNameEl.textContent = `Hello, ${name}`;

  if (auth.isAdmin()) {
    // Admin logged in
    hide(guestLinks);
    hide(customerLinks);
    show(adminLinks);
    hide(mobileGuestLinks);
    hide(mobileCustomerLinks);
    show(mobileAdminLinks);
  } else {
    // Customer logged in
    hide(guestLinks);
    show(customerLinks);
    hide(adminLinks);
    hide(mobileGuestLinks);
    show(mobileCustomerLinks);
    hide(mobileAdminLinks);
  }
}

// ----------------------------------------------------------------
// LOGOUT — Called when user clicks "Logout"
// ----------------------------------------------------------------
function logout() {
  auth.clearSession();
  showToast('You have been logged out.', 'info', 2000);
  setTimeout(() => {
    const isInAdmin = window.location.pathname.includes('/admin/');
    window.location.href = isInAdmin ? '../login.html' : 'login.html';
  }, 1000);
}

function adminLogout() {
  auth.clearSession();
  showToast('Logged out from admin.', 'info', 2000);
  setTimeout(() => {
    const isInAdmin = window.location.pathname.includes('/admin/');
    window.location.href = isInAdmin ? 'admin-login.html' : 'admin/admin-login.html';
  }, 1000);
}

// ----------------------------------------------------------------
// NAVBAR: Highlight the current page's nav link
// Compares current URL with nav link href and adds .active class
// ----------------------------------------------------------------
function highlightActiveNavLink() {
  const currentPath = window.location.pathname;

  document.querySelectorAll('.nav-links a, .nav-mobile-menu a').forEach(link => {
    const linkPath = new URL(link.href, window.location.origin).pathname;
    if (linkPath === currentPath || (currentPath === '/' && linkPath.endsWith('index.html'))) {
      link.classList.add('active');
    }
  });
}

// ----------------------------------------------------------------
// GUARD: Protect pages that require login
// Call requireLogin() at the top of a page's JS to redirect if not logged in
// ----------------------------------------------------------------
function requireLogin(redirectTo = 'login.html') {
  if (!auth.isLoggedIn()) {
    showToast('Please log in to continue.', 'info');
    setTimeout(() => {
      window.location.href = redirectTo;
    }, 1000);
    return false;
  }
  return true;
}

/**
 * Guard: Protect pages that require ADMIN role
 * Call requireAdmin() at the top of admin page JS
 * Note: This is a UI convenience only — the backend enforces actual security
 */
function requireAdmin(redirectTo = '../admin/admin-login.html') {
  if (!auth.isLoggedIn()) {
    window.location.href = redirectTo;
    return false;
  }
  if (!auth.isAdmin()) {
    showToast('You do not have permission to access this page.', 'error');
    setTimeout(() => {
      window.location.href = redirectTo;
    }, 1500);
    return false;
  }
  return true;
}

// ----------------------------------------------------------------
// UTILITY HELPERS — used across many JS files
// ----------------------------------------------------------------

/** Hide an element */
function hide(el) {
  if (el) el.style.display = 'none';
}

/** Show an element (default: flex) */
function show(el, display = 'flex') {
  if (el) el.style.display = display;
}

/** Format a date from "2026-09-15" → "15 Sep 2026" */
function formatDate(dateStr) {
  if (!dateStr) return '—';
  const date = new Date(dateStr);
  return date.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
}

/** Format currency: 2300 → "₹2,300" */
function formatCurrency(amount) {
  if (amount == null) return '₹0';
  return '₹' + Number(amount).toLocaleString('en-IN');
}

/** Format time: "08:00:00" → "8:00 AM" */
function formatTime(timeStr) {
  if (!timeStr) return '—';
  const [h, m] = timeStr.split(':').map(Number);
  const ampm = h >= 12 ? 'PM' : 'AM';
  const hour = h % 12 || 12;
  return `${hour}:${String(m).padStart(2, '0')} ${ampm}`;
}

/** Get a status badge HTML string */
function statusBadge(status) {
  const map = {
    'PENDING':    'badge-warning',
    'CONFIRMED':  'badge-success',
    'CANCELLED':  'badge-danger',
    'COMPLETED':  'badge-info',
    'SUCCESSFUL': 'badge-success',
    'FAILED':     'badge-danger',
    'INITIATED':  'badge-secondary',
    'REFUNDED':   'badge-info'
  };
  const cls = map[status] || 'badge-secondary';
  return `<span class="badge ${cls}">${status}</span>`;
}

/** Capitalize first letter */
function capitalize(str) {
  if (!str) return '';
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}

// Make shared utilities globally available
window.logout = logout;
window.adminLogout = adminLogout;
window.requireLogin = requireLogin;
window.requireAdmin = requireAdmin;
window.show = show;
window.hide = hide;
window.formatDate = formatDate;
window.formatCurrency = formatCurrency;
window.formatTime = formatTime;
window.statusBadge = statusBadge;
window.capitalize = capitalize;

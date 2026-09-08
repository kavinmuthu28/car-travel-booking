/**
 * admin.js — Admin portal logic (Authentication, Dashboard stats, Cars CRUD, Bookings management, Users, and Payments)
 */

// -------------------------------------------------------------
// ADMIN LOGIN
// -------------------------------------------------------------
async function handleAdminLogin(e) {
  e.preventDefault();
  const btn = document.getElementById('btn-admin-submit');
  const email = document.getElementById('admin-email').value.trim();
  const password = document.getElementById('admin-password').value;

  btn.disabled = true;
  btn.textContent = 'Verifying admin credentials...';

  try {
    const data = await api.postPublic('/api/auth/login', { email, password });
    
    if (data.role !== 'ROLE_ADMIN') {
      showToast('Access denied. Administrator accounts only.', 'error');
      btn.disabled = false;
      btn.textContent = 'Access Dashboard';
      return false;
    }

    auth.saveSession(data.token, {
      id: data.id,
      name: data.name,
      email: data.email,
      role: data.role
    });

    showToast('Admin access granted.', 'success');
    setTimeout(() => {
      window.location.href = 'dashboard.html';
    }, 1000);
  } catch (err) {
    showToast(err.message || 'Invalid administrator credentials.', 'error');
    btn.disabled = false;
    btn.textContent = 'Access Dashboard';
  }

  return false;
}

// -------------------------------------------------------------
// ADMIN DASHBOARD
// -------------------------------------------------------------
async function initAdminDashboard() {
  const user = auth.getUser();
  if (user && document.getElementById('admin-display-name')) {
    document.getElementById('admin-display-name').textContent = `Admin: ${user.name || user.email.split('@')[0]}`;
  }

  try {
    const [cars, bookings, payments] = await Promise.allSettled([
      api.get('/api/cars', true),
      api.get('/api/bookings', true),
      api.get('/api/payments', true)
    ]);

    const carsList = cars.status === 'fulfilled' ? (cars.value || []) : [];
    const bookingsList = bookings.status === 'fulfilled' ? (bookings.value || []) : [];
    const paymentsList = payments.status === 'fulfilled' ? (payments.value || []) : [];

    // Calculate metrics
    const totalCars = carsList.length;
    const availCars = carsList.filter(c => c.isAvailable || c.available).length;
    const totalBookings = bookingsList.length;

    let revenue = 0;
    paymentsList.forEach(p => {
      if (p.paymentStatus === 'SUCCESSFUL') {
        revenue += Number(p.amount || 0);
      }
    });

    document.getElementById('stat-total-cars').textContent = totalCars;
    document.getElementById('stat-available-cars').textContent = availCars;
    document.getElementById('stat-total-bookings').textContent = totalBookings;
    document.getElementById('stat-revenue').textContent = formatCurrency(revenue);

    // Recent Bookings (first 5)
    const tbody = document.getElementById('recent-bookings-tbody');
    if (bookingsList.length === 0) {
      tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;padding:20px;">No bookings recorded yet.</td></tr>';
      return;
    }

    tbody.innerHTML = bookingsList.slice(0, 5).map(b => `
      <tr>
        <td><strong>${b.bookingNumber || '#' + b.id}</strong></td>
        <td>${b.customerName || 'Customer'}</td>
        <td>${b.pickupAddress} → ${b.dropoffAddress}</td>
        <td>${b.carBrand || ''} ${b.carName || ''}</td>
        <td>${formatDate(b.pickupDate)}</td>
        <td><strong>${formatCurrency(b.totalAmount)}</strong></td>
        <td>${statusBadge(b.bookingStatus)}</td>
      </tr>
    `).join('');
  } catch (err) {
    console.error('Error loading dashboard stats', err);
  }
}

// -------------------------------------------------------------
// ADMIN CARS MANAGEMENT
// -------------------------------------------------------------
let adminCarsList = [];

async function initAdminCars() {
  const tbody = document.getElementById('admin-cars-tbody');
  try {
    const data = await api.get('/api/cars', true);
    adminCarsList = data || [];
    renderAdminCarsTable(adminCarsList);
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="9" style="text-align:center;color:red;">Failed to load cars: ${err.message}</td></tr>`;
  }
}

function renderAdminCarsTable(cars) {
  const tbody = document.getElementById('admin-cars-tbody');
  const fallbackImg = 'https://images.unsplash.com/photo-1549924231-f129b911e442?w=400&q=80';

  if (!cars || cars.length === 0) {
    tbody.innerHTML = '<tr><td colspan="9" style="text-align:center;padding:20px;">No cars in database. Click "+ Add New Car" to create one.</td></tr>';
    return;
  }

  tbody.innerHTML = cars.map(c => {
    const isAvail = c.isAvailable || c.available;
    return `
      <tr>
        <td><img src="${c.imageUrl || fallbackImg}" class="table-thumb" onerror="this.src='${fallbackImg}'" /></td>
        <td><strong>${c.name}</strong><br/><small style="color:#6c757d;">${c.brand}</small></td>
        <td>${c.modelYear || '—'}</td>
        <td>${c.seatingCapacity} seats</td>
        <td>${c.fuelType}</td>
        <td>${c.transmission}</td>
        <td><strong>₹${c.pricePerKm}/km</strong></td>
        <td>
          <button class="badge ${isAvail ? 'badge-success' : 'badge-danger'}" style="cursor:pointer;" onclick="toggleCarAvail(${c.id}, ${!isAvail})">
            ${isAvail ? 'Available' : 'Unavailable'}
          </button>
        </td>
        <td>
          <button class="btn btn-secondary btn-sm" onclick="editCarModal(${c.id})">Edit</button>
          <button class="btn btn-danger btn-sm" onclick="deleteCarPrompt(${c.id})">Delete</button>
        </td>
      </tr>
    `;
  }).join('');
}

function openCarModal(car = null) {
  const modal = document.getElementById('car-modal');
  const title = document.getElementById('car-modal-title');
  const editId = document.getElementById('car-edit-id');

  if (car) {
    title.textContent = 'Edit Vehicle';
    editId.value = car.id;
    document.getElementById('car-name').value = car.name || '';
    document.getElementById('car-brand').value = car.brand || '';
    document.getElementById('car-year').value = car.modelYear || 2023;
    document.getElementById('car-seats').value = car.seatingCapacity || 4;
    document.getElementById('car-fuel').value = (car.fuelType || 'petrol').toLowerCase();
    document.getElementById('car-trans').value = (car.transmission || 'manual').toLowerCase();
    document.getElementById('car-price').value = car.pricePerKm || 12.0;
    document.getElementById('car-avail').value = String(car.isAvailable != null ? car.isAvailable : true);
    document.getElementById('car-image').value = car.imageUrl || '';
  } else {
    title.textContent = 'Add New Vehicle';
    editId.value = '';
    document.getElementById('car-form').reset();
    document.getElementById('car-avail').value = 'true';
  }

  modal.style.display = 'flex';
}

function closeCarModal() {
  document.getElementById('car-modal').style.display = 'none';
}

function editCarModal(carId) {
  const car = adminCarsList.find(x => x.id === carId);
  if (car) openCarModal(car);
}

async function handleCarFormSubmit(e) {
  e.preventDefault();
  const editId = document.getElementById('car-edit-id').value;
  
  const payload = {
    name: document.getElementById('car-name').value.trim(),
    brand: document.getElementById('car-brand').value.trim(),
    modelYear: parseInt(document.getElementById('car-year').value, 10),
    seatingCapacity: parseInt(document.getElementById('car-seats').value, 10),
    fuelType: document.getElementById('car-fuel').value.toLowerCase(),
    transmission: document.getElementById('car-trans').value.toLowerCase(),
    pricePerKm: parseFloat(document.getElementById('car-price').value),
    isAvailable: document.getElementById('car-avail').value === 'true',
    imageUrl: document.getElementById('car-image').value.trim() || 'https://images.unsplash.com/photo-1549924231-f129b911e442?w=400&q=80'
  };

  try {
    if (editId) {
      await api.put(`/api/cars/${editId}`, payload, true);
      showToast('Car updated successfully.', 'success');
    } else {
      await api.post('/api/cars', payload, true);
      showToast('New car added to fleet.', 'success');
    }
    closeCarModal();
    initAdminCars();
  } catch (err) {
    showToast(`Operation failed: ${err.message}`, 'error');
  }
}

async function toggleCarAvail(carId, newStatus) {
  const car = adminCarsList.find(x => x.id === carId);
  if (!car) return;

  const payload = { ...car, isAvailable: newStatus };
  try {
    await api.put(`/api/cars/${carId}`, payload, true);
    showToast(`Car is now marked as ${newStatus ? 'Available' : 'Unavailable'}.`, 'info');
    initAdminCars();
  } catch (err) {
    showToast(`Failed to update status: ${err.message}`, 'error');
  }
}

async function deleteCarPrompt(carId) {
  if (!confirm('Are you sure you want to delete this car from the fleet?')) {
    return;
  }
  try {
    await api.delete(`/api/cars/${carId}`, true);
    showToast('Car removed from fleet.', 'success');
    initAdminCars();
  } catch (err) {
    showToast(`Delete failed: ${err.message}`, 'error');
  }
}

// -------------------------------------------------------------
// ADMIN BOOKINGS MANAGEMENT
// -------------------------------------------------------------
let adminAllBookings = [];

async function initAdminBookings() {
  const tbody = document.getElementById('admin-bookings-tbody');
  try {
    const data = await api.get('/api/bookings', true);
    adminAllBookings = data || [];
    renderAdminBookingsTable(adminAllBookings);
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="9" style="text-align:center;color:red;">Failed to load bookings: ${err.message}</td></tr>`;
  }
}

function filterAdminBookings(status) {
  if (status === 'ALL') {
    renderAdminBookingsTable(adminAllBookings);
  } else {
    const filtered = adminAllBookings.filter(b => b.bookingStatus === status);
    renderAdminBookingsTable(filtered);
  }
}

function renderAdminBookingsTable(bookings) {
  const tbody = document.getElementById('admin-bookings-tbody');
  if (!bookings || bookings.length === 0) {
    tbody.innerHTML = '<tr><td colspan="9" style="text-align:center;padding:20px;">No bookings found.</td></tr>';
    return;
  }

  tbody.innerHTML = bookings.map(b => `
    <tr>
      <td><strong>${b.bookingNumber || '#' + b.id}</strong></td>
      <td>${b.customerName || 'Customer'}</td>
      <td><small>${b.customerPhone || '—'}<br/>${b.customerEmail || ''}</small></td>
      <td>${b.pickupAddress} → ${b.dropoffAddress}</td>
      <td>${b.carBrand || ''} ${b.carName || ''}</td>
      <td>${formatDate(b.pickupDate)}<br/><small>${formatTime(b.pickupTime)}</small></td>
      <td><strong>${formatCurrency(b.totalAmount)}</strong></td>
      <td>${statusBadge(b.bookingStatus)}</td>
      <td>
        <select onchange="changeBookingStatus(${b.id}, this.value)" style="padding:4px 8px;border-radius:4px;font-size:0.8rem;">
          <option value="">Change...</option>
          <option value="CONFIRMED">Confirm</option>
          <option value="COMPLETED">Complete</option>
          <option value="CANCELLED">Cancel</option>
        </select>
      </td>
    </tr>
  `).join('');
}

async function changeBookingStatus(bookingId, status) {
  if (!status) return;
  try {
    await api.put(`/api/bookings/${bookingId}/status`, { status }, true);
    showToast(`Booking #${bookingId} status updated to ${status}.`, 'success');
    initAdminBookings();
  } catch (err) {
    showToast(`Status update failed: ${err.message}`, 'error');
  }
}

// -------------------------------------------------------------
// ADMIN USERS
// -------------------------------------------------------------
async function initAdminUsers() {
  const tbody = document.getElementById('admin-users-tbody');
  try {
    let users = [];
    try {
      users = await api.get('/api/users', true);
    } catch {
      // Fallback: build user records from unique booking customer info
      const bookings = await api.get('/api/bookings', true);
      const userMap = new Map();
      (bookings || []).forEach(b => {
        if (b.userId && !userMap.has(b.userId)) {
          userMap.set(b.userId, {
            id: b.userId,
            name: b.customerName,
            email: b.customerEmail,
            phone: b.customerPhone,
            role: 'ROLE_CUSTOMER'
          });
        }
      });
      users = Array.from(userMap.values());
    }

    if (!users || users.length === 0) {
      tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;padding:20px;">No user records found.</td></tr>';
      return;
    }

    tbody.innerHTML = users.map(u => `
      <tr>
        <td><strong>#${u.id}</strong></td>
        <td>${u.name || 'User'}</td>
        <td>${u.email || '—'}</td>
        <td>${u.phone || '—'}</td>
        <td><span class="badge ${u.role === 'ROLE_ADMIN' ? 'badge-danger' : 'badge-info'}">${u.role || 'ROLE_CUSTOMER'}</span></td>
      </tr>
    `).join('');
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="5" style="text-align:center;color:red;">Error loading users: ${err.message}</td></tr>`;
  }
}

// -------------------------------------------------------------
// ADMIN PAYMENTS
// -------------------------------------------------------------
async function initAdminPayments() {
  const tbody = document.getElementById('admin-payments-tbody');
  try {
    const payments = await api.get('/api/payments', true);
    if (!payments || payments.length === 0) {
      tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;padding:20px;">No payment transactions recorded.</td></tr>';
      return;
    }

    tbody.innerHTML = payments.map(p => `
      <tr>
        <td><strong>#${p.id}</strong></td>
        <td>#${p.bookingId}</td>
        <td><code>${p.transactionId || '—'}</code></td>
        <td>${p.paymentMethod || 'Online'}</td>
        <td><strong>${formatCurrency(p.amount)}</strong></td>
        <td>${statusBadge(p.paymentStatus)}</td>
        <td>${formatDate(p.paymentDate || p.createdAt)}</td>
      </tr>
    `).join('');
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;color:red;">Error loading payments: ${err.message}</td></tr>`;
  }
}

// Expose globals
window.handleAdminLogin = handleAdminLogin;
window.initAdminDashboard = initAdminDashboard;
window.initAdminCars = initAdminCars;
window.openCarModal = openCarModal;
window.closeCarModal = closeCarModal;
window.editCarModal = editCarModal;
window.handleCarFormSubmit = handleCarFormSubmit;
window.toggleCarAvail = toggleCarAvail;
window.deleteCarPrompt = deleteCarPrompt;
window.initAdminBookings = initAdminBookings;
window.filterAdminBookings = filterAdminBookings;
window.changeBookingStatus = changeBookingStatus;
window.initAdminUsers = initAdminUsers;
window.initAdminPayments = initAdminPayments;

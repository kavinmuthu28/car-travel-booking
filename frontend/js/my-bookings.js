/**
 * my-bookings.js — Customer bookings view, modal breakdown, and cancellation logic
 * KAVIN TRAVELS
 */

let myBookings = [];

document.addEventListener('DOMContentLoaded', () => {
  if (!auth.isLoggedIn()) {
    showToast('Please log in to view your bookings.', 'info');
    setTimeout(() => {
      window.location.href = 'login.html?redirect=my-bookings.html';
    }, 1200);
    return;
  }

  loadCustomerBookings();
});

async function loadCustomerBookings() {
  const container = document.getElementById('bookings-list-container');
  const user = auth.getUser();

  try {
    const data = await api.get(`/api/bookings/user/${user.id}`, true);
    myBookings = data || [];
    renderBookingsList(myBookings);
  } catch (err) {
    container.innerHTML = `
      <div class="empty-state">
        <div class="empty-icon">📂</div>
        <h3>No trips booked yet</h3>
        <p>Your upcoming and completed bookings will appear here.</p>
        <a href="booking.html" class="btn btn-primary" style="margin-top:var(--space-md);">Plan Your First Journey</a>
      </div>
    `;
  }
}

function filterBookingsTab(status) {
  document.querySelectorAll('.booking-tab-btn').forEach(btn => {
    btn.classList.toggle('active', btn.getAttribute('data-status') === status);
  });

  if (status === 'ALL') {
    renderBookingsList(myBookings);
  } else {
    const filtered = myBookings.filter(b => b.bookingStatus === status);
    renderBookingsList(filtered);
  }
}

function renderBookingsList(bookings) {
  const container = document.getElementById('bookings-list-container');

  if (!bookings || bookings.length === 0) {
    container.innerHTML = `
      <div class="empty-state">
        <div class="empty-icon">🚗</div>
        <h3>No matching trips found</h3>
        <p>Try switching to another status tab or book a new journey.</p>
        <a href="booking.html" class="btn btn-secondary btn-sm" style="margin-top:var(--space-md);">Book a Trip</a>
      </div>
    `;
    return;
  }

  const fallbackImg = 'https://images.unsplash.com/photo-1549924231-f129b911e442?w=400&q=80';
  container.innerHTML = bookings.map(b => {
    const isCancelable = b.bookingStatus === 'PENDING' || b.bookingStatus === 'CONFIRMED';
    return `
      <div class="booking-card">
        <div class="booking-card-main">
          <img src="${b.carImageUrl || fallbackImg}" alt="Car" class="booking-thumb" onerror="this.src='${fallbackImg}'" />
          <div class="booking-details">
            <div class="booking-number-row">
              <span class="booking-number">${b.bookingNumber || 'BK-ID-' + b.id}</span>
              ${statusBadge(b.bookingStatus)}
            </div>
            <div class="booking-route">${b.pickupAddress} ➔ ${b.dropoffAddress}</div>
            <div class="booking-meta-row">
              <span>📅 ${formatDate(b.pickupDate)} at ${formatTime(b.pickupTime)}</span>
              <span>🚗 ${b.carBrand || ''} ${b.carName || 'Vehicle'}</span>
              <span>📏 ${b.totalDistanceKm || b.clientDistanceKm || '—'} km</span>
              <span>👥 ${b.passengerCount || 1} passenger(s)</span>
            </div>
          </div>
        </div>
        <div class="booking-card-side">
          <div class="booking-amount">${formatCurrency(b.totalAmount)}</div>
          <div class="booking-actions">
            <button class="btn btn-secondary btn-sm" onclick="showBookingDetail(${b.id})">Details</button>
            ${isCancelable ? `<button class="btn btn-danger btn-sm" onclick="cancelBookingPrompt(${b.id})">Cancel</button>` : ''}
          </div>
        </div>
      </div>
    `;
  }).join('');
}

// Show detail modal
async function showBookingDetail(bookingId) {
  const b = myBookings.find(x => x.id === bookingId);
  if (!b) return;

  const modal = document.getElementById('booking-detail-modal');
  const title = document.getElementById('modal-booking-title');
  const body = document.getElementById('modal-booking-body');

  title.textContent = `Trip Details: ${b.bookingNumber || '#' + b.id}`;
  
  // Try loading payments if available
  let paymentInfo = '';
  try {
    const payment = await api.get(`/api/payments/booking/${b.id}`, true);
    if (payment) {
      paymentInfo = `
        <div style="margin-top:16px;padding:14px;background:#f0fdf4;border-radius:10px;border:1px solid #bbf7d0;">
          <h4 style="color:#166534;margin-bottom:8px;">💳 Payment Receipt</h4>
          <div style="font-size:0.9rem;"><strong>Status:</strong> ${payment.paymentStatus}</div>
          <div style="font-size:0.9rem;"><strong>Method:</strong> ${payment.paymentMethod || 'Online / Cash'}</div>
          <div style="font-size:0.9rem;"><strong>Transaction ID:</strong> ${payment.transactionId || '—'}</div>
        </div>
      `;
    }
  } catch (e) {
    // Payment not recorded yet
  }

  body.innerHTML = `
    <div style="display:flex;flex-direction:column;gap:10px;">
      <div><strong>Status:</strong> ${statusBadge(b.bookingStatus)}</div>
      <div><strong>Pickup Origin:</strong> ${b.pickupAddress}</div>
      <div><strong>Drop Destination:</strong> ${b.dropoffAddress}</div>
      <div><strong>Travel Schedule:</strong> ${formatDate(b.pickupDate)} at ${formatTime(b.pickupTime)}</div>
      <div><strong>Trip Mode:</strong> ${b.tripType}</div>
      <div><strong>Vehicle Allocated:</strong> ${b.carBrand || ''} ${b.carName || ''}</div>
      <div><strong>Total Distance:</strong> ${b.totalDistanceKm || b.clientDistanceKm} km</div>
      <div><strong>Passengers:</strong> ${b.passengerCount}</div>
      <div><strong>Special Requests:</strong> ${b.specialRequests || 'None provided'}</div>
      <hr style="margin:12px 0;border:0;border-top:1px solid var(--color-light-gray);"/>
      <div style="font-size:1.35rem;font-weight:800;color:var(--color-accent);">Total Amount: ${formatCurrency(b.totalAmount)}</div>
      ${paymentInfo}
    </div>
  `;

  modal.classList.add('open');
}

function closeModal() {
  document.getElementById('booking-detail-modal')?.classList.remove('open');
}

// Cancel booking
async function cancelBookingPrompt(bookingId) {
  if (!confirm('Are you sure you want to cancel this booking?')) {
    return;
  }

  try {
    await api.put(`/api/bookings/${bookingId}/cancel`, {}, true);
    showToast('Booking has been cancelled.', 'info');
    loadCustomerBookings();
  } catch (err) {
    showToast(`Cancellation failed: ${err.message}`, 'error');
  }
}

window.filterBookingsTab = filterBookingsTab;
window.showBookingDetail = showBookingDetail;
window.closeModal = closeModal;
window.cancelBookingPrompt = cancelBookingPrompt;

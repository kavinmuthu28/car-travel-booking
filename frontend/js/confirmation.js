/**
 * confirmation.js — Reads and displays last created booking details
 */

document.addEventListener('DOMContentLoaded', () => {
  const lastBookingRaw = sessionStorage.getItem('km_last_booking');
  if (!lastBookingRaw) {
    // If no booking in session, redirect to home
    window.location.href = 'index.html';
    return;
  }

  try {
    const booking = JSON.parse(lastBookingRaw);
    renderConfirmation(booking);
  } catch (e) {
    console.error('Error parsing booking session data', e);
  }
});

function renderConfirmation(b) {
  document.getElementById('conf-booking-number').textContent = `BOOKING # ${b.bookingNumber || b.id || 'CONFIRMED'}`;
  document.getElementById('conf-customer').textContent = b.customerName || auth.getUser()?.name || 'Customer';
  document.getElementById('conf-from').textContent = b.pickupAddress || '—';
  document.getElementById('conf-to').textContent = b.dropoffAddress || '—';
  document.getElementById('conf-car').textContent = `${b.carBrand || ''} ${b.carName || ''}`.trim() || 'Booked Vehicle';

  const dateStr = b.pickupDate ? formatDate(b.pickupDate) : '—';
  const timeStr = b.pickupTime ? formatTime(b.pickupTime) : '—';
  document.getElementById('conf-datetime').textContent = `${dateStr} at ${timeStr}`;

  document.getElementById('conf-triptype').textContent = b.tripType || 'ONE_WAY';
  document.getElementById('conf-distance').textContent = `${b.totalDistanceKm || b.clientDistanceKm || '—'} km`;
  document.getElementById('conf-status').innerHTML = statusBadge(b.bookingStatus || 'PENDING');
  document.getElementById('conf-amount').textContent = formatCurrency(b.totalAmount || 0);
}

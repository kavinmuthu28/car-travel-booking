/**
 * cars.js — Fleet & Cars catalog logic for KAVIN TRAVELS
 */

let allCars = [];

document.addEventListener('DOMContentLoaded', () => {
  loadCars();
});

async function loadCars() {
  const grid = document.getElementById('cars-grid');
  const params = new URLSearchParams(window.location.search);
  const date = params.get('date');
  const passengers = params.get('passengers');

  try {
    let cars;
    if (date && passengers) {
      try {
        cars = await api.get(`/api/cars/available?date=${date}&passengers=${passengers}`);
      } catch {
        cars = await api.get('/api/cars');
      }
    } else {
      cars = await api.get('/api/cars');
    }

    allCars = cars || [];
    applyFilters();
  } catch (err) {
    grid.innerHTML = '<div class="empty-state" style="grid-column:1/-1;"><div class="empty-icon">⚠️</div><p>Unable to load cars. Please verify that the backend service is active.</p></div>';
    document.getElementById('result-count').innerHTML = '<span class="count-badge">0 cars</span>';
  }
}

function applyFilters() {
  const fuel = document.getElementById('filter-fuel').value.toLowerCase();
  const transmission = document.getElementById('filter-transmission').value.toLowerCase();
  const seats = document.getElementById('filter-seats').value;
  const available = document.getElementById('filter-available').value;

  let filtered = [...allCars];

  if (fuel) {
    filtered = filtered.filter(c => (c.fuelType || '').toLowerCase() === fuel);
  }
  if (transmission) {
    filtered = filtered.filter(c => (c.transmission || '').toLowerCase() === transmission);
  }
  if (seats === '2-4') {
    filtered = filtered.filter(c => c.seatingCapacity >= 2 && c.seatingCapacity <= 4);
  } else if (seats === '5-7') {
    filtered = filtered.filter(c => c.seatingCapacity >= 5 && c.seatingCapacity <= 7);
  } else if (seats === '8+') {
    filtered = filtered.filter(c => c.seatingCapacity >= 8);
  }
  if (available === 'yes') {
    filtered = filtered.filter(c => (c.isAvailable ?? c.available ?? true));
  }

  renderCars(filtered);
}

function renderCars(cars) {
  const grid = document.getElementById('cars-grid');
  const countEl = document.getElementById('result-count');
  countEl.innerHTML = `<span>Results</span><span class="count-badge">${cars.length} car${cars.length !== 1 ? 's' : ''}</span>`;

  if (cars.length === 0) {
    grid.innerHTML = '<div class="empty-state" style="grid-column:1/-1;"><div class="empty-icon">🔍</div><h3>No matches found</h3><p>Try resetting or changing your filters.</p></div>';
    return;
  }

  const currentParams = new URLSearchParams(window.location.search);
  const fallbackImg = 'https://images.unsplash.com/photo-1549924231-f129b911e442?w=400&q=80';
  grid.innerHTML = cars.map(car => {
    const isAvail = car.isAvailable ?? car.available ?? true;
    const bookingParams = new URLSearchParams(currentParams);
    bookingParams.set('carId', car.id);

    return `
      <div class="car-card">
        <div class="car-card-img">
          <img src="${car.imageUrl || fallbackImg}" alt="${car.name}" loading="lazy" onerror="this.src='${fallbackImg}'" />
          <span class="car-card-badge badge ${isAvail ? 'badge-success' : 'badge-danger'}">
            ${isAvail ? 'Available' : 'Unavailable'}
          </span>
        </div>
        <div class="car-card-body">
          <h4>${car.name || 'Car'}</h4>
          <div class="car-card-brand">${car.brand || ''} · ${car.modelYear || ''}</div>
          <div class="car-specs">
            <span>👥 ${car.seatingCapacity} seats</span>
            <span>⛽ ${car.fuelType || 'Petrol'}</span>
            <span>⚙️ ${car.transmission || 'Manual'}</span>
          </div>
          <div class="car-card-footer">
            <div class="car-price">₹${car.pricePerKm}<small>/km</small></div>
            <a href="booking.html?${bookingParams.toString()}" class="btn btn-primary btn-sm">Book This Car</a>
          </div>
        </div>
      </div>
    `;
  }).join('');
}

function clearFilters() {
  document.getElementById('filter-fuel').value = '';
  document.getElementById('filter-transmission').value = '';
  document.getElementById('filter-seats').value = '';
  document.getElementById('filter-available').value = '';
  applyFilters();
}

window.applyFilters = applyFilters;
window.clearFilters = clearFilters;

/**
 * home.js — Homepage logic for KAVIN TRAVELS
 */

document.addEventListener('DOMContentLoaded', () => {
  const dateInput = document.getElementById('search-date');
  if (dateInput) {
    const today = new Date().toISOString().split('T')[0];
    dateInput.min = today;
    dateInput.value = today;
  }

  // Initialize Searchable Location Dropdowns & Swap Button
  if (window.LocationSelector) {
    new window.LocationSelector('search-from', {
      placeholder: 'Search pickup city, district, airport...'
    });
    new window.LocationSelector('search-to', {
      placeholder: 'Search destination city, hill station...'
    });
  }

  if (window.setupLocationSwap) {
    window.setupLocationSwap('search-from', 'search-to', 'home-swap-btn');
  }

  loadHomeDestinations();
  loadFeaturedFleet();

  // Render Lucide icons if available
  if (window.lucide && typeof window.lucide.createIcons === 'function') {
    window.lucide.createIcons();
  }
});

async function loadHomeDestinations() {
  const container = document.getElementById('destinations-grid');
  if (!container) return;
  const fallbackImgs = [
    'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=600&q=80',
    'https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=600&q=80',
    'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600&q=80'
  ];
  try {
    const list = await api.get('/api/destinations') || [];
    const items = list.length > 0 ? list : [
      { fromCity: 'Coimbatore', toCity: 'Ooty', distanceKm: 86, estimatedDuration: '2h 45m', imageUrl: fallbackImgs[0] },
      { fromCity: 'Coimbatore', toCity: 'Munnar', distanceKm: 160, estimatedDuration: '4h 30m', imageUrl: fallbackImgs[1] },
      { fromCity: 'Chennai', toCity: 'Pondicherry', distanceKm: 152, estimatedDuration: '3h 15m', imageUrl: fallbackImgs[2] }
    ];
    container.innerHTML = items.slice(0, 3).map((d, i) => `
      <a href="booking.html?from=${encodeURIComponent(d.fromCity)}&to=${encodeURIComponent(d.toCity)}" class="dest-card">
        <img src="${d.imageUrl || fallbackImgs[i % 3]}" alt="${d.fromCity} to ${d.toCity}" onerror="this.src='${fallbackImgs[i % 3]}'" />
        <div class="dest-card-overlay">
          <h4>${d.fromCity} → ${d.toCity}</h4>
          <div class="dest-meta">
            <span>📏 ${d.distanceKm} km</span>
            <span>⏱ ${d.estimatedDuration || 'Direct Route'}</span>
          </div>
          <span class="dest-card-btn">Book Route →</span>
        </div>
      </a>
    `).join('');
  } catch (e) {
    console.warn('Destinations load error:', e);
  }
}

async function loadFeaturedFleet() {
  const container = document.getElementById('featured-cars-grid');
  if (!container) return;
  const fallbackCar = 'https://images.unsplash.com/photo-1549924231-f129b911e442?w=400&q=80';
  try {
    const cars = await api.get('/api/cars') || [];
    if (cars.length === 0) {
      container.innerHTML = '<p style="grid-column:1/-1;text-align:center;color:var(--color-mid-gray);">No vehicles currently available.</p>';
      return;
    }
    container.innerHTML = cars.slice(0, 4).map(c => {
      const isAvail = c.isAvailable ?? c.available ?? true;
      return `
        <div class="car-card">
          <div class="car-card-img">
            <img src="${c.imageUrl || fallbackCar}" alt="${c.name}" loading="lazy" onerror="this.src='${fallbackCar}'" />
            <span class="car-card-badge badge ${isAvail ? 'badge-success' : 'badge-danger'}">
              ${isAvail ? 'Available' : 'Unavailable'}
            </span>
          </div>
          <div class="car-card-body">
            <h4>${c.name}</h4>
            <div class="car-card-brand">${c.brand || ''} · ${c.modelYear || ''}</div>
            <div class="car-specs">
              <span>👥 ${c.seatingCapacity} seats</span>
              <span>⛽ ${c.fuelType || 'Petrol'}</span>
              <span>⚙️ ${c.transmission || 'Manual'}</span>
            </div>
            <div class="car-card-footer">
              <div class="car-price">₹${c.pricePerKm}<small>/km</small></div>
              <a href="booking.html?carId=${c.id}" class="btn btn-primary btn-sm">Book Now</a>
            </div>
          </div>
        </div>
      `;
    }).join('');
  } catch (e) {
    console.warn('Cars load error:', e);
  }
}

function handleSearch(e) {
  e.preventDefault();
  const from = document.getElementById('search-from').value.trim();
  const to = document.getElementById('search-to').value.trim();
  const date = document.getElementById('search-date').value;
  const passengers = document.getElementById('search-passengers').value;

  if (window.validateJourneyLocations) {
    const validation = window.validateJourneyLocations(from, to);
    if (!validation.valid) {
      if (typeof showToast === 'function') {
        showToast(validation.message, 'error');
      } else {
        alert(validation.message);
      }
      return false;
    }
  }

  const params = new URLSearchParams();
  if (from) params.set('from', from);
  if (to) params.set('to', to);
  if (date) params.set('date', date);
  if (passengers) params.set('passengers', passengers);

  window.location.href = `booking.html?${params.toString()}`;
  return false;
}

window.handleSearch = handleSearch;

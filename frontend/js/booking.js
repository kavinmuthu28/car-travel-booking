/**
 * booking.js — Interactive Map routing, live distance computation & booking flow
 */

// State holding booking progress
const bookingState = {
  currentStep: 1,
  pickupAddress: '',
  dropoffAddress: '',
  distanceKm: 0,
  durationText: '',
  travelDate: '',
  travelTime: '09:00',
  tripType: 'ONE_WAY',
  returnDate: '',
  passengerCount: 4,
  selectedCar: null,
  estimatedTotal: 0
};

// Known coordinates for popular travel hubs (South India)
const CITY_COORDINATES = {
  'coimbatore': { lat: 11.0168, lng: 76.9558, name: 'Coimbatore' },
  'cjb': { lat: 11.0298, lng: 77.0434, name: 'Coimbatore Airport' },
  'ooty': { lat: 11.4102, lng: 76.6950, name: 'Ooty (Nilgiris)' },
  'udhagamandalam': { lat: 11.4102, lng: 76.6950, name: 'Ooty' },
  'coonoor': { lat: 11.3530, lng: 76.7959, name: 'Coonoor' },
  'kotagiri': { lat: 11.4243, lng: 76.8778, name: 'Kotagiri' },
  'munnar': { lat: 10.0889, lng: 77.0595, name: 'Munnar, Kerala' },
  'kodaikanal': { lat: 10.2381, lng: 77.4892, name: 'Kodaikanal' },
  'bangalore': { lat: 12.9716, lng: 77.5946, name: 'Bangalore' },
  'bengaluru': { lat: 12.9716, lng: 77.5946, name: 'Bengaluru' },
  'chennai': { lat: 13.0827, lng: 80.2707, name: 'Chennai' },
  'madurai': { lat: 9.9252, lng: 78.1198, name: 'Madurai' },
  'salem': { lat: 11.6643, lng: 78.1460, name: 'Salem' },
  'tiruppur': { lat: 11.1085, lng: 77.3411, name: 'Tiruppur' },
  'erode': { lat: 11.3410, lng: 77.7172, name: 'Erode' },
  'mysore': { lat: 12.2958, lng: 76.6394, name: 'Mysore' },
  'mysuru': { lat: 12.2958, lng: 76.6394, name: 'Mysuru' },
  'pondicherry': { lat: 11.9416, lng: 79.8083, name: 'Pondicherry' },
  'palakkad': { lat: 10.7867, lng: 76.6548, name: 'Palakkad' },
  'kochi': { lat: 9.9312, lng: 76.2673, name: 'Kochi' },
  'yercaud': { lat: 11.7753, lng: 78.2093, name: 'Yercaud' },
  'valparai': { lat: 10.3262, lng: 76.9554, name: 'Valparai' }
};

let leafletMap = null;
let routePolyline = null;
let originMarker = null;
let destMarker = null;

// Initialize on DOM Ready
document.addEventListener('DOMContentLoaded', () => {
  setupInitialValues();
  initInteractiveMap();
  readUrlParameters();
  populateUserIfLoggedIn();
  updateSummarySidebar();
});

function setupInitialValues() {
  const dateInput = document.getElementById('travel-date');
  const returnInput = document.getElementById('return-date');
  const today = new Date().toISOString().split('T')[0];

  if (dateInput) {
    dateInput.min = today;
    if (!dateInput.value) dateInput.value = today;
    bookingState.travelDate = dateInput.value;
  }
  if (returnInput) {
    returnInput.min = today;
  }

  // Event listeners on inputs
  dateInput?.addEventListener('change', (e) => {
    bookingState.travelDate = e.target.value;
    updateSummarySidebar();
  });
  document.getElementById('travel-time')?.addEventListener('change', (e) => {
    bookingState.travelTime = e.target.value;
    updateSummarySidebar();
  });

  const pickupEl = document.getElementById('pickup-input');
  const dropoffEl = document.getElementById('dropoff-input');

  const onLocationChange = () => {
    bookingState.pickupAddress = pickupEl.value.trim();
    bookingState.dropoffAddress = dropoffEl.value.trim();
    if (bookingState.pickupAddress && bookingState.dropoffAddress) {
      calculateAndPlotRoute(bookingState.pickupAddress, bookingState.dropoffAddress);
    }
  };

  // Initialize Searchable Location Dropdown with 38 Tamil Nadu Districts & Hubs
  if (window.LocationSelector) {
    new window.LocationSelector('pickup-input', {
      placeholder: 'Search pickup city, district, airport, or station...',
      onSelect: (loc) => {
        bookingState.pickupAddress = loc.name;
        if (loc.lat && loc.lng) {
          const latEl = document.getElementById('pickup-lat');
          const lngEl = document.getElementById('pickup-lng');
          if (latEl) latEl.value = loc.lat;
          if (lngEl) lngEl.value = loc.lng;
        }
        onLocationChange();
      }
    });

    new window.LocationSelector('dropoff-input', {
      placeholder: 'Search destination hill station, temple city...',
      onSelect: (loc) => {
        bookingState.dropoffAddress = loc.name;
        if (loc.lat && loc.lng) {
          const latEl = document.getElementById('dropoff-lat');
          const lngEl = document.getElementById('dropoff-lng');
          if (latEl) latEl.value = loc.lat;
          if (lngEl) lngEl.value = loc.lng;
        }
        onLocationChange();
      }
    });
  }

  // Attach Location Swap Button Logic
  if (window.setupLocationSwap) {
    window.setupLocationSwap('pickup-input', 'dropoff-input', 'booking-swap-btn', onLocationChange);
  }

  pickupEl?.addEventListener('change', onLocationChange);
  pickupEl?.addEventListener('blur', onLocationChange);
  dropoffEl?.addEventListener('change', onLocationChange);
  dropoffEl?.addEventListener('blur', onLocationChange);

  // Initialize Lucide icons if available
  if (window.lucide && typeof window.lucide.createIcons === 'function') {
    window.lucide.createIcons();
  }
}

// -------------------------------------------------------------
// INTERACTIVE MAP (Leaflet.js + OpenStreetMap - 100% Free & Reliable)
// -------------------------------------------------------------
function initInteractiveMap() {
  const mapContainer = document.getElementById('map');
  if (!mapContainer || !window.L) return;

  try {
    // Default center: Coimbatore / Tamil Nadu
    leafletMap = L.map('map').setView([11.0168, 76.9558], 8);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap contributors'
    }).addTo(leafletMap);
  } catch (err) {
    console.warn('Map rendering note:', err);
  }
}

// Resolve coordinates for given address string using TN_LOCATIONS or fallback
function getCoordinatesForAddress(address) {
  if (!address) return null;
  const lower = address.toLowerCase().trim();

  // 1. Search in comprehensive Tamil Nadu 38-district locations dataset
  if (window.TN_LOCATIONS && Array.isArray(window.TN_LOCATIONS)) {
    // Exact match
    const exact = window.TN_LOCATIONS.find(l => l.name.toLowerCase() === lower);
    if (exact) return { lat: exact.lat, lng: exact.lng, name: exact.name };

    // Name or keyword match
    const match = window.TN_LOCATIONS.find(l => {
      const locName = l.name.toLowerCase();
      const locDist = (l.district || '').toLowerCase();
      const locType = (l.type || '').toLowerCase();
      const matchesKeyword = l.keywords && l.keywords.some(k => lower.includes(k.toLowerCase()) || k.toLowerCase().includes(lower));
      return lower.includes(locName) || locName.includes(lower) || lower.includes(locDist) || matchesKeyword;
    });

    if (match) return { lat: match.lat, lng: match.lng, name: match.name };
  }

  // 2. Check fallback city coordinates
  for (const [key, coords] of Object.entries(CITY_COORDINATES)) {
    if (lower.includes(key) || key.includes(lower)) {
      return coords;
    }
  }

  // 3. Fallback coordinate offset based on hash if not matched
  let hash = 0;
  for (let i = 0; i < address.length; i++) hash = (hash << 5) - hash + address.charCodeAt(i);
  const latOffset = ((Math.abs(hash) % 100) / 100) * 0.8;
  const lngOffset = ((Math.abs(hash >> 2) % 100) / 100) * 0.8;

  return { lat: 11.0168 + latOffset, lng: 76.9558 + lngOffset, name: address };
}

// Calculate distance & plot route on interactive map
function calculateAndPlotRoute(originStr, destStr) {
  const originCoords = getCoordinatesForAddress(originStr);
  const destCoords = getCoordinatesForAddress(destStr);

  if (!originCoords || !destCoords) return;

  // Calculate driving distance
  const distKm = calculateDrivingDistanceKm(originStr, destStr, originCoords, destCoords);
  const hours = distKm / 48; // Average 48 km/h intercity speed
  const hrs = Math.floor(hours);
  const mins = Math.round((hours - hrs) * 60);
  const durationText = hrs > 0 ? `${hrs} hr${hrs > 1 ? 's' : ''} ${mins} mins` : `${mins} mins`;

  bookingState.distanceKm = distKm;
  bookingState.durationText = durationText;

  // Show route info card
  const routeInfo = document.getElementById('route-info');
  if (routeInfo) {
    routeInfo.classList.add('show');
    document.getElementById('disp-distance').textContent = `${distKm} km`;
    document.getElementById('disp-duration').textContent = durationText;
  }

  // Plot on map
  if (leafletMap && window.L) {
    if (originMarker) leafletMap.removeLayer(originMarker);
    if (destMarker) leafletMap.removeLayer(destMarker);
    if (routePolyline) leafletMap.removeLayer(routePolyline);

    originMarker = L.marker([originCoords.lat, originCoords.lng])
      .addTo(leafletMap)
      .bindPopup(`<b>📍 Pickup:</b> ${originStr}`)
      .openPopup();

    destMarker = L.marker([destCoords.lat, destCoords.lng])
      .addTo(leafletMap)
      .bindPopup(`<b>🏁 Destination:</b> ${destStr}`);

    // Draw route curve between points
    const midLat = (originCoords.lat + destCoords.lat) / 2 + 0.02;
    const midLng = (originCoords.lng + destCoords.lng) / 2 + 0.02;

    routePolyline = L.polyline([
      [originCoords.lat, originCoords.lng],
      [midLat, midLng],
      [destCoords.lat, destCoords.lng]
    ], {
      color: '#0a192f',
      weight: 5,
      opacity: 0.85,
      dashArray: '8, 8'
    }).addTo(leafletMap);

    const bounds = L.latLngBounds([
      [originCoords.lat, originCoords.lng],
      [destCoords.lat, destCoords.lng]
    ]);
    leafletMap.fitBounds(bounds, { padding: [50, 50] });
  }

  updateSummarySidebar();
}

// Compute accurate road distances for popular South India city pairs
function calculateDrivingDistanceKm(fromStr, toStr, c1, c2) {
  const f = fromStr.toLowerCase();
  const t = toStr.toLowerCase();

  const isPair = (a, b) => (f.includes(a) && t.includes(b)) || (f.includes(b) && t.includes(a));

  if (isPair('coimbatore', 'ooty')) return 86;
  if (isPair('coimbatore', 'coonoor')) return 68;
  if (isPair('coimbatore', 'kotagiri')) return 71;
  if (isPair('coimbatore', 'munnar')) return 160;
  if (isPair('coimbatore', 'kodaikanal')) return 172;
  if (isPair('coimbatore', 'bangalore') || isPair('coimbatore', 'bengaluru')) return 365;
  if (isPair('coimbatore', 'chennai')) return 505;
  if (isPair('coimbatore', 'madurai')) return 215;
  if (isPair('coimbatore', 'salem')) return 165;
  if (isPair('coimbatore', 'tiruppur')) return 55;
  if (isPair('coimbatore', 'erode')) return 100;
  if (isPair('coimbatore', 'mysore')) return 195;
  if (isPair('coimbatore', 'palakkad')) return 54;
  if (isPair('coimbatore', 'kochi')) return 190;
  if (isPair('coimbatore', 'valparai')) return 105;
  if (isPair('chennai', 'pondicherry')) return 152;
  if (isPair('chennai', 'bangalore')) return 348;
  if (isPair('bangalore', 'mysore')) return 145;
  if (isPair('madurai', 'kodaikanal')) return 118;
  if (isPair('salem', 'yercaud')) return 32;

  if (isPair('ooty', 'coonoor')) return 18;
  if (isPair('sivakasi', 'madurai')) return 74;
  if (isPair('chennai', 'kanyakumari')) return 705;
  if (isPair('tirunelveli', 'rameswaram')) return 225;
  if (isPair('madurai', 'rameswaram')) return 170;
  if ((f.includes('junction') || f.includes('station')) && (t.includes('airport') || t.includes('cjb')) && f.includes('coimbatore')) return 12;

  // Haversine formula with a 1.25 realistic road winding multiplier
  const R = 6371; // Earth radius in km
  const dLat = (c2.lat - c1.lat) * (Math.PI / 180);
  const dLon = (c2.lng - c1.lng) * (Math.PI / 180);
  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(c1.lat * (Math.PI / 180)) * Math.cos(c2.lat * (Math.PI / 180)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  const straightDist = R * c;

  return Math.max(5, Math.round(straightDist * 1.25));
}

// -------------------------------------------------------------
// STEP NAVIGATION
// -------------------------------------------------------------
function goToStep(stepNumber) {
  if (stepNumber === 2) {
    const from = document.getElementById('pickup-input').value.trim();
    const to = document.getElementById('dropoff-input').value.trim();

    if (window.validateJourneyLocations) {
      const val = window.validateJourneyLocations(from, to);
      if (!val.valid) {
        showToast(val.message, 'error');
        return;
      }
    } else if (!from || !to) {
      showToast('Please enter both pickup and destination locations.', 'error');
      return;
    }

    bookingState.pickupAddress = from;
    bookingState.dropoffAddress = to;

    if (!bookingState.distanceKm) {
      calculateAndPlotRoute(from, to);
    }
  }

  if (stepNumber === 3) {
    const travelDate = document.getElementById('travel-date').value;
    if (!travelDate) {
      showToast('Please select a travel date.', 'error');
      return;
    }
    bookingState.travelDate = travelDate;
    bookingState.travelTime = document.getElementById('travel-time').value || '09:00';
    loadAvailableCarsForBooking();
  }

  if (stepNumber === 4) {
    if (!bookingState.selectedCar) {
      showToast('Please select a car for your journey.', 'error');
      return;
    }
    updateReviewDetails();
  }

  // Update Panels
  for (let i = 1; i <= 4; i++) {
    const panel = document.getElementById(`panel-${i}`);
    const node = document.getElementById(`step-node-${i}`);
    const conn = document.getElementById(`step-conn-${i}`);

    if (panel) panel.classList.toggle('active', i === stepNumber);
    if (node) {
      node.classList.toggle('active', i === stepNumber);
      node.classList.toggle('completed', i < stepNumber);
    }
    if (conn) conn.classList.toggle('active', i < stepNumber);
  }

  bookingState.currentStep = stepNumber;
  window.scrollTo({ top: 0, behavior: 'smooth' });

  // Refresh Leaflet map sizing if going to step 1
  if (stepNumber === 1 && leafletMap) {
    setTimeout(() => leafletMap.invalidateSize(), 200);
  }
}

function selectTripType(type) {
  bookingState.tripType = type;
  document.querySelectorAll('.trip-toggle').forEach(btn => {
    btn.classList.toggle('active', btn.getAttribute('data-type') === type);
  });

  const returnGroup = document.getElementById('return-date-group');
  if (returnGroup) {
    returnGroup.style.display = (type === 'ROUND_TRIP' || type === 'MULTI_DAY') ? 'block' : 'none';
  }

  updateSummarySidebar();
}

function adjustPassengers(delta) {
  let count = bookingState.passengerCount + delta;
  if (count < 1) count = 1;
  if (count > 8) count = 8;
  bookingState.passengerCount = count;
  document.getElementById('passenger-count').textContent = count;
  updateSummarySidebar();
}

// -------------------------------------------------------------
// LOAD AVAILABLE CARS
// -------------------------------------------------------------
async function loadAvailableCarsForBooking() {
  const container = document.getElementById('avail-cars-container');
  const date = bookingState.travelDate;
  const passengers = bookingState.passengerCount;

  container.innerHTML = `
    <div class="loading-overlay" style="grid-column:1/-1;">
      <div class="spinner"></div>
      <span>Finding available cars for ${passengers} passengers...</span>
    </div>
  `;

  try {
    let cars = [];
    try {
      cars = await api.get(`/api/cars/available?date=${date}&passengers=${passengers}`);
    } catch {
      cars = await api.get('/api/cars');
    }

    const suitableCars = (cars || []).filter(c => 
      (c.isAvailable || c.available) && (c.seatingCapacity >= passengers)
    );

    if (suitableCars.length === 0) {
      container.innerHTML = `
        <div class="empty-state" style="grid-column:1/-1;">
          <div class="empty-icon">🚗</div>
          <p>No cars available for ${passengers} passengers on ${formatDate(date)}.</p>
          <button class="btn btn-secondary btn-sm" onclick="goToStep(2)">Change Travel Details</button>
        </div>
      `;
      document.getElementById('btn-to-step-4').disabled = true;
      return;
    }

    const fallbackImg = 'https://images.unsplash.com/photo-1549924231-f129b911e442?w=400&q=80';
    container.innerHTML = suitableCars.map(car => {
      const isSelected = bookingState.selectedCar?.id === car.id || bookingState.preselectedCarId === car.id;
      if (isSelected && !bookingState.selectedCar) {
        bookingState.selectedCar = car;
        document.getElementById('btn-to-step-4').disabled = false;
      }
      return `
        <div class="avail-car-card ${isSelected ? 'selected' : ''}" id="car-card-${car.id}" onclick="selectCar(${car.id}, ${JSON.stringify(car).replace(/"/g, '&quot;')})">
          <div class="check-mark">✓</div>
          <img src="${car.imageUrl || fallbackImg}" alt="${car.name}" class="avail-car-img" onerror="this.src='${fallbackImg}'" />
          <div class="avail-car-info">
            <h4>${car.name}</h4>
            <div class="car-meta">${car.brand} · ${car.seatingCapacity} Seats · ${car.fuelType}</div>
            <div class="car-rate">₹${car.pricePerKm}<small style="font-size:0.75rem;font-weight:normal;color:#6c757d;"> /km</small></div>
          </div>
        </div>
      `;
    }).join('');

    updateSummarySidebar();
  } catch (err) {
    container.innerHTML = `<div class="empty-state" style="grid-column:1/-1;"><p>Unable to load available cars. ${err.message}</p></div>`;
  }
}

function selectCar(carId, carData) {
  bookingState.selectedCar = carData;
  document.querySelectorAll('.avail-car-card').forEach(card => card.classList.remove('selected'));
  const targetCard = document.getElementById(`car-card-${carId}`);
  if (targetCard) targetCard.classList.add('selected');

  document.getElementById('btn-to-step-4').disabled = false;
  updateSummarySidebar();
}

// -------------------------------------------------------------
// FARE COMPUTATION
// -------------------------------------------------------------
function calculateEstimatedFare() {
  const dist = Number(bookingState.distanceKm) || 0;
  if (!dist || !bookingState.selectedCar) {
    bookingState.estimatedTotal = 0;
    return 0;
  }
  const rate = Number(bookingState.selectedCar.pricePerKm) || 12.0;
  let multiplier = 1.0;

  if (bookingState.tripType === 'ROUND_TRIP') multiplier = 2.0;
  if (bookingState.tripType === 'MULTI_DAY') multiplier = 2.0;

  const driverAllowance = 500.0;
  const total = Math.round((dist * multiplier * rate) + driverAllowance);
  bookingState.estimatedTotal = total;
  return total;
}

function updateSummarySidebar() {
  document.getElementById('sum-from').textContent = bookingState.pickupAddress || '—';
  document.getElementById('sum-to').textContent = bookingState.dropoffAddress || '—';
  document.getElementById('sum-dist').textContent = bookingState.distanceKm ? `${bookingState.distanceKm} km` : '—';
  document.getElementById('sum-dur').textContent = bookingState.durationText || '—';

  const dateStr = bookingState.travelDate ? formatDate(bookingState.travelDate) : '';
  const timeStr = bookingState.travelTime ? formatTime(bookingState.travelTime) : '';
  document.getElementById('sum-datetime').textContent = (dateStr || timeStr) ? `${dateStr} ${timeStr}`.trim() : '—';

  const typeLabels = { 'ONE_WAY': 'One Way (1x)', 'ROUND_TRIP': 'Round Trip (2x)', 'MULTI_DAY': 'Multi-Day (2x)' };
  document.getElementById('sum-triptype').textContent = typeLabels[bookingState.tripType] || 'One Way';
  document.getElementById('sum-passengers').textContent = bookingState.passengerCount;

  if (bookingState.selectedCar) {
    document.getElementById('sum-car').textContent = `${bookingState.selectedCar.brand} ${bookingState.selectedCar.name}`;
    document.getElementById('sum-rate').textContent = `₹${bookingState.selectedCar.pricePerKm}/km`;
  } else {
    document.getElementById('sum-car').textContent = '—';
    document.getElementById('sum-rate').textContent = '—';
  }

  const total = calculateEstimatedFare();
  const totalEl = document.getElementById('sum-total');
  if (totalEl) {
    if (bookingState.selectedCar) {
      totalEl.textContent = formatCurrency(total);
      totalEl.style.fontSize = '';
    } else if (bookingState.distanceKm) {
      totalEl.innerHTML = '<span style="font-size:0.92rem;font-weight:600;color:var(--color-mid-gray);">Select Car in Step 3</span>';
    } else {
      totalEl.textContent = '—';
    }
  }
}

function updateReviewDetails() {
  const container = document.getElementById('review-trip-details');
  if (!container) return;

  const car = bookingState.selectedCar;
  const total = calculateEstimatedFare();

  container.innerHTML = `
    <h4 style="margin-bottom:12px;">🚗 Trip &amp; Vehicle Summary</h4>
    <div class="summary-row"><span class="label">From:</span><span class="value">${bookingState.pickupAddress}</span></div>
    <div class="summary-row"><span class="label">To:</span><span class="value">${bookingState.dropoffAddress}</span></div>
    <div class="summary-row"><span class="label">Distance &amp; Duration:</span><span class="value">${bookingState.distanceKm} km (~${bookingState.durationText})</span></div>
    <div class="summary-row"><span class="label">Departure:</span><span class="value">${formatDate(bookingState.travelDate)} at ${formatTime(bookingState.travelTime)}</span></div>
    <div class="summary-row"><span class="label">Trip Type:</span><span class="value">${bookingState.tripType}</span></div>
    <div class="summary-row"><span class="label">Vehicle:</span><span class="value">${car?.brand} ${car?.name} (₹${car?.pricePerKm}/km)</span></div>
    <hr class="summary-divider" />
    <div class="summary-total" style="padding:4px 0;">
      <span class="total-label">Total Amount:</span>
      <span class="total-value" style="font-size:1.4rem;">₹${total.toLocaleString('en-IN')}</span>
    </div>
  `;
}

// Pre-fill fields from query parameters (from homepage search or car card)
function readUrlParameters() {
  const params = new URLSearchParams(window.location.search);
  const from = params.get('from');
  const to = params.get('to');
  const date = params.get('date');
  const passengers = params.get('passengers');
  const carId = params.get('carId');

  if (from) {
    document.getElementById('pickup-input').value = from;
    bookingState.pickupAddress = from;
  }
  if (to) {
    document.getElementById('dropoff-input').value = to;
    bookingState.dropoffAddress = to;
  }
  if (date) {
    document.getElementById('travel-date').value = date;
    bookingState.travelDate = date;
  }
  if (passengers) {
    bookingState.passengerCount = parseInt(passengers, 10) || 4;
    document.getElementById('passenger-count').textContent = bookingState.passengerCount;
  }
  if (carId) {
    bookingState.preselectedCarId = parseInt(carId, 10);
  }

  if (from && to) {
    calculateAndPlotRoute(from, to);
  }
}

function populateUserIfLoggedIn() {
  const user = auth.getUser();
  if (user) {
    const nameInput = document.getElementById('cust-name');
    const emailInput = document.getElementById('cust-email');
    const phoneInput = document.getElementById('cust-phone');
    if (nameInput && user.name) nameInput.value = user.name;
    if (emailInput && user.email) emailInput.value = user.email;
    if (phoneInput && user.phone) phoneInput.value = user.phone;
    hide(document.getElementById('guest-auth-alert'));
  } else {
    show(document.getElementById('guest-auth-alert'), 'block');
  }
}

// -------------------------------------------------------------
// CONFIRM AND POST BOOKING
// -------------------------------------------------------------
async function confirmAndCreateBooking() {
  const btn = document.getElementById('btn-confirm-booking');
  const name = document.getElementById('cust-name').value.trim();
  const email = document.getElementById('cust-email').value.trim();
  const phone = document.getElementById('cust-phone').value.trim();
  const specialRequests = document.getElementById('special-requests').value.trim();

  if (!name || !email || !phone) {
    showToast('Please fill in your name, email, and phone number.', 'error');
    return;
  }

  if (!auth.isLoggedIn()) {
    showToast('Please log in or create an account to finalize your booking.', 'info');
    sessionStorage.setItem('km_pending_booking', JSON.stringify(bookingState));
    setTimeout(() => {
      window.location.href = 'login.html?redirect=booking.html';
    }, 1500);
    return;
  }

  btn.disabled = true;
  btn.textContent = '⏳ Creating Booking...';

  const bookingPayload = {
    carId: bookingState.selectedCar.id,
    pickupDate: bookingState.travelDate,
    pickupTime: bookingState.travelTime.length === 5 ? `${bookingState.travelTime}:00` : bookingState.travelTime,
    pickupAddress: bookingState.pickupAddress,
    dropoffAddress: bookingState.dropoffAddress,
    tripType: bookingState.tripType,
    passengerCount: bookingState.passengerCount,
    clientDistanceKm: bookingState.distanceKm || 86.0,
    specialRequests: specialRequests || null
  };

  try {
    const createdBooking = await api.post('/api/bookings', bookingPayload, true);
    sessionStorage.setItem('km_last_booking', JSON.stringify(createdBooking));
    showToast('🎉 Booking Confirmed!', 'success');

    setTimeout(() => {
      window.location.href = 'confirmation.html';
    }, 1000);
  } catch (err) {
    showToast(`Booking failed: ${err.message}`, 'error');
    btn.disabled = false;
    btn.textContent = '✅ Confirm & Book Now';
  }
}

window.goToStep = goToStep;
window.selectTripType = selectTripType;
window.adjustPassengers = adjustPassengers;
window.selectCar = selectCar;
window.confirmAndCreateBooking = confirmAndCreateBooking;

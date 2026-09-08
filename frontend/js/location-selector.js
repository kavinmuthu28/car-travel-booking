/**
 * location-selector.js — Modern Searchable Dropdown & Location Selector UX
 * KAVIN TRAVELS
 */

(function () {
  // Category icons mapping
  const CATEGORY_ICONS = {
    'Popular': 'star',
    'Airports': 'plane',
    'Railway Stations': 'train',
    'Hill Stations': 'mountain',
    'Pilgrimage Places': 'landmark',
    'Tourist Destinations': 'compass',
    'Cities & Towns': 'map-pin'
  };

  const TYPE_ICONS = {
    'plane': '✈️',
    'train': '🚆',
    'mountain': '🏔️',
    'landmark': '🛕',
    'map-pin': '📍',
    'compass': '🏖️',
    'star': '⭐'
  };

  class LocationSelector {
    constructor(inputElement, options = {}) {
      this.input = typeof inputElement === 'string' ? document.getElementById(inputElement) : inputElement;
      if (!this.input) return;

      this.options = Object.assign({
        onSelect: null,
        placeholder: 'Search city, airport, station...',
        showCategories: true,
        maxItemsPerCategory: 6
      }, options);

      this.isOpen = false;
      this.selectedIndex = -1;
      this.filteredLocations = [];
      this.init();
    }

    init() {
      // Wrap input in relative container if not already
      const parent = this.input.parentElement;
      if (getComputedStyle(parent).position === 'static') {
        parent.style.position = 'relative';
      }

      // Create dropdown element
      this.dropdown = document.createElement('div');
      this.dropdown.className = 'location-dropdown-panel';
      this.dropdown.style.display = 'none';
      parent.appendChild(this.dropdown);

      // Event listeners
      this.input.setAttribute('autocomplete', 'off');

      this.input.addEventListener('focus', () => {
        this.open();
        this.renderResults(this.input.value);
      });

      this.input.addEventListener('input', () => {
        if (!this.isOpen) this.open();
        this.renderResults(this.input.value);
      });

      this.input.addEventListener('keydown', (e) => this.handleKeyDown(e));

      // Close when clicking outside
      document.addEventListener('click', (e) => {
        if (!this.input.contains(e.target) && !this.dropdown.contains(e.target)) {
          this.close();
        }
      });
    }

    open() {
      // Close any other open location selectors first
      document.querySelectorAll('.location-dropdown-panel').forEach(p => {
        p.style.display = 'none';
      });

      this.dropdown.style.display = 'block';
      this.isOpen = true;
    }

    close() {
      this.dropdown.style.display = 'none';
      this.isOpen = false;
      this.selectedIndex = -1;
    }

    renderResults(query) {
      if (!window.TN_LOCATIONS) return;

      const q = (query || '').trim().toLowerCase();
      const results = window.searchLocations(q);
      this.filteredLocations = results;

      if (results.length === 0) {
        this.dropdown.innerHTML = `
          <div class="location-empty-state">
            <span style="font-size:1.4rem;">🔍</span>
            <p>No locations found matching "<strong>${query}</strong>"</p>
            <small style="color:var(--color-mid-gray);">Try searching city name, district, or airport code</small>
          </div>
        `;
        return;
      }

      // Group results
      const grouped = window.groupLocationsByCategory(results);
      let html = '';
      let globalIndex = 0;

      for (const [category, items] of Object.entries(grouped)) {
        if (items.length === 0) continue;

        const catIcon = CATEGORY_ICONS[category] || 'map-pin';
        const displayItems = q ? items : items.slice(0, this.options.maxItemsPerCategory);

        html += `
          <div class="location-category-group">
            <div class="location-category-header">
              <span class="category-icon">${TYPE_ICONS[catIcon] || '📍'}</span>
              <span>${category}</span>
            </div>
            <div class="location-category-items">
        `;

        displayItems.forEach(loc => {
          const iconGlyph = TYPE_ICONS[loc.icon] || '📍';
          html += `
            <div class="location-item" data-index="${globalIndex}" data-id="${loc.id}" data-name="${loc.name}">
              <div class="loc-item-icon">${iconGlyph}</div>
              <div class="loc-item-text">
                <div class="loc-item-name">${this.highlightMatch(loc.name, q)}</div>
                <div class="loc-item-sub">${loc.type} · ${loc.district}</div>
              </div>
            </div>
          `;
          globalIndex++;
        });

        html += `</div></div>`;
      }

      this.dropdown.innerHTML = html;

      // Attach click events on items
      this.dropdown.querySelectorAll('.location-item').forEach(item => {
        item.addEventListener('click', () => {
          const locName = item.getAttribute('data-name');
          const locId = item.getAttribute('data-id');
          const locObj = window.TN_LOCATIONS.find(l => l.id === locId) || { name: locName };
          this.selectLocation(locObj);
        });
      });
    }

    highlightMatch(text, query) {
      if (!query) return text;
      const index = text.toLowerCase().indexOf(query);
      if (index === -1) return text;
      return (
        text.substring(0, index) +
        '<mark class="loc-match">' +
        text.substring(index, index + query.length) +
        '</mark>' +
        text.substring(index + query.length)
      );
    }

    selectLocation(locationObj) {
      this.input.value = locationObj.name;
      this.close();

      // Trigger standard change and input events
      this.input.dispatchEvent(new Event('change', { bubbles: true }));
      this.input.dispatchEvent(new Event('input', { bubbles: true }));

      if (typeof this.options.onSelect === 'function') {
        this.options.onSelect(locationObj);
      }
    }

    handleKeyDown(e) {
      if (!this.isOpen) {
        if (e.key === 'ArrowDown' || e.key === 'Enter') {
          this.open();
          this.renderResults(this.input.value);
        }
        return;
      }

      const items = this.dropdown.querySelectorAll('.location-item');
      if (items.length === 0) return;

      if (e.key === 'ArrowDown') {
        e.preventDefault();
        this.selectedIndex = (this.selectedIndex + 1) % items.length;
        this.updateActiveItem(items);
      } else if (e.key === 'ArrowUp') {
        e.preventDefault();
        this.selectedIndex = (this.selectedIndex - 1 + items.length) % items.length;
        this.updateActiveItem(items);
      } else if (e.key === 'Enter') {
        e.preventDefault();
        if (this.selectedIndex >= 0 && this.selectedIndex < items.length) {
          items[this.selectedIndex].click();
        } else if (items.length > 0) {
          items[0].click();
        }
      } else if (e.key === 'Escape') {
        this.close();
      }
    }

    updateActiveItem(items) {
      items.forEach((item, idx) => {
        if (idx === this.selectedIndex) {
          item.classList.add('active');
          item.scrollIntoView({ block: 'nearest' });
        } else {
          item.classList.remove('active');
        }
      });
    }
  }

  // Helper function to attach From/To swap button logic
  function setupLocationSwap(fromInputId, toInputId, swapButtonId, onSwapCallback) {
    const fromInput = document.getElementById(fromInputId);
    const toInput = document.getElementById(toInputId);
    const swapBtn = document.getElementById(swapButtonId);

    if (!fromInput || !toInput || !swapBtn) return;

    swapBtn.addEventListener('click', (e) => {
      e.preventDefault();
      const temp = fromInput.value;
      fromInput.value = toInput.value;
      toInput.value = temp;

      // Animate rotation on button
      swapBtn.classList.add('swapping');
      setTimeout(() => swapBtn.classList.remove('swapping'), 400);

      // Trigger change events
      fromInput.dispatchEvent(new Event('change', { bubbles: true }));
      toInput.dispatchEvent(new Event('change', { bubbles: true }));

      if (typeof onSwapCallback === 'function') {
        onSwapCallback(fromInput.value, toInput.value);
      }
    });
  }

  // Location Validation Helper
  function validateJourneyLocations(from, to) {
    const f = (from || '').trim();
    const t = (to || '').trim();

    if (!f) {
      return { valid: false, message: 'Please select your pickup location.' };
    }
    if (!t) {
      return { valid: false, message: 'Please select your destination.' };
    }
    if (f.toLowerCase() === t.toLowerCase()) {
      return { valid: false, message: 'Pickup and destination cannot be the same place. Please choose distinct locations.' };
    }

    return { valid: true };
  }

  // Expose to window
  window.LocationSelector = LocationSelector;
  window.setupLocationSwap = setupLocationSwap;
  window.validateJourneyLocations = validateJourneyLocations;
})();

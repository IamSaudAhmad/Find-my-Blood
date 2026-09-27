/* Find My Blood theme controls
   Frontend-only: no backend/API changes. */
(() => {
  const STORAGE_KEY = 'bloodBridgeTheme';

  function getSystemTheme() {
    return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
  }

  function getStoredTheme() {
    const saved = localStorage.getItem(STORAGE_KEY);
    return saved === 'dark' || saved === 'light' ? saved : null;
  }

  function applyTheme(theme) {
    document.documentElement.dataset.theme = theme;
    const toggle = document.querySelector('[data-theme-toggle]');
    if (toggle) {
      const isDark = theme === 'dark';
      toggle.setAttribute('aria-pressed', String(isDark));
      toggle.setAttribute('aria-label', isDark ? 'Switch to light theme' : 'Switch to dark theme');
      const icon = toggle.querySelector('.theme-icon');
      const label = toggle.querySelector('.theme-label');
      if (icon) icon.textContent = isDark ? '☀' : '☾';
      if (label) label.textContent = isDark ? 'Light' : 'Dark';
    }
  }

  function init() {
    const stored = getStoredTheme();
    applyTheme(stored || getSystemTheme());

    const toggle = document.querySelector('[data-theme-toggle]');
    if (toggle) {
      toggle.addEventListener('click', () => {
        const current = document.documentElement.dataset.theme || getSystemTheme();
        const next = current === 'dark' ? 'light' : 'dark';
        localStorage.setItem(STORAGE_KEY, next);
        applyTheme(next);
      });
    }

    const media = window.matchMedia('(prefers-color-scheme: dark)');
    media.addEventListener?.('change', () => {
      // If the user has not selected a preference, follow the device automatically.
      if (!getStoredTheme()) applyTheme(getSystemTheme());
    });
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }
})();

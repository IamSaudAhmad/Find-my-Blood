// ===================================================================
// Life Line — interactivity: mobile nav, form validation, toast, API
// ===================================================================

// Points directly at the Spring Boot backend. This works whether you open
// the frontend via Live Server (port 5500) or via Spring Boot itself
// (port 8080) — either way, requests go straight to the real API.
const API_BASE_URL = 'http://localhost:8080';

document.addEventListener('DOMContentLoaded', () => {

  /* ---------------- Mobile nav toggle ---------------- */
  const navToggle = document.getElementById('navToggle');
  const mobileNav = document.getElementById('mobileNav');

  navToggle.addEventListener('click', () => {
    const isOpen = mobileNav.classList.toggle('open');
    navToggle.setAttribute('aria-expanded', String(isOpen));
  });

  mobileNav.querySelectorAll('a').forEach(link => {
    link.addEventListener('click', () => {
      mobileNav.classList.remove('open');
      navToggle.setAttribute('aria-expanded', 'false');
    });
  });

  /* ---------------- Donor registration form ---------------- */
  const form = document.getElementById('donorForm');
  const submitBtn = form.querySelector('.register-submit');

  const rules = {
    fullName: {
      validate: v => v.trim().length >= 2,
      message: 'Enter your full name.'
    },
    age: {
      validate: v => Number(v) >= 18 && Number(v) <= 65,
      message: 'Donors must be between 18 and 65 years old.'
    },
    gender: {
      validate: v => v !== '',
      message: 'Select your gender.'
    },
    bloodGroup: {
      validate: v => v !== '',
      message: 'Select your blood group.'
    },
    mobile: {
      validate: v => /^[0-9]{10}$/.test(v.trim()),
      message: 'Enter a valid 10-digit mobile number.'
    },
    address: {
      validate: v => v.trim().length >= 3,
      message: 'Enter your address.'
    },
    city: {
      validate: v => v.trim().length >= 2,
      message: 'Enter your city.'
    }
  };

  function showError(fieldName, message){
    const input = form.elements[fieldName];
    const errorEl = document.getElementById('err-' + fieldName);
    input.closest('.field').classList.add('invalid');
    if (errorEl) errorEl.textContent = message;
  }

  function clearError(fieldName){
    const input = form.elements[fieldName];
    const errorEl = document.getElementById('err-' + fieldName);
    input.closest('.field').classList.remove('invalid');
    if (errorEl) errorEl.textContent = '';
  }

  function validateField(fieldName){
    const input = form.elements[fieldName];
    const rule = rules[fieldName];
    if (!rule) return true;
    const valid = rule.validate(input.value);
    if (valid){
      clearError(fieldName);
    } else {
      showError(fieldName, rule.message);
    }
    return valid;
  }

  // Live validation on blur
  Object.keys(rules).forEach(fieldName => {
    const input = form.elements[fieldName];
    input.addEventListener('blur', () => validateField(fieldName));
    input.addEventListener('input', () => {
      if (input.closest('.field').classList.contains('invalid')) {
        validateField(fieldName);
      }
    });
  });

  /* ---------------- Backend call ---------------- */
  // Field names here match Donor.java's setters exactly (name, age,
  // gender, bloodGroup, contactNumber, city, address, available).
  async function registerDonor(payload){
    let response;
    try {
      response = await fetch(`${API_BASE_URL}/api/donors`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
    } catch (networkErr) {
      // fetch() itself threw — the browser couldn't even reach the server.
      // Almost always means: backend isn't running, wrong origin/port, or blocked by CORS.
      console.error('Network-level failure reaching the backend:', networkErr);
      throw new Error('network');
    }

    if (!response.ok) {
      const body = await response.text().catch(() => '');
      console.error(`Backend responded with HTTP ${response.status}:`, body);
      throw new Error('http-' + response.status);
    }

    return response.json();
  }

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    let allValid = true;
    Object.keys(rules).forEach(fieldName => {
      const fieldValid = validateField(fieldName);
      if (!fieldValid) allValid = false;
    });

    if (!allValid){
      const firstInvalid = form.querySelector('.field.invalid input, .field.invalid select');
      if (firstInvalid) firstInvalid.focus();
      return;
    }

    const payload = {
      name: form.elements['fullName'].value.trim(),
      age: Number(form.elements['age'].value),
      gender: form.elements['gender'].value,
      bloodGroup: form.elements['bloodGroup'].value,
      contactNumber: form.elements['mobile'].value.trim(),
      city: form.elements['city'].value.trim(),
      address: form.elements['address'].value.trim(),
      available: true,
      donorEmail: sessionStorage.getItem('bloodbridge_user') || null
    };

    submitBtn.disabled = true;
    submitBtn.textContent = 'Registering…';

    try {
      await registerDonor(payload);
      showToast(`Thanks, ${payload.name.split(' ')[0]} — you're registered as a donor.`);
      form.reset();
    } catch (err) {
      if (err.message === 'network') {
        showToast("Couldn't reach the server — is the backend running on port 8080?");
      } else {
        showToast('Registration failed — please check the console for details.');
      }
    } finally {
      submitBtn.disabled = false;
      submitBtn.textContent = 'Register as donor';
    }
  });

  /* ---------------- Toast ---------------- */
  const toast = document.getElementById('toast');
  let toastTimer = null;

  function showToast(message){
    toast.textContent = message;
    toast.classList.add('show');
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => {
      toast.classList.remove('show');
    }, 3800);
  }

});


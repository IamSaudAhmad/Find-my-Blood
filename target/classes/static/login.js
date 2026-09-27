// ===================================================================
// Life Line — sign in / sign up
// ===================================================================

const API_BASE_URL = 'http://localhost:8080';

document.addEventListener('DOMContentLoaded', () => {

  /* ---------------- Tab toggle ---------------- */
  const tabLogin = document.getElementById('tabLogin');
  const tabSignup = document.getElementById('tabSignup');
  const panelLogin = document.getElementById('panelLogin');
  const panelSignup = document.getElementById('panelSignup');

  function showLogin(){
    tabLogin.classList.add('active');
    tabSignup.classList.remove('active');
    tabLogin.setAttribute('aria-selected', 'true');
    tabSignup.setAttribute('aria-selected', 'false');
    panelLogin.hidden = false;
    panelSignup.hidden = true;
  }

  function showSignup(){
    tabSignup.classList.add('active');
    tabLogin.classList.remove('active');
    tabSignup.setAttribute('aria-selected', 'true');
    tabLogin.setAttribute('aria-selected', 'false');
    panelSignup.hidden = false;
    panelLogin.hidden = true;
  }

  tabLogin.addEventListener('click', showLogin);
  tabSignup.addEventListener('click', showSignup);

  /* ---------------- Toast ---------------- */
  const toast = document.getElementById('toast');
  let toastTimer = null;

  function showToast(message){
    toast.textContent = message;
    toast.classList.add('show');
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => toast.classList.remove('show'), 3800);
  }

  /* ---------------- Shared fetch helper ---------------- */
  async function postJson(path, body){
    let response;
    try {
      response = await fetch(`${API_BASE_URL}${path}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
      });
    } catch (networkErr) {
      console.error('Network-level failure reaching the backend:', networkErr);
      throw new Error('network');
    }

    const data = await response.json().catch(() => ({}));

    if (!response.ok) {
      const err = new Error(data.message || 'request-failed');
      err.status = response.status;
      err.data = data;
      throw err;
    }

    return data;
  }

  /* ---------------- Log in ---------------- */
  const loginForm = document.getElementById('loginForm');
  const loginError = document.getElementById('loginError');
  const loginSubmit = loginForm.querySelector('.login-submit');

  loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    loginError.textContent = '';

    const email = loginForm.elements['email'].value.trim();
    const password = loginForm.elements['password'].value;

    loginSubmit.disabled = true;
    loginSubmit.textContent = 'Logging in…';

    try {
      const result = await postJson('/api/auth/login', { email, password });
      sessionStorage.setItem('bloodbridge_user', result.email);
      window.location.href = 'account.html';
    } catch (err) {
      if (err.message === 'network') {
        loginError.textContent = "Couldn't reach the server — is the backend running on port 8080?";
      } else {
        loginError.textContent = 'Invalid email or password.';
      }
    } finally {
      loginSubmit.disabled = false;
      loginSubmit.textContent = 'Log in';
    }
  });

  const termsLink = document.getElementById('termsLink');
  if (termsLink) termsLink.addEventListener('click', (e) => { e.preventDefault(); document.getElementById('terms').scrollIntoView({ behavior: 'smooth' }); });

  /* ---------------- Sign up ---------------- */
  const signupForm = document.getElementById('signupForm');
  const signupError = document.getElementById('signupError');
  const signupSubmit = signupForm.querySelector('.login-submit');

  signupForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    signupError.textContent = '';

    const name = signupForm.elements['name'].value.trim();
    const email = signupForm.elements['email'].value.trim();
    const phone = signupForm.elements['phone'].value.trim();
    const password = signupForm.elements['password'].value;
    const termsAccepted = signupForm.elements['termsAccepted'].checked;

    if (!termsAccepted){
      signupError.textContent = 'Please accept the Terms & Conditions before creating your account.';
      return;
    }
    if (password.length < 6){
      signupError.textContent = 'Password must be at least 6 characters.';
      return;
    }
    if (!/^[0-9]{10}$/.test(phone)){
      signupError.textContent = 'Enter a valid 10-digit phone number.';
      return;
    }

    signupSubmit.disabled = true;
    signupSubmit.textContent = 'Creating account…';

    try {
      await postJson('/api/auth/signup', { name, email, phone, password, termsAccepted });
      showToast(`Welcome, ${name.split(' ')[0]} — your account is ready. Please log in.`);
      signupForm.reset();
      showLogin();
      loginForm.elements['email'].value = email;
      loginForm.elements['password'].focus();
    } catch (err) {
      if (err.message === 'network') {
        signupError.textContent = "Couldn't reach the server — is the backend running on port 8080?";
      } else if (err.status === 409) {
        signupError.textContent = 'An account with this email already exists.';
      } else {
        signupError.textContent = 'Something went wrong — please check your details and try again.';
      }
    } finally {
      signupSubmit.disabled = false;
      signupSubmit.textContent = 'Sign up';
    }
  });

});

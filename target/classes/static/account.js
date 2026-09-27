const API_BASE_URL = 'http://localhost:8080';

document.addEventListener('DOMContentLoaded', async () => {
  const email = sessionStorage.getItem('bloodbridge_user');
  if (!email) { window.location.href = 'login.html'; return; }

  const photo = document.getElementById('profilePhoto');
  const initials = document.getElementById('profileInitials');
  const savedPhoto = localStorage.getItem(`bloodbridge_photo_${email}`);
  if (savedPhoto) { photo.src = savedPhoto; photo.classList.add('has-photo'); initials.style.display = 'none'; }

  try {
    const response = await fetch(`${API_BASE_URL}/api/auth/me?email=${encodeURIComponent(email)}`);
    if (!response.ok) throw new Error('not-found');
    const user = await response.json();
    document.getElementById('accountName').textContent = user.name;
    document.getElementById('profileName').textContent = user.name;
    document.getElementById('profileEmail').textContent = user.email;
    document.getElementById('profilePhone').textContent = user.phone;
    initials.textContent = user.name.split(/\s+/).map(x => x[0]).slice(0,2).join('').toUpperCase();
    renderHistory(user.donations || []);
    updateNextDate(user.nextDonationDate, user.donations || []);
  } catch (e) {
    sessionStorage.removeItem('bloodbridge_user');
    window.location.href = 'login.html';
  }

  document.getElementById('photoInput').addEventListener('change', e => {
    const file = e.target.files?.[0];
    if (!file) return;
    if (!file.type.startsWith('image/')) return;
    const reader = new FileReader();
    reader.onload = () => { localStorage.setItem(`bloodbridge_photo_${email}`, reader.result); photo.src = reader.result; photo.classList.add('has-photo'); initials.style.display = 'none'; };
    reader.readAsDataURL(file);
  });

  const recordBtn = document.getElementById('recordDonationBtn');
  const panel = document.getElementById('recordDonationPanel');
  recordBtn.addEventListener('click', () => { panel.hidden = !panel.hidden; });
  document.getElementById('donationDate').max = new Date().toISOString().split('T')[0];
  document.getElementById('donationForm').addEventListener('submit', async e => {
    e.preventDefault();
    const message = document.getElementById('donationMessage'); message.textContent = '';
    const payload = { donorEmail: email, donationDate: document.getElementById('donationDate').value, bloodGroup: document.getElementById('donationBloodGroup').value, city: document.getElementById('donationCity').value.trim() };
    try {
      const r = await fetch(`${API_BASE_URL}/api/donations`, { method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(payload) });
      if (!r.ok) throw new Error();
      panel.hidden = true; e.target.reset();
      const refreshed = await fetch(`${API_BASE_URL}/api/auth/me?email=${encodeURIComponent(email)}`);
      const user = await refreshed.json(); renderHistory(user.donations || []); updateNextDate(user.nextDonationDate, user.donations || []);
      showToast('Donation added to your history.');
    } catch { message.textContent = 'Could not save the donation. Please check the details and try again.'; }
  });

  document.getElementById('logoutBtn').addEventListener('click', () => { sessionStorage.removeItem('bloodbridge_user'); window.location.href = 'index.html'; });

  function renderHistory(donations) {
    const container = document.getElementById('donationHistory');
    if (!donations.length) { container.innerHTML = '<p class="empty-state">No donation history recorded yet.</p>'; return; }
    container.innerHTML = donations.map(d => `<div class="history-row"><div><strong>${escapeHtml(formatDate(d.donationDate))}</strong><span>${escapeHtml(d.bloodGroup || 'Blood donation')} · ${escapeHtml(d.city || '—')}</span></div><span class="history-badge">Donated</span></div>`).join('');
  }
  function updateNextDate(nextDate, donations) {
    const title = document.getElementById('nextDonationDate'); const text = document.getElementById('nextDonationText');
    if (!nextDate) { title.textContent = 'Available now'; text.textContent = 'No recent donation is recorded. Add your donation history to calculate your next eligible date.'; return; }
    title.textContent = formatDate(nextDate); text.textContent = `Based on a 90-day interval after your latest recorded donation (${formatDate(donations[0]?.donationDate)}).`;
  }
  function formatDate(value) { if (!value) return '—'; return new Date(`${value}T00:00:00`).toLocaleDateString('en-IN',{day:'numeric',month:'short',year:'numeric'}); }
  function escapeHtml(value) { return String(value ?? '').replace(/[&<>'"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;',"'":'&#39;','"':'&quot;'}[c])); }
  function showToast(message){ const toast=document.getElementById('toast'); toast.textContent=message; toast.classList.add('show'); setTimeout(()=>toast.classList.remove('show'),3200); }
});

const API_BASE_URL = 'http://localhost:8080';

document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('requestForm');
  const results = document.getElementById('donorResults');
  const count = document.getElementById('matchCount');
  const message = document.getElementById('requestMessage');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    message.textContent = '';
    const phone = document.getElementById('requestPhone').value.trim();
    if (!/^\d{10}$/.test(phone)) { message.textContent = 'Enter a valid 10-digit contact number.'; return; }

    const payload = {
      requesterName: document.getElementById('requesterName').value.trim(),
      bloodGroupNeeded: document.getElementById('neededBlood').value,
      city: document.getElementById('requestCity').value.trim(),
      contactNumber: phone,
      status: 'PENDING'
    };
    const button = document.getElementById('requestSubmit');
    button.disabled = true; button.textContent = 'Searching…';
    try {
      const create = await fetch(`${API_BASE_URL}/api/requests`, { method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(payload) });
      if (!create.ok) throw new Error('request');
      const request = await create.json();
      const matchResponse = await fetch(`${API_BASE_URL}/api/requests/${request.id}/matches`);
      if (!matchResponse.ok) throw new Error('matches');
      const donors = await matchResponse.json();
      renderDonors(donors);
      message.textContent = donors.length ? 'Your request was created and compatible donors were found.' : 'Request created, but no compatible donor is currently available in this city.';
    } catch (err) {
      message.textContent = 'Could not connect to Find My Blood. Make sure the Spring Boot backend is running on port 8080.';
    } finally { button.disabled = false; button.textContent = 'Find compatible donors'; }
  });

  function renderDonors(donors) {
    count.textContent = `${donors.length} match${donors.length === 1 ? '' : 'es'}`;
    if (!donors.length) { results.innerHTML = '<p class="empty-state">No compatible available donors found right now.</p>'; return; }
    results.innerHTML = donors.map(d => `
      <article class="donor-card"><div class="donor-badge">${escapeHtml(d.bloodGroup)}</div><div class="donor-info"><h3>${escapeHtml(d.name)}</h3><p>${escapeHtml(d.city)}${d.address ? ' · ' + escapeHtml(d.address) : ''}</p><p>${d.age ? d.age + ' years' : ''}${d.gender ? ' · ' + escapeHtml(d.gender) : ''}</p></div><a class="btn btn-primary btn-small" href="tel:${escapeHtml(d.contactNumber)}">Contact</a></article>`).join('');
  }
  function escapeHtml(value) { return String(value ?? '').replace(/[&<>'"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;',"'":'&#39;','"':'&quot;'}[c])); }
});

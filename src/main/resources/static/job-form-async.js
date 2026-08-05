// ============================================================
// Job Board - Async CRUD for Job Form (Create + Edit)
// B: loadDepartments + loadSkills + submitCreate
// C: loadJobForEdit + submitUpdate
// ============================================================

let mode = 'jquery'; // 'jquery' | 'fetch'
let editingId = null; // null = create mode, != null = edit mode

document.addEventListener('DOMContentLoaded', () => {
    // C: detect edit mode từ URL
    detectEditMode();

    loadDepartments();
    loadSkills();

    // C: nếu edit mode → load data (sau khi dropdown sẵn sàng)
    if (editingId != null) {
        loadJobForEdit(editingId).catch(handleApiError);
    }

    document.getElementById('job-form').addEventListener('submit', onSubmit);

    const modeToggle = document.getElementById('modeToggle');
    if (modeToggle) {
        modeToggle.textContent = 'jQuery';
        modeToggle.addEventListener('click', () => {
            mode = (mode === 'jquery') ? 'fetch' : 'jquery';
            modeToggle.textContent = mode === 'jquery' ? 'jQuery' : 'fetch';
            toast(`Switched to ${mode}`, 'info');
        });
    }
});

function detectEditMode() {
    // Pattern 1: /jobs/{id}
    const pathMatch = window.location.pathname.match(/\/jobs\/(\d+)/);
    if (pathMatch) {
        editingId = parseInt(pathMatch[1], 10);
    } else {
        // Pattern 2: ?id=
        const queryId = new URLSearchParams(window.location.search).get('id');
        if (queryId) editingId = parseInt(queryId, 10);
    }

    if (editingId != null) {
        const formTitle = document.getElementById('formTitle');
        if (formTitle) formTitle.textContent = `Edit Job #${editingId}`;

        const saveBtn = document.getElementById('saveBtn');
        if (saveBtn) saveBtn.textContent = 'Update';
    }
}

// ============================================================
// B — Dropdowns (Departments + Skills)
// ============================================================

// TODO B: GET /api/departments -> fill <select id="departmentId">
//  - jQuery: $.getJSON
//  - fetch:  await fetch + json
async function loadDepartments() {
    try {
        const url = '/api/departments';
        const data = await fetchJson(url);

        const select = document.getElementById('departmentId');
        if (!select) return;
        data.forEach(d => {
            const opt = document.createElement('option');
            opt.value = d.id;
            opt.textContent = d.departmentName;
            select.appendChild(opt);
        });
    } catch (err) {
        handleApiError(err);
    }

}

// TODO B: GET /api/skills -> fill checkbox group (#skillIds)
//  - mỗi skill: <input type="checkbox" value="${skill.id}" name="skillIds"> ${skill.skillName}
async function loadSkills() {
    try {
        const url = '/api/skills';
        const data = await fetchJson(url);

        const wrap = document.getElementById('skillIds');
        if (!wrap) return;
        wrap.innerHTML = '';
        data.forEach(s => {
            const id = `skill_${s.id}`;
            const div = document.createElement('div');
            div.className = 'form-check';
            div.innerHTML = `
                <input class="form-check-input" type="checkbox"
                       name="skillIds" value="${s.id}" id="${id}">
                <label class="form-check-label" for="${id}">${s.skillName}</label>`;
            wrap.appendChild(div);
        });
    } catch (err) {
        handleApiError(err);
    }
}

// ============================================================
// B + C — Submit (Create or Update)
// ============================================================

async function onSubmit(e) {
    e.preventDefault();
    const dto = collectForm();
    if (editingId == null) {
        await submitCreate(dto);
    } else {
        await submitUpdate(editingId, dto);
    }
}

// TODO B: gom giá trị từ form -> object JobDto
//  { title, description, location, minSalary, maxSalary, deadline, departmentId, skillIds: [...] }
function collectForm() {
    const skillIds = Array.from(
        document.querySelectorAll('input[name="skillIds"]:checked')
    ).map(cb => parseInt(cb.value, 10));

    return {
        title: document.getElementById('title').value.trim(),
        description: document.getElementById('description').value,
        location: document.getElementById('location').value.trim(),
        minSalary: parseFloat(document.getElementById('minSalary').value),
        maxSalary: parseFloat(document.getElementById('maxSalary').value),
        deadline: document.getElementById('deadline').value || null,
        departmentId: (() => {
            const v = document.getElementById('departmentId').value;
            return v ? parseInt(v, 10) : null;
        })(),
        skillIds: skillIds
    };
}

// TODO B: POST /api/jobs (Content-Type: application/json, body JSON)
//  - jQuery: $.ajax({type:'POST', contentType:'application/json', data: JSON.stringify(dto)})
//  - fetch:  await fetch(url, {method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify(dto)})
//  - thành công -> toast "Created" + reset form
//  - lỗi -> handleApiError
async function submitCreate(dto) {
    try {
        const url = '/api/jobs';
        const body = JSON.stringify(dto);
        if (mode === 'jquery') {
            await $.ajax({
                url, type: 'POST',
                contentType: 'application/json', data: body
            });
        } else {
            const r = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body
            });
            if (!r.ok) throw await toError(r);
        }
        toast('Job created successfully', 'success');
        document.getElementById('job-form').reset();

        // Redirect về /jobs sau 1.5s
        setTimeout(() => { window.location.href = '/jobs'; }, 1500);
    } catch (err) {
        handleApiError(err);
    }
}

// ============================================================
// C — Edit (Load + Update)
// ============================================================

// TODO C: GET /api/jobs/{id} -> fill form fields + set editingId
//  - jQuery: $.getJSON
//  - fetch:  await fetch + json
async function loadJobForEdit(id) {
    try {
        const url = `/api/jobs/${id}`;
        const data = await fetchJson(url);

        document.getElementById('title').value = data.title || '';
        document.getElementById('description').value = data.description || '';
        document.getElementById('location').value = data.location || '';
        document.getElementById('minSalary').value = data.minSalary ?? '';
        document.getElementById('maxSalary').value = data.maxSalary ?? '';
        document.getElementById('deadline').value = data.deadline || '';
        document.getElementById('departmentId').value =
            data.departmentId != null ? data.departmentId : '';

        // Skill checkboxes — tick theo skillIds
        const skillIds = (data.skillIds || []).map(id => parseInt(id, 10));
        document.querySelectorAll('input[name="skillIds"]').forEach(cb => {
            cb.checked = skillIds.includes(parseInt(cb.value, 10));
        });
    } catch (err) {
        handleApiError(err);
    }
}

// TODO C: PUT /api/jobs/{id} (Content-Type: application/json, body JSON)
//  - jQuery: $.ajax({type:'PUT', ...})
//  - fetch:  await fetch(url, {method:'PUT', ...})
//  - thành công -> toast "Updated"
//  - lỗi -> handleApiError
async function submitUpdate(id, dto) {
    try {
        const url = `/api/jobs/${id}`;
        const body = JSON.stringify(dto);
        if (mode === 'jquery') {
            await $.ajax({
                url, type: 'PUT',
                contentType: 'application/json', data: body
            });
        } else {
            const r = await fetch(url, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body
            });
            if (!r.ok) throw await toError(r);
        }
        toast('Job updated successfully', 'success');
        setTimeout(() => { window.location.href = '/jobs'; }, 1500);
    } catch (err) {
        handleApiError(err);
    }
}

// ============================================================
// Shared — fetchJson + toError + handleApiError + toast
// ============================================================

async function fetchJson(url) {
    if (mode === 'jquery') {
        return await $.getJSON(url);
    }
    const r = await fetch(url);
    if (!r.ok) throw await toError(r);
    return await r.json();
}

async function toError(response) {
    try {
        const body = await response.json();
        const err = new Error(body.error || `HTTP ${response.status}`);
        err.body = body;
        if (body.details) {
            const msgs = Object.values(body.details).join('; ');
            err.message = `${body.error}: ${msgs}`;
        }
        return err;
    } catch {
        return new Error(`HTTP ${response.status}`);
    }
}

// TODO: hiển thị lỗi từ API response, dùng toast()
function handleApiError(err) {
    const msg = (err && err.message) || 'Unexpected error';
    toast(msg, 'danger');
    console.error(err);
}

// TODO: Bootstrap toast/alert trong #toastContainer
function toast(message, type) {
    const container = document.getElementById('toastContainer');
    if (!container) return;
    const el = document.createElement('div');
    el.className = `toast align-items-center text-bg-${type} border-0 mb-2 show`;
    el.setAttribute('role', 'alert');
    el.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">${message}</div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto"
                    data-bs-dismiss="toast"></button>
        </div>`;
    container.appendChild(el);
    setTimeout(() => el.remove(), 5000);
}

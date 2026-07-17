// ============================================================
// Job Board - Async CRUD for Job Form (Create + Edit)
// B: loadDepartments + loadSkills + submitCreate
// C: loadJobForEdit + submitUpdate
// ============================================================

let editingId = null;

document.addEventListener('DOMContentLoaded', () => {
    detectEditMode();

    loadDepartments();
    loadSkills();

    if (editingId != null) {
        loadJobForEdit(editingId);
    }

    document.getElementById('job-form').addEventListener('submit', onSubmit);
});

function detectEditMode() {
    const pathMatch = window.location.pathname.match(/\/jobs\/(\d+)/);
    if (pathMatch) {
        editingId = parseInt(pathMatch[1], 10);
    } else {
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

function loadDepartments() {
    $.getJSON('/api/departments')
        .done(data => {
            const select = document.getElementById('departmentId');
            if (!select) return;
            data.forEach(d => {
                const opt = document.createElement('option');
                opt.value = d.id;
                opt.textContent = d.departmentName;
                select.appendChild(opt);
            });
        })
        .fail(handleApiError);
}

function loadSkills() {
    $.getJSON('/api/skills')
        .done(data => {
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
        })
        .fail(handleApiError);
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

async function submitCreate(dto) {
    try {
        await $.ajax({
            url: '/api/jobs',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(dto)
        });
        toast('Job created successfully', 'success');
        document.getElementById('job-form').reset();
        setTimeout(() => { window.location.href = '/jobs'; }, 1500);
    } catch (err) {
        handleApiError(err);
    }
}

// ============================================================
// C — Edit (Load + Update)
// ============================================================

function loadJobForEdit(id) {
    $.getJSON(`/api/jobs/${id}`)
        .done(data => {
            document.getElementById('title').value = data.title || '';
            document.getElementById('description').value = data.description || '';
            document.getElementById('location').value = data.location || '';
            document.getElementById('minSalary').value = data.minSalary ?? '';
            document.getElementById('maxSalary').value = data.maxSalary ?? '';
            document.getElementById('deadline').value = data.deadline || '';
            document.getElementById('departmentId').value =
                data.departmentId != null ? data.departmentId : '';

            const skillIds = (data.skillIds || []).map(id => parseInt(id, 10));
            document.querySelectorAll('input[name="skillIds"]').forEach(cb => {
                cb.checked = skillIds.includes(parseInt(cb.value, 10));
            });
        })
        .fail(handleApiError);
}

async function submitUpdate(id, dto) {
    try {
        await $.ajax({
            url: `/api/jobs/${id}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(dto)
        });
        toast('Job updated successfully', 'success');
        setTimeout(() => { window.location.href = '/jobs'; }, 1500);
    } catch (err) {
        handleApiError(err);
    }
}

// ============================================================
// Shared — error + toast
// ============================================================

function handleApiError(err) {
    let msg = 'Unexpected error';
    if (err.responseJSON && err.responseJSON.error) {
        msg = err.responseJSON.error;
    } else if (err.message) {
        msg = err.message;
    }
    toast(msg, 'danger');
    console.error(err);
}

function toast(message, type) {
    const container = document.getElementById('toastContainer');
    if (!container) return;
    const id = 'toast-' + Date.now();
    const html = `
        <div id="${id}" class="toast align-items-center text-white bg-${type} border-0" role="alert">
            <div class="d-flex">
                <div class="toast-body">${message}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>`;
    container.insertAdjacentHTML('beforeend', html);
    const el = document.getElementById(id);
    const bsToast = new bootstrap.Toast(el, { delay: 3000 });
    bsToast.show();
    el.addEventListener('hidden.bs.toast', () => el.remove());
}

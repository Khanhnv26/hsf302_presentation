// ============================================================
// Job Board - Async CRUD for Dashboard (List + Delete)
// A: loadJobs + renderJobs + searchJobs
// C: deleteJob + confirmAndDelete + delete modal
// ============================================================

let mode = 'jquery';

document.addEventListener('DOMContentLoaded', () => {
    loadJobs();

    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        let debounceTimer;
        searchInput.addEventListener('input', () => {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(() => loadJobs(searchInput.value.trim()), 300);
        });
    }

    document.getElementById('jobTable').addEventListener('click', (e) => {
        const deleteBtn = e.target.closest('.btn-delete');
        if (deleteBtn) {
            confirmAndDelete(deleteBtn.dataset.id);
        }
    });

    const modeToggle = document.getElementById('modeToggle');
    if (modeToggle) {
        modeToggle.addEventListener('click', () => {
            mode = mode === 'jquery' ? 'fetch' : 'jquery';
            modeToggle.textContent = mode === 'jquery' ? 'Fetch' : 'jQuery';
            loadJobs(document.getElementById('searchInput').value.trim());
        });
    }
});

// ============================================================
// A — List + Search
// ============================================================

async function loadJobs(keyword) {
    const url = keyword ? `/api/jobs?keyword=${encodeURIComponent(keyword)}` : '/api/jobs';

    if (mode === 'jquery') {
        $.getJSON(url)
            .done(data => renderJobs(data))
            .fail(handleApiError);
    } else {
        try {
            const r = await fetch(url);
            if (!r.ok) throw new Error(`HTTP ${r.status}`);
            const data = await r.json();
            renderJobs(data);
        } catch (err) {
            handleApiError(err);
        }
    }
}

function renderJobs(jobs) {
    const tbody = document.getElementById('jobTable');
    if (!jobs || jobs.length === 0) {
        tbody.innerHTML = '<tr><td colspan="10" class="text-center text-muted py-4">No jobs found.</td></tr>';
        return;
    }

    tbody.innerHTML = jobs.map(job => {
        const statusBadge = job.status === 'PUBLISH' ? 'bg-success'
            : job.status === 'DELETED' ? 'bg-danger' : 'bg-secondary';

        const deadline = job.deadline
            ? new Date(job.deadline + 'T00:00:00').toLocaleDateString('en-GB', {
                day: '2-digit', month: 'short', year: 'numeric'
              })
            : '-';

        const publishedAt = job.publishedAt
            ? new Date(job.publishedAt + 'T00:00:00').toLocaleDateString('en-GB', {
                day: '2-digit', month: 'short', year: 'numeric'
              })
            : '-';

        return `<tr data-id="${job.id}">
            <td>${job.id}</td>
            <td>${job.title}</td>
            <td>${job.description || '-'}</td>
            <td>${job.location}</td>
            <td>${job.minSalary != null ? job.minSalary.toLocaleString() : '-'}</td>
            <td>${job.maxSalary != null ? job.maxSalary.toLocaleString() : '-'}</td>
            <td><span class="badge ${statusBadge}">${job.status}</span></td>
            <td>${deadline}</td>
            <td>${publishedAt}</td>
            <td>
                <a class="btn btn-primary btn-sm" href="/jobs/${job.id}">Edit</a>
                <button class="btn btn-danger btn-sm btn-delete" data-id="${job.id}">Delete</button>
            </td>
        </tr>`;
    }).join('');
}

// ============================================================
// C — Delete
// ============================================================

function confirmAndDelete(id) {
    // Set id vào modal
    document.getElementById('deleteJobId').textContent = id;
    const confirmBtn = document.getElementById('confirmDeleteBtn');

    // Bind click event (replace any previous binding)
    const newBtn = confirmBtn.cloneNode(true);
    confirmBtn.parentNode.replaceChild(newBtn, confirmBtn);
    newBtn.addEventListener('click', () => {
        const modal = bootstrap.Modal.getInstance(
            document.getElementById('deleteConfirmModal'));
        modal.hide();
        deleteJob(id);
    });

    // Show modal
    const modalEl = document.getElementById('deleteConfirmModal');
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
}

async function deleteJob(id) {
    // TODO C
    try {
        const url = `/api/jobs/${id}`;
        if (mode === 'jquery') {
            await $.ajax({ url, type: 'DELETE' });
        } else {
            const r = await fetch(url, { method: 'DELETE' });
            if (!r.ok) throw await toError(r);
        }

        // Remove row khỏi DOM (fallback: gọi lại loadJobs() nếu không tìm thấy row)
        const row = document.querySelector(`tr[data-id="${id}"]`);
        if (row) {
            row.remove();
        } else {
            loadJobs(document.getElementById('searchInput')?.value.trim() || '');
        }

        // Nếu không còn row nào (chỉ đếm row có data-id) → hiện "No jobs found"
        const tbody = document.getElementById('jobTable');
        if (tbody && tbody.querySelectorAll('tr[data-id]').length === 0) {
            tbody.innerHTML = `<tr><td colspan="10"
                class="text-center text-muted py-4">No jobs found.</td></tr>`;
        }

        toast('Job deleted successfully', 'success');
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

function handleApiError(err) {
    let msg = 'Something went wrong';
    if (err.responseJSON && err.responseJSON.error) {
        msg = err.responseJSON.error;
    } else if (err.message) {
        msg = err.message;
    }
    toast(msg, 'danger');
}

function toast(message, type) {
    const container = document.getElementById('toastContainer');
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

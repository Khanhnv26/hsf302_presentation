// ============================================================
// Job Board - Async CRUD for Dashboard (List + Delete)
// A: loadJobs + renderJobs + searchJobs
// C: deleteJob + confirmAndDelete + delete modal
// ============================================================

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
});

// ============================================================
// A — List + Search
// ============================================================

async function loadJobs(keyword) {
    const url = keyword ? `/api/jobs?keyword=${encodeURIComponent(keyword)}` : '/api/jobs';

    $.getJSON(url)
        .done(data => renderJobs(data))
        .fail(handleApiError);
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
    document.getElementById('deleteJobId').textContent = id;
    const confirmBtn = document.getElementById('confirmDeleteBtn');
    const newBtn = confirmBtn.cloneNode(true);
    confirmBtn.parentNode.replaceChild(newBtn, confirmBtn);
    newBtn.addEventListener('click', async () => {
        bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal')).hide();
        await deleteJob(id);
    });
    new bootstrap.Modal(document.getElementById('deleteConfirmModal')).show();
}

async function deleteJob(id) {
    try {
        await $.ajax({ url: `/api/jobs/${id}`, type: 'DELETE' });
        const row = document.querySelector(`tr[data-id="${id}"]`);
        if (row) row.remove();
        if (document.querySelectorAll('#jobTable tr').length === 0) {
            document.getElementById('jobTable').innerHTML =
                '<tr><td colspan="10" class="text-center text-muted py-4">No jobs found.</td></tr>';
        }
        toast('Job deleted successfully', 'success');
    } catch (err) {
        handleApiError(err);
    }
}

// ============================================================
// Shared — error + toast
// ============================================================

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

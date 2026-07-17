// ============================================================
// Job Board - Async CRUD for Dashboard (List + Delete)
// A: loadJobs + renderJobs + searchJobs
// C: deleteJob + confirmAndDelete + delete modal
// ============================================================

let mode = 'jquery'; // 'jquery' | 'fetch'

document.addEventListener('DOMContentLoaded', () => {
    loadJobs();

    // C: event delegation cho nút Delete trong row
    const jobTable = document.getElementById('jobTable');
    if (jobTable) {
        jobTable.addEventListener('click', (e) => {
            const btn = e.target.closest('.btn-delete');
            if (btn) {
                const id = btn.dataset.id;
                confirmAndDelete(parseInt(id, 10));
            }
        });
    }

    // Mode toggle
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

// ============================================================
// A — List + Search
// ============================================================

// TODO A: gọi API GET /api/jobs?keyword=...
//  - jQuery: $.getJSON(url, cb).fail(handleApiError)
//  - fetch:  const r = await fetch(url); if (!r.ok) throw; const data = await r.json();
// Sau khi có data -> renderJobs(data)
async function loadJobs(keyword) {
    // TODO A
}

// TODO A: build <tr> cho mỗi job, gắn vào #jobTable (tbody)
//  - cột: id, title, description, location, minSalary, maxSalary,
//          status (badge), deadline (format dd MMM yyyy), publishedAt, actions (Edit + Delete)
//  - nút Edit -> /jobs/{id} (link thường, chuyển trang form edit)
//  - nút Delete -> data-id=${job.id}, gắn event -> confirmAndDelete(id)
function renderJobs(jobs) {
    // TODO A
}

// ============================================================
// C — Delete
// ============================================================

// TODO C: mở modal confirm (Bootstrap modal #deleteConfirmModal),
//  gắn id vào nút "Confirm" -> gọi deleteJob(id)
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

// TODO C: gọi API DELETE /api/jobs/{id}
//  - jQuery: $.ajax({type:'DELETE', url}).done().fail()
//  - fetch:  await fetch(url, {method:'DELETE'}); if (!r.ok) throw;
//  - thành công -> xoá row khỏi DOM + toast success
//  - lỗi -> toast error (dùng handleApiError)
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

        // Remove row khỏi DOM
        const row = document.querySelector(`tr[data-id="${id}"]`);
        if (row) row.remove();

        // Nếu bảng rỗng → hiện "No jobs found"
        const tbody = document.getElementById('jobTable');
        if (tbody && tbody.children.length === 0) {
            tbody.innerHTML = `<tr><td colspan="10"
                class="text-center text-muted py-4">No jobs found.</td></tr>`;
        }

        toast('Job deleted successfully', 'success');
    } catch (err) {
        handleApiError(err);
    }
}

// ============================================================
// Shared — error + toast (A hoặc C hiện thực, 2 người dùng chung)
// ============================================================

// TODO: hiển thị lỗi từ API response, dùng toast()
function handleApiError(err) {
    // TODO
}

// TODO: Bootstrap toast/alert trong #toastContainer
//  type = 'success' | 'danger' | 'warning'
function toast(message, type) {
    // TODO
}

// ============================================================
// Job Board - Async CRUD for Dashboard (List + Delete)
// A: loadJobs + renderJobs + searchJobs
// C: deleteJob + confirmAndDelete + delete modal
// ============================================================

let mode = 'jquery'; // 'jquery' | 'fetch'

document.addEventListener('DOMContentLoaded', () => {
    loadJobs();

    // TODO A: bind search input -> debounce -> loadJobs(keyword)
    // TODO C: bind delete click via event delegation on #jobTable
    // TODO A/C: bind mode toggle button (#modeToggle) -> switch mode + reload
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
    // TODO C
}

// TODO C: gọi API DELETE /api/jobs/{id}
//  - jQuery: $.ajax({type:'DELETE', url}).done().fail()
//  - fetch:  await fetch(url, {method:'DELETE'}); if (!r.ok) throw;
//  - thành công -> xoá row khỏi DOM + toast success
//  - lỗi -> toast error (dùng handleApiError)
async function deleteJob(id) {
    // TODO C
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

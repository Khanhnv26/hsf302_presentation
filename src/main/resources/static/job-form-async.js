// ============================================================
// Job Board - Async CRUD for Job Form (Create + Edit)
// B: loadDepartments + loadSkills + submitCreate
// C: loadJobForEdit + submitUpdate
// ============================================================

let mode = 'jquery'; // 'jquery' | 'fetch'
let editingId = null; // null = create mode, != null = edit mode

document.addEventListener('DOMContentLoaded', () => {
    loadDepartments();
    loadSkills();

    // TODO C: detect edit mode từ URL path /jobs/{id} hoặc query param ?id=
    //         -> set editingId, gọi loadJobForEdit(id), đổi title form + nút Save -> "Update"

    document.getElementById('job-form').addEventListener('submit', onSubmit);

    // TODO B/C: bind mode toggle button (#modeToggle) -> switch mode
});

// ============================================================
// B — Dropdowns (Departments + Skills)
// ============================================================

// TODO B: GET /api/departments -> fill <select id="departmentId">
//  - jQuery: $.getJSON
//  - fetch:  await fetch + json
async function loadDepartments() {
    // TODO B
}

// TODO B: GET /api/skills -> fill checkbox group (#skillIds)
//  - mỗi skill: <input type="checkbox" value="${skill.id}" name="skillIds"> ${skill.skillName}
async function loadSkills() {
    // TODO B
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
    // TODO B
    return {};
}

// TODO B: POST /api/jobs (Content-Type: application/json, body JSON)
//  - jQuery: $.ajax({type:'POST', contentType:'application/json', data: JSON.stringify(dto)})
//  - fetch:  await fetch(url, {method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify(dto)})
//  - thành công -> toast "Created" + reset form
//  - lỗi -> handleApiError
async function submitCreate(dto) {
    // TODO B
}

// ============================================================
// C — Edit (Load + Update)
// ============================================================

// TODO C: GET /api/jobs/{id} -> fill form fields + set editingId
//  - jQuery: $.getJSON
//  - fetch:  await fetch + json
async function loadJobForEdit(id) {
    // TODO C
}

// TODO C: PUT /api/jobs/{id} (Content-Type: application/json, body JSON)
//  - jQuery: $.ajax({type:'PUT', ...})
//  - fetch:  await fetch(url, {method:'PUT', ...})
//  - thành công -> toast "Updated"
//  - lỗi -> handleApiError
async function submitUpdate(id, dto) {
    // TODO C
}

// ============================================================
// Shared — error + toast (B hoặc C hiện thực, 2 người dùng chung)
// ============================================================

// TODO: hiển thị lỗi từ API response, dùng toast()
function handleApiError(err) {
    // TODO
}

// TODO: Bootstrap toast/alert trong #toastContainer
function toast(message, type) {
    // TODO
}

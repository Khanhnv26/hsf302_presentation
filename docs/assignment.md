# Phân chia việc — Async Request (jQuery/JS) ứng dụng vào CRUD

> Đề tài: Tìm hiểu Async Request (jQuery/JS) ứng dụng vào màn hình quản lý CRUD.
> Dự án: `ats_springboot_se2035` (Spring Boot + Thymeleaf + SQL Server).
> Áp dụng trực tiếp lên trang `/jobs` (dashboard) + `/jobs/detail` (form) — KHÔNG làm trang demo riêng.
>
> **Cách chia:** full-stack theo CRUD operation — mỗi thành viên làm cả backend lẫn frontend cho 1-2 thao tác.
> **Git:** mỗi người 1 branch, 1 người merge tổng. Thứ tự merge: **A → B → C**.

---

## Quy ước chung

- **Branch:** `feature/list-read` (A), `feature/create` (B), `feature/edit-delete` (C)
- **Merge thứ tự:** A trước (tạo foundation) → B pull master rồi làm → C pull master rồi làm.
- **File "nóng" (3 người cùng đụng):** `JobRestController.java` — A tạo khung + 2 endpoint của A, B/C chỉ **thêm method** vào, **không sửa method của nhau**.
- **Security đang tắt** → AJAX POST/PUT/DELETE không cần CSRF token.
- **JS tách file riêng** (không inline) → dễ show code trong slide.
- **`JobResponse` đang dùng trong JPQL `SELECT new ...`** → KHÔNG được thêm field (gãy constructor). Dùng `JobDto` mới cho API.
- **DTO vs Entity:** luôn trả `JobDto`/`SkillResponse`/`DepartmentDto` từ API, KHÔNG trả entity `Job` (lazy `Set<JobSkill>` + `open-in-view=false` → lỗi serialize).

---

## Khung file đã tạo sẵn (skeleton)

Đã có khung với `TODO A` / `TODO B` / `TODO C` đánh dấu chỗ mỗi người làm:

### Backend
- `dto/JobDto.java` — DTO đầy đủ field (A)
- `controller/JobRestController.java` — 5 endpoint với TODO (A tạo khung, B/C thêm)
- `controller/DepartmentApiController.java` — TODO A
- `controller/SkillApiController.java` — đã hiện thực sẵn (A)
- `exceptions/ApiExceptionHandling.java` — `@RestControllerAdvice` trả JSON (B)
- `services/JobService.java` + `JobServiceImpl.java` — đã có stub `updateJob` (C hiện thực) + stub `toJobDto` (A hiện thực)

### Frontend
- `static/dashboard-async.js` — khung TODO A (list/search) + TODO C (delete)
- `static/job-form-async.js` — khung TODO B (create/dropdowns) + TODO C (edit)
- `docs/presentation-outline.md` — skeleton 9 slide
- `docs/assignment.md` — file này

### Cần sửa (chưa làm, TV tự làm theo phần của mình)
- `templates/views/jobs/general_dashboard.html` — đổi `th:each` → `<tbody id>` + tham chiếu JS (A + C)
- `templates/views/jobs/job_detail.html` — đổi form binding + tham chiếu JS (B + C)

---

## Thành viên A — List + Read (full-stack)

**Branch:** `feature/list-read` · **Merge đầu tiên**

### Backend
| File | Việc | TODO |
|------|------|------|
| `JobServiceImpl.java` | Hiện thực `toJobDto(Job)→JobDto` (đủ field: id, title, description, location, minSalary, maxSalary, deadline, jobType, status, departmentId, skillIds, publishedAt) | `TODO A` |
| `JobRestController.java` | Hiện thực `GET /api/jobs?keyword=` → `List<JobDto>` (gọi `jobService.getAll(keyword)` + map `toJobDto`) | `TODO A` |
| `JobRestController.java` | Hiện thực `GET /api/jobs/{id}` → `JobDto` (gọi `jobService.getJobById(id)` + map) | `TODO A` |
| `DepartmentApiController.java` | Hiện thực `findAll()` → map `Department` sang `DepartmentDto` (id + departmentName) | `TODO A` |
| `SkillApiController.java` | **Đã xong** (gọi `skillService.findAll()`) | — |

### Frontend
| File | Việc | TODO |
|------|------|------|
| `general_dashboard.html` | Bỏ `th:each` loop → `<tbody id="jobTable">` rỗng; giữ search box nhưng đổi thành async (không submit form); thêm `<script th:src="@{/dashboard-async.js}">`; thêm `<div id="toastContainer">`; giữ nút "Create new" | — |
| `dashboard-async.js` | Hiện thực `loadJobs(keyword)` + `renderJobs(jobs)` — cả jQuery `$.getJSON` + `fetch async/await`; bind search debounce 300ms; hiện thực `handleApiError` + `toast` (shared với C) | `TODO A` |

### Slide phụ trách
- Slide 2 — Tiến hoá XHR → jQuery → fetch → async/await
- Slide 4 (phần list) — cú pháp jQuery vs fetch (snippet load list từ code thật)
- Slide 6 — Cấu trúc code (shell → JS → API → Controller → Service → DB)
- Slide 7 — Xử lý lỗi async (`@RestControllerAdvice`, HTTP status, DTO vs entity)

### Test (A tự kiểm tra trước khi merge)
```powershell
# Test API bằng curl/Postman
curl "http://localhost:8080/api/jobs"
curl "http://localhost:8080/api/jobs/1"
curl "http://localhost:8080/api/departments"
curl "http://localhost:8080/api/skills"
```
- `mvnw compile` pass
- Dashboard load list không reload, search hoạt động.

---

## Thành viên B — Create (full-stack)

**Branch:** `feature/create` · **Merge thứ 2** (sau khi A merge xong, pull master)

### Backend
| File | Việc | TODO |
|------|------|------|
| `JobRestController.java` | Hiện thực `POST /api/jobs` — validate `dto.title` != blank + `dto.deadline` != null; map `JobDto` sang `JobRequest`; gọi `jobService.createJob`; trả `ResponseEntity<JobDto>` status 201 | `TODO B` |
| `ApiExceptionHandling.java` | Hiện thực `validationError(MethodArgumentNotValidException)` → 400 + `{error:"validation failed", details:{...}}` | `TODO B` |

### Frontend
| File | Việc | TODO |
|------|------|------|
| `job_detail.html` | Form giữ id `job-form`; bỏ `th:object` binding Thymeleaf (vì async tự fill) HOẶC giữ làm fallback; thêm `<script th:src="@{/job-form-async.js}">`; thêm `<div id="toastContainer">`; nút Save giữ `type=submit` | — |
| `job-form-async.js` | Hiện thực `loadDepartments()` + `loadSkills()` + `collectForm()` + `submitCreate(dto)` — cả jQuery `$.ajax POST` + `fetch POST`; reset form sau create; toast success | `TODO B` |

### Slide phụ trách
- Slide 1 — Mở bài: Sync vs Async + VD thực tế
- Slide 3 — Vì sao cần async
- Slide 4 (phần create) — cú pháp jQuery vs fetch (snippet POST từ code thật)
- Slide 5 (phần create) — demo live: create không reload

### Test (B tự kiểm tra trước khi merge)
```powershell
# Test API
curl -X POST http://localhost:8080/api/jobs -H "Content-Type: application/json" -d "{\"title\":\"Dev\",\"location\":\"HN\",\"minSalary\":1000,\"maxSalary\":2000,\"deadline\":\"2026-12-31\",\"departmentId\":1,\"skillIds\":[1,2]}"
```
- Form submit → toast "Created" + form reset, không reload, list dashboard tự cập nhật (nếu A đã merge).

---

## Thành viên C — Edit + Delete + Tổng hợp slide (full-stack)

**Branch:** `feature/edit-delete` · **Merge cuối** (sau khi A + B merge, pull master, xử lý conflict với B ở `job_detail.html`)

### Backend
| File | Việc | TODO |
|------|------|------|
| `JobServiceImpl.java` | Hiện thực `updateJob(Long id, JobRequest)` — `findById(id).orElseThrow(JobNotFoundException)`; gán field từ `jobRequest`; `save`; return | `TODO C` |
| `JobRestController.java` | Hiện thực `PUT /api/jobs/{id}` → `JobDto` (map dto→JobRequest, gọi `updateJob`, trả `toJobDto`) | `TODO C` |
| `JobRestController.java` | Hiện thực `DELETE /api/jobs/{id}` → `ResponseEntity<Void>` 204 (gọi `jobService.delete`) | `TODO C` |

### Frontend
| File | Việc | TODO |
|------|------|------|
| `general_dashboard.html` | Thêm modal confirm delete Bootstrap (`#deleteConfirmModal`); nút Delete trong row gắn `data-id` | — |
| `dashboard-async.js` | Hiện thực `confirmAndDelete(id)` + `deleteJob(id)` — jQuery `$.ajax DELETE` + `fetch DELETE`; xoá row khỏi DOM; toast | `TODO C` |
| `job_detail.html` | Detect edit mode từ URL `/jobs/{id}` hoặc query `?id=` → set `editingId` + đổi title/nút Save→"Update"; đảm bảo không conflict với phần create của B | — |
| `job-form-async.js` | Hiện thực `loadJobForEdit(id)` + `submitUpdate(id, dto)` — jQuery + fetch; toast | `TODO C` |

### Slide phụ trách
- Slide 4 (phần edit/delete) — cú pháp jQuery vs fetch (snippet PUT/DELETE từ code thật)
- Slide 5 (phần edit/delete) — demo live: edit + delete không reload
- Slide 8 — Ưu/nhược jQuery vs fetch, khi nào dùng
- Slide 9 — Q&A + câu hỏi có thể bị cô hỏi
- **Tổng hợp** `docs/presentation-outline.md` — gom snippet thật từ A + B, cập nhật flow demo chính xác, đảm bảo 9 slide hoàn chỉnh

### Test (C tự kiểm tra trước khi merge)
```powershell
# Test API
curl -X PUT http://localhost:8080/api/jobs/1 -H "Content-Type: application/json" -d "{\"title\":\"Dev Sr\",\"location\":\"HN\",\"minSalary\":1500,\"maxSalary\":2500,\"deadline\":\"2026-12-31\",\"departmentId\":1,\"skillIds\":[1]}"
curl -X DELETE http://localhost:8080/api/jobs/1
```
- Edit: click Edit → form prefilled → Save → toast "Updated", không reload.
- Delete: click Delete → modal confirm → Confirm → row biến mất, toast, không reload.

---

## Lịch trình phối hợp

| Giai đoạn | A | B | C |
|-----------|---|---|---|
| **Đầu** | Tạo `JobDto` + `toJobDto` + GET endpoints + Dept/Skill API | Viết slide 1, 3 lý thuyết; đọc form; lên plan JS create | Viết slide 8; đọc form + dashboard; lên plan JS edit/delete |
| **Giữa** | Dashboard list + search async; viết slide 2,4,6,7; **merge branch trước** | Form create async (jQuery + fetch); viết slide 4,5 create; **merge sau A** | Form edit + dashboard delete async; viết slide 4,5 edit/delete; **merge cuối** |
| **Cuối** | Hỗ trợ debug API | Hoàn thiện form create | Hoàn thiện edit/delete + **tổng hợp `presentation-outline.md`** + verify tổng |

---

## Mốc giao nhận

1. **A merge trước:** `JobDto` + `toJobDto` + GET endpoints + Dept/Skill API sẵn trên master → B, C pull master có foundation.
2. **B merge sau A:** POST endpoint + exception handler + form create chạy được.
3. **C merge cuối:** PUT/DELETE + `updateJob` + edit/delete UI + slide tổng hợp.
4. **Verify cuối (C điều phối, cả 3 test):**
   - `mvnw compile` → BUILD SUCCESS
   - `mvnw spring-boot:run` → test đủ 4 CRUD trên cả 2 mode jQuery/fetch + DevTools Network
   - `docs/presentation-outline.md` đầy đủ 9 slide

---

## Luồng demo ngày thuyết trình

```
1. Mở http://localhost:8080/auths/login
2. Login: admin@ats.org / 123  →  redirect /jobs (dashboard)
3. Mở DevTools → Network tab → filter XHR/Fetch
4. Demo List:    trang load → thấy GET /api/jobs trả JSON, không reload
5. Demo Search:  gõ keyword → request mới, bảng tự cập nhật
6. Demo Create:  click "Create new" → /jobs/detail → điền form → Save
                 → thấy POST /api/jobs, toast success, không reload
7. Demo Edit:    quay lại /jobs → click Edit → form prefilled → sửa → Save
                 → thấy PUT /api/jobs/{id}, toast updated
8. Demo Delete:  click Delete → modal confirm → Confirm
                 → thấy DELETE /api/jobs/{id}, row biến mất, không reload
9. Toggle mode jQuery/fetch → làm lại 1 thao tác → so sánh request giống nhau
```

---

## Lưu ý quan trọng (đọc trước khi code)

1. **`JobRestController.java` file nóng:** A tạo khung + 2 method của A; B/C **chỉ thêm** method của mình, **không sửa** method của nhau. Merge theo thứ tự A→B→C.
2. **`JobServiceImpl.java`:** A thêm `toJobDto`, C thêm `updateJob` — 2 method khác nhau, ít conflict. KHÔNG đụng `toEntity`/`getJobsByCriteria`/`toDto(Job)→JobRequest` (đã sửa ở đợt base).
3. **`job_detail.html`:** B (create) + C (edit) — chia block rõ:
   - B: form submit handler chung `onSubmit` (đã có stub), `submitCreate`.
   - C: `loadJobForEdit` + `submitUpdate` + detect edit mode.
   - Quy ước: B làm trước phần create + dropdowns, C thêm phần edit sau.
4. **`dashboard-async.js`:** A (list/search) + C (delete) — chia function rõ, ít conflict.
5. **`handleApiError` + `toast`:** shared function — A hiện thực trong `dashboard-async.js`, B/C copy sang `job-form-async.js` (hoặc tách `common.js` nếu muốn, nhưng khuyến nghị copy để 2 file độc lập).
6. **Security tắt** → không cần CSRF. Khi demo, nói rõ với cô "security đang tắt để đơn giản hoá, khi bật cần gửi header `X-CSRF-TOKEN`" (slide 9 câu 3).
7. **DB chưa có job PUBLISH** → trang public `/public/jobs` vẫn rỗng. Demo CRUD trên `/jobs` (admin) không bị ảnh hưởng. Nếu muốn demo public có data → insert SQL tay `UPDATE jobs SET status='PUBLISH'` hoặc thêm nút Publish (ngoài scope).
8. **Account demo:** `admin@ats.org` / `123` (role ADMIN, status ACTIVE).

---

## Slide outline (tham chiếu nhanh)

| # | Slide | Người |
|---|-------|-------|
| 1 | Sync vs Async + VD thực tế | B |
| 2 | Tiến hoá XHR → jQuery → fetch → async/await | A |
| 3 | Vì sao cần async | B |
| 4 | Cú pháp jQuery vs fetch (list/create/edit/delete) | A + B + C |
| 5 | Demo live (dashboard + form, DevTools Network) | B + C |
| 6 | Cấu trúc code | A |
| 7 | Xử lý lỗi async | A |
| 8 | Ưu/nhược jQuery vs fetch | C |
| 9 | Q&A + câu hỏi có thể bị cô hỏi | C tổng hợp |

Chi tiết nội dung mỗi slide xem `docs/presentation-outline.md`.

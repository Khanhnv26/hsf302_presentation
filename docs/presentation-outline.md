# Async Request (jQuery/JS) — Presentation Outline

> Đề tài: Tìm hiểu Async Request (jQuery/JS) ứng dụng vào màn hình quản lý CRUD.
> Tổng số slide: 9. Đóng góp: A, B, C (ghi rõ sau mỗi slide).

---

## Slide 1 — Sync vs Async là gì? (B)

- **Sync (đồng bộ):** trình duyệt gửi request → chờ server xử lý → reload toàn bộ trang.
  - VD: form submit truyền thống, link `<a href>`.
- **Async (bất đồng bộ):** trình duyệt gửi request ngầm → không reload → cập nhật một phần DOM.
  - VD thực tế: Gmail (gửi mail không reload), Facebook (like bài viết), YouTube (load thêm comment).
- **Hình so sánh:** Sync = cả trang nhấp nháy; Async = chỉ vùng nội dung cập nhật.

---

## Slide 2 — Tiến hoá của async trong JavaScript (A)

| Thế hệ | Công nghệ | Năm | Đặc điểm |
|---------|-----------|-----|-----------|
| 1 | `XMLHttpRequest` (XHR) | 2006 | API thô, callback lồng nhau |
| 2 | `jQuery $.ajax` / `$.getJSON` | 2006 | Ngắn gọn, callback, xử lý lỗi `.fail()` |
| 3 | `fetch` + Promise | 2015 (ES6) | Chuẩn trình duyệt, Promise chaining |
| 4 | `async/await` | 2017 (ES8) | Cú pháp trông như sync, dễ debug |

- **Trend:** callback → Promise → async/await (giảm "callback hell").

---

## Slide 3 — Vì sao cần Async? (B)

1. **Không reload trang** → UX mượt, không nhấp nháy.
2. **Tiết kiệm bandwidth** → chỉ tải JSON dữ liệu, không tải lại HTML/CSS/JS.
3. **Phản hồi tức thì** → toast/modal thay vì chuyển trang.
4. **Tách luồng:** UI (HTML) và Data (JSON API) độc lập → dễ mở rộng (mobile app dùng chung API).
5. **Demo so sánh:** trang `/jobs` cũ (sync, reload) vs `/jobs` mới (async, không reload).

---

## Slide 4 — Cú pháp jQuery vs fetch (A + B + C)

### List (A)
```javascript
// jQuery
$.getJSON('/api/jobs', function(data){ renderJobs(data); });
// fetch + async/await
const r = await fetch('/api/jobs'); const data = await r.json(); renderJobs(data);
```

### Create (B)
```javascript
// jQuery
$.ajax({type:'POST', url:'/api/jobs', contentType:'application/json', data: JSON.stringify(dto)});
// fetch
await fetch('/api/jobs', {method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify(dto)});
```

### Update (C)
```javascript
// jQuery
$.ajax({type:'PUT', url:'/api/jobs/'+id, contentType:'application/json', data: JSON.stringify(dto)});
// fetch
await fetch('/api/jobs/'+id, {method:'PUT', headers:{'Content-Type':'application/json'}, body: JSON.stringify(dto)});
```

### Delete (C)
```javascript
// jQuery
$.ajax({type:'DELETE', url:'/api/jobs/'+id}).done(()=> removeRow(id));
// fetch
await fetch('/api/jobs/'+id, {method:'DELETE'}); removeRow(id);
```

---

## Slide 5 — Demo Live (B + C)

**Chuẩn bị:** mở Chrome DevTools → tab Network → filter XHR/Fetch.

| Thao tác | Trang | Người demo | Quan sát |
|----------|-------|------------|----------|
| Load list | `/jobs` | B/A | request `GET /api/jobs` trả JSON, trang không reload |
| Search | ô search dashboard | A | gõ keyword → request mới, bảng tự cập nhật |
| Create | `/jobs/detail` → submit | B | request `POST /api/jobs`, toast success, form reset |
| Edit | click Edit → sửa → Save | C | request `PUT /api/jobs/{id}`, toast updated |
| Delete | nút Delete → confirm modal | C | request `DELETE /api/jobs/{id}`, row biến mất, không reload |

**Điểm nhấn:** chỉ thấy request nhỏ JSON, không thấy document reload.

---

## Slide 6 — Cấu trúc code (A)

```
[HTML shell: general_dashboard.html / job_detail.html]
        │  (tham chiếu)
        ▼
[JS: dashboard-async.js / job-form-async.js]
        │  fetch / $.ajax
        ▼
[REST API: /api/jobs, /api/departments, /api/skills]
        │
        ▼
[@RestController: JobRestController, DepartmentApiController, SkillApiController]
        │
        ▼
[Service: JobService / DepartmentService / SkillService]
        │
        ▼
[Repository / DAO → SQL Server]
```

- **Tách biệt rõ ràng:** UI (Thymeleaf shell) ↔ Data (REST API JSON).
- **Lý do trả DTO (JobDto) thay vì entity:** `open-in-view=false` + lazy `Set<JobSkill>` → serialize entity sẽ lỗi `LazyInitializationException`.

---

## Slide 7 — Xử lý lỗi Async (A)

- **Backend:** `@RestControllerAdvice` (`ApiExceptionHandling`) → trả JSON `{error: "..."}` + HTTP status (400/404/500).
- **Frontend:**
  - jQuery: `.fail(function(jqXHR){ toast(jqXHR.responseJSON.error, 'danger'); })`
  - fetch: `if (!response.ok) { const e = await response.json(); throw new Error(e.error); }` → `catch`
- **UI:** Bootstrap toast/alert đỏ cho lỗi, xanh cho thành công.
- **Ví dụ:** xóa job không tồn tại → server trả 404 `{error:"Job not found"}` → toast đỏ.

---

## Slide 8 — Ưu/nhược jQuery vs fetch (C)

| Tiêu chí | jQuery $.ajax | fetch + async/await |
|----------|---------------|----------------------|
| Cú pháp | Ngắn, callback | Hơi dài, Promise/await |
| Callback hell | Có thể gặp | Không (await phẳng) |
| Xử lý lỗi | `.fail()` tách bạch | Phải check `res.ok` + throw |
| Thư viện | Cần CDN jQuery | Built-in trình duyệt |
| Convert JSON | Tự động (`responseJSON`) | Phải `await res.json()` |
| Promise | Hỗ trợ | Native |
| Hiện đại | Cũ nhưng phổ biến | Chuẩn ES, tương lai |

**Khi nào dùng gì?**
- Dự án đã có jQuery → dùng `$.ajax` cho đồng bộ.
- Dự án mới / không jQuery → `fetch` + `async/await`.
- Cả hai đều hợp lệ cho đề tài này.

---

## Slide 9 — Q&A + Câu hỏi có thể bị cô hỏi (C tổng hợp)

1. **Promise vs Callback khác nhau thế nào?**
   - Callback: hàm truyền vào hàm khác, lồng nhau khó đọc (callback hell).
   - Promise: đối tượng đại diện cho kết quả tương lai, `.then().catch()`.
   - async/await: cú pháp trên Promise, trông như code đồng bộ.
2. **Tại sao gửi JSON mà không dùng form encode?**
   - JSON hỗ trợ cấu trúc phức tạp (list `skillIds`), REST API chuẩn, dễ dùng chung cho mobile.
3. **CSRF token đâu?**
   - Spring Security đang tắt trong demo → không cần. Khi bật security, phải gửi header `X-CSRF-TOKEN`.
4. **Tại sao trả DTO (JobDto) thay vì entity Job?**
   - Entity có `Set<JobSkill>` lazy + `open-in-view=false` → serialize JSON sẽ lỗi. DTO kiểm soát field trả về.
5. **Sao không dùng PutMapping form Thymeleaf luôn?**
   - HTML form chỉ hỗ trợ GET/POST (phải dùng `_method=PUT` hack) → AJAX gửi được PUT/DELETE thật.
6. **Debounce search là gì?**
   - Trì hoãn gửi request 300-500ms sau khi user ngừng gõ → tránh spam API mỗi phím bấm.

---

> **Ghi chú cho C (tổng hợp):** gom snippet thật từ code A + B + C thay cho placeholder ở Slide 4. Cập nhật Slide 5 theo flow demo thực tế ngày thuyết trình.

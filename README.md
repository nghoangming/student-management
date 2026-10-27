# Lab 08: Coding Standards, Best Practices & Code Review
## Module Xác thực (Authentication Module) - Hệ thống Quản lý Học sinh
**Môn học:** Công nghệ Phần mềm  
**Nhóm thực hiện:** Nhóm VQMN (Lớp TH CNPM Nhóm 2 - Tổ 1)  
- **Nguyễn Hoàng Minh** (MSSV: 52400216) – Nhóm trưởng / BA / Product Owner  
- **Tăng Duy Việt** (MSSV: 52400255) – Backend Developer  
- **Nguyễn Trí Nguyên** (MSSV: 52400140) – UI Developer  
- **Lưu Nhật Quang** (MSSV: 52400229) – Tester  

---

## 1. Giới thiệu dự án

Dự án này là phân hệ Xác thực người dùng (Authentication Module) thuộc **Hệ thống Quản lý Học sinh**. Module cung cấp 2 API cốt lõi:
1. `POST /api/auth/register`: Đăng ký tài khoản mới cho người dùng (Học sinh, Giáo viên, Quản trị viên).
2. `POST /api/auth/login`: Xác thực đăng nhập vào hệ thống và khởi tạo phiên làm việc.

Dự án được xây dựng theo chuẩn mã nguồn công nghiệp (Industry Coding Standards), tách biệt tầng Controller (`app.js`) và tầng Business Logic (`services/authService.js`), áp dụng các thực hành bảo mật chống SQL Injection, XSS, Brute-force/Enumeration, và kiểm thử tự động toàn diện với Jest + Supertest.

---

## 2. Cấu trúc thư mục

```text
Bai tap tuan 8/
├── app.js                 # Khởi tạo Express, cấu hình middleware và routes
├── server.js              # Entry point khởi chạy máy chủ Node.js HTTP
├── package.json           # Danh sách dependencies, scripts và cấu hình Jest
├── package-lock.json      # Khóa phiên bản chi tiết các thư viện
├── services/
│   └── authService.js     # Tầng nghiệp vụ: validation, băm mật khẩu, xử lý đăng ký/đăng nhập
├── tests/
│   └── auth.test.js       # Bộ test tự động (>= 7 ca kiểm thử Jest + Supertest)
├── BAO_CAO_LAB08.md       # Báo cáo tổng hợp minh chứng Lab 08, Git Flow, PR & Peer Review
├── Lab08_MASTER_PROMPT_AI.md # Đề bài & yêu cầu chi tiết của Lab08
└── README.md              # Tài liệu hướng dẫn cài đặt, chạy và kiểm thử
```

---

## 3. Yêu cầu môi trường

- **Node.js**: Phiên bản 18.x trở lên (khuyên dùng Node.js v20.x hoặc v24.x LTS).
- **npm**: Phiên bản 9.x trở lên.

---

## 4. Hướng dẫn cài đặt

Mở Terminal tại thư mục `Bai tap tuan 8`:

```bash
# Cài đặt toàn bộ dependencies (express, bcryptjs) và devDependencies (jest, supertest)
npm install
```

---

## 5. Hướng dẫn chạy ứng dụng

### Chạy máy chủ Express:
```bash
npm start
```
Máy chủ sẽ lắng nghe tại cổng mặc định `http://localhost:3000`.

### Giao diện Web kiểm thử trực quan (UI Testbed):
Sau khi chạy máy chủ, mở trình duyệt truy cập:
👉 **`http://localhost:3000`**
Giao diện cho phép:
- Chuyển đổi giữa 2 tab **Đăng ký** và **Đăng nhập**.
- Chọn vai trò người dùng: Học sinh (`student`), Giáo viên (`teacher`), Quản trị viên (`admin`).
- Xem trực tiếp phản hồi JSON kèm mã HTTP Status (201, 200, 400, 401, 409).
- Bấm nút thử nhanh các kịch bản Lab 08: dữ liệu hợp lệ, trùng lặp, mật khẩu ngắn, username khoảng trắng, thử chuỗi XSS.
- Minh chứng cơ chế an toàn phòng chống XSS bằng `textContent` khi hiển thị tên người dùng đã đăng nhập.

### Danh sách API Endpoints:
- **Giao diện Web UI:** `GET http://localhost:3000/`
- **Đăng ký tài khoản:** `POST http://localhost:3000/api/auth/register`
- **Đăng nhập:** `POST http://localhost:3000/api/auth/login`

---

## 6. Hướng dẫn kiểm thử (Automated Testing)

### 6.1. Chạy toàn bộ kiểm thử
```bash
npm test
```

### 6.2. Chạy kiểm thử kèm báo cáo độ bao phủ mã nguồn (Code Coverage)
```bash
npm run test:coverage
```

Bộ kiểm thử bao gồm tối thiểu 7 ca kiểm thử chính cùng các ca kiểm thử biên:
1. `Register thành công` với thông tin hợp lệ (mã 201).
2. `Register trùng` username (báo lỗi 409 Conflict).
3. `Login thành công` với username và password chính xác (mã 200).
4. `Sai mật khẩu` khi đăng nhập (báo lỗi 401 Unauthorized).
5. `Mật khẩu ngắn` dưới 8 ký tự (báo lỗi 400 Bad Request).
6. `Username khoảng trắng` hoặc để trống (báo lỗi 400 Bad Request).
7. `Thiếu password` trong payload request (báo lỗi 400 Bad Request).
8. *Bonus:* Đăng nhập với tài khoản không tồn tại (báo lỗi 401 tránh dò quét user).
9. *Bonus:* Kiểm tra bảo mật không trả về `passwordHash` hoặc `password` thô ra response.

---

## 7. Quy chuẩn lập trình (Coding Standards) & Best Practices

| Thành phần | Quy tắc | Ví dụ áp dụng trong dự án |
| :--- | :--- | :--- |
| **Biến (Variables)** | `camelCase` | `sanitizedUsername`, `passwordHash`, `isPasswordValid` |
| **Hàm (Functions)** | `verbMeaning` | `registerUser()`, `loginUser()`, `resetUsers()` |
| **Hằng số (Constants)** | `UPPER_SNAKE_CASE` | `MIN_PASSWORD_LENGTH = 8`, `BCRYPT_SALT_ROUNDS = 10` |
| **Tên tệp (File names)** | Tên rõ nghĩa | `app.js`, `server.js`, `authService.js`, `auth.test.js` |
| **Quy ước đặt tên** | Cấm dùng biến vô nghĩa | Không dùng `a`, `b`, `x1`, `temp` |
| **Xử lý ngoại lệ** | Tách lớp và `try...catch` | Sử dụng `AppError` kèm HTTP statusCode chuẩn RESTful |
| **Bảo mật phản hồi** | Không leak `passwordHash` | Luôn loại bỏ trường mật khẩu băm trước khi trả về client |

---

## 8. Thực hành An toàn Bảo mật (Security Best Practices)

### 8.1. Phòng chống SQL Injection (Prepared Statements / Parameterized Queries)
Khi tương tác với hệ quản trị cơ sở dữ liệu (ví dụ MySQL/PostgreSQL/SQL Server), không bao giờ cộng chuỗi SQL trực tiếp với biến người dùng gửi lên. Thay vào đó, áp dụng truy vấn có tham số hóa:

```javascript
// ✅ CÁCH LÀM AN TOÀN (Parameterized Query):
const query = "SELECT id, username, password_hash, role FROM users WHERE username = ?";
const [rows] = await connection.execute(query, [sanitizedUsername]);
```

**Tại sao an toàn?**
- Khi sử dụng cơ chế `PreparedStatement`, câu lệnh SQL được gửi tới Database Server để phân tích cú pháp (compile / parse) và tạo cây thực thi trước.
- Dữ liệu người dùng truyền vào mảng tham số `[sanitizedUsername]` được cơ chế database coi thuần túy là dữ liệu thô (literal value / data), không thể làm thay đổi cấu trúc cú pháp của câu lệnh SQL, triệt tiêu hoàn toàn nguy cơ SQL Injection.

### 8.2. Phòng chống Cross-Site Scripting (XSS)
Phía Frontend khi hiển thị dữ liệu của người dùng (như tên đăng nhập `username` hoặc thông tin học sinh):

```javascript
// ✅ CÁCH LÀM AN TOÀN (Không gây XSS):
const usernameDisplayElement = document.getElementById("user-display-name");
usernameDisplayElement.textContent = username;
```

**Tại sao không dùng `innerHTML`?**
- Nếu dùng `innerHTML = username`, nếu kẻ tấn công đặt tên chứa mã độc như `<img src=x onerror=alert('Hacked')>` hoặc `<script src="evil.js"></script>`, trình duyệt sẽ diễn giải chuỗi đó thành các thẻ HTML và đoạn mã JavaScript có thể thực thi được.
- Khi dùng thuộc tính `textContent`, trình duyệt sẽ coi mọi ký tự (kể cả `<, >, &, "`) là chuỗi văn bản thuần túy (plain text), tự động mã hóa và không thực thi mã độc.

---

## 9. Quy trình Git Flow & Code Review

Dự án áp dụng chặt chẽ quy trình Git Flow:
- Nhánh cơ sở: `develop`
- Nhánh tính năng: `feature/auth-module`
- Quy trình Pull Request: Tạo PR từ `feature/auth-module` vào `develop`.
- Peer Review: 3 lượt đánh giá nghiêm túc từ các thành viên trong nhóm.
- Sửa lỗi và hoàn thiện mã trước khi hợp nhất (Merge).

*(Chi tiết xem tại tệp [BAO_CAO_LAB08.md](file:///d:/cnpm_ketthucmon/Bai%20tap%20tuan%208/BAO_CAO_LAB08.md))*

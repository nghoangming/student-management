# MASTER PROMPT -- LAB08 Coding Standards, Best Practices & Code Review

> Mục tiêu: AI phải hoàn thành toàn bộ Lab08 theo đúng tài liệu, tạo đầy
> đủ mã nguồn, kiểm thử, Git Flow, Pull Request, Peer Code Review và
> minh chứng.

## Bối cảnh

-   Môn: Công nghệ phần mềm -- Lab08.
-   Ngôn ngữ: Node.js (Express).
-   Test: Jest + Supertest.
-   Bảo mật: bcryptjs.
-   Nhánh Git: `feature/auth-module` → `develop`.

## Yêu cầu bắt buộc

1.  Tạo project hoàn chỉnh.
2.  API Register/Login.
3.  Code chuẩn (camelCase, tách service, try/catch).
4.  Kiểm tra đầu vào.
5.  bcrypt hash.
6.  Không trả passwordHash.
7.  Chống SQL Injection (parameterized query ví dụ).
8.  Chống XSS.
9.  Viết ít nhất 7 test.
10. Chạy coverage.
11. Git Flow đầy đủ.
12. Pull Request.
13. Peer Review (3 nhận xét).
14. Sửa review rồi merge.
15. Xuất đầy đủ file.

## Cấu trúc project

``` text
Project/
├── app.js
├── server.js
├── package.json
├── package-lock.json
├── services/
│   └── authService.js
├── tests/
│   └── auth.test.js
└── README.md
```

## Sinh đầy đủ code

### package.json

-   express
-   bcryptjs
-   jest
-   supertest

Script:

``` json
{
  "scripts": {
    "test": "jest"
  }
}
```

### server.js

Khởi chạy Express.

### app.js

Tạo:

-   POST /api/auth/register
-   POST /api/auth/login

Dùng `try...catch`.

### services/authService.js

Thực hiện:

-   register()
-   login()

Bao gồm:

-   trim()
-   toLowerCase()
-   password \>= 8
-   username unique
-   bcrypt hash
-   bcrypt compare

### tests/auth.test.js

Viết ít nhất 7 test:

1.  Register thành công
2.  Register trùng
3.  Login thành công
4.  Sai mật khẩu
5.  Mật khẩu ngắn
6.  Username khoảng trắng
7.  Thiếu password

## Chạy lệnh

``` bash
npm install
npm test
npm test -- --coverage
```

## Coverage

Ghi lại:

-   Statements
-   Branches
-   Functions
-   Lines

## Coding Standards

Áp dụng:

  Thành phần   Quy tắc
  ------------ ------------------
  Variable     camelCase
  Function     verbMeaning
  Constant     UPPER_SNAKE_CASE
  File         rõ ràng

Không dùng:

-   a
-   b
-   x1

Không trả passwordHash.

## Exception Handling

Bắt các lỗi:

-   thiếu username
-   thiếu password
-   password ngắn
-   username trùng
-   login sai
-   sai kiểu dữ liệu

## SQL Injection

Cho ví dụ:

``` js
connection.execute(
  "SELECT * FROM users WHERE username = ?",
  [username]
);
```

Giải thích tại sao an toàn.

## XSS

Cho ví dụ:

``` js
element.textContent = username;
```

Giải thích vì sao không dùng innerHTML.

## Git Flow

Sinh đầy đủ lệnh:

``` bash
git switch develop
git pull origin develop
git switch -c feature/auth-module
git add .
git commit -m "feat: implement authentication API and tests"
git push -u origin feature/auth-module
```

## Pull Request

Sinh:

-   Title
-   Description

Theo mẫu.

## Peer Code Review

Viết 3 review thật:

### Review 1

-   vị trí
-   vấn đề
-   rủi ro
-   đề xuất

### Review 2

...

### Review 3

...

## Fix Review

Sinh commit:

``` bash
git add .
git commit -m "fix: address code review feedback"
git push
```

## Merge

Mô tả quy trình merge.

## README

Viết README gồm:

-   giới thiệu
-   cài đặt
-   chạy
-   test
-   coverage
-   cấu trúc project

## Output bắt buộc

AI phải xuất toàn bộ:

-   app.js
-   server.js
-   package.json
-   authService.js
-   auth.test.js
-   README.md
-   Git commands
-   PR description
-   3 Peer Reviews
-   Coverage template
-   Checklist nộp bài

## Quy tắc sinh code

-   Không dùng placeholder.
-   Code chạy được.
-   Node CommonJS (`require`).
-   Tên file đúng.
-   Không thiếu import/export.
-   Giữ phong cách sinh viên năm 2.

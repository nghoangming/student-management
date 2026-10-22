# Đăng nhập và quản trị

## Khởi chạy

Yêu cầu JDK 17 trở lên và Maven 3.8 trở lên. Trong PowerShell, đặt mật khẩu quản trị ban đầu (ít nhất 8 ký tự), rồi chạy backend:

```powershell
$env:APP_ADMIN_PASSWORD = "mat-khau-rieng-cua-ban"
cd backend
mvn spring-boot:run
```

Mở `http://localhost:8080/dang-nhap`, đăng nhập bằng tên `admin` và mật khẩu vừa đặt. Có thể đổi tên bằng biến `APP_ADMIN_USERNAME`. Không đặt mật khẩu mặc định trong mã nguồn. Cơ sở dữ liệu H2 được lưu tại `backend/data`; để dùng SQL Server, cấu hình `DB_URL`, `DB_USER`, `DB_PASSWORD` và `DB_DRIVER=com.microsoft.sqlserver.jdbc.SQLServerDriver` cùng JDBC driver trong Maven.

## Chức năng

- `UC01`: đăng nhập bằng tên người dùng và mật khẩu BCrypt, đăng xuất, khóa tài khoản vô hiệu hóa đăng nhập.
- `UC14`: quản trị tạo, sửa vai trò, bật/tắt, xóa và đặt lại mật khẩu tài khoản; người dùng đổi mật khẩu hiện tại.
- `UC15`: quản trị thêm, cập nhật và xóa tham số hệ thống.

Các URL `/quan-tri/**` yêu cầu vai trò `ADMIN`. Vai trò hợp lệ là `ADMIN`, `GIAO_VIEN`, `CAN_BO`. Hệ thống không cho vô hiệu hóa, hạ quyền hoặc xóa tài khoản ADMIN đang hoạt động cuối cùng.

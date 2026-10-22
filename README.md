# Hệ thống quản lý học sinh

Ứng dụng web quản lý hồ sơ học sinh, lớp học, điểm và kết quả học tập. Thiết kế theo kiến trúc ba tầng với các vai trò học sinh, giáo viên, cán bộ quản lý và quản trị viên. Phạm vi gồm đăng nhập, quản lý dữ liệu học tập, báo cáo và cấu hình hệ thống.

## Công nghệ

- Java 17, Spring Boot 3, Spring MVC, Spring Data JPA, Spring Security
- Thymeleaf và Bootstrap
- H2 dùng làm cơ sở dữ liệu local mặc định. Thiết kế dữ liệu hướng tới SQL Server; cấu hình driver SQL Server chưa có trong Maven.

## Chạy ứng dụng

Cần JDK 17+ và Maven 3.8+. Trong PowerShell:

```powershell
$env:APP_ADMIN_PASSWORD = "mat-khau-rieng-it-nhat-8-ky-tu"
cd backend
mvn spring-boot:run
```

Mở <http://localhost:8080/dang-nhap>. Tên đăng nhập quản trị ban đầu là `admin`; đặt mật khẩu riêng qua `APP_ADMIN_PASSWORD` trước lần chạy đầu tiên. Có thể đổi tên bằng `APP_ADMIN_USERNAME`.

## Cấu trúc

- `backend/src/main/java/com/studentmanagement/`: controller, domain, repository, service và cấu hình.
- `backend/src/main/resources/templates/`: giao diện Thymeleaf.
- `docs/`: hướng dẫn và quy ước làm việc.

Nhóm làm việc theo Git Flow: nhánh `feature/*` tạo từ `develop`, sau đó mở Pull Request về `develop` để review trước khi hợp nhất.

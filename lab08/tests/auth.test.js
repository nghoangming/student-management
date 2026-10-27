/**
 * auth.test.js
 * Bộ kiểm thử tự động (Unit & Integration Tests) cho API Xác thực Hệ thống Quản lý Học sinh
 * Sử dụng: Jest & Supertest
 * Nhóm: VQMN - Lớp TH CNPM Nhóm 2 Tổ 1
 */

const request = require('supertest');
const app = require('../app');
const authService = require('../services/authService');

describe('Module Xác thực (Authentication Module) - Lab 08', () => {
  // Đặt lại dữ liệu bộ nhớ trước mỗi ca kiểm thử
  beforeEach(() => {
    authService.resetUsers();
  });

  // ==========================================
  // NHÓM KIỂM THỬ ĐĂNG KÝ (REGISTER)
  // ==========================================
  describe('POST /api/auth/register', () => {
    // Test 1: Register thành công
    test('1. Đăng ký tài khoản thành công với thông tin hợp lệ', async () => {
      const response = await request(app)
        .post('/api/auth/register')
        .send({
          username: 'hocsinh_nguyenvana',
          password: 'Password123@',
          role: 'student'
        });

      expect(response.status).toBe(201);
      expect(response.body.success).toBe(true);
      expect(response.body.message).toBe('Đăng ký tài khoản thành công');
      expect(response.body.data).toHaveProperty('id');
      expect(response.body.data.username).toBe('hocsinh_nguyenvana');
      expect(response.body.data.role).toBe('student');

      // Đảm bảo không bao giờ để lộ password hoặc passwordHash
      expect(response.body.data.password).toBeUndefined();
      expect(response.body.data.passwordHash).toBeUndefined();
    });

    // Test 1b: Register không truyền role -> mặc định là 'student'
    test('1b. Đăng ký tài khoản không truyền role thì nhận role mặc định', async () => {
      const response = await request(app)
        .post('/api/auth/register')
        .send({
          username: 'hocsinh_macdinh',
          password: 'Password123@'
        });

      expect(response.status).toBe(201);
      expect(response.body.data.role).toBe('student');
      expect(authService.getAllUsers().length).toBe(1);
    });

    // Test 2: Register trùng username
    test('2. Đăng ký thất bại khi username đã tồn tại (trùng lặp)', async () => {
      // Đăng ký lần 1
      await request(app)
        .post('/api/auth/register')
        .send({
          username: 'giaovien_tranthib',
          password: 'Password123@',
          role: 'teacher'
        });

      // Đăng ký lần 2 với cùng username (kể cả khác chữ hoa/thường để kiểm tra toLowerCase)
      const duplicateResponse = await request(app)
        .post('/api/auth/register')
        .send({
          username: 'GIAOVIEN_TRANTHIB',
          password: 'AnotherPassword456@',
          role: 'teacher'
        });

      expect(duplicateResponse.status).toBe(409);
      expect(duplicateResponse.body.success).toBe(false);
      expect(duplicateResponse.body.message).toMatch(/đã tồn tại/i);
    });

    // Test 5: Mật khẩu ngắn (< 8 ký tự)
    test('5. Đăng ký thất bại khi mật khẩu ngắn hơn 8 ký tự', async () => {
      const response = await request(app)
        .post('/api/auth/register')
        .send({
          username: 'hocsinh_lethic',
          password: '1234567' // 7 ký tự
        });

      expect(response.status).toBe(400);
      expect(response.body.success).toBe(false);
      expect(response.body.message).toMatch(/tối thiểu 8 ký tự/i);
    });

    // Test 6: Username chứa toàn khoảng trắng hoặc rỗng
    test('6. Đăng ký thất bại khi username chỉ chứa khoảng trắng', async () => {
      const response = await request(app)
        .post('/api/auth/register')
        .send({
          username: '    ',
          password: 'ValidPassword123'
        });

      expect(response.status).toBe(400);
      expect(response.body.success).toBe(false);
      expect(response.body.message).toMatch(/khoảng trắng/i);
    });

    // Test 7: Thiếu password trong request
    test('7. Đăng ký thất bại khi không cung cấp password', async () => {
      const response = await request(app)
        .post('/api/auth/register')
        .send({
          username: 'hocsinh_test'
        });

      expect(response.status).toBe(400);
      expect(response.body.success).toBe(false);
      expect(response.body.message).toMatch(/Mật khẩu không được để trống/i);
    });

    // Test bổ sung: Thiếu username hoặc username sai kiểu dữ liệu
    test('Bonus: Đăng ký thất bại khi thiếu username hoặc sai kiểu dữ liệu', async () => {
      const responseNoUser = await request(app)
        .post('/api/auth/register')
        .send({
          password: 'ValidPassword123'
        });
      expect(responseNoUser.status).toBe(400);
      expect(responseNoUser.body.message).toMatch(/Tên đăng nhập không được để trống/i);

      const responseWrongType = await request(app)
        .post('/api/auth/register')
        .send({
          username: 12345,
          password: 'ValidPassword123'
        });
      expect(responseWrongType.status).toBe(400);

      const responseWrongPassType = await request(app)
        .post('/api/auth/register')
        .send({
          username: 'user_valid',
          password: 12345678
        });
      expect(responseWrongPassType.status).toBe(400);
    });

    // Test xử lý lỗi máy chủ 500 khi có ngoại lệ bất ngờ
    test('Bonus: Xử lý lỗi hệ thống 500 khi tầng service gặp sự cố bất ngờ', async () => {
      const spy = jest.spyOn(authService, 'registerUser').mockImplementationOnce(() => {
        throw new Error('Lỗi kết nối cơ sở dữ liệu giả lập');
      });

      const response = await request(app)
        .post('/api/auth/register')
        .send({
          username: 'hocsinh_error',
          password: 'Password123@'
        });

      expect(response.status).toBe(500);
      expect(response.body.success).toBe(false);
      expect(response.body.message).toBe('Lỗi kết nối cơ sở dữ liệu giả lập');

      spy.mockRestore();
    });
  });

  // ==========================================
  // NHÓM KIỂM THỬ ĐĂNG NHẬP (LOGIN)
  // ==========================================
  describe('POST /api/auth/login', () => {
    beforeEach(async () => {
      // Chuẩn bị tài khoản mẫu trong cơ sở dữ liệu
      await authService.registerUser('admin_quantri', 'AdminSecret2026@', 'admin');
    });

    // Test 3: Login thành công
    test('3. Đăng nhập thành công với thông tin chính xác', async () => {
      const response = await request(app)
        .post('/api/auth/login')
        .send({
          username: '  admin_quantri  ', // Có khoảng trắng để kiểm tra trim()
          password: 'AdminSecret2026@'
        });

      expect(response.status).toBe(200);
      expect(response.body.success).toBe(true);
      expect(response.body.message).toBe('Đăng nhập thành công');
      expect(response.body.data.username).toBe('admin_quantri');
      expect(response.body.data.role).toBe('admin');
      expect(response.body.data.passwordHash).toBeUndefined();
    });

    // Test 4: Sai mật khẩu
    test('4. Đăng nhập thất bại khi nhập sai mật khẩu', async () => {
      const response = await request(app)
        .post('/api/auth/login')
        .send({
          username: 'admin_quantri',
          password: 'WrongPassword999!'
        });

      expect(response.status).toBe(401);
      expect(response.body.success).toBe(false);
      expect(response.body.message).toMatch(/không chính xác/i);
    });

    // Test bổ sung: Đăng nhập với tài khoản không tồn tại
    test('Bonus: Đăng nhập thất bại khi username không tồn tại trong hệ thống', async () => {
      const response = await request(app)
        .post('/api/auth/login')
        .send({
          username: 'user_khong_ton_tai',
          password: 'SomePassword123'
        });

      expect(response.status).toBe(401);
      expect(response.body.success).toBe(false);
      expect(response.body.message).toMatch(/không chính xác/i);
    });

    // Test bổ sung: Thiếu trường khi đăng nhập hoặc sai kiểu dữ liệu
    test('Bonus: Đăng nhập thất bại khi thiếu thông tin đăng nhập hoặc sai kiểu', async () => {
      const responseNoUsername = await request(app)
        .post('/api/auth/login')
        .send({ password: 'Password123' });
      expect(responseNoUsername.status).toBe(400);

      const responseNoPassword = await request(app)
        .post('/api/auth/login')
        .send({ username: 'admin_quantri' });
      expect(responseNoPassword.status).toBe(400);

      const responseEmptyUsername = await request(app)
        .post('/api/auth/login')
        .send({ username: '   ', password: 'Password123' });
      expect(responseEmptyUsername.status).toBe(400);

      const responseWrongUserType = await request(app)
        .post('/api/auth/login')
        .send({ username: 12345, password: 'Password123' });
      expect(responseWrongUserType.status).toBe(400);

      const responseWrongPassType = await request(app)
        .post('/api/auth/login')
        .send({ username: 'admin_quantri', password: 12345 });
      expect(responseWrongPassType.status).toBe(400);
    });

    // Test xử lý lỗi máy chủ 500 khi login gặp sự cố bất ngờ
    test('Bonus: Xử lý lỗi hệ thống 500 khi login gặp sự cố bất ngờ', async () => {
      const spy = jest.spyOn(authService, 'loginUser').mockImplementationOnce(() => {
        throw new Error('Lỗi máy chủ nội bộ khi login');
      });

      const response = await request(app)
        .post('/api/auth/login')
        .send({
          username: 'admin_quantri',
          password: 'AdminSecret2026@'
        });

      expect(response.status).toBe(500);
      expect(response.body.success).toBe(false);
      expect(response.body.message).toBe('Lỗi máy chủ nội bộ khi login');

      spy.mockRestore();
    });
  });
});

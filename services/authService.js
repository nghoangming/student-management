/**
 * authService.js
 * Service xử lý logic xác thực và quản lý tài khoản cho Hệ thống Quản lý Học sinh
 * Nhóm: VQMN - Lớp TH CNPM Nhóm 2 Tổ 1
 * Tuân thủ Coding Standards: camelCase cho biến, verbMeaning cho hàm, UPPER_SNAKE_CASE cho hằng số.
 */

const bcrypt = require('bcryptjs');

// Các hằng số cấu hình nghiệp vụ (UPPER_SNAKE_CASE)
const MIN_PASSWORD_LENGTH = 8;
const BCRYPT_SALT_ROUNDS = 10;
const DEFAULT_USER_ROLE = 'student';

// Mock database lưu trữ người dùng trong bộ nhớ
let usersDatabase = [];

/**
 * Lớp lỗi tùy biến kèm theo mã trạng thái HTTP
 */
class AppError extends Error {
  constructor(message, statusCode) {
    super(message);
    this.statusCode = statusCode;
    this.name = 'AppError';
  }
}

/**
 * Đăng ký tài khoản người dùng mới
 * @param {string} rawUsername - Tên tài khoản người dùng
 * @param {string} rawPassword - Mật khẩu thô chưa băm
 * @param {string} [role] - Quyền hạn (admin, teacher, student)
 * @returns {Promise<Object>} Thông tin người dùng đã tạo (không chứa passwordHash)
 */
async function registerUser(rawUsername, rawPassword, role = DEFAULT_USER_ROLE) {
  // 1. Kiểm tra đầu vào tồn tại và đúng kiểu dữ liệu
  if (rawUsername === undefined || rawUsername === null || typeof rawUsername !== 'string') {
    throw new AppError('Tên đăng nhập không được để trống và phải là chuỗi ký tự', 400);
  }

  if (rawPassword === undefined || rawPassword === null || typeof rawPassword !== 'string') {
    throw new AppError('Mật khẩu không được để trống và phải là chuỗi ký tự', 400);
  }

  // 2. Chuẩn hóa dữ liệu đầu vào: trim khoảng trắng và chuyển về chữ thường
  const sanitizedUsername = rawUsername.trim().toLowerCase();

  if (sanitizedUsername.length === 0) {
    throw new AppError('Tên đăng nhập không được chỉ chứa khoảng trắng', 400);
  }

  // 3. Kiểm tra độ dài mật khẩu tối thiểu (>= 8 ký tự)
  if (rawPassword.length < MIN_PASSWORD_LENGTH) {
    throw new AppError(`Mật khẩu phải có độ dài tối thiểu ${MIN_PASSWORD_LENGTH} ký tự`, 400);
  }

  // 4. Kiểm tra tính duy nhất của username
  const isUsernameTaken = usersDatabase.some((user) => user.username === sanitizedUsername);
  if (isUsernameTaken) {
    throw new AppError('Tên đăng nhập đã tồn tại trong hệ thống', 409);
  }

  // 5. Băm mật khẩu an toàn bằng bcryptjs
  const generatedSalt = await bcrypt.genSalt(BCRYPT_SALT_ROUNDS);
  const passwordHash = await bcrypt.hash(rawPassword, generatedSalt);

  // 6. Lưu trữ người dùng vào cơ sở dữ liệu
  const newUser = {
    id: usersDatabase.length + 1,
    username: sanitizedUsername,
    passwordHash: passwordHash,
    role: role || DEFAULT_USER_ROLE,
    createdAt: new Date().toISOString()
  };

  usersDatabase.push(newUser);

  // 7. Bảo mật: Không bao giờ trả về passwordHash cho phía client
  return {
    id: newUser.id,
    username: newUser.username,
    role: newUser.role,
    createdAt: newUser.createdAt
  };
}

/**
 * Đăng nhập vào hệ thống
 * @param {string} rawUsername - Tên tài khoản
 * @param {string} rawPassword - Mật khẩu
 * @returns {Promise<Object>} Thông tin phiên đăng nhập người dùng (không chứa passwordHash)
 */
async function loginUser(rawUsername, rawPassword) {
  // 1. Kiểm tra đầu vào
  if (rawUsername === undefined || rawUsername === null || typeof rawUsername !== 'string') {
    throw new AppError('Vui lòng cung cấp tên đăng nhập hợp lệ', 400);
  }

  if (rawPassword === undefined || rawPassword === null || typeof rawPassword !== 'string') {
    throw new AppError('Vui lòng cung cấp mật khẩu hợp lệ', 400);
  }

  const sanitizedUsername = rawUsername.trim().toLowerCase();
  if (sanitizedUsername.length === 0) {
    throw new AppError('Tên đăng nhập không được để trống', 400);
  }

  // 2. Tìm người dùng trong cơ sở dữ liệu
  const existingUser = usersDatabase.find((user) => user.username === sanitizedUsername);

  // 3. Bảo mật: Trả về thông báo lỗi chung để tránh tấn công User Enumeration
  if (!existingUser) {
    throw new AppError('Tên đăng nhập hoặc mật khẩu không chính xác', 401);
  }

  // 4. So sánh mật khẩu bằng bcrypt.compare
  const isPasswordValid = await bcrypt.compare(rawPassword, existingUser.passwordHash);
  if (!isPasswordValid) {
    throw new AppError('Tên đăng nhập hoặc mật khẩu không chính xác', 401);
  }

  // 5. Trả về thông tin người dùng an toàn (không chứa passwordHash)
  return {
    id: existingUser.id,
    username: existingUser.username,
    role: existingUser.role
  };
}

/**
 * Đặt lại cơ sở dữ liệu về trạng thái ban đầu (phục vụ kiểm thử tự động)
 */
function resetUsers() {
  usersDatabase = [];
}

/**
 * Lấy danh sách người dùng hiện tại (phục vụ debug/test kiểm tra nội bộ)
 */
function getAllUsers() {
  return [...usersDatabase];
}

module.exports = {
  AppError,
  registerUser,
  loginUser,
  resetUsers,
  getAllUsers,
  MIN_PASSWORD_LENGTH,
  BCRYPT_SALT_ROUNDS
};

/**
 * app.js
 * Định cấu hình ứng dụng Express và định tuyến API cho Module Xác thực (Hệ thống Quản lý Học sinh)
 * Nhóm: VQMN - Lớp TH CNPM Nhóm 2 Tổ 1
 */

const express = require('express');
const path = require('path');
const authService = require('./services/authService');

const app = express();

// Middleware phân tích dữ liệu JSON trong body request
app.use(express.json());

// Middleware phục vụ giao diện web kiểm thử trực quan trong thư mục public
app.use(express.static(path.join(__dirname, 'public')));

/**
 * Route: POST /api/auth/register
 * Mô tả: Đăng ký tài khoản mới cho học sinh / giáo viên / quản trị viên
 */
app.post('/api/auth/register', async (req, res) => {
  try {
    const { username, password, role } = req.body;
    const registeredUser = await authService.registerUser(username, password, role);

    return res.status(201).json({
      success: true,
      message: 'Đăng ký tài khoản thành công',
      data: registeredUser
    });
  } catch (error) {
    const statusCode = error.statusCode || 500;
    return res.status(statusCode).json({
      success: false,
      message: error.message || 'Lỗi hệ thống nội bộ'
    });
  }
});

/**
 * Route: POST /api/auth/login
 * Mô tả: Đăng nhập vào hệ thống quản lý học sinh
 */
app.post('/api/auth/login', async (req, res) => {
  try {
    const { username, password } = req.body;
    const authenticatedUser = await authService.loginUser(username, password);

    return res.status(200).json({
      success: true,
      message: 'Đăng nhập thành công',
      data: authenticatedUser
    });
  } catch (error) {
    const statusCode = error.statusCode || 500;
    return res.status(statusCode).json({
      success: false,
      message: error.message || 'Lỗi hệ thống nội bộ'
    });
  }
});

// Xuất app để phục vụ kiểm thử đơn vị và tích hợp (Supertest)
module.exports = app;

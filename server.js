/**
 * server.js
 * Entrypoint khởi chạy máy chủ Express cho Hệ thống Quản lý Học sinh (Lab 08)
 * Nhóm: VQMN - Lớp TH CNPM Nhóm 2 Tổ 1
 */

const app = require('./app');

const DEFAULT_SERVER_PORT = 3000;
const SERVER_PORT = process.env.PORT || DEFAULT_SERVER_PORT;

app.listen(SERVER_PORT, () => {
  console.log(`[INFO] Server đang chạy tại: http://localhost:${SERVER_PORT}`);
  console.log(`[INFO] API Register: POST http://localhost:${SERVER_PORT}/api/auth/register`);
  console.log(`[INFO] API Login:    POST http://localhost:${SERVER_PORT}/api/auth/login`);
});

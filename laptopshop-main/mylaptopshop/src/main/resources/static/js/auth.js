const token = localStorage.getItem('authToken');
const loginRegisterSection = document.getElementById('login-register');
const userInfoSection = document.getElementById('user-info');
const usernameSpan = document.getElementById('username');

if (token) {
    // Nếu đã đăng nhập, ẩn phần đăng nhập/đăng ký và hiển thị phần thông tin người dùng
    loginRegisterSection.style.display = 'none';
    userInfoSection.style.display = 'block';
} else {
    // Nếu chưa đăng nhập, hiển thị phần đăng nhập và đăng ký
    loginRegisterSection.style.display = 'block';
    userInfoSection.style.display = 'none';
}

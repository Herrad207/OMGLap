// Ẩn/hiện mật khẩu
function togglePassword() {
    const passwordField = document.getElementById('password');
    const type = passwordField.type === 'password' ? 'text' : 'password';
    passwordField.type = type;
}
// Xử lý sự kiện đăng nhập
document.getElementById('login-button').addEventListener('click', async function () {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();
    const errorMessage = document.getElementById('error-message');

    errorMessage.style.display = 'none';
    errorMessage.innerText = '';

    if (!username || !password) {
        errorMessage.style.display = 'block';
        errorMessage.innerText = 'Vui lòng nhập đầy đủ email và mật khẩu.';
        return;
    }

    try {
        const response = await fetch('http://localhost:9193/api/v1/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email: username, password: password }),
        });

        if (!response.ok) {
            throw new Error('Tài khoản hoặc mật khẩu không chính xác.');
        }

        const data = await response.json();
        localStorage.setItem('authToken', data.data.token);
        localStorage.setItem('userId', data.data.id);
        // localStorage.setItem('userData', data.data);

        window.location.href = 'mainpage.html';
    } catch (error) {
        errorMessage.style.display = 'block';
        errorMessage.innerText = error.message;
    }
});


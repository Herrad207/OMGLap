document.getElementById('registrationForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const formData = new FormData(this);
    const formObject = {};
    formData.forEach((value, key) => formObject[key] = value);

    const jsonData = JSON.stringify(formObject);

    fetch('http://localhost:9193/api/v1/users/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: jsonData,
    })
        .then((response) => {
            if (!response.ok) {
                return response.json().then((error) => {
                    throw new Error(error.message || 'Đăng ký thất bại!');
                });
            }
            return response.json();
        })
        .then((data) => {
            console.log('User created:', data);

            const loginData = {
                email: formObject.email,
                password: formObject.password,
            };

            return fetch('http://localhost:9193/api/v1/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(loginData),
            });
        })
        .then((loginResponse) => {
            if (!loginResponse.ok) {
                return loginResponse.json().then((error) => {
                    throw new Error(error.message || 'Đăng nhập thất bại!');
                });
            }
            return loginResponse.json();
        })
        .then((loginData) => {
            console.log('Login successful:', loginData);

            // Lưu authToken và thông tin người dùng vào localStorage
            localStorage.setItem('authToken', loginData.data.token);
            localStorage.setItem('userId', loginData.data.id);
            alert('Đăng ký và đăng nhập thành công!');
            window.location.href = 'mainpage.html'; // Chuyển hướng đến trang chủ
        })
        .catch((error) => {
            console.error('Error:', error.message);
            alert(error.message);
        });
});

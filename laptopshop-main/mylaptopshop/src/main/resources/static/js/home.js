document.addEventListener("DOMContentLoaded", async function () {
    const token = localStorage.getItem('authToken');
    const userId = localStorage.getItem('userId');
    const userInfo = document.getElementById('user-info');
    const usernameSpan = document.getElementById('username');
    const logoutButton = document.getElementById('logout-link');
    
    if (userInfo && usernameSpan) {
        if (token) {
            if (userId) {
                try {
                    const response = await fetch(`http://localhost:9193/api/v1/users/${userId}/user`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${token}`
                        },
                    });

                    if (!response.ok) {
                        throw new Error('Không thể lấy thông tin người dùng.');
                    }

                    const responseData = await response.json();
                    const firstName = responseData.firstName; 
                    userInfo.style.display = 'block';
                    usernameSpan.textContent = firstName;
                } catch (error) {
                    console.error('Lỗi khi lấy thông tin người dùng:', error);
                }
            }

            if (logoutButton) {
                logoutButton.addEventListener('click', function () {
                    localStorage.removeItem('authToken');
                    localStorage.removeItem('userId');
                    location.reload();
                });
            }
        } else {
            userInfo.style.display = 'none';
        }
    }
});

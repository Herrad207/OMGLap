document.addEventListener("DOMContentLoaded", function () {
    // Lấy token và userId từ localStorage
    const token = localStorage.getItem('authToken');
    const userId = localStorage.getItem('userId');
    const userApiEndpoint = `http://localhost:9193/api/v1/users/${userId}/user`;

    // Các phần tử HTML
    const changePasswordButton = document.getElementById("change-password-button");
    const changePasswordModal = document.getElementById("changePasswordModal");
    const closePasswordModal = document.getElementById("closePasswordModal");
    const changePasswordForm = document.getElementById("changePasswordForm");
    const errorMessage = document.getElementById("error-message");

    // Hiển thị thông báo lỗi
    function showError(message) {
        errorMessage.style.display = "block";
        errorMessage.innerText = message;
    }

    // Ẩn thông báo lỗi
    function hideError() {
        errorMessage.style.display = "none";
        errorMessage.innerText = "";
    }

    // Lấy dữ liệu người dùng từ API
    async function fetchUserData() {
        try {
            const response = await fetch(userApiEndpoint, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const userData = await response.json();
            updateUserProfile(userData);
            // Lưu thông tin người dùng vào localStorage
            localStorage.setItem('firstName', userData.firstName);
            localStorage.setItem('lastName', userData.lastName);
            localStorage.setItem('email', userData.email);
        } catch (error) {
            console.error("Error fetching user data:", error);
            showError("Không thể tải dữ liệu người dùng.");
        }
    }

    // Cập nhật thông tin người dùng trên giao diện
    function updateUserProfile(user) {
        if (!user) return;
        const firstNameElement = document.getElementById("firstName");
        const lastNameElement = document.getElementById("lastName");
        const emailElement = document.getElementById("email");
        const passwordElement = document.getElementById("password");
        const userIdElement = document.getElementById("userId");

        if (userIdElement) userIdElement.textContent = user.id;
        if (firstNameElement) firstNameElement.textContent = user.firstName;
        if (lastNameElement) lastNameElement.textContent = user.lastName;
        if (emailElement) emailElement.textContent = user.email;
        if (passwordElement) passwordElement.textContent = "******"; // Ẩn mật khẩu
    }

    // Hiển thị modal thay đổi mật khẩu
    changePasswordButton.addEventListener("click", function () {
        changePasswordModal.style.display = "block";
        hideError(); // Ẩn thông báo lỗi khi mở modal
    });

    // Đóng modal thay đổi mật khẩu
    closePasswordModal.addEventListener("click", function () {
        changePasswordModal.style.display = "none";
    });

    // Đóng modal khi click ra ngoài
    window.addEventListener("click", function (event) {
        if (event.target === changePasswordModal) {
            changePasswordModal.style.display = "none";
        }
    });

    // Xử lý thay đổi mật khẩu
    changePasswordForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const newPassword = document.getElementById("newPassword").value.trim();
        const repeatPassword = document.getElementById("repeatPassword").value.trim();

        // Kiểm tra trường nhập mật khẩu
        if (!newPassword || !repeatPassword) {
            showError("Vui lòng nhập đầy đủ mật khẩu mới và xác nhận mật khẩu.");
            return;
        }

        // Kiểm tra mật khẩu có khớp không
        if (newPassword !== repeatPassword) {
            showError("Mật khẩu mới và mật khẩu xác nhận không khớp.");
            return;
        }

        const email = document.getElementById("email").innerText.trim(); // Lấy email từ giao diện

        // Gửi yêu cầu thay đổi mật khẩu
        fetch(`http://localhost:9193/api/v1/forgotpassword/changePassword/${email}`, {
            method: "POST",
            body: JSON.stringify({
                password: newPassword,
                repeatPassword: repeatPassword
            }),
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (!response.ok) {
                    return response.text().then((message) => {
                        throw new Error(message || "Không thể thay đổi mật khẩu.");
                    });
                }
                return response.text();
            })
            .then((message) => {
                alert(message); // Hiển thị thông báo thành công
                changePasswordModal.style.display = "none"; // Đóng modal
            })
            .catch((error) => {
                showError(error.message); // Hiển thị thông báo lỗi
            });
    });

    // Khởi chạy: lấy dữ liệu người dùng
    fetchUserData();

    // Edit Profile Logic
    const editProfileBtn = document.getElementById('editProfileBtn');
    const modal = document.getElementById('editProfileModal');
    const closeModal = document.getElementById('closeModal');
    const editProfileForm = document.getElementById('editProfileForm');

    // Khi nhấn nút chỉnh sửa thông tin
    editProfileBtn.addEventListener('click', function() {
        modal.style.display = 'block';
        // Lấy thông tin hiện tại và hiển thị trong form chỉnh sửa
        document.getElementById('editFirstName').value = document.getElementById('firstName').innerText;
        document.getElementById('editLastName').value = document.getElementById('lastName').innerText;
    });

    // Đóng modal khi nhấn nút đóng
    closeModal.addEventListener('click', function() {
        modal.style.display = 'none';
    });

    // Xử lý khi người dùng gửi form chỉnh sửa
    editProfileForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const firstName = document.getElementById('editFirstName').value;
        const lastName = document.getElementById('editLastName').value;
        
        const updateUserRequest = {
            firstName: firstName,
            lastName: lastName
        };

        // Gửi yêu cầu PUT để cập nhật thông tin
        fetch(`http://localhost:9193/api/v1/users/${userId}/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(updateUserRequest)
        })
        .then(response => response.json())
        .then(data => {
            if (data.message === "Success Update User!") {  // Kiểm tra thông báo thành công
                // Cập nhật lại giao diện
                document.getElementById('firstName').innerText = data.data.firstName;
                document.getElementById('lastName').innerText = data.data.lastName;

                // Lưu thông tin người dùng đã cập nhật vào localStorage
                localStorage.setItem('firstName', data.data.firstName);
                localStorage.setItem('lastName', data.data.lastName);

                // Đóng modal
                modal.style.display = 'none';
            } else {
                alert('Cập nhật thất bại');
            }
        })
        .catch(error => {
            console.error('Có lỗi xảy ra:', error);
            alert('Lỗi khi cập nhật thông tin');
        });
    });

    // Đóng modal nếu người dùng click ngoài modal
    window.addEventListener('click', function(event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });

    // Khi trang được tải lại, lấy dữ liệu từ localStorage và cập nhật giao diện
    const savedFirstName = localStorage.getItem('firstName');
    const savedLastName = localStorage.getItem('lastName');
    
    if (savedFirstName && savedLastName) {
        document.getElementById('firstName').innerText = savedFirstName;
        document.getElementById('lastName').innerText = savedLastName;
    }
});

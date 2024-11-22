document.addEventListener("DOMContentLoaded", function () {
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

    // Mở modal thay đổi mật khẩu
    changePasswordButton.addEventListener("click", function () {
        changePasswordModal.style.display = "block";
        hideError(); // Ẩn thông báo lỗi khi mở modal
    });

    // Đóng modal khi click vào nút đóng
    closePasswordModal.addEventListener("click", function () {
        changePasswordModal.style.display = "none";
    });

    // Đóng modal khi click ra ngoài modal
    window.addEventListener("click", function (event) {
        if (event.target === changePasswordModal) {
            changePasswordModal.style.display = "none";
        }
    });

    // Xử lý sự kiện khi thay đổi mật khẩu
    changePasswordForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const newPassword = document.getElementById("newPassword").value.trim();
        const repeatPassword = document.getElementById("repeatPassword").value.trim();

        // Kiểm tra trường mật khẩu
        if (!newPassword || !repeatPassword) {
            showError("Vui lòng nhập đầy đủ mật khẩu mới và xác nhận mật khẩu.");
            return;
        }

        // Kiểm tra mật khẩu có khớp không
        if (newPassword !== repeatPassword) {
            showError("Mật khẩu mới và mật khẩu xác nhận không khớp.");
            return;
        }

        const email = document.getElementById("email").innerText.trim(); // Lấy email từ trang

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
                showError(error.message);  // Hiển thị thông báo lỗi
            });
    });
});

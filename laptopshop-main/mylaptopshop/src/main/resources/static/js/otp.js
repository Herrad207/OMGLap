document.addEventListener("DOMContentLoaded", function () {
    const emailForm = document.getElementById("email-form");
    const otpForm = document.getElementById("otp-form");
    const changePasswordForm = document.getElementById("change-password-form");
    const resetPasswordButton = document.getElementById("reset-password-button");
    const verifyOtpButton = document.getElementById("verify-otp-button");
    const changePasswordButton = document.getElementById("change-password-button");
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

    // Xử lý sự kiện Gửi yêu cầu OTP
    resetPasswordButton.addEventListener("click", function (event) {
        event.preventDefault();

        const email = document.getElementById("email").value.trim();

        if (!email) {
            showError("Vui lòng nhập email của bạn.");
            return;
        }

        fetch(`http://localhost:9193/api/v1/forgotpassword/verifyMail/${email}`, {
            method: "POST",
        }).then((response) => {
                if (!response.ok) {
                    return response.text().then((message) => {
                        throw new Error(message || "Không thể gửi OTP. Vui lòng thử lại sau.");
                    });
                }
                return response.text();
            }).then((message) => {
                alert(message);
                emailForm.style.display = "none";
                otpForm.style.display = "block";
                verifyOtpButton.style.display = "inline-block";
                resetPasswordButton.style.display = "none";
                hideError();
            })
            .catch((error) => {
                showError(error.message);
            });
    });

    // Xử lý sự kiện Xác nhận OTP
    verifyOtpButton.addEventListener("click", function (event) {
        event.preventDefault();

        const otp = document.getElementById("otp").value.trim();
        const email = document.getElementById("email").value.trim();

        if (!otp) {
            showError("Vui lòng nhập mã OTP.");
            return;
        }

        fetch(`http://localhost:9193/api/v1/forgotpassword/verifyOtp/${otp}/${email}`, {
            method: "POST",
        })
            .then((response) => {
                if (!response.ok) {
                    return response.text().then((message) => {
                        throw new Error(message || "OTP không hợp lệ hoặc đã hết hạn.");
                    });
                }
                return response.text();
            })
            .then((message) => {
                alert(message);
                otpForm.style.display = "none";
                verifyOtpButton.style.display = "none";
                changePasswordForm.style.display = "block";
                changePasswordButton.style.display = "inline-block";
            })
            .catch((error) => {
                showError(error.message);
            });
    });

    // Xử lý sự kiện Thay đổi mật khẩu
    changePasswordButton.addEventListener("click", function (event) {
        event.preventDefault();

        const newPassword = document.getElementById("new-password").value.trim();
        const repeatPassword = document.getElementById("repeat-password").value.trim();

        if (!newPassword || !repeatPassword) {
            showError("Vui lòng nhập đầy đủ mật khẩu mới và xác nhận mật khẩu.");
            return;
        }

        if (newPassword !== repeatPassword) {
            showError("Mật khẩu mới và mật khẩu xác nhận không khớp.");
            return;
        }

        const email = document.getElementById("email").value.trim();

        fetch(`http://localhost:9193/api/v1/forgotpassword/changePassword/${email}`, {
            method: "POST",
            body: JSON.stringify({
                password: newPassword, repeatPassword : repeatPassword
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
                alert(message);  // Hiển thị thông báo thành công
                // Điều hướng về trang đăng nhập hoặc trang chủ
                window.location.href = 'login.html';
            })
            .catch((error) => {
                showError(error.message);  // Hiển thị thông báo lỗi
            });
    });
});

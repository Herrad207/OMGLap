document.addEventListener('DOMContentLoaded', function() {
    const editProfileBtn = document.getElementById('editProfileBtn');
    const modal = document.getElementById('editProfileModal');
    const closeModal = document.getElementById('closeModal');
    const editProfileForm = document.getElementById('editProfileForm');
    const token = localStorage.getItem('authToken');
    const userId = localStorage.getItem('userId');

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

    // Xử lý khi người dùng gửi form
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

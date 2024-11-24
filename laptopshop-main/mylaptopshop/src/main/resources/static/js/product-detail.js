document.addEventListener("DOMContentLoaded", function () {
    const productId = new URLSearchParams(window.location.search).get("id");
    const apiUrl = `http://localhost:9193/api/v1/products/${productId}`;
    const addToCartUrl = "http://localhost:9193/api/v1/cartItems/item/add";
    const detailContainer = document.getElementById("productDetails");
    const token = localStorage.getItem("authToken");

    // Hàm hiển thị thông báo
    const showNotification = (message, isSuccess = true) => {
        const existingNotification = document.querySelector(".notification");
        if (existingNotification) {
            existingNotification.remove();
        }

        const notification = document.createElement("div");
        notification.className = `notification ${isSuccess ? "success" : "error"}`;
        notification.textContent = message;

        document.body.appendChild(notification);
        setTimeout(() => {
            notification.remove();
        }, 3000);
    };

    // Hàm thêm sản phẩm vào giỏ hàng
    const addItemToCart = async (productId, quantity = 1) => {
        if (!token) {
            showNotification("Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng!", false);
            return;
        }

        try {
            const params = new URLSearchParams();
            params.append('productId', productId);
            params.append('quantity', quantity);

            const response = await fetch(addToCartUrl, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "Authorization": `Bearer ${token}`,
                },
                body: params.toString(),
            });

            if (response.ok) {
                const result = await response.json();
                showNotification(result.message || "Đã thêm vào giỏ hàng!");
            } else {
                const error = await response.json();
                showNotification(error.message || "Không thể thêm vào giỏ hàng!", false);
            }
        } catch (error) {
            showNotification("Có lỗi xảy ra, vui lòng thử lại!", false);
        }
    };

    // Kiểm tra nếu không có productId
    if (!productId) {
        detailContainer.innerHTML = `<p>ID sản phẩm không hợp lệ.</p>`;
        return;
    }

    // Fetch dữ liệu sản phẩm từ API
    fetch(apiUrl)
        .then((response) => {
            if (!response.ok) {
                throw new Error("Sản phẩm không tồn tại hoặc có lỗi xảy ra");
            }
            return response.json();
        })
        .then((product) => {
            detailContainer.innerHTML = `
                <h2>${product.name}</h2>
                <p><strong>Hãng:</strong> ${product.brand}</p>
                <p><strong>Giá:</strong> ${product.price} VNĐ</p>
                <p><strong>Mô tả:</strong> ${product.description}</p>
                <p><strong>Số lượng:</strong> ${product.quantity}</p>
                <div>
                    ${product.image.map(
                        (img) => `<img src="http://localhost:9193${img.downloadUrl}" 
                                   alt="${img.fileName}" style="width: 200px; height: auto;">`
                    ).join("")}
                </div>
                <button id="addToCartBtn" class="add-to-cart-btn">
                    <i class="fas fa-cart-plus"></i> Thêm vào giỏ hàng
                </button>
                <button onclick="goBack()" class="go-back-btn">
                    Quay lại
                </button>
            `;

            // Gán sự kiện click cho nút "Thêm vào giỏ hàng"
            const addToCartBtn = document.getElementById("addToCartBtn");
            addToCartBtn.addEventListener("click", () => {
                addItemToCart(productId, 1);
            });
        })
        .catch((error) => {
            detailContainer.innerHTML = `<p>Lỗi: ${error.message}</p>`;
        });
});

// Hàm quay lại danh sách
function goBack() {
    window.history.back();
}

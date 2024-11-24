document.addEventListener("DOMContentLoaded", function () {
    const apiUrl = "http://localhost:9193/api/v1/products/all";
    const addToCartUrl = "http://localhost:9193/api/v1/cartItems/item/add";
    const baseUrl = "http://localhost:9193";
    const productGrid = document.getElementById("productGrid");
    const token = localStorage.getItem("authToken");
    const userId = localStorage.getItem('userId');

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

    const addItemToCart = async (productId, quantity = 1) => {
        // Kiểm tra token trong localStorage
        const token = localStorage.getItem("authToken");
    
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
                    "Content-Type": "application/x-www-form-urlencoded",  // Changed to match your controller
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

    fetch(apiUrl)
        .then((response) => response.json())
        .then((data) => {
            data.forEach((product) => {
                // Tạo khung sản phẩm
                const productCard = document.createElement("div");
                productCard.className = "product-card";

                // Ảnh sản phẩm
                const productImage = product.image && product.image.length > 0
                    ? `${baseUrl}${product.image[0].downloadUrl}`
                    : "/default.jpg";

                // Tạo nội dung bên trong khung
                productCard.innerHTML = `
                    <img src="${productImage}" 
                         alt="${product.image[0]?.fileName || "Hình sản phẩm"}" 
                         class="product-image">
                    <div class="product-info">
                        <h2 class="product-name">
                            <a href="product-detail.html?id=${product.id}">
                                ${product.name}
                            </a>
                        </h2>
                        <p class="product-brand">Hãng: ${product.brand}</p>
                        <p class="product-price">Giá: ${product.price} VND</p>
                        <p class="product-description">${product.description}</p>
                        <p class="product-quantity">Số lượng: ${product.quantity}</p>
                    </div>
                    <button class="add-to-cart-btn">
                        <i class="fas fa-cart-plus"></i> Thêm vào giỏ
                    </button>
                `;
                productCard.querySelector(".add-to-cart-btn").addEventListener("click", () => {
                    addItemToCart(product.id, 1);
                });

                productGrid.appendChild(productCard);
            });
        })
        .catch((error) => {
            console.error("Error while calling API:", error);
        });
});

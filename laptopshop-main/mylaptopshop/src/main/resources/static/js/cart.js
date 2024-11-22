document.addEventListener("DOMContentLoaded", () => {
    const cartContainer = document.getElementById("cart-container");
    const cartId = 1; // Thay giá trị ID giỏ hàng của bạn
    const token = localStorage.getItem('authToken');
    const userId = localStorage.getItem('userId');

    // Hàm fetch giỏ hàng từ API
    const fetchCart = async () => {
        try {
            const response = await fetch(`http://localhost:9193/api/v1/carts/${cartId}/my-cart`, {
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': `Bearer ${token}`
                },
            });
            const result = await response.json();

            if (response.ok) {
                renderCart(result.data);
            } else {
                renderError(result.message || "Không thể tải giỏ hàng.");
            }
        } catch (error) {
            renderError("Có lỗi xảy ra khi tải giỏ hàng.");
        }
    };

    // Hàm hiển thị giỏ hàng
    const renderCart = (cart) => {
        cartContainer.innerHTML = `
            <h2>Chi tiết giỏ hàng</h2>
            <p><strong>Mã giỏ hàng:</strong> ${cart.id}</p>
            <p><strong>Số lượng sản phẩm:</strong> ${cart.items.length}</p>
            <p><strong>Tổng giá trị:</strong> ${cart.totalPrice} VND</p>
            <ul>
                ${cart.items
                    .map(
                        (item) => `
                    <li>
                        <p><strong>Sản phẩm:</strong> ${item.productName}</p>
                        <p><strong>Số lượng:</strong> ${item.quantity}</p>
                        <p><strong>Giá:</strong> ${item.price} VND</p>
                    </li>
                `
                    )
                    .join("")}
            </ul>
        `;
    };

    // Hàm hiển thị lỗi
    const renderError = (message) => {
        cartContainer.innerHTML = `
            <p style="color: red;">${message}</p>
        `;
    };

    // Gọi hàm fetchCart để tải dữ liệu khi trang load
    fetchCart();
});

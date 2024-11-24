document.addEventListener("DOMContentLoaded", () => {
    const cartContainer = document.getElementById("cart-container");
    const productsContainer = document.getElementById("products");
    const cartIdElement = document.getElementById("cart-id");
    const totalItemsElement = document.getElementById("total-items");
    const totalPriceElement = document.getElementById("total-price");
    const errorMessage = document.getElementById("error-message");

    const token = localStorage.getItem("authToken");
    const userId = localStorage.getItem("userId");

    if (!userId || !token) {
        renderError("Vui lòng đăng nhập để xem giỏ hàng.");
        return;
    }

    // Hàm lấy cartId theo userId
    const fetchCartId = async () => {
        try {
            const response = await fetch(`http://localhost:9193/api/v1/carts/${userId}/get-cart-id`, {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                const cartId = await response.json();
                cartIdElement.textContent = cartId; // Cập nhật cartId lên giao diện
                fetchCart(cartId); // Gọi API lấy giỏ hàng
            } else {
                renderError("Không thể lấy mã giỏ hàng.");
            }
        } catch (error) {
            console.error("Error fetching cart ID:", error);
            renderError("Có lỗi xảy ra khi lấy mã giỏ hàng.");
        }
    };

    // Hàm lấy thông tin giỏ hàng
    const fetchCart = async (cartId) => {
        try {
            const response = await fetch(`http://localhost:9193/api/v1/carts/${cartId}/my-cart`, {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            });

            const result = await response.json();

            if (response.ok) {
                renderCart(result.data);
            } else {
                renderError(result.message || "Không thể tải giỏ hàng.");
            }
        } catch (error) {
            console.error("Error fetching cart:", error);
            renderError("Có lỗi xảy ra khi tải giỏ hàng.");
        }
    };

    // Hàm hiển thị giỏ hàng
    const renderCart = (cart) => {
        cartIdElement.textContent = cart.cartId;
        totalItemsElement.textContent = cart.items.length;
        totalPriceElement.textContent = `${cart.totalAmount.toLocaleString()} VND`;

        if (cart.items.length === 0) {
            cartContainer.innerHTML = "<p>Giỏ hàng của bạn đang trống.</p>";
            document.getElementById("checkout-button").disabled = true;
            return;
        }

        const sortedItems = cart.items.sort((a, b) => a.product.id - b.product.id); // Sắp xếp sản phẩm theo ID
        const cartItemsHtml = sortedItems
            .map((item) => {
                const product = item.product;
                const imageUrl =
                    product.image && product.image.length > 0
                        ? `http://localhost:9193${product.image[0].downloadUrl}`
                        : "https://via.placeholder.com/80";
                const totalPrice = (item.quantity * item.unitPrice).toLocaleString();

                return `
                <div class="cart-item" data-item-id="${item.itemId}" data-cart-id="${cart.cartId}" data-unit-price="${item.unitPrice}">
                    <img src="${imageUrl}" alt="${product.name}" class="cart-item-image">
                    <div class="cart-item-details">
                        <h3>${product.name}</h3>
                        <p><strong>Thương hiệu:</strong> ${product.brand}</p>
                        <p><strong>Mô tả:</strong> ${product.description}</p>
                        <div class="cart-item-quantity">
                            <button class="quantity-decrease">-</button>
                            <span class="quantity">${item.quantity}</span>
                            <button class="quantity-increase">+</button>
                        </div>
                        <p><strong>Đơn giá:</strong> ${item.unitPrice.toLocaleString()} VND</p>
                        <p><strong>Thành tiền:</strong> <span class="cart-item-total">${totalPrice} VND</span></p>
                        <button class="remove-item-button">Xóa</button>
                    </div>
                </div>
            `;
            })
            .join("");

        productsContainer.innerHTML = cartItemsHtml;

        const decreaseButtons = document.querySelectorAll(".quantity-decrease");
        const increaseButtons = document.querySelectorAll(".quantity-increase");
        const removeButtons = document.querySelectorAll(".remove-item-button");

        removeButtons.forEach((button) => button.addEventListener("click", handleRemoveItem));
        decreaseButtons.forEach((button) => button.addEventListener("click", handleQuantityChange));
        increaseButtons.forEach((button) => button.addEventListener("click", handleQuantityChange));
    };

    // Hàm xử lý thay đổi số lượng
    const handleQuantityChange = async (event) => {
        const cartItem = event.target.closest(".cart-item");
    
        // Kiểm tra cartItem
        if (!cartItem) {
            renderError("Không tìm thấy sản phẩm trong giỏ hàng.");
            return;
        }
    
        const quantityElement = cartItem.querySelector(".quantity");
        const totalPriceElement = cartItem.querySelector(".cart-item-total");
    
        // Kiểm tra sự tồn tại của các phần tử
        if (!quantityElement) {
            renderError("Không tìm thấy số lượng sản phẩm.");
            return;
        }
    
        if (!totalPriceElement) {
            renderError("Không tìm thấy phần hiển thị tổng tiền của sản phẩm.");
            return;
        }
    
        // Tiếp tục xử lý nếu các phần tử đều tồn tại
        let quantity = parseInt(quantityElement.textContent);
    
        if (event.target.classList.contains("quantity-decrease") && quantity > 1) {
            quantity--;
        } else if (event.target.classList.contains("quantity-increase")) {
            quantity++;
        }
    
        try {
            const response = await fetch(
                `http://localhost:9193/api/v1/cartItems/cart/${cartItem.getAttribute("data-cart-id")}/item/${cartItem.getAttribute("data-item-id")}/update?quantity=${quantity}`,
                {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${localStorage.getItem("authToken")}`,
                    },
                }
            );
    
            const result = await response.json();
    
            if (response.ok) {
                // Cập nhật số lượng và tổng tiền
                quantityElement.textContent = quantity;
                const unitPrice = parseFloat(cartItem.getAttribute("data-unit-price"));
                const newTotalPrice = (quantity * unitPrice).toLocaleString();
                totalPriceElement.innerHTML = `<span class="cart-item-total">${newTotalPrice} VND</span>`;
                updateCartTotals();
            } else {
                renderError(result.message || "Không thể cập nhật số lượng.");
            }
        } catch (error) {
            console.error("Error updating quantity:", error);
            renderError("Có lỗi xảy ra khi cập nhật số lượng.");
        }
    };

    const handleRemoveItem = async (event) => {
        const cartItem = event.target.closest(".cart-item");
        const cartId = cartItem.getAttribute("data-cart-id");
        const itemId = cartItem.getAttribute("data-item-id");

        try {
            const response = await fetch(`http://localhost:9193/api/v1/cartItems/cart/${cartId}/item/${itemId}/remove`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                cartItem.remove();
                updateCartTotals();

                if (productsContainer.children.length === 0) {
                    cartContainer.innerHTML = "<p>Giỏ hàng của bạn đang trống.</p>";
                }
            } else {
                const result = await response.json();
                renderError(result.message || "Không thể xóa sản phẩm.");
            }
        } catch (error) {
            console.error("Error removing item:", error);
            renderError("Có lỗi xảy ra khi xóa sản phẩm.");
        }
    };

    const updateCartTotals = () => {
        const allItems = document.querySelectorAll(".cart-item");
        let totalItems = 0;
        let totalAmount = 0;

        allItems.forEach((item) => {
            const itemQuantity = parseInt(item.querySelector(".quantity").textContent);
            const itemUnitPrice = parseFloat(item.getAttribute("data-unit-price"));
            totalItems += itemQuantity;
            totalAmount += itemQuantity * itemUnitPrice;
        });

        totalItemsElement.textContent = totalItems;
        totalPriceElement.textContent = `${totalAmount.toLocaleString()} VND`;
    };

    const renderError = (message) => {
        errorMessage.style.display = "block";
        errorMessage.textContent = message;
    };

    fetchCartId();
});

document.getElementById("checkout-button").addEventListener("click", async function () {
    const cartId = document.getElementById("cart-id").textContent;
    const token = localStorage.getItem("authToken");
    const userId = localStorage.getItem("userId");

    try {
        const createOrderResponse = await fetch(`http://localhost:9193/api/v1/orders/order?userId=${userId}`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        const orderResult = await createOrderResponse.json();

        if (!createOrderResponse.ok) {
            alert(orderResult.message || "Không thể tạo đơn hàng. Vui lòng thử lại.");
            return;
        }
        alert("Tạo đơn hàng thành công!");

        const clearCartResponse = await fetch(`http://localhost:9193/api/v1/carts/${cartId}/clear`, {
            method: "DELETE",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        const clearCartResult = await clearCartResponse.json();

        if (!clearCartResponse.ok) {
            alert(clearCartResult.message || "Không thể xóa giỏ hàng. Vui lòng thử lại.");
            return;
        }

        alert("Thanh toán thành công! Giỏ hàng đã được xóa.");
        window.location.href = "invoice.html";
    } catch (error) {
        console.error("Error during checkout process:", error);
        alert("Có lỗi xảy ra trong quá trình thanh toán. Vui lòng thử lại sau.");
    }
});

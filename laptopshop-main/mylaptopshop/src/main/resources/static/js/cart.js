document.addEventListener("DOMContentLoaded", () => {
    const cartContainer = document.getElementById("cart-container");
    const productsContainer = document.getElementById("products");
    const cartIdElement = document.getElementById("cart-id");
    const totalItemsElement = document.getElementById("total-items");
    const totalPriceElement = document.getElementById("total-price");
    const errorMessage = document.getElementById("error-message");
    const checkoutButton = document.getElementById("checkout-button");

    const token = localStorage.getItem("authToken");
    const userId = localStorage.getItem("userId");

    if (!userId || !token) {
        renderError("Vui lòng đăng nhập để xem giỏ hàng.");
        return;
    }

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
                cartIdElement.textContent = cartId;
                fetchCart(cartId);
            } else {
                renderError("Không thể lấy mã giỏ hàng.");
            }
        } catch (error) {
            console.error("Error fetching cart ID:", error);
            renderError("Có lỗi xảy ra khi lấy mã giỏ hàng.");
        }
    };

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

    const renderCart = (cart) => {
        cartIdElement.textContent = cart.cartId;
        const totalQuantity = cart.items.reduce((sum, item) => sum + item.quantity, 0);
        totalItemsElement.textContent = totalQuantity;
        totalPriceElement.textContent = `${cart.totalAmount.toLocaleString()} VND`;

        if (cart.items.length === 0) {
            cartContainer.innerHTML = "<p>Giỏ hàng của bạn đang trống.</p>";
            checkoutButton.disabled = true;
            return;
        }

        const sortedItems = cart.items.sort((a, b) => a.product.id - b.product.id);
        const cartItemsHtml = sortedItems
            .map((item) => {
                const product = item.product;
                const imageUrl =
                    product.image && product.image.length > 0
                        ? `http://localhost:9193${product.image[0].downloadUrl}`
                        : "../static/img/gf66.png";
                const totalPrice = (item.quantity * item.unitPrice).toLocaleString();

                return `
                <div class="cart-item" data-item-id="${item.itemId}" data-cart-id="${cart.cartId}" data-unit-price="${item.unitPrice}" data-available-quantity="${product.quantity}">
                    <img src="${imageUrl}" alt="${product.name}" class="cart-item-image">
                    <div class="cart-item-details">
                        <h3>${product.name}</h3>
                        <p><strong>Thương hiệu:</strong> ${product.brand}</p>
                        <p><strong>Mô tả:</strong> ${product.description}</p>
                        <p><strong>Số lượng:</strong>
                        <div class="cart-item-quantity">
                            <input type="number" class="quantity-input" value="${item.quantity}" min="1" max="${product.quantity}">
                        </div>
                        </p>
                        <p><strong>Đơn giá:</strong> ${item.unitPrice.toLocaleString()} VND</p>
                        <p><strong>Thành tiền:</strong> <span class="cart-item-total">${totalPrice} VND</span></p>
                    </div>
                    <div class="cart-product-actions">
                        <span class="remove-item-icon">X</span>
                    </div>
                </div>
            `;
            })
            .join("");

        productsContainer.innerHTML = cartItemsHtml;
        const quantityInputs = document.querySelectorAll(".quantity-input");
        const removeButtons = document.querySelectorAll(".remove-item-icon");

        removeButtons.forEach((button) => button.addEventListener("click", handleRemoveItem));
        quantityInputs.forEach((input) => input.addEventListener("input", handleQuantityInputChange));
    };

    const handleQuantityInputChange = async (event) => {
        const cartItem = event.target.closest(".cart-item");

        if (!cartItem) {
            renderError("Không tìm thấy sản phẩm trong giỏ hàng.");
            return;
        }

        const quantityElement = event.target;
        const totalPriceElement = cartItem.querySelector(".cart-item-total");
        const availableQuantity = parseInt(cartItem.getAttribute("data-available-quantity"));
        const unitPrice = parseFloat(cartItem.getAttribute("data-unit-price"));

        if (!quantityElement || !totalPriceElement) {
            renderError("Không tìm thấy thông tin sản phẩm.");
            return;
        }

        let quantity = parseInt(quantityElement.value);

        if (isNaN(quantity) || quantity < 1) {
            quantityElement.value = 1;
            quantity = 1;
        } else if (quantity > availableQuantity) {
            renderError("Số lượng nhập vượt quá số lượng trong kho.");
            quantityElement.value = availableQuantity;
            quantity = availableQuantity;
        }

        try {
            const response = await fetch(
                `http://localhost:9193/api/v1/cartItems/cart/${cartItem.getAttribute("data-cart-id")}/item/${cartItem.getAttribute("data-item-id")}/update?quantity=${quantity}`,
                {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            const result = await response.json();

            if (response.ok) {
                const newTotalPrice = (quantity * unitPrice).toLocaleString();
                totalPriceElement.textContent = `${newTotalPrice} VND`;
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
                    checkoutButton.disabled = true;
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
            const quantityInput = item.querySelector(".quantity-input");
            const unitPrice = parseFloat(item.getAttribute("data-unit-price"));

            if (quantityInput && !isNaN(unitPrice)) {
                const quantity = parseInt(quantityInput.value);
                totalItems += quantity;
                totalAmount += quantity * unitPrice;
            }
        });

        totalItemsElement.textContent = totalItems;
        totalPriceElement.textContent = `${totalAmount.toLocaleString()} VND`;
    };

    const renderError = (message) => {
        errorMessage.style.display = "block";
        errorMessage.textContent = message;
    };

    checkoutButton.addEventListener("click", async () => {
        const cartId = cartIdElement.textContent;

        try {
            const createOrderResponse = await fetch(`http://localhost:9193/api/v1/orders/order?userId=${userId}`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (!createOrderResponse.ok) {
                const orderResult = await createOrderResponse.json();
                alert(orderResult.message || "Không thể tạo đơn hàng. Vui lòng thử lại.");
                return;
            }

            alert("Tạo đơn hàng thành công!");

            const clearCartResponse = await fetch(
                `http://localhost:9193/api/v1/carts/${cartId}/clear`,
                {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            if (clearCartResponse.ok) {
                location.reload();
            } else {
                alert("Không thể xóa giỏ hàng sau khi tạo đơn.");
            }
        } catch (error) {
            console.error("Error during checkout:", error);
            alert("Có lỗi xảy ra trong quá trình thanh toán.");
        }
    });

    fetchCartId();
});

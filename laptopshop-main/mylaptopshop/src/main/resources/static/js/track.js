document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("authToken");
    const userId = localStorage.getItem("userId");

    // Kiểm tra xem người dùng đã đăng nhập chưa
    if (!token || !userId) {
        alert("Vui lòng đăng nhập để tiếp tục.");
        return;
    }

    // Lấy danh sách đơn hàng của người dùng
    fetchUserOrders(userId, token);
});

// Hàm lấy danh sách đơn hàng của người dùng
async function fetchUserOrders(userId, token) {
    try {
        const response = await fetch(`http://localhost:9193/api/v1/orders/${userId}/user_order`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        });
        const data = await response.json();

        if (data.message === "Success" && Array.isArray(data.data)) {
            const orders = data.data;
            if (orders.length > 0) {
                renderOrderList(orders);
            } else {
                alert("Bạn chưa có đơn hàng nào. Hãy tạo đơn hàng mới.");
            }
        } else {
            alert("Không thể tải danh sách đơn hàng.");
        }
    } catch (error) {
        console.error("Error fetching user orders:", error);
        alert("Không thể tải danh sách đơn hàng. Vui lòng thử lại sau!");
    }
}

// Hàm hiển thị danh sách đơn hàng
function renderOrderList(orders) {
    const orderList = document.getElementById("order-list");
    if (!orderList) {
        console.error("Không tìm thấy phần tử 'order-list' trong HTML.");
        return;
    }

    orderList.innerHTML = ""; // Xóa nội dung cũ

    orders.forEach((order) => {
        const orderItem = `
            <li>
                <span>Đơn hàng ID: ${order.id} - Ngày: ${new Date(order.orderDate).toLocaleString()}</span>
                <button class="view-order-btn" data-order-id="${order.id}">Xem chi tiết</button>
            </li>
        `;
        orderList.insertAdjacentHTML("beforeend", orderItem);
    });

    // Gắn sự kiện cho nút "Xem chi tiết"
    document.querySelectorAll(".view-order-btn").forEach((button) => {
        button.addEventListener("click", () => {
            const selectedOrderId = button.getAttribute("data-order-id");
            fetchOrderDetails(selectedOrderId); // Lấy thông tin chi tiết đơn hàng
        });
    });
}

// Hàm lấy thông tin chi tiết đơn hàng
async function fetchOrderDetails(orderId) {
    const token = localStorage.getItem("authToken");

    try {
        const response = await fetch(`http://localhost:9193/api/v1/orders/${orderId}/order`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        });
        const data = await response.json();

        if (data.message === "Success" && data.data) {
            const order = data.data;

            // Điền thông tin đơn hàng vào trang HTML
            document.getElementById("order-id").textContent = order.id || "N/A";
            document.getElementById("order-date").textContent = new Date(order.orderDate).toLocaleString();
            document.getElementById("order-status").textContent = order.status;

            // Render danh sách sản phẩm
            const productList = document.getElementById("product-list");
            productList.innerHTML = ""; // Xóa nội dung cũ

            order.items.forEach((item) => {
                const total = item.quantity * item.price;
                const row = `
                    <tr>
                        <td>${item.productId}</td>
                        <td>${item.productName}</td>
                        <td>${item.quantity}</td>
                        <td>${Number(item.price).toLocaleString()} VND</td>
                        <td>${Number(total).toLocaleString()} VND</td>
                    </tr>
                `;
                productList.insertAdjacentHTML("beforeend", row);
            });

            // Hiển thị tổng số tiền
            document.getElementById("total-amount").textContent = `${Number(order.totalAmount).toLocaleString()} VND`;
        } else {
            alert("Không thể lấy thông tin chi tiết đơn hàng.");
        }
    } catch (error) {
        console.error("Error fetching order details:", error);
        alert("Không thể tải thông tin chi tiết đơn hàng. Vui lòng thử lại sau!");
    }
}

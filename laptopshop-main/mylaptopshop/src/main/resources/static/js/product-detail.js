document.addEventListener("DOMContentLoaded", function () {
    const productId = new URLSearchParams(window.location.search).get("id");
    const apiUrl = `http://localhost:9193/api/v1/products/${productId}`;
    const detailContainer = document.getElementById("productDetails");

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
            `;
        })
        .catch((error) => {
            detailContainer.innerHTML = `<p>Lỗi: ${error.message}</p>`;
        });
});

// Hàm quay lại danh sách
function goBack() {
    window.history.back();
}

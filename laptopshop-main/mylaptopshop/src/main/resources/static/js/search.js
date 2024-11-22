async function searchProducts(event) {
    event.preventDefault();

    const searchInput = document.getElementById("searchInput").value.toLowerCase();
    const resultsDiv = document.getElementById("results");

    if (!searchInput.trim()) {
        resultsDiv.innerHTML = "<p>Vui lòng nhập tên sản phẩm để tìm kiếm.</p>";
        return;
    }

    try {
        // Gọi API để lấy toàn bộ danh sách sản phẩm
        const response = await fetch(`http://localhost:9193/api/v1/products/all`);
        if (!response.ok) {
            throw new Error(`Lỗi ${response.status}: ${response.statusText}`);
        }

        const products = await response.json();

        // Lọc sản phẩm dựa trên từ khóa tìm kiếm
        const filteredProducts = products.filter((product) =>
            product.name.toLowerCase().includes(searchInput)
        );

        // Hiển thị kết quả
        if (filteredProducts.length > 0) {
            resultsDiv.innerHTML = `
                <h3>Kết quả tìm kiếm:</h3>
                <ul>
                    ${filteredProducts
                        .map(
                            (product) => `
                        <li>
                            <h4>${product.name}</h4>
                            <p><strong>ID:</strong> ${product.id}</p>
                            <p><strong>Thương hiệu:</strong> ${product.brand}</p>
                            <p><strong>Giá:</strong> ${product.price} VND</p>
                            <p><strong>Số lượng:</strong> ${product.quantity}</p>
                            <p><strong>Mô tả:</strong> ${product.description}</p>
                            <p><strong>Hình ảnh:</strong></p>
                            <ul>
                                ${
                                    product.image && product.image.length > 0
                                        ? product.image.map(
                                            (img) => `<img src="http://localhost:9193${img.downloadUrl}" 
                                                alt="${img.fileName}" style="width: 200px; height: auto;">`
                                     ).join("")
                                        : "<li>Không có hình ảnh</li>"
                                }
                            </ul>
                        </li>
                    `
                        )
                        .join("")}
                </ul>`;
        } else {
            resultsDiv.innerHTML = "<p>Không tìm thấy sản phẩm nào.</p>";
        }
    } catch (error) {
        resultsDiv.innerHTML = `<p style="color: red;">${error.message}</p>`;
    }
}
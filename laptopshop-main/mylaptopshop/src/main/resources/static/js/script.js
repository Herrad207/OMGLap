document.addEventListener("DOMContentLoaded", () => {
    const tableBody = document.getElementById("tableBody");

    // Base URL của backend
    const baseUrl = "http://localhost:9193";

    // Gọi API lấy danh sách sản phẩm
    fetch(`${baseUrl}/api/v1/products/all`)
        .then(response => response.json())
        .then(products => {
            products.forEach(product => {
                // Tạo hàng mới trong bảng
                const row = document.createElement("tr");

                // Thêm các cột thông tin sản phẩm
                row.innerHTML = `
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td>${product.brand}</td>
                    <td>${product.price}</td>
                    <td>${product.description}</td>
                    <td>${product.quantity}</td>
                    <td>
                        ${product.image.map(img => `
                            <img src="${baseUrl}${img.downloadUrl}" alt="${img.fileName}" style="width: 100px; height: auto;">
                        `).join("")}
                    </td>
                `;
                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error("Error fetching products:", error));
});

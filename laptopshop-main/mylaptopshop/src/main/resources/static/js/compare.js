// Lấy danh sách so sánh từ localStorage
let compareList = JSON.parse(localStorage.getItem('compareList')) || [];

// Hàm lưu danh sách so sánh vào localStorage
function saveCompareList() {
    localStorage.setItem('compareList', JSON.stringify(compareList));
}

// Hàm hiển thị danh sách sản phẩm trong bảng
function renderCompareTable() {
    const compareTable = document.getElementById('compareTable');

    // Kiểm tra nếu danh sách so sánh trống
    if (compareList.length === 0) {
        compareTable.innerHTML = "<p>Danh sách so sánh trống.</p>";
        return;
    }

    // Gửi yêu cầu lấy thông tin chi tiết sản phẩm
    Promise.all(
        compareList.map(product =>
            fetch(`http://localhost:9193/api/v1/products/${product.id}`)
                .then(res => res.json())
        )
    )
        .then(products => {
            // Lấy danh sách tất cả các thuộc tính (attributes) xuất hiện trong các sản phẩm
            const allAttributes = Array.from(
                new Set(
                    products.flatMap(product =>
                        product.data.attributes.map(attr => attr.attributeName)
                    )
                )
            );

            // Tạo bảng hiển thị
            const table = `
                <table>
                    <thead>
                        <tr>
                            <th>Feature</th>
                            ${products.map(product => `<th>${product.data.name}</th>`).join('')}
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Category</td>
                            ${products.map(product => `<td>${product.data.category.name}</td>`).join('')}
                        </tr>
                        <tr>
                            <td>Brand</td>
                            ${products.map(product => `<td>${product.data.brand}</td>`).join('')}
                        </tr>
                        <tr>
                            <td>Price</td>
                            ${products.map(product => `<td>${product.data.price}</td>`).join('')}
                        </tr>
                        ${allAttributes
                            .map(attr => `
                                <tr>
                                    <td>${attr}</td>
                                    ${products.map(product => {
                                        const attribute = product.data.attributes.find(a => a.attributeName === attr);
                                        return `<td>${attribute ? attribute.value : 'N/A'}</td>`;
                                    }).join('')}
                                </tr>
                            `)
                            .join('')}
                        <tr>
                            <td>Actions</td>
                            ${products.map(product => `
                                <td>
                                    <button class="remove-button" data-id="${product.data.id}">
                                        Remove
                                    </button>
                                </td>
                            `).join('')}
                        </tr>
                    </tbody>
                </table>
            `;
            compareTable.innerHTML = table;

            // Gắn sự kiện xóa sản phẩm cho các nút Remove
            const removeButtons = document.querySelectorAll('.remove-button');
            removeButtons.forEach(button => {
                button.addEventListener('click', (e) => {
                    const productId = parseInt(e.target.getAttribute('data-id'));
                    removeProductFromCompare(productId);
                });
            });
        })
        .catch(error => console.error("Lỗi khi tải thông tin sản phẩm:", error));
}

// Hàm xóa sản phẩm khỏi danh sách so sánh
function removeProductFromCompare(productId) {
    // Lọc bỏ sản phẩm có id tương ứng
    compareList = compareList.filter(product => product.id !== productId);
    saveCompareList(); // Cập nhật localStorage
    renderCompareTable(); // Cập nhật giao diện
}

// Hiển thị danh sách so sánh ban đầu
renderCompareTable();

function handleSearch(event) {
    if (event.key === "Enter") {
        redirectToSearch();
    }
}

function redirectToSearch() {
    const keyword = document.getElementById("searchInput").value.trim();
    if (keyword) {
        // Chuyển hướng đến trang tìm kiếm với từ khóa
        window.location.href = `productname.html?search=${encodeURIComponent(keyword)}`;
    }
}

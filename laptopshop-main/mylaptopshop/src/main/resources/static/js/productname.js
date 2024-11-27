async function fetchAndDisplayProducts() {
    const urlParams = new URLSearchParams(window.location.search);
    const searchQuery = urlParams.get('search') || ''; // Lấy từ khóa tìm kiếm từ URL

    try {
        const response = await fetch('http://localhost:9193/api/v1/products/all'); // Đường dẫn API
        const products = await response.json();

        // Lọc sản phẩm theo từ khóa
        const filteredProducts = products.filter(product =>
            product.name.toLowerCase().includes(searchQuery.toLowerCase())
        );

        displayProducts(filteredProducts); // Hiển thị sản phẩm sau khi lọc
    } catch (error) {
        console.error("Error fetching products:", error);
    }
}

function displayProducts(products) {
    const productBox = document.getElementById('productBox');
    productBox.innerHTML = ''; // Xóa hết sản phẩm cũ

    if (products.length === 0) {
        productBox.innerHTML = `<p>Không tìm thấy sản phẩm nào khớp với từ khóa "${decodeURIComponent(searchQuery)}".</p>`;
        return;
    }

    products.forEach(product => {
        const productDiv = document.createElement('div');
        productDiv.classList.add('product');

        const productImg = document.createElement('img');
        productImg.src = product.image[0]?.downloadUrl 
            ? `http://localhost:9193${product.image[0].downloadUrl}` 
            : '../static/img/gf66.png';
        productImg.alt = product.name;

        const productName = document.createElement('div');
        productName.classList.add('name');
        productName.innerHTML = `<span>${product.name}</span><h5>${new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(product.price)}</h5>`;

        productDiv.appendChild(productImg);
        productDiv.appendChild(productName);

        productBox.appendChild(productDiv);
    });
}

// Gọi hàm hiển thị sản phẩm khi trang tải xong
fetchAndDisplayProducts();
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
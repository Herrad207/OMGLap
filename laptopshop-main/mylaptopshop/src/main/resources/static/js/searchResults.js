document.addEventListener("DOMContentLoaded", async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const searchKeyword = urlParams.get("search")?.toLowerCase(); // Lấy từ khóa tìm kiếm

    if (!searchKeyword) {
        document.getElementById("searchResults").innerHTML = "<p>Please enter a keyword to search.</p>";
        return;
    }

    try {
        // Gọi API để lấy danh sách tất cả sản phẩm
        const response = await fetch("http://localhost:9193/api/v1/products/all"); // Đường dẫn này cần trỏ đúng vào API của bạn
        const products = await response.json();

        // Lọc sản phẩm theo từ khóa
        const filteredProducts = products.filter(product => 
            product.name.toLowerCase().includes(searchKeyword)
        );

        if (filteredProducts.length === 0) {
            document.getElementById("searchResults").innerHTML = `<p>No results found for "${searchKeyword}".</p>`;
        } else {
            // Hiển thị sản phẩm tìm được
            const resultsHtml = filteredProducts.map(product => `
                <div class="product-item">
                    <h2>${product.name}</h2>
                    <p>Brand: ${product.brand}</p>
                    <p>Price: ${product.price}</p>
                </div>
            `).join("");
            document.getElementById("searchResults").innerHTML = resultsHtml;
        }
    } catch (error) {
        console.error("Error fetching products:", error);
        document.getElementById("searchResults").innerHTML = "<p>Error loading products. Please try again later.</p>";
    }
});

// Cập nhật giá trị hiển thị cho thanh trượt
document.getElementById("minPrice").addEventListener("input", function () {
    const minPrice = document.getElementById("minPrice").value;
    document.getElementById("minPriceValue").textContent = `$${minPrice}`;
    applyFilters();  // Gọi hàm lọc sau mỗi lần thay đổi
});

document.getElementById("maxPrice").addEventListener("input", function () {
    const maxPrice = document.getElementById("maxPrice").value;
    document.getElementById("maxPriceValue").textContent = `$${maxPrice}`;
    applyFilters();  // Gọi hàm lọc sau mỗi lần thay đổi
});

// Hàm áp dụng bộ lọc
async function applyFilters() {
    const selectedBrand = document.querySelector(".brand-radio:checked")?.value || "";
    const minPrice = document.getElementById("minPrice").value || "";
    const maxPrice = document.getElementById("maxPrice").value || "";

    const queryParams = new URLSearchParams();
    if (selectedBrand) queryParams.append('brand', selectedBrand);
    if (minPrice) queryParams.append('minPrice', minPrice);
    if (maxPrice) queryParams.append('maxPrice', maxPrice);

    try {
        const response = await fetch(`http://localhost:9193/api/v1/products/filter/brand?${queryParams.toString()}`);
        const data = await response.json();
        if (data && data.data) {
            displayProducts(data.data);
        }
    } catch (error) {
        console.error("Lỗi khi lọc sản phẩm:", error);
    }
}

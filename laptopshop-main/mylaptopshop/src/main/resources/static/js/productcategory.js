// Lấy giá trị category từ URL
function getCategoryFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get('category'); // Trả về giá trị của query string "category"
}

// Fetch sản phẩm theo category
async function fetchProductsByCategory(category) {
    try {
        const response = await fetch(`http://localhost:9193/api/v1/products/product/${category}/all/products`);
        const data = await response.json();

        // Tạo một Set để lưu các thương hiệu không trùng lặp
        const brands = new Set();
        brands.add(""); // Thêm giá trị rỗng để hỗ trợ "All Brands"

        // Duyệt qua danh sách sản phẩm và lấy thương hiệu
        data.data.forEach(product => {
            if (product.brand) {
                brands.add(product.brand); // Thêm thương hiệu vào Set
            }
        });

        // Hiển thị danh sách thương hiệu dưới dạng radio buttons
        const brandBox = document.getElementById("brandBox");
        brandBox.innerHTML = ""; // Xóa các thương hiệu cũ

        // Tùy chọn "All Brands" (mặc định)
        const allBrandsOption = document.createElement("div");
        allBrandsOption.classList.add("brand");
        allBrandsOption.innerHTML = 
            `<input type="radio" name="brand" id="brand-all" value="" class="brand-radio" checked>
            <label for="brand-all">All Brands</label>`;
        brandBox.appendChild(allBrandsOption);

        // Hiển thị các thương hiệu khác
        Array.from(brands).forEach(brand => {
            if (brand) { // Bỏ qua giá trị rỗng đã thêm cho "All Brands"
                const brandDiv = document.createElement("div");
                brandDiv.classList.add("brand");
                brandDiv.innerHTML = 
                    `<input type="radio" name="brand" id="brand-${brand}" value="${brand}" class="brand-radio">
                    <label for="brand-${brand}">${brand}</label>`;
                brandBox.appendChild(brandDiv);
            }
        });

        // Hiển thị danh sách sản phẩm
        displayProducts(data.data);
    } catch (error) {
        console.error(`Lỗi khi lấy sản phẩm của danh mục ${category}:`, error);
    }
}


// Lọc sản phẩm khi thay đổi radio button
document.getElementById("brandBox").addEventListener("change", async function () {
    const selectedBrand = document.querySelector(".brand-radio:checked").value;

    document.getElementById("minPrice").value = "";
    document.getElementById("maxPrice").value = ""; 
    const category = getCategoryFromUrl();
    if (selectedBrand === "") {
        // Nếu không có brand nào được chọn, lấy tất cả sản phẩm theo category
        await fetchProductsByCategory(category);
    } else {
        // Lấy sản phẩm theo category và thương hiệu được chọn
        await fetchProductsByCategoryAndBrand(category, selectedBrand);
    }
});

// Fetch sản phẩm theo category và thương hiệu được chọn
async function fetchProductsByCategoryAndBrand(category, selectedBrand) {
    try {
        const response = await fetch(`http://localhost:9193/api/v1/products/product/by/brand-and-category?category=${category}&brand=${selectedBrand}`);
        const data = await response.json();
        displayProducts(data.data);
    } catch (error) {
        console.error(`Lỗi khi lấy sản phẩm theo thương hiệu:`, error);
    }
}

// Lấy danh mục từ URL và hiển thị sản phẩm
const category = getCategoryFromUrl();
if (category) {
    fetchProductsByCategory(category);
} else {
    console.error("Không có danh mục nào được chọn.");
}

function handleProductClick(productId) {
    // Chuyển hướng đến trang product.html với productId trong URL
    window.location.href = `product.html?productId=${productId}`;
}

function displayProducts(products) {
    const productBox = document.getElementById("productBox");
    productBox.innerHTML = ""; // Xóa các sản phẩm cũ

    products.forEach(product => {
        const productDiv = document.createElement("div");
        productDiv.classList.add("product");
        productDiv.addEventListener("click", () => handleProductClick(product.id)); // Thêm sự kiện click

        const productImg = document.createElement("img");
        productImg.src = product.image[0]?.downloadUrl
            ? `http://localhost:9193${product.image[0].downloadUrl}`
            : '../static/img/gf66.png';
        productImg.alt = product.name;

        const productName = document.createElement("div");
        productName.classList.add("name");
        productName.innerHTML = `<span>${product.name}</span><h5>${new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(product.price)}</h5>`;

        productDiv.appendChild(productImg);
        productDiv.appendChild(productName);

        productBox.appendChild(productDiv);
    });
}

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



// Thêm sự kiện cho nút sắp xếp tăng dần
document.getElementById("sortAscBtn").addEventListener("click", () => {
    sortProductsByPrice("asc");
});

// Thêm sự kiện cho nút sắp xếp giảm dần
document.getElementById("sortDescBtn").addEventListener("click", () => {
    sortProductsByPrice("desc");
});

// Hàm sắp xếp sản phẩm theo giá
function sortProductsByPrice(order) {
    const productBox = document.getElementById("productBox");
    const productElements = Array.from(productBox.children); // Lấy danh sách các phần tử sản phẩm

    // Chuyển đổi các phần tử sản phẩm thành danh sách dữ liệu
    const products = productElements.map((element) => {
        const priceText = element.querySelector("h5").textContent.trim();
        const price = parseFloat(priceText.replace(/[^0-9.]/g, "")); // Loại bỏ các ký tự không phải số
        return {
            element,
            price,
        };
    });

    // Sắp xếp danh sách dựa trên giá
    products.sort((a, b) => (order === "asc" ? a.price - b.price : b.price - a.price));

    // Xóa danh sách sản phẩm hiện tại
    productBox.innerHTML = "";

    // Thêm lại sản phẩm sau khi sắp xếp
    products.forEach(({ element }) => productBox.appendChild(element));
}


// Cập nhật giá trị hiển thị cho thanh trượt
document.getElementById("minPrice").addEventListener("input", function () {
    const minPrice = document.getElementById("minPrice").value;
    applyFilters();  // Gọi hàm lọc sau mỗi lần thay đổi
});

document.getElementById("maxPrice").addEventListener("input", function () {
    const maxPrice = document.getElementById("maxPrice").value;
    applyFilters();  // Gọi hàm lọc sau mỗi lần thay đổi
});

// Hàm áp dụng bộ lọc
async function applyFilters() {
    const selectedBrand = document.querySelector(".brand-radio:checked")?.value || "";
    const minPrice = document.getElementById("minPrice").value || "";
    const maxPrice = document.getElementById("maxPrice").value || "";

    const category = getCategoryFromUrl();

    const queryParams = new URLSearchParams();
    queryParams.append('category', category);   
    if (selectedBrand) queryParams.append('brand', selectedBrand);
    if (minPrice) queryParams.append('minPrice', minPrice);
    if (maxPrice) queryParams.append('maxPrice', maxPrice);

    try {
        const response = await fetch(`http://localhost:9193/api/v1/products/filter/brand?${queryParams.toString()}`);
        const data = await response.json();
        console.log("Data:", data);
        if (data && data.data) {
            displayProducts(data.data);
        }
    } catch (error) {
        console.error("Lỗi khi lọc sản phẩm:", error);
    }
}

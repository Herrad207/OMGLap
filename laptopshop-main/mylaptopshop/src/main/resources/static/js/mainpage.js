    let currentPage = 1;
    const productsPerPage = 20;
    let totalPages = 1;

    async function fetchProducts(page, size) {
        const response = await fetch(`http://localhost:9193/api/v1/products/products?name=&page=${page - 1}&size=${size}`);
        const products = await response.json();
        displayProducts(products);
        totalPages = products.length;
        updatePaginationButtons();
    }
    async function fetchCategories() {
        try {
            const response = await fetch("http://localhost:9193/api/v1/categories/all");
            const data = await response.json();

            if (data.message === "Found!" && data.data) {
                displayCategories(data.data);
            } else {
                console.error("Không tìm thấy category");
            }
        } catch (error) {
            console.error("Lỗi khi lấy category:", error);
        }
    }

    async function fetchCategories() {
        try {
            const response = await fetch("http://localhost:9193/api/v1/categories/all");
            const data = await response.json();

            if (data.data && Array.isArray(data.data)) {
                const categories = data.data;
                const categoryBox = document.getElementById("categoryBox");

                categories.forEach(category => {
                    const categoryItem = document.createElement("div");
                    categoryItem.classList.add("category-item");
                    categoryItem.textContent = category.name; // Thay 'name' bằng thuộc tính category của bạn

                    // Thêm sự kiện nhấn vào để chuyển sang trang mới
                    categoryItem.onclick = () => {
                        window.location.href = `productcategory.html?category=${encodeURIComponent(category.name)}`;
                    };

                    categoryBox.appendChild(categoryItem);
                });
            } else {
                console.error("Không tìm thấy danh sách category.");
            }
        } catch (error) {
            console.error("Lỗi khi lấy category:", error);
        }
    }

    fetchCategories();

    let currentSlide = 0;
    const slides = document.querySelectorAll('.slider-image');
    const totalSlides = slides.length;

    // Hiển thị slide theo index
    function showSlide(index) {
        slides.forEach((slide, i) => {
            slide.style.display = i === index ? 'block' : 'none';
        });
    }

    // Chuyển slide
    function changeSlide(direction) {
        if (direction === 'next') {
            currentSlide = (currentSlide + 1) % totalSlides;
        } else if (direction === 'prev') {
            currentSlide = (currentSlide - 1 + totalSlides) % totalSlides;
        }
        showSlide(currentSlide);
    }

    // Tự động chuyển slide sau 10 giây
    setInterval(() => {
        changeSlide('next');
    }, 10000);

    // Khởi tạo slider
    showSlide(currentSlide);


    function displayProducts(products) {
        const productBox = document.getElementById('productBox');
        productBox.innerHTML = ''; // Xóa hết sản phẩm cũ

        products.forEach(product => {
            const productDiv = document.createElement('div');
            productDiv.classList.add('product');
            
            const productImg = document.createElement('img');
            productImg.src = product.image[0]?.downloadUrl ? "http://localhost:9193" + product.image[0].downloadUrl : '../static/img/gf66.png';
            productImg.alt = product.name;
            
            const productName = document.createElement('div');
            productName.classList.add('name');
            productName.innerHTML = `<span>${product.name}</span><h5>${product.price}</h5>`;
            
            productDiv.appendChild(productImg);
            productDiv.appendChild(productName);
            
            productBox.appendChild(productDiv);
        });
    }

    function changePage(direction) {
        if (direction === 'prev' && currentPage > 1) {
            currentPage--;
        } else if (direction === 'next') {
            currentPage++;
        }

        document.getElementById('pageNumber').textContent = currentPage;
        fetchProducts(currentPage, productsPerPage);
        updatePaginationButtons();
    }
    function updatePaginationButtons(productCount) {
        document.getElementById('prevBtn').disabled = currentPage <= 1;
        document.getElementById('nextBtn').disabled = currentPage >= totalPages; // Kiểm tra tổng số trang để disable nút Next nếu cần
    }

    // Initial load
    fetchProducts(currentPage, productsPerPage);
    function handleProductClick(productId) {
        // Chuyển hướng đến trang product.html với productId trong URL
        window.location.href = `product.html?productId=${productId}`;
    }

    // Thêm sự kiện click cho mỗi sản phẩm
    function displayProducts(products) {
        const productBox = document.getElementById("productBox");
        productBox.innerHTML = ""; // Xóa các sản phẩm cũ

        products.forEach(product => {
            const productDiv = document.createElement("div");
            productDiv.classList.add("product");
            productDiv.addEventListener("click", () => handleProductClick(product.id)); // Thêm sự kiện click

            const productImg = document.createElement("img");
            productImg.src = product.image[0]?.downloadUrl
                ? `http://localhost:9193${product.image[0].downloadUrl}`: '../static/img/gf66.png';
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


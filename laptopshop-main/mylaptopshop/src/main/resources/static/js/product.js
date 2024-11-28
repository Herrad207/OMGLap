// Lấy giá trị productId từ URL
function getProductIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get('productId');
}

// Fetch chi tiết sản phẩm theo productId
async function fetchProductDetails(productId) {
    try {
        const response = await fetch(`http://localhost:9193/api/v1/products/${productId}`);
        const data = await response.json();
        if (data.data) {
            displayProductDetails(data.data);
        } else {
            console.error("Không tìm thấy chi tiết sản phẩm.");
        }
    } catch (error) {
        console.error("Lỗi khi lấy chi tiết sản phẩm:", error);
    }
}

// Hiển thị chi tiết sản phẩm trong các div tương ứng
function displayProductDetails(product) {
    // Hiển thị chi tiết sản phẩm trong #productDetails
    const productDetails = document.getElementById("productDetails");
    productDetails.innerHTML = `
        <div class="product-images">
            <img src="${
                product.image[0]?.downloadUrl
                    ? `http://localhost:9193${product.image[0].downloadUrl}`
                    : '../static/img/gf66.png'
            }" alt="${product.name}">
        </div>
        <div class="product-info">
            <h1>${product.name}</h1>
            <p>Category: ${product.category.name}</p>
            <p>Brand: ${product.brand}</p>
            <p>Price: ${product.price}</p>
            <p>Quantity: ${product.quantity}</p>
             <button id="addToCartButton" class="btn-add-to-cart">Add to Cart</button>
            <button class="add-to-compare" onclick="addToCompare(${product.id}, '${product.name}')">Add to Compare</button>
        </div>
    `;
    const addToCartButton = document.getElementById("addToCartButton");
    addToCartButton.addEventListener("click", () => addToCart(product.id));

    // Hiển thị bảng thuộc tính trong #attributes
    const attributesDiv = document.getElementById("attributes");
    if (product.attributes.length > 0) {
        attributesDiv.innerHTML = `
            <h2>Specifications</h2>
            <table>
                <thead>
                    <tr>
                        <th>Attribute</th>
                        <th>Value</th>
                    </tr>
                </thead>
                <tbody>
                    ${product.attributes
                        .map(
                            attr => `
                        <tr>
                            <td>${attr.attributeName}</td>
                            <td>${attr.value}</td>
                        </tr>
                    `
                        )
                        .join('')}
                </tbody>
            </table>
        `;
    } else {
        attributesDiv.innerHTML = "<p>No specifications available.</p>";
    }
}

// Lấy productId từ URL và hiển thị sản phẩm
const productId = getProductIdFromUrl();
if (productId) {
    fetchProductDetails(productId);
} else {
    console.error("Không có productId được cung cấp.");
}

function addToCompare(productId, productName) {
    const maxCompare = 3;
    // Lấy danh sách so sánh từ localStorage
    let compareList = JSON.parse(localStorage.getItem('compareList')) || [];

    // Kiểm tra sản phẩm đã tồn tại trong danh sách chưa
    if (compareList.some(product => product.id === productId)) {
        alert(`${productName} đã có trong danh sách so sánh.`);
        return;
    }

    // Kiểm tra danh sách so sánh có vượt quá số lượng tối đa
    if (compareList.length >= maxCompare) {
        alert("Danh sách so sánh chỉ được chứa tối đa 3 sản phẩm.");
        return;
    }

    // Thêm sản phẩm vào danh sách so sánh
    compareList.push({ id: productId, name: productName });
    localStorage.setItem('compareList', JSON.stringify(compareList));
    showNotification("Product added to compare successfully!", "success");

}

// Thêm sản phẩm vào giỏ hàng
async function addToCart(productId) {
    try {
        const response = await fetch(`http://localhost:9193/api/v1/cartItems/item/add`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "Authorization": `Bearer ${localStorage.getItem('authToken')}`
            },
            body: new URLSearchParams({
                productId: productId,
                quantity: 1, // Giả sử thêm 1 sản phẩm mỗi lần click
            }),
        });

        if (response.ok) {
            showNotification("Product added to cart successfully!", "success");
        } else {
            const errorData = await response.json();
            showNotification(`Failed to add product: ${errorData.message}`, "error");
        }
    } catch (error) {
        console.error("Error adding product to cart:", error);
        showNotification("An unexpected error occurred. Please try again.", "error");
    }
}

function showNotification(message, type) {
    const notification = document.createElement("div");
    notification.className = `notification ${type}`;
    notification.textContent = message;

    document.body.appendChild(notification);

    setTimeout(() => {
        notification.remove();
    }, 3000); // Thông báo biến mất sau 3 giây
}

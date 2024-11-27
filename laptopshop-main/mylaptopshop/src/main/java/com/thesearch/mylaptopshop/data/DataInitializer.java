package com.thesearch.mylaptopshop.data;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thesearch.mylaptopshop.model.Category;
import com.thesearch.mylaptopshop.model.Product;
import com.thesearch.mylaptopshop.model.Role;
import com.thesearch.mylaptopshop.model.User;
import com.thesearch.mylaptopshop.model.Attribute;
import com.thesearch.mylaptopshop.model.ProductAttribute;
import com.thesearch.mylaptopshop.repository.AttributeRepository;
import com.thesearch.mylaptopshop.repository.CategoryRepository;
import com.thesearch.mylaptopshop.repository.ProductAttributeRepository;
import com.thesearch.mylaptopshop.repository.ProductRepository;
import com.thesearch.mylaptopshop.repository.RoleRepository;
import com.thesearch.mylaptopshop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
    private final ProductAttributeRepository productAttributeRepository;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event){
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultRoleIfNotExists(defaultRoles);
        createDefaultUserIfNotExists();
        createDefaultAdminIfNotExists();
        productInit();
    }

    private void createDefaultUserIfNotExists(){
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        for(int i  = 1;i<=5;i++){
            String defaultEmail = "NKB"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("Bao"+i);
            user.setLastName("Nguyen Kim");
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
        }
    }
    private void createDefaultAdminIfNotExists(){
        Role userRole = roleRepository.findByName("ROLE_ADMIN").get();
        for(int i  = 1;i<=2;i++){
            String defaultEmail = "Baorista"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("Rista"+i);
            user.setLastName("Nguyen");
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
        }
    }
    private void createDefaultRoleIfNotExists(Set<String> roles){
        roles.stream().filter(role->roleRepository.findByName(role).isEmpty()).map(Role::new).forEach(roleRepository::save);
    }
    private void productInit(){
        // Laptops
    addProduct("Dell XPS 13", "Dell", BigDecimal.valueOf(25000000), 30, "Laptop cao cấp, màn hình 13 inch, thiết kế mỏng nhẹ", "Laptop");
    addProduct("MacBook Air M2", "Apple", BigDecimal.valueOf(30000000), 20, "Laptop mỏng nhẹ, vi xử lý M2 mạnh mẽ", "Laptop");
    addProduct("HP Spectre x360", "HP", BigDecimal.valueOf(27000000), 25, "Laptop 2 trong 1, màn hình cảm ứng, thiết kế sang trọng", "Laptop");
    addProduct("Lenovo ThinkPad X1 Carbon", "Lenovo", BigDecimal.valueOf(28000000), 35, "Laptop doanh nhân, bàn phím thoải mái, hiệu năng cao", "Laptop");
    addProduct("Acer Swift 3", "Acer", BigDecimal.valueOf(19000000), 40, "Laptop mỏng nhẹ, màn hình Full HD", "Laptop");
    addProduct("Asus ZenBook 14", "Asus", BigDecimal.valueOf(24000000), 50, "Laptop siêu nhẹ, vi xử lý Intel Core i7", "Laptop");
    addProduct("Razer Blade 15", "Razer", BigDecimal.valueOf(35000000), 15, "Laptop chơi game, màn hình 15.6 inch, card đồ họa RTX", "Laptop");
    addProduct("MSI GS66 Stealth", "MSI", BigDecimal.valueOf(32000000), 10, "Laptop chơi game, thiết kế mỏng và mạnh mẽ", "Laptop");
    addProduct("MacBook Pro 16", "Apple", BigDecimal.valueOf(40000000), 20, "Laptop cao cấp, màn hình Retina 16 inch", "Laptop");
    addProduct("Gigabyte Aero 15", "Gigabyte", BigDecimal.valueOf(33000000), 25, "Laptop dành cho sáng tạo nội dung, màn hình 4K", "Laptop");
    addProduct("Huawei MateBook X Pro", "Huawei", BigDecimal.valueOf(27000000), 30, "Laptop siêu mỏng, màn hình 13.9 inch", "Laptop");
    addProduct("Samsung Galaxy Book Pro", "Samsung", BigDecimal.valueOf(22000000), 40, "Laptop mỏng nhẹ, hiệu năng mạnh mẽ", "Laptop");
    addProduct("Microsoft Surface Laptop 4", "Microsoft", BigDecimal.valueOf(25000000), 35, "Laptop siêu mỏng, màn hình cảm ứng", "Laptop");
    addProduct("Xiaomi Mi Laptop Pro", "Xiaomi", BigDecimal.valueOf(21000000), 45, "Laptop với màn hình AMOLED 15.6 inch", "Laptop");
    addProduct("Alienware m15", "Alienware", BigDecimal.valueOf(38000000), 20, "Laptop chơi game cao cấp với hiệu suất mạnh mẽ", "Laptop");
    addProduct("Acer Predator Helios 300", "Acer", BigDecimal.valueOf(28000000), 25, "Laptop chơi game với GPU RTX 3060", "Laptop");
    addProduct("HP Omen 15", "HP", BigDecimal.valueOf(29000000), 30, "Laptop chơi game, màn hình 15.6 inch", "Laptop");
    addProduct("Asus ROG Zephyrus G14", "Asus", BigDecimal.valueOf(34000000), 18, "Laptop chơi game siêu nhẹ", "Laptop");
    addProduct("Lenovo Legion 5", "Lenovo", BigDecimal.valueOf(30000000), 30, "Laptop chơi game với cấu hình cao", "Laptop");
    addProduct("Dell G15", "Dell", BigDecimal.valueOf(25000000), 50, "Laptop chơi game tầm trung, màn hình 15.6 inch", "Laptop");

// Mice (Chuột)
    addProduct("Logitech G Pro X", "Logitech", BigDecimal.valueOf(900000), 40, "Chuột chơi game, có thể thay đổi switch", "Chuột");
    addProduct("Razer DeathAdder Elite", "Razer", BigDecimal.valueOf(1200000), 50, "Chuột gaming thiết kế ergonomic, cảm biến cao cấp", "Chuột");
    addProduct("SteelSeries Rival 600", "SteelSeries", BigDecimal.valueOf(1400000), 30, "Chuột gaming, cảm biến Dual Sensor", "Chuột");
    addProduct("Corsair Dark Core RGB", "Corsair", BigDecimal.valueOf(1600000), 20, "Chuột không dây, đèn RGB", "Chuột");
    addProduct("Logitech MX Master 3", "Logitech", BigDecimal.valueOf(1500000), 25, "Chuột đa dụng, thiết kế tiện dụng cho làm việc", "Chuột");
    addProduct("Razer Naga X", "Razer", BigDecimal.valueOf(1300000), 45, "Chuột MMO, nhiều phím bấm", "Chuột");
    addProduct("SteelSeries Sensei 310", "SteelSeries", BigDecimal.valueOf(1100000), 40, "Chuột chơi game với cảm biến cao cấp", "Chuột");
    addProduct("HyperX Pulsefire FPS Pro", "HyperX", BigDecimal.valueOf(1400000), 30, "Chuột FPS, cảm biến Pixart 3389", "Chuột");
    addProduct("Logitech G502 Hero", "Logitech", BigDecimal.valueOf(1400000), 50, "Chuột chơi game, cảm biến HERO", "Chuột");
    addProduct("Corsair Scimitar RGB Elite", "Corsair", BigDecimal.valueOf(1800000), 20, "Chuột MMO, nhiều nút bấm", "Chuột");
    addProduct("Razer Basilisk V2", "Razer", BigDecimal.valueOf(1600000), 25, "Chuột chơi game, đèn RGB", "Chuột");
    addProduct("Logitech G903 Lightspeed", "Logitech", BigDecimal.valueOf(2000000), 15, "Chuột không dây, cảm biến HERO", "Chuột");
    addProduct("Glorious Model O", "Glorious", BigDecimal.valueOf(1300000), 30, "Chuột gaming siêu nhẹ, cảm biến 16k", "Chuột");
    addProduct("SteelSeries Rival 310", "SteelSeries", BigDecimal.valueOf(1100000), 50, "Chuột chơi game, thiết kế ergonomic", "Chuột");
    addProduct("Razer Viper Ultimate", "Razer", BigDecimal.valueOf(2500000), 20, "Chuột chơi game không dây, cảm biến 20k", "Chuột");
    addProduct("HyperX Pulsefire Haste", "HyperX", BigDecimal.valueOf(1300000), 40, "Chuột gaming siêu nhẹ", "Chuột");
    addProduct("Corsair M65 Elite", "Corsair", BigDecimal.valueOf(1700000), 30, "Chuột chơi game FPS, cảm biến 18k", "Chuột");
    addProduct("Logitech G703 Lightspeed", "Logitech", BigDecimal.valueOf(1600000), 35, "Chuột gaming không dây, đèn RGB", "Chuột");
    addProduct("Razer Orochi V2", "Razer", BigDecimal.valueOf(1300000), 45, "Chuột không dây siêu nhẹ", "Chuột");
    addProduct("Logitech G305 Lightspeed", "Logitech", BigDecimal.valueOf(1000000), 60, "Chuột không dây, cảm biến 12k", "Chuột");

// Keyboards (Bàn phím)
    addProduct("Razer BlackWidow V3", "Razer", BigDecimal.valueOf(2500000), 25, "Bàn phím cơ, đèn RGB, thiết kế chắc chắn", "Bàn phím");
    addProduct("Corsair K95 RGB Platinum", "Corsair", BigDecimal.valueOf(3500000), 30, "Bàn phím cơ, đèn RGB, switch Cherry MX", "Bàn phím");
    addProduct("Logitech G Pro X", "Logitech", BigDecimal.valueOf(2700000), 32, "Bàn phím cơ với switch có thể thay thế", "Bàn phím");
    addProduct("HyperX Alloy FPS Pro", "HyperX", BigDecimal.valueOf(2200000), 50, "Bàn phím cơ, thiết kế nhỏ gọn", "Bàn phím");
    addProduct("Redragon K552", "Redragon", BigDecimal.valueOf(800000), 55, "Bàn phím cơ giá rẻ, hiệu suất tốt", "Bàn phím");
    addProduct("Razer Cynosa V2", "Razer", BigDecimal.valueOf(1200000), 40, "Bàn phím cao cấp, đèn RGB", "Bàn phím");
    addProduct("Corsair K68", "Corsair", BigDecimal.valueOf(1800000), 45, "Bàn phím cơ chống nước, bền bỉ", "Bàn phím");
    addProduct("Logitech G413", "Logitech", BigDecimal.valueOf(1500000), 50, "Bàn phím cơ, thiết kế đơn giản", "Bàn phím");
    addProduct("Keychron K4", "Keychron", BigDecimal.valueOf(2200000), 38, "Bàn phím cơ với kết nối đa dạng", "Bàn phím");
    addProduct("HyperX Alloy Elite 2", "HyperX", BigDecimal.valueOf(3500000), 25, "Bàn phím cơ với đèn RGB đẹp mắt", "Bàn phím");
    addProduct("Logitech G910 Orion Spectrum", "Logitech", BigDecimal.valueOf(3000000), 30, "Bàn phím cơ, đèn RGB, switch Romer-G", "Bàn phím");
    addProduct("Corsair K70 RGB MK.2", "Corsair", BigDecimal.valueOf(3200000), 40, "Bàn phím cơ với cảm giác gõ mượt mà", "Bàn phím");
    addProduct("Razer Huntsman Elite", "Razer", BigDecimal.valueOf(4000000), 35, "Bàn phím cơ với switch quang học, đèn RGB", "Bàn phím");
    addProduct("SteelSeries Apex Pro", "SteelSeries", BigDecimal.valueOf(5000000), 20, "Bàn phím cơ với điều chỉnh lực nhấn từng phím", "Bàn phím");
    addProduct("Logitech G413 Carbon", "Logitech", BigDecimal.valueOf(1500000), 50, "Bàn phím cơ, thiết kế thanh lịch", "Bàn phím");
    addProduct("Razer BlackWidow V2", "Razer", BigDecimal.valueOf(2300000), 45, "Bàn phím cơ, switch Green, đèn RGB", "Bàn phím");
    addProduct("Corsair K60 RGB PRO", "Corsair", BigDecimal.valueOf(1700000), 60, "Bàn phím cơ, thiết kế nhôm chắc chắn", "Bàn phím");
    addProduct("HyperX Alloy FPS", "HyperX", BigDecimal.valueOf(1200000), 65, "Bàn phím cơ thiết kế dành cho game thủ FPS", "Bàn phím");
    addProduct("Logitech G513", "Logitech", BigDecimal.valueOf(2000000), 30, "Bàn phím cơ, cảm giác gõ êm ái", "Bàn phím");
    addProduct("Redragon K550", "Redragon", BigDecimal.valueOf(1500000), 50, "Bàn phím cơ, đèn RGB, giá cả phải chăng", "Bàn phím");
    addProduct("Corsair Vengeance LPX 8GB", "Corsair", BigDecimal.valueOf(1200000), 50, "RAM DDR4, 8GB, 2666MHz", "RAM");
    addProduct("G.SKILL Ripjaws V 16GB", "G.SKILL", BigDecimal.valueOf(2000000), 40, "RAM DDR4, 16GB, 3200MHz", "RAM");
    addProduct("Kingston HyperX Fury 16GB", "Kingston", BigDecimal.valueOf(1800000), 60, "RAM DDR4, 16GB, 3200MHz", "RAM");
    addProduct("Crucial Ballistix 8GB", "Crucial", BigDecimal.valueOf(1300000), 45, "RAM DDR4, 8GB, 3000MHz", "RAM");
    addProduct("Team T-Force Vulcan Z 8GB", "Team", BigDecimal.valueOf(1000000), 40, "RAM DDR4, 8GB, 2400MHz", "RAM");
    addProduct("Corsair Vengeance LPX 16GB", "Corsair", BigDecimal.valueOf(2500000), 50, "RAM DDR4, 16GB, 3200MHz", "RAM");
    addProduct("Patriot Viper Steel 8GB", "Patriot", BigDecimal.valueOf(1200000), 45, "RAM DDR4, 8GB, 3200MHz", "RAM");
    addProduct("Adata XPG Gammix D30 16GB", "Adata", BigDecimal.valueOf(2200000), 35, "RAM DDR4, 16GB, 3000MHz", "RAM");
    addProduct("Kingston HyperX Predator 8GB", "Kingston", BigDecimal.valueOf(1600000), 60, "RAM DDR4, 8GB, 3600MHz", "RAM");
    addProduct("Corsair Dominator Platinum 16GB", "Corsair", BigDecimal.valueOf(3500000), 30, "RAM DDR4, 16GB, 3200MHz", "RAM");
    addProduct("G.SKILL Trident Z RGB 16GB", "G.SKILL", BigDecimal.valueOf(3000000), 40, "RAM DDR4, 16GB, 3600MHz, đèn RGB", "RAM");
    addProduct("Crucial Ballistix Sport AT 8GB", "Crucial", BigDecimal.valueOf(1200000), 55, "RAM DDR4, 8GB, 2666MHz", "RAM");
    addProduct("Kingston Fury Beast 8GB", "Kingston", BigDecimal.valueOf(1400000), 50, "RAM DDR4, 8GB, 2933MHz", "RAM");
    addProduct("Corsair Vengeance LPX 32GB", "Corsair", BigDecimal.valueOf(5000000), 25, "RAM DDR4, 32GB, 3000MHz", "RAM");
    addProduct("Adata XPG Z1 8GB", "Adata", BigDecimal.valueOf(1000000), 30, "RAM DDR4, 8GB, 2400MHz", "RAM");
    addProduct("G.SKILL Ripjaws V 32GB", "G.SKILL", BigDecimal.valueOf(4500000), 20, "RAM DDR4, 32GB, 3200MHz", "RAM");
    addProduct("Patriot Viper Elite 16GB", "Patriot", BigDecimal.valueOf(2200000), 35, "RAM DDR4, 16GB, 3200MHz", "RAM");
    addProduct("Team T-Force Dark Za 16GB", "Team", BigDecimal.valueOf(2300000), 40, "RAM DDR4, 16GB, 3600MHz", "RAM");
    addProduct("Corsair Vengeance RGB Pro 8GB", "Corsair", BigDecimal.valueOf(1600000), 30, "RAM DDR4, 8GB, 3200MHz, đèn RGB", "RAM");
    addProduct("Samsung 970 EVO Plus 500GB", "Samsung", BigDecimal.valueOf(2200000), 40, "SSD NVMe, tốc độ đọc 3500MB/s", "SSD");
    addProduct("WD Blue SN550 500GB", "Western Digital", BigDecimal.valueOf(1500000), 50, "SSD NVMe, tốc độ đọc 2400MB/s", "SSD");
    addProduct("Crucial P3 500GB", "Crucial", BigDecimal.valueOf(1800000), 60, "SSD NVMe, tốc độ đọc 3500MB/s", "SSD");
    addProduct("Kingston NV1 500GB", "Kingston", BigDecimal.valueOf(1300000), 55, "SSD NVMe, tốc độ đọc 2100MB/s", "SSD");
    addProduct("ADATA XPG SX8200 Pro 1TB", "ADATA", BigDecimal.valueOf(2800000), 40, "SSD NVMe, tốc độ đọc 3500MB/s", "SSD");
    addProduct("SanDisk Ultra 3D 500GB", "SanDisk", BigDecimal.valueOf(1700000), 45, "SSD SATA, tốc độ đọc 560MB/s", "SSD");
    addProduct("Samsung 860 EVO 500GB", "Samsung", BigDecimal.valueOf(2000000), 50, "SSD SATA, tốc độ đọc 550MB/s", "SSD");
    addProduct("Crucial MX500 500GB", "Crucial", BigDecimal.valueOf(1900000), 60, "SSD SATA, tốc độ đọc 560MB/s", "SSD");
    addProduct("Western Digital Blue 1TB", "Western Digital", BigDecimal.valueOf(2500000), 30, "SSD SATA, tốc độ đọc 560MB/s", "SSD");
    addProduct("ADATA SU800 512GB", "ADATA", BigDecimal.valueOf(1700000), 55, "SSD SATA, tốc độ đọc 560MB/s", "SSD");
    addProduct("Seagate Barracuda 1TB", "Seagate", BigDecimal.valueOf(2200000), 40, "SSD SATA, tốc độ đọc 550MB/s", "SSD");
    addProduct("PNY CS900 240GB", "PNY", BigDecimal.valueOf(1000000), 60, "SSD SATA, tốc độ đọc 500MB/s", "SSD");
    addProduct("Kingston A2000 1TB", "Kingston", BigDecimal.valueOf(2700000), 45, "SSD NVMe, tốc độ đọc 2200MB/s", "SSD");
    addProduct("Intel 660p 500GB", "Intel", BigDecimal.valueOf(2200000), 50, "SSD NVMe, tốc độ đọc 1800MB/s", "SSD");
    addProduct("Patriot P3 1TB", "Patriot", BigDecimal.valueOf(2600000), 35, "SSD NVMe, tốc độ đọc 3500MB/s", "SSD");
    addProduct("Team Group T-Force Vulcan 512GB", "Team Group", BigDecimal.valueOf(1500000), 50, "SSD SATA, tốc độ đọc 560MB/s", "SSD");
    addProduct("Corsair MP600 500GB", "Corsair", BigDecimal.valueOf(3000000), 25, "SSD NVMe, tốc độ đọc 4950MB/s", "SSD");
    addProduct("Samsung 980 PRO 1TB", "Samsung", BigDecimal.valueOf(4200000), 20, "SSD NVMe, tốc độ đọc 7000MB/s", "SSD");
    addProduct("Western Digital Black SN850 1TB", "Western Digital", BigDecimal.valueOf(4500000), 20, "SSD NVMe, tốc độ đọc 7000MB/s", "SSD");
    addProduct("Crucial P5 1TB", "Crucial", BigDecimal.valueOf(2500000), 30, "SSD NVMe, tốc độ đọc 3400MB/s", "SSD");
    
    }
    private void  addProduct(String name,String brand,BigDecimal price,int quantity,String description,String c){
        if(productRepository.existsByNameAndBrand(name,brand)){
            return;
        }
        Category category = Optional.ofNullable(categoryRepository.findByName(c))
            .orElseGet(()->{
                Category newCategory = new Category(c);
                return categoryRepository.save(newCategory);
        });
        productRepository.save(new Product(
            name,
            brand,
            price,
            quantity,
            description,
            category
        ));
    }
    private void attributeInit(){
        addAttribute("CPU");
        addAttribute("RAM");
        addAttribute("Ổ cứng");
        addAttribute("Card đồ họa");
        addAttribute("Màn hình");
        addAttribute("Độ phân giải");
        addAttribute("Hệ điều hành");
        addAttribute("Pin");
        addAttribute("Cân nặng");
        addAttribute("Kích thước");
        addAttribute("Cổng kết nối");
        addAttribute("Kết nối không dây");
        addAttribute("Bàn phím");
        addAttribute("Webcam");
        addAttribute("Loa");
        addAttribute("Chất liệu");
        addAttribute("Bảo hành");
    }
    private void addAttribute(String attribute){
        if(attributeRepository.existsByName(attribute)){
            return;
        }
        Attribute newAttribute = new Attribute(attribute);
        attributeRepository.save(newAttribute);
    }
    private void addProductAttribute(long productId,long attributeId, String value){
        ProductAttribute productAttribute = new ProductAttribute();
        if(productAttributeRepository.existsByProductIdAndAttributeId(productId, attributeId)){
            return;
        }
        productAttribute.setProductId(productId);
        productAttribute.setAttributeId(attributeId);
        productAttribute.setValue(value);
        productAttributeRepository.save(productAttribute);
    }
    private void productAttributeInit(){
        // Dell XPS 13
        addProductAttribute(1, 1, "Intel Core i7-1165G7");
        addProductAttribute(1, 2, "16GB LPDDR4x");
        addProductAttribute(1, 3, "512GB SSD");
        addProductAttribute(1, 4, "Intel Iris Xe Graphics");
        addProductAttribute(1, 5, "13.3 inch");
        addProductAttribute(1, 6, "1920x1200");
        addProductAttribute(1, 7, "Windows 11");
        addProductAttribute(1, 8, "52 Wh");
        addProductAttribute(1, 9, "1.2kg");
        addProductAttribute(1, 10, "296 x 199 x 15 mm");
        addProductAttribute(1, 11, "2x Thunderbolt 4, 1x USB-C");
        addProductAttribute(1, 12, "Wi-Fi 6, Bluetooth 5.1");
        addProductAttribute(1, 13, "Có đèn nền");
        addProductAttribute(1, 14, "HD Webcam 720p");
        addProductAttribute(1, 15, "Stereo Speakers");
        addProductAttribute(1, 16, "Nhôm");
        addProductAttribute(1, 17, "1 năm");

        // MacBook Air M2
        addProductAttribute(2, 1, "Apple M2");
        addProductAttribute(2, 2, "8GB Unified Memory");
        addProductAttribute(2, 3, "256GB SSD");
        addProductAttribute(2, 4, "Apple GPU 8-core");
        addProductAttribute(2, 5, "13.6 inch");
        addProductAttribute(2, 6, "2560x1664");
        addProductAttribute(2, 7, "macOS Ventura");
        addProductAttribute(2, 8, "52.6 Wh");
        addProductAttribute(2, 9, "1.24kg");
        addProductAttribute(2, 10, "304 x 212 x 11.3 mm");
        addProductAttribute(2, 11, "2x Thunderbolt 3, MagSafe 3");
        addProductAttribute(2, 12, "Wi-Fi 6E, Bluetooth 5.3");
        addProductAttribute(2, 13, "Có đèn nền");
        addProductAttribute(2, 14, "Full HD Webcam");
        addProductAttribute(2, 15, "Stereo Speakers với Spatial Audio");
        addProductAttribute(2, 16, "Nhôm tái chế");
        addProductAttribute(2, 17, "1 năm");

        // HP Spectre x360
        addProductAttribute(3, 1, "Intel Core i7-1165G7");
        addProductAttribute(3, 2, "16GB DDR4");
        addProductAttribute(3, 3, "512GB SSD");
        addProductAttribute(3, 4, "Intel Iris Xe Graphics");
        addProductAttribute(3, 5, "13.5 inch cảm ứng");
        addProductAttribute(3, 6, "1920x1280");
        addProductAttribute(3, 7, "Windows 11");
        addProductAttribute(3, 8, "66 Wh");
        addProductAttribute(3, 9, "1.3kg");
        addProductAttribute(3, 10, "297 x 210 x 15 mm");
        addProductAttribute(3, 11, "2x USB-C, 1x USB-A");
        addProductAttribute(3, 12, "Wi-Fi 6, Bluetooth 5.1");
        addProductAttribute(3, 13, "Có đèn nền");
        addProductAttribute(3, 14, "Full HD Webcam");
        addProductAttribute(3, 15, "B&O Stereo Speakers");
        addProductAttribute(3, 16, "Nhôm");
        addProductAttribute(3, 17, "2 năm");

        // Lenovo ThinkPad X1 Carbon
        addProductAttribute(4, 1, "Intel Core i7-1185G7");
        addProductAttribute(4, 2, "16GB LPDDR4x");
        addProductAttribute(4, 3, "1TB SSD");
        addProductAttribute(4, 4, "Intel Iris Xe Graphics");
        addProductAttribute(4, 5, "14 inch");
        addProductAttribute(4, 6, "1920x1200");
        addProductAttribute(4, 7, "Windows 11 Pro");
        addProductAttribute(4, 8, "57 Wh");
        addProductAttribute(4, 9, "1.13kg");
        addProductAttribute(4, 10, "314 x 221 x 15 mm");
        addProductAttribute(4, 11, "2x Thunderbolt 4, 2x USB-A");
        addProductAttribute(4, 12, "Wi-Fi 6, Bluetooth 5.1");
        addProductAttribute(4, 13, "Có đèn nền");
        addProductAttribute(4, 14, "HD Webcam với Privacy Shutter");
        addProductAttribute(4, 15, "Stereo Speakers với Dolby Atmos");
        addProductAttribute(4, 16, "Carbon Fiber");
        addProductAttribute(4, 17, "1 năm");

// Tương tự, thêm thông tin chi tiết từng sản phẩm từ 5 đến 20...
        // Acer Swift 3
        addProductAttribute(5, 1, "Intel Core i5-1135G7");
        addProductAttribute(5, 2, "8GB DDR4");
        addProductAttribute(5, 3, "512GB SSD");
        addProductAttribute(5, 4, "Intel Iris Xe Graphics");
        addProductAttribute(5, 5, "14 inch");
        addProductAttribute(5, 6, "1920x1080");
        addProductAttribute(5, 7, "Windows 11 Home");
        addProductAttribute(5, 8, "48 Wh");
        addProductAttribute(5, 9, "1.2kg");
        addProductAttribute(5, 10, "324 x 219 x 15 mm");
        addProductAttribute(5, 11, "1x USB-C, 2x USB-A, HDMI");
        addProductAttribute(5, 12, "Wi-Fi 6, Bluetooth 5.0");
        addProductAttribute(5, 13, "Có đèn nền");
        addProductAttribute(5, 14, "HD Webcam 720p");
        addProductAttribute(5, 15, "Stereo Speakers");
        addProductAttribute(5, 16, "Nhôm");
        addProductAttribute(5, 17, "1 năm");

        // Asus ZenBook 14
        addProductAttribute(6, 1, "Intel Core i7-1260P");
        addProductAttribute(6, 2, "16GB LPDDR5");
        addProductAttribute(6, 3, "1TB SSD");
        addProductAttribute(6, 4, "Intel Iris Xe Graphics");
        addProductAttribute(6, 5, "14 inch OLED");
        addProductAttribute(6, 6, "2880x1800");
        addProductAttribute(6, 7, "Windows 11 Home");
        addProductAttribute(6, 8, "75 Wh");
        addProductAttribute(6, 9, "1.4kg");
        addProductAttribute(6, 10, "319 x 210 x 16 mm");
        addProductAttribute(6, 11, "2x Thunderbolt 4, 1x USB-A, HDMI");
        addProductAttribute(6, 12, "Wi-Fi 6E, Bluetooth 5.2");
        addProductAttribute(6, 13, "Có đèn nền");
        addProductAttribute(6, 14, "Full HD Webcam với 3D Noise Reduction");
        addProductAttribute(6, 15, "Stereo Speakers với Dolby Atmos");
        addProductAttribute(6, 16, "Nhôm");
        addProductAttribute(6, 17, "2 năm");

        // Razer Blade 15
        addProductAttribute(7, 1, "Intel Core i7-11800H");
        addProductAttribute(7, 2, "16GB DDR4");
        addProductAttribute(7, 3, "1TB SSD");
        addProductAttribute(7, 4, "NVIDIA GeForce RTX 3070");
        addProductAttribute(7, 5, "15.6 inch");
        addProductAttribute(7, 6, "2560x1440, 240Hz");
        addProductAttribute(7, 7, "Windows 11 Home");
        addProductAttribute(7, 8, "80 Wh");
        addProductAttribute(7, 9, "2.1kg");
        addProductAttribute(7, 10, "355 x 235 x 16.8 mm");
        addProductAttribute(7, 11, "1x USB-C, 3x USB-A, HDMI");
        addProductAttribute(7, 12, "Wi-Fi 6, Bluetooth 5.2");
        addProductAttribute(7, 13, "Có đèn nền RGB");
        addProductAttribute(7, 14, "HD Webcam 720p");
        addProductAttribute(7, 15, "Stereo Speakers");
        addProductAttribute(7, 16, "Nhôm CNC");
        addProductAttribute(7, 17, "2 năm");

        // MSI GS66 Stealth
        addProductAttribute(8, 1, "Intel Core i9-11900H");
        addProductAttribute(8, 2, "32GB DDR4");
        addProductAttribute(8, 3, "2TB SSD");
        addProductAttribute(8, 4, "NVIDIA GeForce RTX 3080");
        addProductAttribute(8, 5, "15.6 inch");
        addProductAttribute(8, 6, "3840x2160, 120Hz");
        addProductAttribute(8, 7, "Windows 11 Pro");
        addProductAttribute(8, 8, "99.9 Wh");
        addProductAttribute(8, 9, "2.1kg");
        addProductAttribute(8, 10, "358 x 248 x 20 mm");
        addProductAttribute(8, 11, "1x Thunderbolt 4, 3x USB-A, HDMI");
        addProductAttribute(8, 12, "Wi-Fi 6E, Bluetooth 5.2");
        addProductAttribute(8, 13, "Có đèn nền RGB");
        addProductAttribute(8, 14, "Full HD Webcam");
        addProductAttribute(8, 15, "Stereo Speakers với Hi-Res Audio");
        addProductAttribute(8, 16, "Nhôm");
        addProductAttribute(8, 17, "2 năm");
        // MacBook Pro 16
        addProductAttribute(9, 1, "Apple M1 Pro");
        addProductAttribute(9, 2, "16GB Unified Memory");
        addProductAttribute(9, 3, "512GB SSD");
        addProductAttribute(9, 4, "Apple GPU 16-core");
        addProductAttribute(9, 5, "16 inch");
        addProductAttribute(9, 6, "3456x2234");
        addProductAttribute(9, 7, "macOS Ventura");
        addProductAttribute(9, 8, "100 Wh");
        addProductAttribute(9, 9, "2.2kg");
        addProductAttribute(9, 10, "357.9 x 248.1 x 16.8 mm");
        addProductAttribute(9, 11, "3x Thunderbolt 4, MagSafe 3, HDMI");
        addProductAttribute(9, 12, "Wi-Fi 6, Bluetooth 5.0");
        addProductAttribute(9, 13, "Có đèn nền");
        addProductAttribute(9, 14, "Full HD Webcam");
        addProductAttribute(9, 15, "Stereo Speakers với Spatial Audio");
        addProductAttribute(9, 16, "Nhôm tái chế");
        addProductAttribute(9, 17, "1 năm");

        // Gigabyte Aero 15
        addProductAttribute(10, 1, "Intel Core i9-11900H");
        addProductAttribute(10, 2, "32GB DDR4");
        addProductAttribute(10, 3, "1TB SSD");
        addProductAttribute(10, 4, "NVIDIA GeForce RTX 3080");
        addProductAttribute(10, 5, "15.6 inch");
        addProductAttribute(10, 6, "3840x2160");
        addProductAttribute(10, 7, "Windows 11 Home");
        addProductAttribute(10, 8, "99 Wh");
        addProductAttribute(10, 9, "2.3kg");
        addProductAttribute(10, 10, "356 x 250 x 19 mm");
        addProductAttribute(10, 11, "1x USB-C, 3x USB-A, HDMI");
        addProductAttribute(10, 12, "Wi-Fi 6, Bluetooth 5.2");
        addProductAttribute(10, 13, "Có đèn nền RGB");
        addProductAttribute(10, 14, "HD Webcam");
        addProductAttribute(10, 15, "Stereo Speakers");
        addProductAttribute(10, 16, "Nhôm");
        addProductAttribute(10, 17, "2 năm");

        // Huawei MateBook X Pro
        addProductAttribute(11, 1, "Intel Core i7-1260P");
        addProductAttribute(11, 2, "16GB LPDDR4x");
        addProductAttribute(11, 3, "1TB SSD");
        addProductAttribute(11, 4, "Intel Iris Xe Graphics");
        addProductAttribute(11, 5, "13.9 inch");
        addProductAttribute(11, 6, "3000x2000");
        addProductAttribute(11, 7, "Windows 11 Home");
        addProductAttribute(11, 8, "60 Wh");
        addProductAttribute(11, 9, "1.33kg");
        addProductAttribute(11, 10, "304 x 217 x 14.6 mm");
        addProductAttribute(11, 11, "2x USB-C, 1x USB-A");
        addProductAttribute(11, 12, "Wi-Fi 6, Bluetooth 5.1");
        addProductAttribute(11, 13, "Có đèn nền");
        addProductAttribute(11, 14, "HD Webcam 720p");
        addProductAttribute(11, 15, "Stereo Speakers với Dolby Atmos");
        addProductAttribute(11, 16, "Nhôm");
        addProductAttribute(11, 17, "1 năm");

        // Samsung Galaxy Book Pro
        addProductAttribute(12, 1, "Intel Core i5-1135G7");
        addProductAttribute(12, 2, "8GB DDR4");
        addProductAttribute(12, 3, "256GB SSD");
        addProductAttribute(12, 4, "Intel Iris Xe Graphics");
        addProductAttribute(12, 5, "13.3 inch AMOLED");
        addProductAttribute(12, 6, "1920x1080");
        addProductAttribute(12, 7, "Windows 11 Home");
        addProductAttribute(12, 8, "63 Wh");
        addProductAttribute(12, 9, "0.87kg");
        addProductAttribute(12, 10, "304.4 x 202 x 11.2 mm");
        addProductAttribute(12, 11, "1x USB-C, 2x USB-A, HDMI");
        addProductAttribute(12, 12, "Wi-Fi 6, Bluetooth 5.1");
        addProductAttribute(12, 13, "Có đèn nền");
        addProductAttribute(12, 14, "HD Webcam 720p");
        addProductAttribute(12, 15, "Stereo Speakers với Dolby Atmos");
        addProductAttribute(12, 16, "Nhôm");
        addProductAttribute(12, 17, "1 năm");

        // Microsoft Surface Laptop 4
        addProductAttribute(13, 1, "AMD Ryzen 7 4980U");
        addProductAttribute(13, 2, "16GB DDR4");
        addProductAttribute(13, 3, "512GB SSD");
        addProductAttribute(13, 4, "AMD Radeon Graphics");
        addProductAttribute(13, 5, "15 inch cảm ứng");
        addProductAttribute(13, 6, "2256x1504");
        addProductAttribute(13, 7, "Windows 11 Home");
        addProductAttribute(13, 8, "47.4 Wh");
        addProductAttribute(13, 9, "1.54kg");
        addProductAttribute(13, 10, "339.5 x 244 x 14.5 mm");
        addProductAttribute(13, 11, "1x USB-C, 1x USB-A, Surface Connect");
        addProductAttribute(13, 12, "Wi-Fi 6, Bluetooth 5.0");
        addProductAttribute(13, 13, "Có đèn nền");
        addProductAttribute(13, 14, "HD Webcam 720p");
        addProductAttribute(13, 15, "Stereo Speakers với Dolby Atmos");
        addProductAttribute(13, 16, "Nhôm");
        addProductAttribute(13, 17, "1 năm");

// Tiếp tục từ 14 đến 20...


// Tiếp tục cho các sản phẩm còn lại từ 9 đến 20...


    }
}

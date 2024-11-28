package com.thesearch.mylaptopshop.data;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thesearch.mylaptopshop.model.Attribute;
import com.thesearch.mylaptopshop.model.Category;
import com.thesearch.mylaptopshop.model.Image;
import com.thesearch.mylaptopshop.model.Product;
import com.thesearch.mylaptopshop.model.ProductAttribute;
import com.thesearch.mylaptopshop.model.Role;
import com.thesearch.mylaptopshop.model.User;
import com.thesearch.mylaptopshop.repository.AttributeRepository;
import com.thesearch.mylaptopshop.repository.CategoryRepository;
import com.thesearch.mylaptopshop.repository.ImageRepository;
import com.thesearch.mylaptopshop.repository.ProductAttributeRepository;
import com.thesearch.mylaptopshop.repository.ProductRepository;
import com.thesearch.mylaptopshop.repository.RoleRepository;
import com.thesearch.mylaptopshop.repository.UserRepository;
import com.thesearch.mylaptopshop.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent>{
    private final AttributeRepository attributeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final IProductService productService;
    private final ImageRepository imageRepository;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event){
        Set<String> defaultRoles = Set.of("ROLE_ADMIN","ROLE_USER");
        createDefaultRoleIfNotExists(defaultRoles);
        createDefaultUserIfNotExists();
        
        createDefaultAdminIfNotExists();
        productInit();
        attributeInit();
        productAttributeInit();
        imageInit();
        
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
        addProduct("Dell XPS 13", "Dell", BigDecimal.valueOf(25000000), 30, "Laptop cao cấp, màn hình 13 inch, thiết kế mỏng nhẹ", "Laptop Doanh nhân");
        addProduct("MacBook Air M2", "Apple", BigDecimal.valueOf(30000000), 20, "Laptop mỏng nhẹ, vi xử lý M2 mạnh mẽ", "Laptop Doanh nhân");
        addProduct("HP Spectre x360", "HP", BigDecimal.valueOf(27000000), 25, "Laptop 2 trong 1, màn hình cảm ứng, thiết kế sang trọng", "Laptop Mỏng nhẹ");
        addProduct("Lenovo ThinkPad X1 Carbon", "Lenovo", BigDecimal.valueOf(28000000), 35, "Laptop doanh nhân, bàn phím thoải mái, hiệu năng cao", "Laptop Doanh nhân");
        addProduct("Acer Swift 3", "Acer", BigDecimal.valueOf(19000000), 40, "Laptop mỏng nhẹ, màn hình Full HD", "Laptop Văn phòng");
        addProduct("Asus ZenBook 14", "Asus", BigDecimal.valueOf(24000000), 50, "Laptop siêu nhẹ, vi xử lý Intel Core i7", "Laptop Văn phòng");
        addProduct("Razer Blade 15", "Razer", BigDecimal.valueOf(35000000), 15, "Laptop chơi game, màn hình 15.6 inch, card đồ họa RTX", "Laptop Chơi game");
        addProduct("MSI GS66 Stealth", "MSI", BigDecimal.valueOf(32000000), 10, "Laptop chơi game, thiết kế mỏng và mạnh mẽ", "Laptop Chơi game");
        addProduct("MacBook Pro 16", "Apple", BigDecimal.valueOf(40000000), 20, "Laptop cao cấp, màn hình Retina 16 inch", "Laptop Đồ họa");
        addProduct("Gigabyte Aero 15", "Gigabyte", BigDecimal.valueOf(33000000), 25, "Laptop dành cho sáng tạo nội dung, màn hình 4K", "Laptop Đồ họa");
        addProduct("Huawei MateBook X Pro", "Huawei", BigDecimal.valueOf(27000000), 30, "Laptop siêu mỏng, màn hình 13.9 inch", "Laptop Doanh nhân");
        addProduct("Samsung Galaxy Book Pro", "Samsung", BigDecimal.valueOf(22000000), 40, "Laptop mỏng nhẹ, hiệu năng mạnh mẽ", "Laptop Văn phòng");
        addProduct("Microsoft Surface Laptop 4", "Microsoft", BigDecimal.valueOf(25000000), 35, "Laptop siêu mỏng, màn hình cảm ứng", "Laptop Mỏng nhẹ");
        addProduct("Xiaomi Mi Laptop Pro", "Xiaomi", BigDecimal.valueOf(21000000), 45, "Laptop với màn hình AMOLED 15.6 inch", "Laptop Văn phòng");
        addProduct("Alienware m15", "Alienware", BigDecimal.valueOf(38000000), 20, "Laptop chơi game cao cấp với hiệu suất mạnh mẽ", "Laptop Chơi game");
        addProduct("Acer Predator Helios 300", "Acer", BigDecimal.valueOf(28000000), 25, "Laptop chơi game với GPU RTX 3060", "Laptop Chơi game");
        addProduct("HP Omen 15", "HP", BigDecimal.valueOf(29000000), 30, "Laptop chơi game, màn hình 15.6 inch", "Laptop Chơi game");
        addProduct("Asus ROG Zephyrus G14", "Asus", BigDecimal.valueOf(34000000), 18, "Laptop chơi game siêu nhẹ", "Laptop Chơi game");
        addProduct("Lenovo Legion 5", "Lenovo", BigDecimal.valueOf(30000000), 30, "Laptop chơi game với cấu hình cao", "Laptop Chơi game");
        addProduct("Dell G15", "Dell", BigDecimal.valueOf(25000000), 50, "Laptop chơi game tầm trung, màn hình 15.6 inch", "Laptop Chơi game");
        
        //Laptop Gamming

        addProduct("Asus ROG Strix G16", "Asus", BigDecimal.valueOf(45000000), 25, "Laptop chơi game cao cấp với GPU RTX 4080", "Laptop Chơi game");
        addProduct("MSI Raider GE78 HX", "MSI", BigDecimal.valueOf(58000000), 15, "Laptop chơi game với hiệu suất vượt trội và màn hình 4K", "Laptop Chơi game");
        addProduct("Alienware x17 R2", "Alienware", BigDecimal.valueOf(52000000), 10, "Laptop chơi game cao cấp với thiết kế sang trọng", "Laptop Chơi game");
        addProduct("Acer Nitro 5", "Acer", BigDecimal.valueOf(22000000), 40, "Laptop chơi game giá rẻ với GPU GTX 1650", "Laptop Chơi game");
        addProduct("HP Victus 16", "HP", BigDecimal.valueOf(25000000), 30, "Laptop chơi game tầm trung với hiệu năng ổn định", "Laptop Chơi game");
        addProduct("Dell Alienware m18", "Dell", BigDecimal.valueOf(65000000), 5, "Laptop chơi game với màn hình lớn và cấu hình mạnh mẽ", "Laptop Chơi game");
        addProduct("Lenovo Legion 7i", "Lenovo", BigDecimal.valueOf(40000000), 20, "Laptop chơi game với màn hình QHD và hiệu năng cao", "Laptop Chơi game");
        addProduct("Gigabyte Aorus 17G", "Gigabyte", BigDecimal.valueOf(48000000), 12, "Laptop chơi game với bàn phím cơ và GPU RTX 4070", "Laptop Chơi game");
        addProduct("Asus TUF Gaming F15", "Asus", BigDecimal.valueOf(30000000), 35, "Laptop chơi game bền bỉ với thiết kế quân đội", "Laptop Chơi game");
        addProduct("MSI Stealth 16 Studio", "MSI", BigDecimal.valueOf(52000000), 8, "Laptop chơi game dành cho streamer và nhà sáng tạo", "Laptop Chơi game");
        addProduct("Acer Predator Triton 300", "Acer", BigDecimal.valueOf(37000000), 18, "Laptop chơi game mỏng nhẹ với GPU RTX 3060", "Laptop Chơi game");
        addProduct("Razer Blade 18", "Razer", BigDecimal.valueOf(62000000), 7, "Laptop chơi game cao cấp với màn hình 18 inch", "Laptop Chơi game");
        addProduct("HP Omen Transcend 16", "HP", BigDecimal.valueOf(45000000), 25, "Laptop chơi game với màn hình OLED và hiệu năng mạnh mẽ", "Laptop Chơi game");
        addProduct("Dell G16", "Dell", BigDecimal.valueOf(32000000), 30, "Laptop chơi game tầm trung với thiết kế tinh tế", "Laptop Chơi game");
        addProduct("Lenovo IdeaPad Gaming 3i", "Lenovo", BigDecimal.valueOf(20000000), 50, "Laptop chơi game giá rẻ phù hợp với học sinh, sinh viên", "Laptop Chơi game");
        addProduct("Alienware x15 R1", "Alienware", BigDecimal.valueOf(50000000), 12, "Laptop chơi game mỏng nhẹ, màn hình Full HD 360Hz", "Laptop Chơi game");
        addProduct("Asus ROG Flow X16", "Asus", BigDecimal.valueOf(58000000), 8, "Laptop chơi game 2 trong 1 với màn hình cảm ứng", "Laptop Chơi game");
        addProduct("Gigabyte Aero 16", "Gigabyte", BigDecimal.valueOf(46000000), 15, "Laptop chơi game dành cho thiết kế đồ họa và sáng tạo", "Laptop Chơi game");
        addProduct("HP Pavilion Gaming 15", "HP", BigDecimal.valueOf(23000000), 45, "Laptop chơi game giá rẻ với màn hình 144Hz", "Laptop Chơi game");
        addProduct("Razer Blade Stealth 14", "Razer", BigDecimal.valueOf(54000000), 10, "Laptop chơi game với màn hình QHD 165Hz", "Laptop Chơi game");

        // Laptop Văn phòng

        addProduct("Dell Latitude 7430", "Dell", BigDecimal.valueOf(35000000), 25, "Laptop văn phòng cao cấp, thiết kế bền bỉ và hiệu năng mạnh", "Laptop Văn phòng");
        addProduct("HP EliteBook 840 G9", "HP", BigDecimal.valueOf(38000000), 20, "Laptop văn phòng dành cho doanh nghiệp, bảo mật cao", "Laptop Văn phòng");
        addProduct("Lenovo ThinkPad T14s Gen 3", "Lenovo", BigDecimal.valueOf(37000000), 30, "Laptop văn phòng mỏng nhẹ, bàn phím thoải mái", "Laptop Văn phòng");
        addProduct("MacBook Air M1", "Apple", BigDecimal.valueOf(25000000), 50, "Laptop văn phòng với hiệu năng M1 và pin bền", "Laptop Văn phòng");
        addProduct("Acer Aspire 5", "Acer", BigDecimal.valueOf(15000000), 60, "Laptop văn phòng giá rẻ, hiệu năng ổn định", "Laptop Văn phòng");
        addProduct("Asus VivoBook S15", "Asus", BigDecimal.valueOf(19000000), 40, "Laptop văn phòng mỏng nhẹ, màn hình 15.6 inch", "Laptop Văn phòng");
        addProduct("HP ProBook 450 G9", "HP", BigDecimal.valueOf(22000000), 35, "Laptop văn phòng với thiết kế bền bỉ và hiệu năng tốt", "Laptop Văn phòng");
        addProduct("Dell Vostro 3510", "Dell", BigDecimal.valueOf(18000000), 45, "Laptop văn phòng giá tốt, phù hợp cho học sinh và sinh viên", "Laptop Văn phòng");
        addProduct("Microsoft Surface Laptop Go 2", "Microsoft", BigDecimal.valueOf(23000000), 30, "Laptop văn phòng siêu nhẹ, màn hình cảm ứng", "Laptop Văn phòng");
        addProduct("Huawei MateBook D14", "Huawei", BigDecimal.valueOf(16000000), 50, "Laptop văn phòng với màn hình Full HD, thiết kế mỏng", "Laptop Văn phòng");
        addProduct("Samsung Galaxy Book 2 Pro", "Samsung", BigDecimal.valueOf(32000000), 20, "Laptop văn phòng siêu nhẹ, màn hình AMOLED", "Laptop Văn phòng");
        addProduct("Xiaomi RedmiBook 15", "Xiaomi", BigDecimal.valueOf(14000000), 70, "Laptop văn phòng giá rẻ với hiệu năng đủ dùng", "Laptop Văn phòng");
        addProduct("Asus ExpertBook B5", "Asus", BigDecimal.valueOf(30000000), 15, "Laptop văn phòng cao cấp dành cho doanh nhân", "Laptop Văn phòng");
        addProduct("Lenovo IdeaPad Slim 5", "Lenovo", BigDecimal.valueOf(20000000), 50, "Laptop văn phòng mỏng nhẹ, hiệu năng ổn định", "Laptop Văn phòng");
        addProduct("Dell Inspiron 16 Plus", "Dell", BigDecimal.valueOf(27000000), 25, "Laptop văn phòng với màn hình lớn và chất lượng hiển thị cao", "Laptop Văn phòng");
        addProduct("HP Envy x360", "HP", BigDecimal.valueOf(33000000), 20, "Laptop văn phòng 2 trong 1 với màn hình cảm ứng", "Laptop Văn phòng");
        addProduct("Apple MacBook Pro 13 M2", "Apple", BigDecimal.valueOf(45000000), 10, "Laptop văn phòng cao cấp, vi xử lý M2 mạnh mẽ", "Laptop Văn phòng");
        addProduct("Acer Swift Edge 16", "Acer", BigDecimal.valueOf(26000000), 35, "Laptop văn phòng siêu nhẹ, màn hình OLED", "Laptop Văn phòng");
        addProduct("Lenovo Yoga Slim 7i Carbon", "Lenovo", BigDecimal.valueOf(28000000), 30, "Laptop văn phòng với thiết kế siêu nhẹ và pin lâu", "Laptop Văn phòng");
        addProduct("Microsoft Surface Laptop Studio", "Microsoft", BigDecimal.valueOf(55000000), 5, "Laptop văn phòng cao cấp, màn hình cảm ứng đa năng", "Laptop Văn phòng");

        //Laptop ĐỒ họa

        addProduct("MacBook Pro 16 M2 Max", "Apple", BigDecimal.valueOf(80000000), 10, "Laptop đồ họa cao cấp, màn hình Retina XDR, vi xử lý M2 Max mạnh mẽ", "Laptop Đồ họa");
        addProduct("Dell XPS 17", "Dell", BigDecimal.valueOf(60000000), 20, "Laptop đồ họa với màn hình 4K Ultra HD+, card đồ họa NVIDIA RTX 4060", "Laptop Đồ họa");
        addProduct("HP ZBook Fury G8", "HP", BigDecimal.valueOf(75000000), 8, "Laptop đồ họa chuyên nghiệp, màn hình 15.6 inch, GPU Quadro RTX A2000", "Laptop Đồ họa");
        addProduct("Lenovo ThinkPad P16", "Lenovo", BigDecimal.valueOf(70000000), 15, "Laptop đồ họa với cấu hình mạnh mẽ, màn hình 16 inch 4K", "Laptop Đồ họa");
        addProduct("Asus ProArt Studiobook 16", "Asus", BigDecimal.valueOf(68000000), 12, "Laptop đồ họa dành cho sáng tạo nội dung, màn hình OLED 4K HDR", "Laptop Đồ họa");
        addProduct("Acer ConceptD 7 Ezel", "Acer", BigDecimal.valueOf(72000000), 10, "Laptop đồ họa 2 trong 1 với màn hình cảm ứng 4K và bút stylus", "Laptop Đồ họa");
        addProduct("Gigabyte Aero 16 OLED", "Gigabyte", BigDecimal.valueOf(65000000), 18, "Laptop đồ họa với màn hình OLED, viền siêu mỏng, GPU RTX 4070", "Laptop Đồ họa");
        addProduct("MSI Creator Z16", "MSI", BigDecimal.valueOf(64000000), 15, "Laptop đồ họa với màn hình True Pixel 16:10 QHD+, GPU RTX 4060", "Laptop Đồ họa");
        addProduct("Razer Blade 17 Studio Edition", "Razer", BigDecimal.valueOf(85000000), 7, "Laptop đồ họa siêu cao cấp, màn hình 17 inch 4K 120Hz, GPU RTX 3080 Ti", "Laptop Đồ họa");
        addProduct("Microsoft Surface Studio Laptop", "Microsoft", BigDecimal.valueOf(55000000), 20, "Laptop đồ họa 2 trong 1 với màn hình cảm ứng PixelSense 14.4 inch", "Laptop Đồ họa");
        addProduct("Huawei MateBook X Pro 2024", "Huawei", BigDecimal.valueOf(58000000), 25, "Laptop đồ họa siêu mỏng, màn hình cảm ứng 3K, hiệu năng vượt trội", "Laptop Đồ họa");
        addProduct("Asus ZenBook Pro Duo 14", "Asus", BigDecimal.valueOf(62000000), 12, "Laptop đồ họa hai màn hình 4K OLED, card đồ họa RTX 3050 Ti", "Laptop Đồ họa");
        addProduct("Dell Precision 5570", "Dell", BigDecimal.valueOf(73000000), 9, "Laptop đồ họa chuyên nghiệp, card NVIDIA RTX A3000", "Laptop Đồ họa");
        addProduct("HP Envy 16", "HP", BigDecimal.valueOf(52000000), 18, "Laptop đồ họa với màn hình 16 inch 2.5K, hiệu năng mạnh mẽ", "Laptop Đồ họa");
        addProduct("Lenovo Legion 7i", "Lenovo", BigDecimal.valueOf(60000000), 22, "Laptop đồ họa với GPU RTX 3080, màn hình QHD 165Hz", "Laptop Đồ họa");
        addProduct("Acer Predator Triton 500 SE", "Acer", BigDecimal.valueOf(68000000), 12, "Laptop đồ họa với màn hình WQXGA, hiệu năng tối ưu", "Laptop Đồ họa");
        addProduct("Xiaomi Mi Notebook Pro X 15", "Xiaomi", BigDecimal.valueOf(48000000), 30, "Laptop đồ họa giá tốt với màn hình 3.5K OLED, GPU RTX 3050 Ti", "Laptop Đồ họa");
        addProduct("MSI Stealth GS77", "MSI", BigDecimal.valueOf(70000000), 10, "Laptop đồ họa cao cấp, màn hình 17.3 inch, hiệu năng mạnh", "Laptop Đồ họa");
        addProduct("Razer Blade 15 Advanced Model", "Razer", BigDecimal.valueOf(78000000), 8, "Laptop đồ họa cao cấp, màn hình 4K OLED, card đồ họa RTX 3070 Ti", "Laptop Đồ họa");
        addProduct("Samsung Galaxy Book 3 Ultra", "Samsung", BigDecimal.valueOf(62000000), 20, "Laptop đồ họa với màn hình Dynamic AMOLED 3K, GPU RTX 4060", "Laptop Đồ họa");

        //Laptop Doanh Nhân

        addProduct("Dell Latitude 9420", "Dell", BigDecimal.valueOf(45000000), 20, "Laptop doanh nhân cao cấp, thiết kế mỏng nhẹ, bảo mật tiên tiến", "Laptop Doanh nhân");
        addProduct("Lenovo ThinkPad X1 Carbon Gen 10", "Lenovo", BigDecimal.valueOf(48000000), 15, "Laptop doanh nhân siêu bền, bàn phím thoải mái, hiệu năng mạnh mẽ", "Laptop Doanh nhân");
        addProduct("HP Elite Dragonfly G3", "HP", BigDecimal.valueOf(47000000), 18, "Laptop doanh nhân siêu nhẹ, màn hình cảm ứng, thiết kế sang trọng", "Laptop Doanh nhân");
        addProduct("Asus ExpertBook B9", "Asus", BigDecimal.valueOf(42000000), 25, "Laptop doanh nhân siêu nhẹ, thời lượng pin dài", "Laptop Doanh nhân");
        addProduct("Apple MacBook Air 15 M2", "Apple", BigDecimal.valueOf(38000000), 30, "Laptop doanh nhân với thiết kế tối giản, hiệu năng vượt trội", "Laptop Doanh nhân");
        addProduct("Microsoft Surface Laptop 5", "Microsoft", BigDecimal.valueOf(40000000), 22, "Laptop doanh nhân với màn hình cảm ứng PixelSense và thiết kế tinh tế", "Laptop Doanh nhân");
        addProduct("Samsung Galaxy Book 2 Pro", "Samsung", BigDecimal.valueOf(39000000), 28, "Laptop doanh nhân mỏng nhẹ, màn hình AMOLED chất lượng cao", "Laptop Doanh nhân");
        addProduct("LG Gram 16", "LG", BigDecimal.valueOf(41000000), 25, "Laptop doanh nhân siêu nhẹ với màn hình lớn, pin lâu", "Laptop Doanh nhân");
        addProduct("Huawei MateBook X Pro 2024", "Huawei", BigDecimal.valueOf(45000000), 18, "Laptop doanh nhân cao cấp, màn hình cảm ứng 3K, thiết kế mỏng nhẹ", "Laptop Doanh nhân");
        addProduct("Dell XPS 13 Plus", "Dell", BigDecimal.valueOf(48000000), 10, "Laptop doanh nhân sang trọng, viền màn hình siêu mỏng", "Laptop Doanh nhân");
        addProduct("HP Spectre x360 14", "HP", BigDecimal.valueOf(46000000), 15, "Laptop doanh nhân 2 trong 1, màn hình OLED, hiệu năng cao", "Laptop Doanh nhân");
        addProduct("Lenovo Yoga 9i", "Lenovo", BigDecimal.valueOf(44000000), 20, "Laptop doanh nhân với thiết kế xoay gập, màn hình cảm ứng", "Laptop Doanh nhân");
        addProduct("Asus ZenBook S 13 OLED", "Asus", BigDecimal.valueOf(37000000), 25, "Laptop doanh nhân siêu nhẹ, màn hình OLED cao cấp", "Laptop Doanh nhân");
        addProduct("Acer Swift Edge", "Acer", BigDecimal.valueOf(36000000), 28, "Laptop doanh nhân với thiết kế mỏng nhẹ và thời lượng pin lâu", "Laptop Doanh nhân");
        addProduct("Razer Book 13", "Razer", BigDecimal.valueOf(42000000), 12, "Laptop doanh nhân với hiệu năng mạnh mẽ, thiết kế tối giản", "Laptop Doanh nhân");
        addProduct("Gigabyte U4 UD", "Gigabyte", BigDecimal.valueOf(33000000), 40, "Laptop doanh nhân nhỏ gọn, hiệu năng ổn định", "Laptop Doanh nhân");
        addProduct("MSI Prestige 14 Evo", "MSI", BigDecimal.valueOf(39000000), 20, "Laptop doanh nhân với thiết kế thanh lịch, thời lượng pin dài", "Laptop Doanh nhân");
        addProduct("Toshiba Dynabook Portégé X30", "Toshiba", BigDecimal.valueOf(42000000), 18, "Laptop doanh nhân siêu bền, màn hình Full HD, hiệu năng ổn định", "Laptop Doanh nhân");
        addProduct("Xiaomi Mi Notebook Pro 14", "Xiaomi", BigDecimal.valueOf(34000000), 35, "Laptop doanh nhân mỏng nhẹ, màn hình 2.5K", "Laptop Doanh nhân");
        addProduct("Sony VAIO SX14", "Sony", BigDecimal.valueOf(50000000), 10, "Laptop doanh nhân cao cấp, thiết kế sang trọng, hiệu năng tối ưu", "Laptop Doanh nhân");

        //Laptop Mỏng nhẹ

        addProduct("Dell XPS 13 9310", "Dell", BigDecimal.valueOf(35000000), 30, "Laptop mỏng nhẹ, thiết kế sang trọng, màn hình Full HD+", "Laptop Mỏng nhẹ");
        addProduct("MacBook Air 13 M2", "Apple", BigDecimal.valueOf(33000000), 25, "Laptop mỏng nhẹ, vi xử lý M2, thời lượng pin dài", "Laptop Mỏng nhẹ");
        addProduct("HP Pavilion Aero 13", "HP", BigDecimal.valueOf(24000000), 40, "Laptop mỏng nhẹ với khối lượng dưới 1kg, hiệu năng ổn định", "Laptop Mỏng nhẹ");
        addProduct("Lenovo Yoga Slim 7 Carbon", "Lenovo", BigDecimal.valueOf(31000000), 20, "Laptop mỏng nhẹ, màn hình OLED, viền siêu mỏng", "Laptop Mỏng nhẹ");
        addProduct("Asus ZenBook 13 OLED", "Asus", BigDecimal.valueOf(28000000), 35, "Laptop mỏng nhẹ, màn hình OLED sống động", "Laptop Mỏng nhẹ");
        addProduct("LG Gram 14", "LG", BigDecimal.valueOf(32000000), 25, "Laptop mỏng nhẹ, thời lượng pin lâu, phù hợp cho công việc di chuyển", "Laptop Mỏng nhẹ");
        addProduct("Samsung Galaxy Book Pro 360", "Samsung", BigDecimal.valueOf(30000000), 18, "Laptop mỏng nhẹ, màn hình AMOLED cảm ứng, xoay gập linh hoạt", "Laptop Mỏng nhẹ");
        addProduct("Huawei MateBook 14s", "Huawei", BigDecimal.valueOf(29000000), 28, "Laptop mỏng nhẹ, màn hình 2.5K, âm thanh chất lượng cao", "Laptop Mỏng nhẹ");
        addProduct("Microsoft Surface Laptop Go 3", "Microsoft", BigDecimal.valueOf(27000000), 40, "Laptop mỏng nhẹ, thiết kế đơn giản, phù hợp cho sinh viên", "Laptop Mỏng nhẹ");
        addProduct("Acer Swift 5", "Acer", BigDecimal.valueOf(26000000), 35, "Laptop mỏng nhẹ với hiệu năng mạnh mẽ và màn hình Full HD", "Laptop Mỏng nhẹ");
        addProduct("Razer Book 13", "Razer", BigDecimal.valueOf(35000000), 15, "Laptop mỏng nhẹ, thiết kế kim loại cao cấp, màn hình viền mỏng", "Laptop Mỏng nhẹ");
        addProduct("Xiaomi Mi Notebook Air", "Xiaomi", BigDecimal.valueOf(22000000), 50, "Laptop mỏng nhẹ, màn hình Full HD, phù hợp công việc văn phòng", "Laptop Mỏng nhẹ");
        addProduct("Sony VAIO Z Flip", "Sony", BigDecimal.valueOf(50000000), 12, "Laptop mỏng nhẹ, thiết kế cao cấp, xoay gập màn hình", "Laptop Mỏng nhẹ");
        addProduct("Dell Inspiron 14 Plus", "Dell", BigDecimal.valueOf(29000000), 40, "Laptop mỏng nhẹ, màn hình cảm ứng, thời lượng pin bền bỉ", "Laptop Mỏng nhẹ");
        addProduct("HP Envy 13", "HP", BigDecimal.valueOf(31000000), 25, "Laptop mỏng nhẹ, hiệu năng vượt trội, phù hợp công việc sáng tạo", "Laptop Mỏng nhẹ");
        addProduct("Asus ExpertBook B5", "Asus", BigDecimal.valueOf(27000000), 45, "Laptop mỏng nhẹ, bảo mật cao, hiệu năng ổn định", "Laptop Mỏng nhẹ");
        addProduct("Lenovo IdeaPad Slim 5i", "Lenovo", BigDecimal.valueOf(25000000), 30, "Laptop mỏng nhẹ, thiết kế đơn giản, hiệu năng tối ưu", "Laptop Mỏng nhẹ");
        addProduct("LG Ultra PC 13U70Q", "LG", BigDecimal.valueOf(28000000), 22, "Laptop mỏng nhẹ, màn hình IPS, thời lượng pin dài", "Laptop Mỏng nhẹ");
        addProduct("Samsung Notebook 9 Pen", "Samsung", BigDecimal.valueOf(35000000), 10, "Laptop mỏng nhẹ với bút cảm ứng đi kèm, xoay gập 360 độ", "Laptop Mỏng nhẹ");
        addProduct("Huawei MateBook X", "Huawei", BigDecimal.valueOf(40000000), 15, "Laptop mỏng nhẹ, màn hình cảm ứng 3K, thiết kế siêu mỏng", "Laptop Mỏng nhẹ");

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

        addProductAttribute(14, 1, "Intel Core i5-11300H");
        addProductAttribute(14, 2, "16GB LPDDR4x");
        addProductAttribute(14, 3, "512GB SSD");
        addProductAttribute(14, 4, "Intel Iris Xe Graphics");
        addProductAttribute(14, 5, "15.6 inch");
        addProductAttribute(14, 6, "AMOLED 3.2K (3200 x 2000)");
        addProductAttribute(14, 7, "Windows 11 Home");
        addProductAttribute(14, 8, "70Wh, lên đến 11 giờ sử dụng");
        addProductAttribute(14, 9, "1.8kg");
        addProductAttribute(14, 10, "356.4 x 243.7 x 17.9 mm");
        addProductAttribute(14, 11, "2x USB-C, 1x USB-A, HDMI");
        addProductAttribute(14, 12, "Wi-Fi 6, Bluetooth 5.1");
        addProductAttribute(14, 13, "Bàn phím Chiclet có đèn nền");
        addProductAttribute(14, 14, "HD Webcam 720p");
        addProductAttribute(14, 15, "2 loa stereo 2W");
        addProductAttribute(14, 16, "Kim loại nguyên khối");
        addProductAttribute(14, 17, "12 tháng");

        addProductAttribute(15, 1, "Intel Core i7-11800H");
        addProductAttribute(15, 2, "32GB DDR4");
        addProductAttribute(15, 3, "1TB SSD");
        addProductAttribute(15, 4, "NVIDIA GeForce RTX 3070");
        addProductAttribute(15, 5, "15.6 inch");
        addProductAttribute(15, 6, "Full HD 1920 x 1080, 165Hz");
        addProductAttribute(15, 7, "Windows 11 Pro");
        addProductAttribute(15, 8, "86Wh, lên đến 6 giờ sử dụng");
        addProductAttribute(15, 9, "2.5kg");
        addProductAttribute(15, 10, "356.2 x 272.5 x 19.9 mm");
        addProductAttribute(15, 11, "3x USB-A, HDMI, Thunderbolt 4");
        addProductAttribute(15, 12, "Wi-Fi 6, Bluetooth 5.2");
        addProductAttribute(15, 13, "Bàn phím cơ có đèn RGB");
        addProductAttribute(15, 14, "HD Webcam 720p");
        addProductAttribute(15, 15, "2 loa stereo 3W");
        addProductAttribute(15, 16, "Hợp kim nhôm và magie");
        addProductAttribute(15, 17, "24 tháng");

        addProductAttribute(16, 1, "Intel Core i7-10750H");
        addProductAttribute(16, 2, "16GB DDR4");
        addProductAttribute(16, 3, "512GB SSD");
        addProductAttribute(16, 4, "NVIDIA GeForce RTX 3060");
        addProductAttribute(16, 5, "15.6 inch");
        addProductAttribute(16, 6, "Full HD 1920 x 1080, 144Hz");
        addProductAttribute(16, 7, "Windows 10 Home");
        addProductAttribute(16, 8, "59Wh, lên đến 7 giờ sử dụng");
        addProductAttribute(16, 9, "2.3kg");
        addProductAttribute(16, 10, "363 x 255 x 23.9 mm");
        addProductAttribute(16, 11, "2x USB-A, 1x USB-C, HDMI, Mini DisplayPort");
        addProductAttribute(16, 12, "Wi-Fi 6, Bluetooth 5.1");
        addProductAttribute(16, 13, "Bàn phím RGB tùy chỉnh");
        addProductAttribute(16, 14, "HD Webcam 720p");
        addProductAttribute(16, 15, "2 loa DTS:X Ultra");
        addProductAttribute(16, 16, "Nhựa và kim loại");
        addProductAttribute(16, 17, "12 tháng");

        addProductAttribute(17, 1, "AMD Ryzen 7 5800H");
        addProductAttribute(17, 2, "16GB DDR4");
        addProductAttribute(17, 3, "1TB SSD");
        addProductAttribute(17, 4, "NVIDIA GeForce RTX 3070");
        addProductAttribute(17, 5, "15.6 inch");
        addProductAttribute(17, 6, "Full HD 1920 x 1080, 144Hz");
        addProductAttribute(17, 7, "Windows 10 Home");
        addProductAttribute(17, 8, "70.9Wh, lên đến 8 giờ sử dụng");
        addProductAttribute(17, 9, "2.37kg");
        addProductAttribute(17, 10, "357.9 x 239.7 x 22.6 mm");
        addProductAttribute(17, 11, "2x USB-A, 1x USB-C, HDMI, Mini DisplayPort");
        addProductAttribute(17, 12, "Wi-Fi 6, Bluetooth 5.2");
        addProductAttribute(17, 13, "Bàn phím RGB 4 vùng");
        addProductAttribute(17, 14, "HD Webcam 720p");
        addProductAttribute(17, 15, "2 loa stereo B&O");
        addProductAttribute(17, 16, "Nhôm và nhựa");
        addProductAttribute(17, 17, "24 tháng");

        addProductAttribute(18, 1, "AMD Ryzen 9 5900HS");
        addProductAttribute(18, 2, "32GB DDR4");
        addProductAttribute(18, 3, "1TB SSD");
        addProductAttribute(18, 4, "NVIDIA GeForce RTX 3060");
        addProductAttribute(18, 5, "14 inch");
        addProductAttribute(18, 6, "Full HD 1920 x 1080, 120Hz");
        addProductAttribute(18, 7, "Windows 11 Home");
        addProductAttribute(18, 8, "76Wh, lên đến 10 giờ sử dụng");
        addProductAttribute(18, 9, "1.7kg");
        addProductAttribute(18, 10, "324 x 222 x 17.9 mm");
        addProductAttribute(18, 11, "2x USB-C, 2x USB-A, HDMI");
        addProductAttribute(18, 12, "Wi-Fi 6, Bluetooth 5.1");
        addProductAttribute(18, 13, "Bàn phím có đèn LED trắng");
        addProductAttribute(18, 14, "HD Webcam 720p");
        addProductAttribute(18, 15, "2 loa stereo Dolby Atmos");
        addProductAttribute(18, 16, "Kim loại và nhựa");
        addProductAttribute(18, 17, "12 tháng");

        addProductAttribute(19, 1, "AMD Ryzen 7 4800H");
        addProductAttribute(19, 2, "16GB DDR4");
        addProductAttribute(19, 3, "512GB SSD");
        addProductAttribute(19, 4, "NVIDIA GeForce GTX 1660 Ti");
        addProductAttribute(19, 5, "15.6 inch");
        addProductAttribute(19, 6, "Full HD 1920 x 1080, 120Hz");
        addProductAttribute(19, 7, "Windows 10 Home");
        addProductAttribute(19, 8, "60Wh, lên đến 8 giờ sử dụng");
        addProductAttribute(19, 9, "2.3kg");
        addProductAttribute(19, 10, "363 x 259 x 24.9 mm");
        addProductAttribute(19, 11, "2x USB-C, 3x USB-A, HDMI");
        addProductAttribute(19, 12, "Wi-Fi 5, Bluetooth 5.0");
        addProductAttribute(19, 13, "Bàn phím RGB 4 vùng");
        addProductAttribute(19, 14, "HD Webcam 720p");
        addProductAttribute(19, 15, "2 loa Harman Kardon");
        addProductAttribute(19, 16, "Nhựa ABS cao cấp");
        addProductAttribute(19, 17, "24 tháng");

        addProductAttribute(20, 1, "Intel Core i7-12700H");
        addProductAttribute(20, 2, "16GB DDR4");
        addProductAttribute(20, 3, "512GB SSD");
        addProductAttribute(20, 4, "NVIDIA GeForce GTX 1660 Ti");
        addProductAttribute(20, 5, "15.6 inch Full HD");
        addProductAttribute(20, 6, "1920 x 1080 pixels");
        addProductAttribute(20, 7, "Windows 11 Home");
        addProductAttribute(20, 8, "6 hours");
        addProductAttribute(20, 9, "2.5 kg");
        addProductAttribute(20, 10, "36 x 24 x 2.5 cm");
        addProductAttribute(20, 11, "USB 3.2, HDMI, RJ45");
        addProductAttribute(20, 12, "Wi-Fi 6, Bluetooth 5.1");
        addProductAttribute(20, 13, "Backlit keyboard");
        addProductAttribute(20, 14, "720p HD Webcam");
        addProductAttribute(20, 15, "Stereo speakers");
        addProductAttribute(20, 16, "Plastic and metal");
        addProductAttribute(20, 17, "1 year");

        addProductAttribute(21, 1, "Intel Core i9-13900HX");
        addProductAttribute(21, 2, "32GB DDR5");
        addProductAttribute(21, 3, "1TB SSD");
        addProductAttribute(21, 4, "NVIDIA GeForce RTX 4080");
        addProductAttribute(21, 5, "16 inch QHD+");
        addProductAttribute(21, 6, "2560 x 1600 pixels");
        addProductAttribute(21, 7, "Windows 11 Pro");
        addProductAttribute(21, 8, "7 hours");
        addProductAttribute(21, 9, "2.4 kg");
        addProductAttribute(21, 10, "36 x 26 x 2.4 cm");
        addProductAttribute(21, 11, "USB 4.0, HDMI, Thunderbolt 4");
        addProductAttribute(21, 12, "Wi-Fi 6E, Bluetooth 5.2");
        addProductAttribute(21, 13, "RGB Backlit keyboard");
        addProductAttribute(21, 14, "1080p HD Webcam");
        addProductAttribute(21, 15, "Stereo speakers with Dolby Atmos");
        addProductAttribute(21, 16, "Aluminum chassis");
        addProductAttribute(21, 17, "2 years");

        addProductAttribute(22, 1, "Intel Core i9-12900HX");
        addProductAttribute(22, 2, "64GB DDR5");
        addProductAttribute(22, 3, "2TB SSD");
        addProductAttribute(22, 4, "NVIDIA GeForce RTX 4090");
        addProductAttribute(22, 5, "17.3 inch 4K");
        addProductAttribute(22, 6, "3840 x 2160 pixels");
        addProductAttribute(22, 7, "Windows 11 Pro");
        addProductAttribute(22, 8, "8 hours");
        addProductAttribute(22, 9, "3.1 kg");
        addProductAttribute(22, 10, "39 x 27 x 3 cm");
        addProductAttribute(22, 11, "USB-C, HDMI, DisplayPort");
        addProductAttribute(22, 12, "Wi-Fi 6E, Bluetooth 5.2");
        addProductAttribute(22, 13, "Backlit mechanical keyboard");
        addProductAttribute(22, 14, "1080p HD Webcam");
        addProductAttribute(22, 15, "Dual speakers with DTS:X Ultra");
        addProductAttribute(22, 16, "Steel chassis");
        addProductAttribute(22, 17, "2 years");

        addProductAttribute(23, 1, "Intel Core i7-12700H");
        addProductAttribute(23, 2, "16GB DDR5");
        addProductAttribute(23, 3, "1TB SSD");
        addProductAttribute(23, 4, "NVIDIA GeForce RTX 3080");
        addProductAttribute(23, 5, "17.3 inch Full HD");
        addProductAttribute(23, 6, "1920 x 1080 pixels");
        addProductAttribute(23, 7, "Windows 11 Home");
        addProductAttribute(23, 8, "6 hours");
        addProductAttribute(23, 9, "2.5 kg");
        addProductAttribute(23, 10, "38 x 26 x 2.6 cm");
        addProductAttribute(23, 11, "USB 3.2, HDMI");
        addProductAttribute(23, 12, "Wi-Fi 6, Bluetooth 5.0");
        addProductAttribute(23, 13, "Chiclet keyboard");
        addProductAttribute(23, 14, "720p HD Webcam");
        addProductAttribute(23, 15, "Stereo speakers");
        addProductAttribute(23, 16, "Plastic and aluminum");
        addProductAttribute(23, 17, "1 year");

        addProductAttribute(24, 1, "Intel Core i5-11400H");
        addProductAttribute(24, 2, "8GB DDR4");
        addProductAttribute(24, 3, "512GB SSD");
        addProductAttribute(24, 4, "NVIDIA GTX 1650");
        addProductAttribute(24, 5, "15.6 inch Full HD");
        addProductAttribute(24, 6, "1920 x 1080 pixels");
        addProductAttribute(24, 7, "Windows 11 Home");
        addProductAttribute(24, 8, "5 hours");
        addProductAttribute(24, 9, "2.3 kg");
        addProductAttribute(24, 10, "37 x 25 x 2.3 cm");
        addProductAttribute(24, 11, "USB 3.2, HDMI, RJ45");
        addProductAttribute(24, 12, "Wi-Fi 6, Bluetooth 5.0");
        addProductAttribute(24, 13, "Backlit keyboard");
        addProductAttribute(24, 14, "720p Webcam");
        addProductAttribute(24, 15, "Stereo speakers");
        addProductAttribute(24, 16, "Plastic body");
        addProductAttribute(24, 17, "1 year");

        addProductAttribute(25, 1, "AMD Ryzen 5 5600H");
        addProductAttribute(25, 2, "8GB DDR4");
        addProductAttribute(25, 3, "512GB SSD");
        addProductAttribute(25, 4, "NVIDIA GTX 1650 Ti");
        addProductAttribute(25, 5, "16.1 inch Full HD");
        addProductAttribute(25, 6, "1920 x 1080 pixels");
        addProductAttribute(25, 7, "Windows 11 Home");
        addProductAttribute(25, 8, "5 hours");
        addProductAttribute(25, 9, "2.4 kg");
        addProductAttribute(25, 10, "37.5 x 25 x 2.5 cm");
        addProductAttribute(25, 11, "USB 3.0, HDMI");
        addProductAttribute(25, 12, "Wi-Fi 5, Bluetooth 4.0");
        addProductAttribute(25, 13, "Backlit keyboard");
        addProductAttribute(25, 14, "720p Webcam");
        addProductAttribute(25, 15, "Dual stereo speakers");
        addProductAttribute(25, 16, "Plastic chassis");
        addProductAttribute(25, 17, "1 year");

        addProductAttribute(26, 1, "Intel Core i7-11800H");
        addProductAttribute(26, 2, "16GB DDR4");
        addProductAttribute(26, 3, "1TB SSD");
        addProductAttribute(26, 4, "NVIDIA RTX 3070");
        addProductAttribute(26, 5, "15.6 inch Full HD");
        addProductAttribute(26, 6, "1920 x 1080 pixels");
        addProductAttribute(26, 7, "Windows 10 Pro");
        addProductAttribute(26, 8, "6 hours");
        addProductAttribute(26, 9, "2.2 kg");
        addProductAttribute(26, 10, "36 x 24 x 2.4 cm");
        addProductAttribute(26, 11, "USB 3.1, HDMI");
        addProductAttribute(26, 12, "Wi-Fi 6, Bluetooth 5.0");
        addProductAttribute(26, 13, "RGB Backlit keyboard");
        addProductAttribute(26, 14, "1080p Webcam");
        addProductAttribute(26, 15, "Stereo speakers with Dolby Audio");
        addProductAttribute(26, 16, "Metallic chassis");
        addProductAttribute(26, 17, "2 years");

        addProductAttribute(27, 1, "Intel Core i7-11800H");
        addProductAttribute(27, 2, "16GB DDR4");
        addProductAttribute(27, 3, "1TB SSD");
        addProductAttribute(27, 4, "NVIDIA RTX 3070");
        addProductAttribute(27, 5, "15.6 inch Full HD");
        addProductAttribute(27, 6, "1920 x 1080 pixels");
        addProductAttribute(27, 7, "Windows 11 Home");
        addProductAttribute(27, 8, "7 hours");
        addProductAttribute(27, 9, "2.3 kg");
        addProductAttribute(27, 10, "37 x 25 x 2.5 cm");
        addProductAttribute(27, 11, "USB 3.2, HDMI");
        addProductAttribute(27, 12, "Wi-Fi 6, Bluetooth 5.0");
        addProductAttribute(27, 13, "Backlit keyboard");
        addProductAttribute(27, 14, "720p Webcam");
        addProductAttribute(27, 15, "Stereo speakers");
        addProductAttribute(27, 16, "Aluminum body");
        addProductAttribute(27, 17, "1 year");

        addProductAttribute(28, 1, "Intel Core i9-12900H");
        addProductAttribute(28, 2, "32GB DDR5");
        addProductAttribute(28, 3, "1TB SSD");
        addProductAttribute(28, 4, "NVIDIA GeForce RTX 4070");
        addProductAttribute(28, 5, "17.3 inch Full HD");
        addProductAttribute(28, 6, "1920 x 1080 pixels");
        addProductAttribute(28, 7, "Windows 11 Pro");
        addProductAttribute(28, 8, "8 hours");
        addProductAttribute(28, 9, "2.9 kg");
        addProductAttribute(28, 10, "38 x 26 x 2.8 cm");
        addProductAttribute(28, 11, "USB-C, HDMI, Thunderbolt");
        addProductAttribute(28, 12, "Wi-Fi 6E, Bluetooth 5.2");
        addProductAttribute(28, 13, "RGB mechanical keyboard");
        addProductAttribute(28, 14, "1080p HD Webcam");
        addProductAttribute(28, 15, "Stereo speakers with Dolby Atmos");
        addProductAttribute(28, 16, "Metallic chassis");
        addProductAttribute(28, 17, "2 years");

        addProductAttribute(29, 1, "Intel Core i7-12700H");
        addProductAttribute(29, 2, "16GB DDR5");
        addProductAttribute(29, 3, "512GB SSD");
        addProductAttribute(29, 4, "NVIDIA GeForce RTX 3060");
        addProductAttribute(29, 5, "15.6 inch Full HD");
        addProductAttribute(29, 6, "1920 x 1080 pixels");
        addProductAttribute(29, 7, "Windows 11 Home");
        addProductAttribute(29, 8, "5 hours");
        addProductAttribute(29, 9, "2.5 kg");
        addProductAttribute(29, 10, "37.8 x 25.5 x 2.5 cm");
        addProductAttribute(29, 11, "USB 3.1, HDMI");
        addProductAttribute(29, 12, "Wi-Fi 6, Bluetooth 5.0");
        addProductAttribute(29, 13, "Chiclet keyboard");
        addProductAttribute(29, 14, "720p Webcam");
        addProductAttribute(29, 15, "Stereo speakers");
        addProductAttribute(29, 16, "Plastic and aluminum");
        addProductAttribute(29, 17, "1 year");

        addProductAttribute(30, 1, "AMD Ryzen 7 5800H");
        addProductAttribute(30, 2, "16GB DDR4");
        addProductAttribute(30, 3, "1TB SSD");
        addProductAttribute(30, 4, "NVIDIA GeForce RTX 3070");
        addProductAttribute(30, 5, "16.1 inch Full HD");
        addProductAttribute(30, 6, "1920 x 1080 pixels");
        addProductAttribute(30, 7, "Windows 11 Home");
        addProductAttribute(30, 8, "6 hours");
        addProductAttribute(30, 9, "2.4 kg");
        addProductAttribute(30, 10, "37.4 x 25.2 x 2.3 cm");
        addProductAttribute(30, 11, "USB 3.0, HDMI");
        addProductAttribute(30, 12, "Wi-Fi 5, Bluetooth 4.2");
        addProductAttribute(30, 13, "Backlit keyboard");
        addProductAttribute(30, 14, "720p Webcam");
        addProductAttribute(30, 15, "Dual stereo speakers");
        addProductAttribute(30, 16, "Plastic body");
        addProductAttribute(30, 17, "1 year");

        addProductAttribute(31, 1, "Intel Core i7-11800H");
        addProductAttribute(31, 2, "16GB DDR4");
        addProductAttribute(31, 3, "1TB SSD");
        addProductAttribute(31, 4, "NVIDIA GeForce RTX 3070");
        addProductAttribute(31, 5, "16 inch QHD");
        addProductAttribute(31, 6, "2560 x 1600 pixels");
        addProductAttribute(31, 7, "Windows 10 Pro");
        addProductAttribute(31, 8, "5 hours");
        addProductAttribute(31, 9, "2.6 kg");
        addProductAttribute(31, 10, "37.9 x 25.4 x 2.4 cm");
        addProductAttribute(31, 11, "USB 3.0, HDMI");
        addProductAttribute(31, 12, "Wi-Fi 6, Bluetooth 5.0");
        addProductAttribute(31, 13, "Mechanical keyboard");
        addProductAttribute(31, 14, "1080p Webcam");
        addProductAttribute(31, 15, "Stereo speakers with Dolby Atmos");
        addProductAttribute(31, 16, "Aluminum body");
        addProductAttribute(31, 17, "1 year");

        addProductAttribute(32, 1, "Intel Core i9-12900H");
        addProductAttribute(32, 2, "64GB DDR5");
        addProductAttribute(32, 3, "2TB SSD");
        addProductAttribute(32, 4, "NVIDIA GeForce RTX 4080");
        addProductAttribute(32, 5, "17.3 inch 4K");
        addProductAttribute(32, 6, "3840 x 2160 pixels");
        addProductAttribute(32, 7, "Windows 11 Pro");
        addProductAttribute(32, 8, "8 hours");
        addProductAttribute(32, 9, "3.0 kg");
        addProductAttribute(32, 10, "38.5 x 27.0 x 2.8 cm");
        addProductAttribute(32, 11, "USB 4.0, HDMI");
        addProductAttribute(32, 12, "Wi-Fi 6E, Bluetooth 5.2");
        addProductAttribute(32, 13, "RGB mechanical keyboard");
        addProductAttribute(32, 14, "1080p Webcam");
        addProductAttribute(32, 15, "Stereo speakers with Dolby Atmos");
        addProductAttribute(32, 16, "Steel chassis");
        addProductAttribute(32, 17, "2 years");

        addProductAttribute(33, 1, "AMD Ryzen 7 5800H");
        addProductAttribute(33, 2, "16GB DDR4");
        addProductAttribute(33, 3, "1TB SSD");
        addProductAttribute(33, 4, "NVIDIA GeForce RTX 3070");
        addProductAttribute(33, 5, "15.6 inch Full HD");
        addProductAttribute(33, 6, "1920 x 1080 pixels");
        addProductAttribute(33, 7, "Windows 10 Home");
        addProductAttribute(33, 8, "6 hours");
        addProductAttribute(33, 9, "2.4 kg");
        addProductAttribute(33, 10, "36.8 x 25.0 x 2.2 cm");
        addProductAttribute(33, 11, "USB 3.1, HDMI");
        addProductAttribute(33, 12, "Wi-Fi 6, Bluetooth 5.0");
        addProductAttribute(33, 13, "Backlit keyboard");
        addProductAttribute(33, 14, "720p Webcam");
        addProductAttribute(33, 15, "Stereo speakers");
        addProductAttribute(33, 16, "Plastic body");
        addProductAttribute(33, 17, "1 year");

        addProductAttribute(34, 1, "Intel Core i9-12900K");
        addProductAttribute(34, 2, "64GB DDR5");
        addProductAttribute(34, 3, "2TB SSD");
        addProductAttribute(34, 4, "NVIDIA GeForce RTX 4080");
        addProductAttribute(34, 5, "16 inch QHD");
        addProductAttribute(34, 6, "2560 x 1600 pixels");
        addProductAttribute(34, 7, "Windows 11 Pro");
        addProductAttribute(34, 8, "7 hours");
        addProductAttribute(34, 9, "2.9 kg");
        addProductAttribute(34, 10, "38 x 27 x 2.7 cm");
        addProductAttribute(34, 11, "USB 4.0, HDMI");
        addProductAttribute(34, 12, "Wi-Fi 6E, Bluetooth 5.2");
        addProductAttribute(34, 13, "RGB mechanical keyboard");
        addProductAttribute(34, 14, "1080p Webcam");
        addProductAttribute(34, 15, "Stereo speakers with Dolby Atmos");
        addProductAttribute(34, 16, "Metallic chassis");
        addProductAttribute(34, 17, "2 years");

        addProductAttribute(35, 1, "Intel Core i5-12400H");
        addProductAttribute(35, 2, "8GB DDR4");
        addProductAttribute(35, 3, "512GB SSD");
        addProductAttribute(35, 4, "NVIDIA GTX 1650");
        addProductAttribute(35, 5, "15.6 inch Full HD");
        addProductAttribute(35, 6, "1920 x 1080 pixels");
        addProductAttribute(35, 7, "Windows 10 Home");
        addProductAttribute(35, 8, "5 hours");
        addProductAttribute(35, 9, "2.2 kg");
        addProductAttribute(35, 10, "37.0 x 25.2 x 2.3 cm");
        addProductAttribute(35, 11, "USB 3.2, HDMI");
        addProductAttribute(35, 12, "Wi-Fi 5, Bluetooth 4.2");
        addProductAttribute(35, 13, "Chiclet keyboard");
        addProductAttribute(35, 14, "720p Webcam");
        addProductAttribute(35, 15, "Stereo speakers");
        addProductAttribute(35, 16, "Plastic body");
        addProductAttribute(35, 17, "1 year");

        addProductAttribute(36, 1, "AMD Ryzen 5 5600X");
        addProductAttribute(36, 2, "16GB DDR4");
        addProductAttribute(36, 3, "1TB SSD");
        addProductAttribute(36, 4, "NVIDIA GTX 1660 Ti");
        addProductAttribute(36, 5, "16 inch Full HD");
        addProductAttribute(36, 6, "1920 x 1080 pixels");
        addProductAttribute(36, 7, "Windows 11 Home");
        addProductAttribute(36, 8, "6 hours");
        addProductAttribute(36, 9, "2.3 kg");
        addProductAttribute(36, 10, "37.5 x 25.5 x 2.6 cm");
        addProductAttribute(36, 11, "USB 3.1, HDMI");
        addProductAttribute(36, 12, "Wi-Fi 6, Bluetooth 5.0");
        addProductAttribute(36, 13, "Backlit keyboard");
        addProductAttribute(36, 14, "720p Webcam");
        addProductAttribute(36, 15, "Stereo speakers");
        addProductAttribute(36, 16, "Aluminum body");
        addProductAttribute(36, 17, "1 year");

        addProductAttribute(37, 1, "Intel Core i7-11700H");
        addProductAttribute(37, 2, "16GB DDR4");
        addProductAttribute(37, 3, "1TB SSD");
        addProductAttribute(37, 4, "NVIDIA RTX 3060");
        addProductAttribute(37, 5, "15.6 inch Full HD");
        addProductAttribute(37, 6, "1920 x 1080 pixels");
        addProductAttribute(37, 7, "Windows 11 Home");
        addProductAttribute(37, 8, "6 hours");
        addProductAttribute(37, 9, "2.3 kg");
        addProductAttribute(37, 10, "36.0 x 25.0 x 2.5 cm");
        addProductAttribute(37, 11, "USB 3.2, HDMI");
        addProductAttribute(37, 12, "Wi-Fi 6, Bluetooth 5.0");
        addProductAttribute(37, 13, "Mechanical keyboard");
        addProductAttribute(37, 14, "1080p Webcam");
        addProductAttribute(37, 15, "Stereo speakers");
        addProductAttribute(37, 16, "Plastic and aluminum");
        addProductAttribute(37, 17, "2 years");

        addProductAttribute(38, 1, "Intel Core i9-11900H");
        addProductAttribute(38, 2, "32GB DDR4");
        addProductAttribute(38, 3, "1TB SSD");
        addProductAttribute(38, 4, "NVIDIA GeForce RTX 3080");
        addProductAttribute(38, 5, "17.3 inch 4K");
        addProductAttribute(38, 6, "3840 x 2160 pixels");
        addProductAttribute(38, 7, "Windows 11 Pro");
        addProductAttribute(38, 8, "8 hours");
        addProductAttribute(38, 9, "3.1 kg");
        addProductAttribute(38, 10, "39.0 x 27.5 x 3.1 cm");
        addProductAttribute(38, 11, "USB 4.0, HDMI");
        addProductAttribute(38, 12, "Wi-Fi 6E, Bluetooth 5.2");
        addProductAttribute(38, 13, "RGB Backlit keyboard");
        addProductAttribute(38, 14, "1080p Webcam");
        addProductAttribute(38, 15, "Dual stereo speakers with Dolby Atmos");
        addProductAttribute(38, 16, "Steel chassis");
        addProductAttribute(38, 17, "2 years");

        addProductAttribute(39, 1, "Intel Core i5-11400H");
        addProductAttribute(39, 2, "8GB DDR4");
        addProductAttribute(39, 3, "512GB SSD");
        addProductAttribute(39, 4, "NVIDIA GTX 1650 Ti");
        addProductAttribute(39, 5, "15.6 inch Full HD");
        addProductAttribute(39, 6, "1920 x 1080 pixels");
        addProductAttribute(39, 7, "Windows 10 Home");
        addProductAttribute(39, 8, "5 hours");
        addProductAttribute(39, 9, "2.2 kg");
        addProductAttribute(39, 10, "37.4 x 25.4 x 2.3 cm");
        addProductAttribute(39, 11, "USB 3.2, HDMI");
        addProductAttribute(39, 12, "Wi-Fi 5, Bluetooth 4.2");
        addProductAttribute(39, 13, "Chiclet keyboard");
        addProductAttribute(39, 14, "720p Webcam");
        addProductAttribute(39, 15, "Stereo speakers");
        addProductAttribute(39, 16, "Plastic body");
        addProductAttribute(39, 17, "1 year");

        addProductAttribute(40, 1, "AMD Ryzen 7 5800X");
        addProductAttribute(40, 2, "32GB DDR5");
        addProductAttribute(40, 3, "2TB SSD");
        addProductAttribute(40, 4, "NVIDIA RTX 3080");
        addProductAttribute(40, 5, "16.1 inch Full HD");
        addProductAttribute(40, 6, "1920 x 1080 pixels");
        addProductAttribute(40, 7, "Windows 11 Pro");
        addProductAttribute(40, 8, "7 hours");
        addProductAttribute(40, 9, "2.8 kg");
        addProductAttribute(40, 10, "38 x 26 x 2.7 cm");
        addProductAttribute(40, 11, "USB 3.1, HDMI");
        addProductAttribute(40, 12, "Wi-Fi 6, Bluetooth 5.1");
        addProductAttribute(40, 13, "RGB Backlit keyboard");
        addProductAttribute(40, 14, "1080p Webcam");
        addProductAttribute(40, 15, "Dual stereo speakers");
        addProductAttribute(40, 16, "Metallic chassis");
        addProductAttribute(40, 17, "2 years");








        

    }
    private void uploadImage(long productId, String filePath){
        try{
            Product product = productService.getProductById(productId);
            Image image1 = new Image();
            String filePath1 = filePath +"1.png";
            Path path1 = Paths.get(filePath1);
            byte[]  imageData1 = Files.readAllBytes(path1);
            image1.setFileName("laptop image");
            image1.setFileType("image/png");
            image1.setImage(new SerialBlob(imageData1));
            image1.setProduct(product);
            String downloadUrl1 = "/api/v1/images/image/download/"+(productId*2-1);
            image1.setDownloadUrl(downloadUrl1);
            imageRepository.save(image1);
            Image image2 = new Image();
            String filePath2 = filePath +"2.png";
            Path path2 = Paths.get(filePath2);
            byte[]  imageData2 = Files.readAllBytes(path2);
            image2.setFileName("laptop image");
            image2.setFileType("image/png");
            image2.setImage(new SerialBlob(imageData2));
            image2.setProduct(product);
            String downloadUrl2 = "/api/v1/images/image/download/"+(productId*2);
            image2.setDownloadUrl(downloadUrl2);
            imageRepository.save(image2);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return;

        }
    }
    private void imageInit(){
        for(int i = 0;i<=80;i+=20){
            uploadImage(i+1, "src/main/resources/static/dellxps13");
            uploadImage(i+2, "src/main/resources/static/macbookairm2");
            uploadImage(i+3, "src/main/resources/static/hpspectrex360");
            uploadImage(i+4, "src/main/resources/static/thinkpadx1carbon");
            uploadImage(i+5, "src/main/resources/static/acerswift3");
            uploadImage(i+6, "src/main/resources/static/asuszenbook14");
            uploadImage(i+7, "src/main/resources/static/razerblade15");
            uploadImage(i+8, "src/main/resources/static/msigs66stealth");
            uploadImage(i+9, "src/main/resources/static/macbookpro16");
            uploadImage(i+10, "src/main/resources/static/gigabyteaero15");
            uploadImage(i+11, "src/main/resources/static/huaweimatebookxpro");
            uploadImage(i+12, "src/main/resources/static/samsunggalaxybookpro");
            uploadImage(i+13, "src/main/resources/static/microsoftsurfacelaptop4");
            uploadImage(i+14, "src/main/resources/static/xiaomimilaptoppro");
            uploadImage(i+15, "src/main/resources/static/alienwarem15");
            uploadImage(i+16, "src/main/resources/static/acerpredatorhelios300");
            uploadImage(i+17, "src/main/resources/static/hpomen15");
            uploadImage(i+18, "src/main/resources/static/asusrogzephyrusg14");
            uploadImage(i+19, "src/main/resources/static/lenovolegion5");
            uploadImage(i+20, "src/main/resources/static/dellg15");
        }


    }
}
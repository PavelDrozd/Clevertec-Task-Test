package ru.clevertec.product.data;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

@Builder(setterPrefix = "with")
@Data
public class ProductTestData {

    @Builder.Default
    private UUID uuid = UUID.fromString("59006a5d-ec94-4aa4-a151-30307ebc32c6");

    @Builder.Default
    private String name = "Пицца";

    @Builder.Default
    private String description = "Классическая итальянская с твёрдым тестом";

    @Builder.Default
    private BigDecimal price = BigDecimal.valueOf(10.24);

    @Builder.Default
    private LocalDateTime created = LocalDateTime.of(
            2023, Month.OCTOBER, 27, 10, 28, 10);

    public Product buildProduct() {
        return new Product(uuid, name, description, price, created);
    }

    public ProductDto buildProductDto() {
        return new ProductDto(name, description, price);
    }

    public InfoProductDto buildInfoProductDto() {
        return new InfoProductDto(uuid, name, description, price);
    }
}

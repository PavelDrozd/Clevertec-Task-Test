package ru.clevertec.product.data;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
public class ProductTestBuilder {

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

    public List<Product> buildListOfProducts() {
        return List.of(
                ProductTestBuilder.builder().build().buildProduct(),
                ProductTestBuilder.builder()
                        .withUuid(UUID.fromString("75e0abb5-c38c-41f4-ad0c-8067273eb3aa"))
                        .withName("Роллы")
                        .withDescription("Очень даже вкусные")
                        .withPrice(BigDecimal.valueOf(7.99))
                        .build().buildProduct(),
                ProductTestBuilder.builder()
                        .withUuid(UUID.fromString("a68ed10d-7adf-4014-aba8-3c4f80092da3"))
                        .withName("Томат")
                        .withDescription("Хорош для салатика")
                        .withPrice(BigDecimal.valueOf(0.33))
                        .build().buildProduct());
    }

    public List<InfoProductDto> buildListOfInfoProductDto() {
        return List.of(
                ProductTestBuilder.builder().build().buildInfoProductDto(),
                ProductTestBuilder.builder()
                        .withUuid(UUID.fromString("75e0abb5-c38c-41f4-ad0c-8067273eb3aa"))
                        .withName("Роллы")
                        .withDescription("Очень даже вкусные")
                        .withPrice(BigDecimal.valueOf(7.99))
                        .build().buildInfoProductDto(),
                ProductTestBuilder.builder()
                        .withUuid(UUID.fromString("a68ed10d-7adf-4014-aba8-3c4f80092da3"))
                        .withName("Томат")
                        .withDescription("Хорош для салатика")
                        .withPrice(BigDecimal.valueOf(0.33))
                        .build().buildInfoProductDto());
    }
}

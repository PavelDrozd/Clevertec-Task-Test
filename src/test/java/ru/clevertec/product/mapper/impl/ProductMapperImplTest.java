package ru.clevertec.product.mapper.impl;

import org.junit.jupiter.api.Test;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.data.ProductTestData;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.mapper.ProductMapperImpl;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductMapperImplTest {

    private ProductMapper productMapper = new ProductMapperImpl();

    @Test
    public void toProductShouldReturnProductWithoutUuid() {
        // given
        ProductDto productDto = ProductTestData.builder().build().buildProductDto();
        Product expected = ProductTestData.builder()
                .withUuid(null)
                .build().buildProduct();

        // when
        Product actual = productMapper.toProduct(productDto);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice());
    }

    @Test
    public void toInfoProductDtoShouldReturnInfoProductDtoWithUuid() {
        // given
        InfoProductDto expected = ProductTestData.builder().build().buildInfoProductDto();
        Product product = ProductTestData.builder().build().buildProduct();

        // when
        InfoProductDto actual = productMapper.toInfoProductDto(product);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.uuid())
                .hasFieldOrPropertyWithValue(Product.Fields.name, expected.name())
                .hasFieldOrPropertyWithValue(Product.Fields.description, expected.description())
                .hasFieldOrPropertyWithValue(Product.Fields.price, expected.price());
    }

    @Test
    public void mergeShouldReturnExpectedProductWithMergedNameAndDescriptionAndPrice() {
        // given
        Product expected = ProductTestData.builder().build().buildProduct();
        Product product = ProductTestData.builder()
                .withName("Компоте")
                .withDescription("Чёто на умном")
                .withPrice(BigDecimal.valueOf(33.3))
                .build().buildProduct();
        ProductDto productDto = ProductTestData.builder().build().buildProductDto();

        // when
        Product actual = productMapper.merge(product, productDto);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice())
                .hasFieldOrPropertyWithValue(Product.Fields.created, expected.getCreated());
    }


}

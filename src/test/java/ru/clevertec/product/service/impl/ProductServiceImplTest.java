package ru.clevertec.product.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.data.ProductTestData;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductMapper productMapper;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;

    @Captor
    private ArgumentCaptor<Product> captor;

    @Test
    public void get() {
        // given
        UUID uuid = UUID.fromString("59006a5d-ec94-4aa4-a151-30307ebc32c9");

        // when
        Throwable actual = catchThrowable(() -> productService.get(uuid));

        // then
        assertThat(actual)
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    public void getAll() {
        // given
        List<Product> products = List.of(
                ProductTestData.builder().build().buildProduct(),
                ProductTestData.builder()
                        .withUuid(UUID.fromString("75e0abb5-c38c-41f4-ad0c-8067273eb3aa"))
                        .withName("Роллы")
                        .withDescription("Очень даже вкусные")
                        .withPrice(BigDecimal.valueOf(7.99))
                        .build().buildProduct(),
                ProductTestData.builder()
                        .withUuid(UUID.fromString("a68ed10d-7adf-4014-aba8-3c4f80092da3"))
                        .withName("Томат")
                        .withDescription("Хорош для салатика")
                        .withPrice(BigDecimal.valueOf(0.33))
                        .build().buildProduct());
        InfoProductDto infoProductDto1 = ProductTestData.builder().build().buildInfoProductDto();
        InfoProductDto infoProductDto2 = ProductTestData.builder()
                .withUuid(UUID.fromString("75e0abb5-c38c-41f4-ad0c-8067273eb3aa"))
                .withName("Роллы")
                .withDescription("Очень даже вкусные")
                .withPrice(BigDecimal.valueOf(7.99))
                .build().buildInfoProductDto();
        InfoProductDto infoProductDto3 = ProductTestData.builder()
                .withUuid(UUID.fromString("a68ed10d-7adf-4014-aba8-3c4f80092da3"))
                .withName("Томат")
                .withDescription("Хорош для салатика")
                .withPrice(BigDecimal.valueOf(0.33))
                .build().buildInfoProductDto();
        List<InfoProductDto> expected = List.of(
                ProductTestData.builder().build().buildInfoProductDto(),
                ProductTestData.builder()
                        .withUuid(UUID.fromString("75e0abb5-c38c-41f4-ad0c-8067273eb3aa"))
                        .withName("Роллы")
                        .withDescription("Очень даже вкусные")
                        .withPrice(BigDecimal.valueOf(7.99))
                        .build().buildInfoProductDto(),
                ProductTestData.builder()
                        .withUuid(UUID.fromString("a68ed10d-7adf-4014-aba8-3c4f80092da3"))
                        .withName("Томат")
                        .withDescription("Хорош для салатика")
                        .withPrice(BigDecimal.valueOf(0.33))
                        .build().buildInfoProductDto());
        Mockito.when(productRepository.findAll()).thenReturn(products);
        Mockito.when(productMapper.toInfoProductDto(Mockito.any(Product.class))).thenReturn(
                infoProductDto1, infoProductDto2, infoProductDto3);

        // when
        List<InfoProductDto> actual = productService.getAll();

        // then
        assertThat(actual)
                .hasSameElementsAs(expected);
    }

    @Test
    public void create() {
        // given
        Product productToSave = ProductTestData.builder()
                .withUuid(null)
                .build().buildProduct();
        Product expected = ProductTestData.builder().build().buildProduct();
        ProductDto productDto = ProductTestData.builder().build().buildProductDto();
        Mockito.when(productMapper.toProduct(productDto))
                .thenReturn(productToSave);
        Mockito.doReturn(expected)
                .when(productRepository).save(productToSave);

        // when
        productService.create(productDto);

        // then
        verify(productRepository).save(captor.capture());
        Product actual = captor.getValue();
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, null);

    }

    @Test
    public void updateShouldInvokeRepositoryMethodSaveOneTime() {
        // given
        ProductDto productDto = ProductTestData.builder().build().buildProductDto();
        Product productAfterMapping = ProductTestData.builder()
                .withUuid(null)
                .withCreated(null)
                .build().buildProduct();
        UUID uuid = UUID.fromString("59006a5d-ec94-4aa4-a151-30307ebc32c6");
        Product productToSave = ProductTestData.builder()
                .withCreated(null)
                .build().buildProduct();
        Product expected = ProductTestData.builder().build().buildProduct();
        Mockito.when(productMapper.toProduct(productDto)).thenReturn(productAfterMapping);
        Mockito.when(productRepository.save(productToSave)).thenReturn(expected);

        // when
        productService.update(uuid, productDto);

        // then
        verify(productRepository)
                .save(captor.capture());
        Product actual = captor.getValue();
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice());
    }

    @Test
    public void deleteShouldInvokeRepositoryMethodDeleteOneTime() {
        // given
        UUID uuid = UUID.fromString("59006a5d-ec94-4aa4-a151-30307ebc32c6");

        // when
        productService.delete(uuid);

        // then
        verify(productRepository)
                .delete(uuid);
    }
}

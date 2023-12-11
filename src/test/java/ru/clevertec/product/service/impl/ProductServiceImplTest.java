package ru.clevertec.product.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.data.ProductTestBuilder;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.product.constant.ProductTestConstant.PRODUCT_TEST_UUID;

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
        UUID uuid = PRODUCT_TEST_UUID;

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
        List<Product> products = ProductTestBuilder.builder().build().buildListOfProducts();
        List<InfoProductDto> infoProductDtos = ProductTestBuilder.builder().build().buildListOfInfoProductDto();
        InfoProductDto infoProductDto1 = infoProductDtos.get(0);
        InfoProductDto infoProductDto2 = infoProductDtos.get(1);
        InfoProductDto infoProductDto3 = infoProductDtos.get(2);
        List<InfoProductDto> expected = ProductTestBuilder.builder().build().buildListOfInfoProductDto();

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toInfoProductDto(any(Product.class))).thenReturn(
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
        ProductDto productDto = ProductTestBuilder.builder().build().buildProductDto();

        Product productToSave = ProductTestBuilder.builder()
                .withUuid(null)
                .build().buildProduct();

        Product expected = ProductTestBuilder.builder().build().buildProduct();

        when(productMapper.toProduct(productDto))
                .thenReturn(productToSave);
        doReturn(expected)
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
        UUID uuid = PRODUCT_TEST_UUID;
        ProductDto productDto = ProductTestBuilder.builder().build().buildProductDto();

        Product productAfterMapping = ProductTestBuilder.builder()
                .withUuid(null)
                .withCreated(null)
                .build().buildProduct();
        Product productToSave = ProductTestBuilder.builder()
                .withCreated(null)
                .build().buildProduct();

        Product expected = ProductTestBuilder.builder().build().buildProduct();

        when(productMapper.toProduct(productDto)).thenReturn(productAfterMapping);
        when(productRepository.save(productToSave)).thenReturn(expected);

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
        UUID uuid = PRODUCT_TEST_UUID;

        // when
        productService.delete(uuid);

        // then
        verify(productRepository)
                .delete(uuid);
    }
}

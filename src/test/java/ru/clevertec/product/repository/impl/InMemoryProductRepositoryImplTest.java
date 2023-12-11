package ru.clevertec.product.repository.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.data.ProductTestBuilder;
import ru.clevertec.product.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static ru.clevertec.product.constant.ProductTestConstant.PRODUCT_TEST_UUID;

@ExtendWith(MockitoExtension.class)
public class InMemoryProductRepositoryImplTest {

    @InjectMocks
    private InMemoryProductRepository productRepository;

    @Test
    public void findByIdShouldReturnOptionalOfEmpty() {
        // given
        UUID uuid = PRODUCT_TEST_UUID;

        // when
        Optional<Product> actual = productRepository.findById(uuid);

        // then
        assertThat(actual)
                .isEmpty();
    }

    @Test
    public void findAllShouldReturnListOfElementsTypeOfProduct() {
        // when
        List<Product> actual = productRepository.findAll();

        // then
        assertThat(actual)
                .hasOnlyElementsOfType(Product.class);
    }

    @ParameterizedTest
    @MethodSource("productProvider")
    public void saveShouldReturnSavedExactProduct(Product expected) {
        // when
        Product actual = productRepository.save(expected);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.getUuid())
                .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice())
                .hasFieldOrPropertyWithValue(Product.Fields.created, expected.getCreated());
    }

    @Test
    public void deleteShouldInvokeMethodOneTime() {
        // given
        UUID uuid = PRODUCT_TEST_UUID;

        // when, then
        assertDoesNotThrow(
                () -> productRepository.delete(uuid));
    }

    public static Stream<Product> productProvider() {
        return ProductTestBuilder.builder().build().buildListOfProducts().stream();
    }
}

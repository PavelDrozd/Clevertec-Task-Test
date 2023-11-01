package ru.clevertec.product.repository.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.data.ProductTestData;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class InMemoryProductRepositoryImplTest {

    @Spy
    private InMemoryProductRepository productRepository;

    @Test
    public void findByIdShouldReturnOptionalOfEmpty() {
        // given
        UUID uuid = UUID.fromString("59006a5d-ec94-4aa4-a151-30307ebc32c9");

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

    public static Stream<Product> productProvider() {
        return Stream.of(ProductTestData.builder().build().buildProduct(),
                ProductTestData.builder()
                        .withUuid(UUID.fromString("75e0abb5-c38c-41f4-ad0c-8067273eb3aa"))
                        .withName("Роллы")
                        .withDescription("Очень даже вкусные")
                        .withPrice(BigDecimal.valueOf(7.99))
                        .withCreated(LocalDateTime.of(
                                2023, Month.OCTOBER, 27, 17, 20, 0))
                        .build().buildProduct(),
                ProductTestData.builder()
                        .withUuid(UUID.fromString("a68ed10d-7adf-4014-aba8-3c4f80092da3"))
                        .withName("Томат")
                        .withDescription("Хорош для салатика")
                        .withPrice(BigDecimal.valueOf(0.33))
                        .withCreated(LocalDateTime.of(
                                2023, Month.OCTOBER, 26, 12, 0, 0))
                        .build().buildProduct());
    }

    @Test
    public void deleteShouldInvokeMethodOneTime() {
        // given
        UUID uuid = UUID.fromString("59006a5d-ec94-4aa4-a151-30307ebc32c6");

        // when
        productRepository.delete(uuid);

        // then
        verify(productRepository)
                .delete(uuid);
    }
}

import org.mycompany.DeliveryCostCalculationUtils;
import org.mycompany.Size;
import org.mycompany.Workload;
import org.mycompany.exception.ExceptionMessages;
import org.mycompany.exception.FragileFarDeliveryException;
import org.mycompany.exception.NoDistanceException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.mycompany.DeliveryCostCalculationUtils.MIN_DELIVERY_SUM;
import static org.mycompany.Size.LARGE;
import static org.mycompany.Size.SMALL;
import static org.mycompany.Workload.*;

/**
 * Покрываем тестами класс DeliveryCostCalculationUtils (testNg)
 */
public class DeliveryCostCalculationUtilsTest {

    @Test(description = "Использование положительных значений, не хрупкий груз, стоимость доставки больше минимальной")
    void simpleDeliveryCostCalculationTest() {
        double actualDeliveryCost = DeliveryCostCalculationUtils.calculateDeliveryCost(
                25.5,
                LARGE,
                false,
                VERY_HIGH);
        double expectedDeliveryCost = (200 + 200 + 0) * 1.6;

        Assert.assertEquals(actualDeliveryCost, expectedDeliveryCost, "Суммы должны быть одинаковыми");
    }

    @Test(description = "Использование положительных значений, не хрупкий груз, стоимость доставки МЕНЬШЕ минимальной")
    void simpleTooCheapDeliveryTest() {
        double actualDeliveryCost = DeliveryCostCalculationUtils.calculateDeliveryCost(
                1,
                SMALL,
                false,
                OTHER);

        Assert.assertEquals(actualDeliveryCost, MIN_DELIVERY_SUM, "Суммы должны быть одинаковыми");
    }

    @DataProvider
    public Object[][] distanceAndSumDataProvider() {
        return new Object[][]{
                {List.of(1), 1, 50},
                {List.of(2), 8, 100},
                {List.of(3), 25, 200}
        };
    }

    @Test(description = "Проверка подсчёта доставки ХРУПКОГО груза при расстоянии до 30км", dataProvider = "distanceAndSumDataProvider")
    public void fragileDeliveryCostCalculationTest(List<Integer> list, int actualDistanceKm, int expectedDistanceCostRub) {
//        System.out.printf("Параметры расстояния: %s, %s, %s. ", list, actualDistanceKm, expectedDistanceCostRub);

        double actualDeliveryCost = DeliveryCostCalculationUtils.calculateDeliveryCost(
                actualDistanceKm,
                LARGE,
                true,
                VERY_HIGH);
        double calculatedDeliveryCost = (expectedDistanceCostRub + 200 + 300) * 1.6;
        // Math.max() returns the greater of two double values
        double expectedDeliveryCost = Math.max(calculatedDeliveryCost, MIN_DELIVERY_SUM);

        Assert.assertEquals(actualDeliveryCost, expectedDeliveryCost, "Суммы должны быть одинаковыми");
    }

    @DataProvider
    public Object[][] positiveCombinationsProvider() {
        return new Object[][]{
                {List.of(1), 999999999, LARGE, false, VERY_HIGH, 800},
                {List.of(2), 30.01, SMALL, false, HIGH, 560},
                {List.of(3), 35, LARGE, false, INCREASED, 600},
                {List.of(4), 40, SMALL, false, OTHER, 400},
                {List.of(5), 10.001, SMALL, true, OTHER, 600},
                {List.of(6), 15, LARGE, false, VERY_HIGH, 640},
                {List.of(7), 29.99, SMALL, true, HIGH, 840},
                {List.of(8), 30, LARGE, false, INCREASED, 480},
                {List.of(9), 2.001, LARGE, true, INCREASED, 720},
                {List.of(10), 5, SMALL, false, OTHER, 200},
                {List.of(11), 9.99, LARGE, true, VERY_HIGH, 960},
                {List.of(12), 10, SMALL, false, HIGH, 280},
                {List.of(13), 0.001, SMALL, true, HIGH, 630},
                {List.of(14), 1, LARGE, false, INCREASED, 300},
                {List.of(15), 1.99, SMALL, true, OTHER, 450},
                {List.of(16), 2, LARGE, false, VERY_HIGH, 400}
        };
    }

    @Test(description = "Проверка всех возможных положительных комбинаций и граничных значений", dataProvider = "positiveCombinationsProvider")
    void positiveCombinationsAndBoundaryTest(List<Integer> list, double distanceKm, Size size, boolean isFragile, Workload workload, double expectedSum) {
        System.out.printf("Тест с положительными комбинациями: %s, %s, %s, %s, %s, %s. ", list, distanceKm, size, isFragile, workload, expectedSum);

        Assert.assertEquals(DeliveryCostCalculationUtils.calculateDeliveryCost(distanceKm, size, isFragile, workload),
                Math.max(expectedSum, MIN_DELIVERY_SUM),
                "Суммы должны быть одинаковыми");
    }

    @DataProvider
    public Object[][] distanceDataProvider() {
        return new Object[][]{{30.001}, {31}, {46}, {999999999}};
    }

    @Test(description = "Запрос доставки ХРУПКОГО груза на расстояние БОЛЕЕ 30км", dataProvider = "distanceDataProvider")
    void fragileFarDeliveryTest(double distance) {
        try {
            DeliveryCostCalculationUtils.calculateDeliveryCost(
                    distance,
                    LARGE,
                    true,
                    HIGH);
            Assert.fail("Ожидалось исключение FragileFarDeliveryException");
        } catch (FragileFarDeliveryException e) {
            Assert.assertEquals(e.getMessage(), ExceptionMessages.FRAGILE_FAR_DELIVERY_EXCEPTION.getExceptionMessage());
        }
    }

    @DataProvider
    public Object[][] negativeDistanceDataProvider() {
        return new Object[][]{{-10}, {-1}};
    }

    @Test(description = "Запрос доставки на расстояние меньше нуля", dataProvider = "negativeDistanceDataProvider")
    void denyDeliveryTest(int distanceKm) {
        try {
            DeliveryCostCalculationUtils.calculateDeliveryCost(
                    distanceKm,
                    LARGE,
                    true,
                    HIGH);
            Assert.fail("Ожидалось исключение NoDistanceException");
        } catch (NoDistanceException e) {
            Assert.assertEquals(e.getMessage(), ExceptionMessages.NEGATIVE_NUMBER_DISTANCE_EXCEPTION.getExceptionMessage());
        }
    }

    @Test(description = "Запрос доставки на расстояние 0")
    void denyDeliveryTest2() {
        try {
            DeliveryCostCalculationUtils.calculateDeliveryCost(
                    0,
                    LARGE,
                    true,
                    HIGH);
            Assert.fail("Ожидалось исключение NoDistanceException");
        } catch (NoDistanceException e) {
            Assert.assertEquals(e.getMessage(), ExceptionMessages.ZERO_NO_DISTANCE_EXCEPTION.getExceptionMessage());
        }
    }

}

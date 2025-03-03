import org.mycompany.Fraction;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Покрываем тестами класс Fraction (testNg)
 */
public class FractionTest {

    @Test(description = "Сложение двух положительных дробных чисел")
    void simpleFractionTest() {
        Fraction first = new Fraction(1, 2);
        Fraction second = new Fraction(2, 3);
        Fraction expectedSum = new Fraction(7, 6);

        Assert.assertEquals(Fraction.sum(first, second), expectedSum,"Суммы должны быть одинаковыми");
    }

    @Test(description = "Сложение двух положительных дробных чисел с одинаковым знаменателем")
    void simpleFractionTest2() {
        Fraction first = new Fraction(1, 4);
        Fraction second = new Fraction(2, 4);
        Fraction expectedSum = new Fraction(3, 4);

        Assert.assertEquals(Fraction.sum(first, second), expectedSum, "Суммы должны быть одинаковыми");
    }

    @Test(description = "Сложение отрицательных дробных чисел")
    void negativeFractionTest() {
        Fraction first = new Fraction(1, 2);
        Fraction second = new Fraction(-2, 3);
        Fraction expectedSum = new Fraction(-1, 6);

        Assert.assertEquals(Fraction.sum(first, second), expectedSum, "Суммы должны быть одинаковыми");
    }

    @Test(description = "Нулевой числитель")
    void zeroNumFractionTest() {
        Fraction first = new Fraction(1, 2);
        Fraction second = new Fraction(-2, 4);
        Fraction expectedSum = new Fraction(0, 8);

        Assert.assertEquals(Fraction.sum(first, second), expectedSum, "Суммы должны быть одинаковыми");
    }

    @Test(description = "Нулевой знаменатель", expectedExceptions = ArithmeticException.class, expectedExceptionsMessageRegExp = "Cannot divide to zero")
    void zeroDenFractionTest() {
        new Fraction(1, 0);
    }

    @Test(description = "Не заполнены числитель и знаменатель", expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "Values should be not null")
    void nullFractionTest() {
        new Fraction(null, null);
    }

    @Test(description = "Переполнение", expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Common denominator is too big!")
    void longFractionTest() {
        Fraction first = new Fraction(1, 2);
        Fraction second = new Fraction(1, Integer.MAX_VALUE);

        Fraction.sum(first, second);
    }

}

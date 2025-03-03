package org.mycompany;

import java.util.Objects;

/**
 * Класс для решения задачи для дробных чисел, который работает с дробными числами не как с десятичной дробью, а как с числом, у которого есть числитель и знаменатель.
 * 1/2 + 3/2 = 4/2
 */
public class Fraction {

    // Можно использовать int, будет даже лучше, но мы сделали так, поэтому нужна ещё проверка на 0
    Integer num; // Числитель
    Integer den; // Знаменатель

    // Поля не приватные, но и не публичные, поэтому в тестах нам будет их сложно получить, поэтому пишем геттеры
    public Integer getNum() {
        return num;
    }

    public Integer getDen() {
        return den;
    }

    public Fraction(Integer num, Integer den) {
        if (num == null || den == null) {
            throw new NullPointerException("Values should be not null");
        }
        if (den == 0) {
            throw new ArithmeticException("Cannot divide to zero");
        }
        this.num = num;
        this.den = den;
    }

    /**
     * Метод для подсчёта суммы двух дробных чисел
     *
     * @param first  - первая дробь (числитель + знаменатель)
     * @param second - вторая дробь (числитель + знаменатель)
     * @return - итоговая дробь
     */
    public static Fraction sum(Fraction first, Fraction second) {
        Integer sumNum;
        Integer commonDen;

        if (first.getDen().equals(second.getDen())) {

            // Если у нас общий знаменатель, мы просто складываем числители
            sumNum = first.getNum() + second.getNum();
            commonDen = first.getDen();

        } else {

            // Перемножаем наши знаменатели и должны соответствующим образом перемножить и наши числители.
            // Но в ходе нашего перемножения мы можем выйти за границы int. Поэтому мы проверяем для начала с помощью long
            long bigCommonDen = ((long) first.getDen() * (long) second.getDen());
            if (bigCommonDen > Integer.MAX_VALUE) {
                throw new RuntimeException("Common denominator is too big!");
            }

            // Правило сложения дробей с разными знаменателями:
            // Умножить числитель и знаменатель каждой дроби на дополнительный множитель и сложить дроби с одинаковыми знаменателями
            sumNum = first.getNum() * second.getDen() + second.getNum() * first.getDen();
            commonDen = first.getDen() * second.getDen();

        }

        return new Fraction(sumNum, commonDen);
    }

    @Override
    public String toString() {
        return num + "/" + den;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fraction fraction = (Fraction) o;
        return Objects.equals(num, fraction.num) && Objects.equals(den, fraction.den);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, den);
    }

}

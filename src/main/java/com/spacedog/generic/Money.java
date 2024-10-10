package com.spacedog.generic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

@Getter
public class Money {

    public static final Money ZERO = Money.wons(0);

    private final BigDecimal amount;


    @JsonCreator
    public Money(@JsonProperty("amount") BigDecimal amount) {
        this.amount = amount;
    }



    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money wons(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }



    public static <T> Money sum(Collection<T> bags, Function<T, Money> monetary) {
        return bags.stream()
                .map(bag -> monetary.apply(bag))
                .reduce(Money.ZERO, Money::plus);
    }

    public Money plus(Money amount) {
        return new Money(this.amount.add(amount.amount));
    }

    public Money minus(Money amount) {
        return new Money(this.amount.subtract(amount.amount));
    }

    //곱셈 (할인, 세금 계산, 또는 기타 비율에 따른 금액 조정에 사용)
    public Money times(double percent) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(percent)));
    }


    public Money divide(double percent) {
        return new Money(this.amount.divide(BigDecimal.valueOf(percent)));
    }

    public Long longValue() {
        return amount.longValue();
    }

    public Double doubleValue() {
        return amount.doubleValue();
    }


    // 금액 비교 메서드

    /**
     * compareTo 메서드는 두 BigDecimal 객체의 값을 비교
     * amount.compareTo(other.amount)의 결과가 -1인 경우 (즉, this.amount가 other.amount보다 작으면) true를 반환하고, 그렇지 않으면 false를 반환
     *
     */
    public boolean isLessThan(Money other) {
        return amount.compareTo(other.amount) < 0;
    }

    // 위와 반대
    public boolean isGreaterThanOrEqual(Money other) {
        return amount.compareTo(other.amount) >= 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Money)) {
            return false;
        }
        Money other = (Money)object;
        return Objects.equals(amount.doubleValue(), other.amount.doubleValue());
    }

    public int hashCode() {
        return Objects.hashCode(amount);
    }

    public String toString() {
        return amount.toString() + "원";
    }

}

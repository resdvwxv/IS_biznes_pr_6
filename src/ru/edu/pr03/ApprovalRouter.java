package ru.edu.pr03;

public class ApprovalRouter {
    public static final long FINANCE_LIMIT = 500_000L;

    /** Вариант 6: порог FAST_TRACK на 35 % ниже финансового порога. */
    public static final int FAST_TRACK_DISCOUNT_PERCENT = 35;
    public static final long FAST_TRACK_LIMIT =
            FINANCE_LIMIT * (100 - FAST_TRACK_DISCOUNT_PERCENT) / 100; // 325 000

    public ApprovalRoute route(long amount, boolean urgent, RiskLevel risk,
                               boolean containsPersonalData) {
        validate(amount, risk);

        // порядок: от самого строгого правила к общему
        if (containsPersonalData)    return ApprovalRoute.SECURITY_REVIEW;
        if (risk == RiskLevel.HIGH)  return ApprovalRoute.DIRECTOR_REVIEW;
        if (amount >= FINANCE_LIMIT) return ApprovalRoute.FINANCE_REVIEW;
        if (isFastTrack(urgent, amount)) return ApprovalRoute.FAST_TRACK;
        return ApprovalRoute.STANDARD;
    }

    private boolean isFastTrack(boolean urgent, long amount) {
        return urgent && amount < FAST_TRACK_LIMIT;
    }

    private void validate(long amount, RiskLevel risk) {
        if (amount < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной: " + amount);
        }
        if (risk == null) {
            throw new IllegalArgumentException("Уровень риска не задан");
        }
    }
}

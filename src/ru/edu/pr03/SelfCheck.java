package ru.edu.pr03;

public class SelfCheck {
    private static int failed = 0;
    private static final ApprovalRouter R = new ApprovalRouter();

    public static void main(String[] args) {
        // исходная проверка из задания
        check("исходная 1", R.route(10_000, true, RiskLevel.LOW, true), ApprovalRoute.SECURITY_REVIEW);
        check("исходная 2", R.route(500_000, false, RiskLevel.LOW, false), ApprovalRoute.FINANCE_REVIEW);

        // граница финансового порога
        check("499999",  R.route(499_999, false, RiskLevel.LOW, false), ApprovalRoute.STANDARD);
        check("500000",  R.route(500_000, false, RiskLevel.LOW, false), ApprovalRoute.FINANCE_REVIEW);
        check("500001",  R.route(500_001, false, RiskLevel.LOW, false), ApprovalRoute.FINANCE_REVIEW);

        // граница FAST_TRACK (вариант 6)
        check("urgent 324999", R.route(324_999, true, RiskLevel.LOW, false), ApprovalRoute.FAST_TRACK);
        check("urgent 325000", R.route(325_000, true, RiskLevel.LOW, false), ApprovalRoute.STANDARD);
        check("urgent 499999", R.route(499_999, true, RiskLevel.LOW, false), ApprovalRoute.STANDARD);
        check("urgent 500000", R.route(500_000, true, RiskLevel.LOW, false), ApprovalRoute.FINANCE_REVIEW);

        // приоритеты
        check("PD > HIGH",      R.route(1, false, RiskLevel.HIGH, true),  ApprovalRoute.SECURITY_REVIEW);
        check("HIGH > сумма",   R.route(900_000, false, RiskLevel.HIGH, false), ApprovalRoute.DIRECTOR_REVIEW);
        check("HIGH > urgent",  R.route(10_000, true, RiskLevel.HIGH, false), ApprovalRoute.DIRECTOR_REVIEW);
        check("PD > urgent",    R.route(10_000, true, RiskLevel.LOW, true), ApprovalRoute.SECURITY_REVIEW);
        check("MEDIUM обычная", R.route(120_000, false, RiskLevel.MEDIUM, false), ApprovalRoute.STANDARD);
        check("нулевая сумма",  R.route(0, false, RiskLevel.LOW, false), ApprovalRoute.STANDARD);

        // ошибки
        expectError("отрицательная сумма", () -> R.route(-1, false, RiskLevel.LOW, false));
        expectError("risk == null",        () -> R.route(1, false, null, false));

        System.out.println(failed == 0 ? "PASS" : "FAIL  (" + failed + ")");
    }

    private static void check(String name, ApprovalRoute actual, ApprovalRoute expected) {
        if (actual != expected) {
            failed++;
            System.out.println("FAIL " + name + ": ожидалось " + expected + ", получено " + actual);
        }
    }

    private static void expectError(String name, Runnable r) {
        try { r.run(); failed++; System.out.println("FAIL " + name + ": исключение не выброшено"); }
        catch (IllegalArgumentException ignored) { }
    }
}

package ru.edu.pr03;

import java.nio.file.*;
import java.util.List;

public class CsvRunner {
    public static void main(String[] args) throws Exception {
        Path file = Path.of(args.length > 0 ? args[0] : "data/sample.csv");
        List<String> lines = Files.readAllLines(file);
        ApprovalRouter router = new ApprovalRouter();

        for (int i = 1; i < lines.size(); i++) {           // строка 0 — заголовок
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;
            try {
                String[] p = line.split(";", -1);
                if (p.length != 4) throw new IllegalArgumentException("ожидалось 4 поля, получено " + p.length);
                long amount = parseAmount(p[0]);
                boolean urgent = parseBool(p[1], "urgent");
                RiskLevel risk = parseRisk(p[2]);
                boolean personal = parseBool(p[3], "personal");
                System.out.println(line + " -> " + router.route(amount, urgent, risk, personal));
            } catch (IllegalArgumentException e) {
                System.out.println(line + " -> ОШИБКА (строка " + (i + 1) + "): " + e.getMessage());
            }
        }
    }

    private static long parseAmount(String s) {
        if (s.isBlank()) throw new IllegalArgumentException("пустая сумма");
        try { return Long.parseLong(s.trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("неверный формат суммы: '" + s + "'"); }
    }

    private static boolean parseBool(String s, String name) {
        String v = s.trim().toLowerCase();
        if (v.equals("true")) return true;
        if (v.equals("false")) return false;
        throw new IllegalArgumentException("поле " + name + " должно быть true/false: '" + s + "'");
    }

    private static RiskLevel parseRisk(String s) {
        if (s.isBlank()) throw new IllegalArgumentException("пустой уровень риска");
        try { return RiskLevel.valueOf(s.trim().toUpperCase()); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("неизвестный риск: '" + s + "'"); }
    }
}

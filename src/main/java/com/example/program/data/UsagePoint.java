package com.example.program.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.Instant;

public record UsagePoint(
        @JsonFormat(shape = JsonFormat.Shape.STRING) Instant time,
        BigDecimal kwh,
        String accountNo,
        String substation,
        String type,
        String zipCode
) {
    public UsagePoint withTime(Instant newTime) { return new UsagePoint(newTime, kwh, accountNo, substation, type, zipCode); }
    public UsagePoint withKwh(BigDecimal newKwh) { return new UsagePoint(time, newKwh, accountNo, substation, type, zipCode); }

    // Optional: lightweight point for charts if you don’t need the extra fields
    public ChartPoint toChartPoint() { return new ChartPoint(time, kwh); }

    public record ChartPoint(@JsonFormat(shape = JsonFormat.Shape.STRING) Instant time, BigDecimal value) {}
}
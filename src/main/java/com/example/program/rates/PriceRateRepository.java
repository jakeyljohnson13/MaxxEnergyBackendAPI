package com.example.program.rates;


import com.example.program.rates.PriceRate.DayType;
import com.example.program.rates.PriceRate.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRateRepository extends JpaRepository<PriceRate, Long> {

    List<PriceRate> findByPlanAndDayTypeOrderByStartTimeAsc(Plan plan, DayType dayType);

    List<PriceRate> findByPlanOrderByDayTypeAscStartTimeAsc(Plan plan);
}
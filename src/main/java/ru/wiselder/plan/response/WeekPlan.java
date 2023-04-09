package ru.wiselder.plan.response;

import java.util.List;

public record WeekPlan(List<DayPlan> dayPlans) {
}

package it.polito.tdp.poweroutages.model;

import java.time.Month;
import java.time.Period;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestModel 
{
	public static void main(String[] args) 
	{	
		
		LocalDateTime ldt1 = LocalDateTime.of(2019, Month.MARCH, 26, 13, 0);
		LocalDateTime ldt2 = LocalDateTime.of(2021, Month.MARCH, 27, 5, 0);
		
		System.out.println("LocalDateTime:");
		System.out.println(ldt1);
		System.out.println(ldt2);
		
		LocalDate ld1 = LocalDate.from(ldt1);
		LocalDate ld2 = LocalDate.from(ldt2);

		System.out.println("\nLocalDate:");
		System.out.println(ld1);
		System.out.println(ld2);
		
		System.out.println("\nPeriod between:");
		Period diff = Period.between(ld1, ld2);
		System.out.println(diff);
		System.out.println("\nyears: " + diff.getYears());
		System.out.println("\nmonths: " + diff.getMonths());
		System.out.println("\ndays: " + diff.getDays());
		
		///////
		
		System.out.println();
		System.out.println("duration of 0 hours: "+Duration.ofHours(0));
		
	}
}

package zzh.project.stocksystem.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ＭyTest {
	public static void main(String[] args) {
		System.out.println(new SimpleDateFormat("yyyy MM-dd").format(new Date(System.currentTimeMillis())));
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 3);
		System.out.println(new SimpleDateFormat("yyyy MM-dd").format(calendar.getTime()));
	}
}

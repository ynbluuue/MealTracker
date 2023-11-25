package com.example.mealtracker.FoodInfo;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomCalendarActivity extends MaterialCalendarView {

    public List<FoodRecord> foodRecords;

    public CustomCalendarActivity(@NonNull Context context) {
        super(context);
        // 생성자에서 필요한 초기화를 수행
    }

    public void setFoodRecords(List<FoodRecord> foodRecords) {
        this.foodRecords = foodRecords;
        // 데이터가 설정되면 달력에 데코레이터를 추가하여 음식 정보를 표시
        addDecorator(new FoodRecordDecorator(foodRecords));
    }

    public static class FoodRecordDecorator implements DayViewDecorator {
        private List<CalendarDay> datesWithFood;

        public FoodRecordDecorator(List<FoodRecord> foodRecords) {
            this.datesWithFood = new ArrayList<>();
            for (FoodRecord foodRecord : foodRecords) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date date = dateFormat.parse(foodRecord.getDateTime());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    CalendarDay calendarDay = CalendarDay.from(calendar);
                    datesWithFood.add(calendarDay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            // 해당 날짜에 음식 정보가 있는 경우에만 데코레이션을 적용
            return datesWithFood.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            // 특정 날짜에 대한 데코레이션을 여기에 추가
            // 예: 점으로 표시
            view.addSpan(new DotSpan(5, Color.BLACK));
        }
    }
}
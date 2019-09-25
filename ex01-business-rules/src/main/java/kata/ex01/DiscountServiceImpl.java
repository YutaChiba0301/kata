package kata.ex01;

import kata.ex01.model.HighwayDrive;
import kata.ex01.model.RouteType;
import kata.ex01.model.VehicleFamily;
import kata.ex01.util.HolidayUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author kawasima
 */
public class DiscountServiceImpl implements DiscountService {

    @Override
    public long calc(HighwayDrive drive) {


        LocalDateTime morningEnd, morningStart;
        if (drive.getEnteredAt().getHour() >= 9) {
            morningStart = LocalDateTime.of(
                    LocalDate.from(drive.getEnteredAt().plusDays(1)),
                    LocalTime.of(6, 0));
            morningEnd = LocalDateTime.of(
                    LocalDate.from(drive.getExitedAt().plusDays(1)),
                    LocalTime.of(9, 0));
        } else {
            morningStart = LocalDateTime.of(
                    LocalDate.from(drive.getEnteredAt()),
                    LocalTime.of(6, 0));
            morningEnd = LocalDateTime.of(
                    LocalDate.from(drive.getExitedAt()),
                    LocalTime.of(9, 0));
        }

        LocalDateTime eveningStart, eveningEnd;
        if (drive.getEnteredAt().getHour() >= 17) {
            eveningStart = LocalDateTime.of(
                    LocalDate.from(drive.getEnteredAt().plusDays(1)),
                    LocalTime.of(17, 0));
            eveningEnd = LocalDateTime.of(
                    LocalDate.from(drive.getExitedAt().plusDays(1)),
                    LocalTime.of(20, 0));
        } else {
            eveningStart = LocalDateTime.of(
                    LocalDate.from(drive.getEnteredAt()),
                    LocalTime.of(17, 0));
            eveningEnd = LocalDateTime.of(
                    LocalDate.from(drive.getExitedAt()),
                    LocalTime.of(20, 0));
        }

        LocalDateTime midnightStart, midnightEnd;
        if (drive.getEnteredAt().getHour() >= 0) {
            midnightStart = LocalDateTime.of(
                    LocalDate.from(drive.getEnteredAt().plusDays(1)),
                    LocalTime.of(0, 0));
            midnightEnd = LocalDateTime.of(
                    LocalDate.from(drive.getExitedAt().plusDays(1)),
                    LocalTime.of(4, 0));
        } else {
            midnightStart = LocalDateTime.of(
                    LocalDate.from(drive.getEnteredAt()),
                    LocalTime.of(0, 0));
            midnightEnd = LocalDateTime.of(
                    LocalDate.from(drive.getExitedAt()),
                    LocalTime.of(4, 0));
        }


        // 休日割引
        if (drive.getRouteType() == RouteType.RURAL) {
            if (drive.getVehicleFamily() == VehicleFamily.MINI || drive.getVehicleFamily() == VehicleFamily.STANDARD) {
                if (HolidayUtils.isHoliday(drive.getEnteredAt().toLocalDate())
                        || HolidayUtils.isHoliday(drive.getExitedAt().toLocalDate())) {
                    return 30;
                }
            }
        }

        // 平日朝夕割引
        if (drive.getRouteType() == RouteType.RURAL) {
            if (isMorningServiceTime(drive, morningStart, morningEnd) || isEveningServiceTime(drive, eveningStart, eveningEnd)) {
                if (drive.getDriver().getCountPerMonth() >= 10) {
                    return 50;
                }

                if (drive.getDriver().getCountPerMonth() >= 5 && drive.getDriver().getCountPerMonth() >= 9) {
                    return 30;
                }
            }
        }

        // 深夜割
        if (isMidNight(drive, midnightStart, midnightEnd)) {
            return 30;
        }

        return 0;

    }

    private Boolean isMorningServiceTime(HighwayDrive drive, LocalDateTime morningStart, LocalDateTime morningEnd) {
        return (drive.getEnteredAt().isBefore(morningEnd)) && (drive.getExitedAt().isAfter(morningStart));
    }

    private Boolean isEveningServiceTime(HighwayDrive drive, LocalDateTime eveningStart, LocalDateTime eveningEnd) {
        return (drive.getEnteredAt().isBefore(eveningEnd)) && (drive.getExitedAt().isAfter(eveningStart));
    }

    private Boolean isMidNight(HighwayDrive drive, LocalDateTime midNightStart, LocalDateTime midNightEnd) {
        return (drive.getEnteredAt().isBefore(midNightEnd) && drive.getEnteredAt().isAfter(midNightStart));
    }
}

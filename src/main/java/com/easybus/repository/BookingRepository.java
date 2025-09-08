package com.easybus.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easybus.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // All bookings of a user
    List<Booking> findByUserId(Long userId);

    // Dynamic popular routes based on most booked from → to
    @Query(value = """
        SELECT CONCAT(b.source, ' → ', b.destination) AS route
        FROM bookings bk
        JOIN bus b ON bk.bus_id = b.id
        GROUP BY b.source, b.destination
        ORDER BY COUNT(*) DESC
        LIMIT 5
    """, nativeQuery = true)
    List<String> findTopPopularRoutes();
    
    
    @Query("SELECT b.seatNumbers FROM Booking b WHERE b.bus.id = :busId AND b.journeyDate = :date AND b.status IN ('CONFIRMED', 'RESCHEDULED')")
    List<String> findBookedSeats(@Param("busId") Long busId, @Param("date") LocalDate date);

}



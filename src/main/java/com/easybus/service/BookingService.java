package com.easybus.service;

import com.easybus.model.BookingRequest;
import com.easybus.model.BookingResponse;
import com.easybus.model.RescheduleRequest;

public interface BookingService {

    BookingResponse bookTicket(BookingRequest request);

    void cancelTicket(Long bookingId);

    BookingResponse rescheduleTicket(Long bookingId, RescheduleRequest request);
}

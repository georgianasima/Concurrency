package org.concurrency;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class FestivalGateTest {
    @Test
    void addTicket_QueueNotEmptyAfterAddingTicket() {
        FestivalGate gate = new FestivalGate();
        gate.addTicket(TicketType.FULL);
        assertFalse(gate.ticketQueue.isEmpty());
    }
    @Test
    void validateTicket_ReturnsValidTicketType() throws InterruptedException {
        FestivalGate gate = new FestivalGate();
        gate.addTicket(TicketType.FULL_VIP);
        TicketType ticketType = gate.validateTicket();
        assertEquals(TicketType.FULL_VIP, ticketType);
    }
    @Test
    void validateTicket_QueueEmptyAfterValidation() throws InterruptedException {
        FestivalGate gate = new FestivalGate();
        gate.addTicket(TicketType.FREE_PASS);
        gate.validateTicket();
        assertTrue(gate.ticketQueue.isEmpty());
    }
}
class FestivalAppTest {
    @Test
    void main_ExecutesWithoutExceptions() {
        FestivalApp.main(new String[]{});
    }
}
class FestivalAttendeeThreadTest {
    @Test
    void run_DoesNotThrowExceptions() {
        FestivalGate gate = new FestivalGate();
        FestivalAttendeeThread attendee = new FestivalAttendeeThread(TicketType.ONE_DAY_VIP, gate);
        attendee.start();
    }
}
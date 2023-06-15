package org.concurrency;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

enum TicketType {
    FULL, FULL_VIP, FREE_PASS, ONE_DAY, ONE_DAY_VIP
}
class FestivalGate {
    final Queue<TicketType> ticketQueue;
    public FestivalGate() {
        ticketQueue = new LinkedList<>();
    }
    public synchronized void addTicket(TicketType ticketType) {
        ticketQueue.add(ticketType);
        notify();
    }
    public synchronized TicketType validateTicket() throws InterruptedException {
        while (ticketQueue.isEmpty()) {
            wait();
        }
        return ticketQueue.poll();
    }
}
class FestivalAttendeeThread extends Thread {
    private final FestivalGate gate;
    private final TicketType ticketType;

    public FestivalAttendeeThread(TicketType ticketType, FestivalGate gate) {
        this.gate = gate;
        this.ticketType = ticketType;
    }
    @Override
    public void run() {
        try {
            sleep(new Random().nextInt(1000));
            gate.addTicket(ticketType);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class FestivalStatisticsThread extends Thread {
    private final FestivalGate gate;

    public FestivalStatisticsThread(FestivalGate gate) {
        this.gate = gate;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(5000); // Wait for 5 seconds
                generateStatistics();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void generateStatistics() throws InterruptedException {
        int totalAttendees = 0;
        int fullTickets = 0;
        int fullVipTickets = 0;
        int freePasses = 0;
        int oneDayTickets = 0;
        int oneDayVipTickets = 0;

        TicketType ticketType;
        do {
            ticketType = gate.validateTicket();
            switch (ticketType) {
                case FULL -> fullTickets++;
                case FULL_VIP -> fullVipTickets++;
                case FREE_PASS -> freePasses++;
                case ONE_DAY -> oneDayTickets++;
                case ONE_DAY_VIP -> oneDayVipTickets++;
            }
            totalAttendees++;
        } while (!gate.ticketQueue.isEmpty());
        System.out.println("Festival Statistics:");
        System.out.println("Total attendees: " + totalAttendees);
        System.out.println("Full tickets: " + fullTickets);
        System.out.println("Full VIP tickets: " + fullVipTickets);
        System.out.println("Free passes: " + freePasses);
        System.out.println("One-day tickets: " + oneDayTickets);
        System.out.println("One-day VIP tickets: " + oneDayVipTickets);
        System.out.println();
    }
}
public class FestivalApp {
    public static void main(String[] args) {
        FestivalGate gate = new FestivalGate();
        FestivalStatisticsThread statisticsThread = new FestivalStatisticsThread(gate);
        statisticsThread.start();

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            TicketType ticketType = TicketType.values()[random.nextInt(TicketType.values().length)];
            FestivalAttendeeThread attendee = new FestivalAttendeeThread(ticketType, gate);
            attendee.start();
        }
    }
}

package com.railbookpro.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private long totalUsers;
    private long totalTrains;
    private long totalBookings;
    private long todaysTrips;
    private long cancelledTickets;
    private double revenue;
    private long activeRoutes;
}

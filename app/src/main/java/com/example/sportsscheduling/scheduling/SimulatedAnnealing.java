package com.example.sportsscheduling.scheduling;

import java.util.*;

/**
 * The type Simulated annealing.
 */
public class SimulatedAnnealing {
    private List<Match> currentSolution;
    private List<Match> bestSolution;
    private Random random = new Random(1234567);
    private long maxTime = 60000;    // Set the time limit to be 60 seconds
    private double temperature = 8000;
    private double coolingRate = 0.95;
    private int iterationPerTemperature = 10;
    private String cost = "";
    private String bestCostTrack = "";
    /**
     * The Empty time slot.
     */
    List<String> emptyTimeSlot = new ArrayList<>();
    private int count1 = 0;
    private int count2 = 0;
    private String probability = "";
    private String temperatureTrack = "";

    /**
     * Instantiates a new Simulated annealing.
     *
     * @param initialSolution the initial solution
     */
    public SimulatedAnnealing(List<Match> initialSolution) {
        this.currentSolution = new ArrayList<>(initialSolution);
        this.bestSolution = new ArrayList<>(initialSolution);
    }

    /**
     * Schedule list.
     *
     * @return the list
     */
    public List<Match> schedule() {
        count1 = 0;
        cost = "";
        bestCostTrack = "";

        int noImprovement = 0;
        long startTime = System.currentTimeMillis();
        // Initialise the best solution
        copySolution(bestSolution, currentSolution);
        double bestCost = calculateCost(bestSolution);
        emptyTimeSlot = Initialisation.getInstance().getTimeSlot();

        // Satisfy the time limit for app to respond
        while (System.currentTimeMillis() - startTime < maxTime && temperature > 1) {
            for (int i = 0; i < iterationPerTemperature; i++) {
                List<Match> newSolution = new ArrayList<>(currentSolution);

                int maxSwapTry = 30;
                int swapAttempt = 0;
                boolean swapped = false;

                while (swapAttempt < maxSwapTry && !swapped) {
                    double option = random.nextDouble();
                    if (option < Initialisation.getInstance().getProportion() || temperature < 6000) {
                        // Mutation: randomly swap the timeslots of two matches
                        int index1 = random.nextInt(newSolution.size());
                        int index2 = random.nextInt(newSolution.size());
                        while (index1 == index2) {
                            index2 = random.nextInt(newSolution.size());
                        }
                        List<Match> tempSolution = new ArrayList<>();
                        copySolution(tempSolution, newSolution);

                        Match match1 = tempSolution.get(index1);
                        Match match2 = tempSolution.get(index2);

                        Team tempTeamA = match1.getA();
                        match1.setA(match2.getA());
                        match2.setA(tempTeamA);

                        Team tempTeamB = match1.getB();
                        match1.setB(match2.getB());
                        match2.setB(tempTeamB);

                        // Check the validity of the tempSolution
                        if (isValidSchedule(tempSolution)) {
                            copySolution(newSolution, tempSolution);
                            swapped = true;
                        } else {
                            // If invalid, then try another attempt
                            swapAttempt++;
                        }
                    } else {
                        // Randomly move one match to a free timeslot
                        moveToEmptyTimeSlot(newSolution);
                    }
                }

                count2++;

                // Calculate the current cost and the new cost
                double currentCost = calculateCost(currentSolution);
                double newCost = calculateCost(newSolution);

                // Move acceptance
                double delta = newCost - currentCost;
                double r = random.nextDouble();
                if (delta < 0 || r < Math.exp((-delta) / temperature)) {
                    copySolution(currentSolution, newSolution);
                }

                if (calculateCost(currentSolution) > calculateCost(bestSolution) * 1.5) {
                    copySolution(currentSolution, bestSolution);    // Restore the optimal solution
                }

                // Update the best solution
                if (calculateCost(currentSolution) < bestCost) {
                    copySolution(bestSolution, currentSolution);
                    bestCost = calculateCost(currentSolution);
                    noImprovement = 0;
                } else {
                    noImprovement++;
                }
                if (noImprovement > 30) {
                    noImprovement = 0;

                    // Reheat the temperature
                    temperature = temperature * 1.2;
                    break;
                }
                if(delta > 0) {
                    double p = Math.exp((-delta) / temperature);
                    String prob = String.format(Locale.US, "%.2f", p);
                    if(probability.isEmpty()){
                        probability += prob;
                    }
                    else{
                        probability = probability + "," + prob;
                    }
                }
                else{
                    if(probability.isEmpty()){
                        probability += "-1";
                    }
                    else{
                        probability = probability + "," + "-1";
                    }
                }

                String temp = String.format(Locale.US, "%.2f", temperature);
                if (temperatureTrack.isEmpty()){
                    temperatureTrack += temp;
                } else {
                    temperatureTrack = temperatureTrack + "," + temp;
                }

                cost = cost + calculateCost(currentSolution) + ",";
                bestCostTrack = bestCostTrack + bestCost + ",";
            }
            // Cool the temperature
            temperature = temperature * coolingRate;
//            Try linear cooling
//            temperature = temperature - iteration * 30;
        }

        return bestSolution;
    }

    private void moveToEmptyTimeSlot(List<Match> schedule) {
        int maxTry = 30;
        int attempt = 0;
        boolean move = false;

        while (attempt < maxTry && !move) {
            List<Match> tempSolution_empty_slot = new ArrayList<>();
            copySolution(tempSolution_empty_slot, schedule);

            // Randomly select a match
            int index = random.nextInt(tempSolution_empty_slot.size());
            Match match = tempSolution_empty_slot.get(index);
            int day = match.getDay();
            Venue venue = match.getV();
            String time = match.getTime();
            String oldSlot = day + "," + venue.getName() + "," + time;

            // Randomly select an empty timeslot
            int idx = random.nextInt(emptyTimeSlot.size());
            String[] slotArr = emptyTimeSlot.get(idx).split(",");

            // Assign new slot information to the match
            int newDay = Integer.parseInt(slotArr[0]);
            String newTime = slotArr[2];
            match.setDay(newDay);
            match.setTime(newTime);

            Venue newVenue = null;
            for (Venue v : Initialisation.getInstance().getVenues()) {
                if (v.getName().equals(slotArr[1])) {
                    newVenue = v;
                }
            }
            match.setV(newVenue);

            if (isValidSchedule(tempSolution_empty_slot)) {
                // If valid, update emptyTimeSlot and return
                emptyTimeSlot.remove(idx);
                emptyTimeSlot.add(oldSlot);
                move = true;
            } else {
                attempt++;
            }
        }
    }

    /**
     * Calculate cost double.
     *
     * @param solution the solution
     * @return the double
     */
    public double calculateCost(List<Match> solution) {
        double cost = 0;
        if (Initialisation.getInstance().isGroupConstraint())
            cost += groupConstraint(solution);
        if (Initialisation.getInstance().isDateConstraint())
            cost += dateConstraint(solution);
        if (Initialisation.getInstance().isSeperationConstraint())
            cost += separationConstraint(solution);
        if (Initialisation.getInstance().isRestDifferenceConstraint())
            cost += restDifferenceConstraint(solution);
        if (Initialisation.getInstance().isDailyConstraint())
            cost += dailyMatchesConstraint(solution);
        if (Initialisation.getInstance().isPreferenceConstraint())
            cost += preferenceConstraint(solution);
        return cost * 50;
    }

    /**
     * Group constraint double.
     *
     * @param schedule the schedule
     * @return the double
     */
// Group constraint: limit the maximum number of games that can be played in a time period
    // For example: up to 3 games are playing at any one time
    public double groupConstraint(List<Match> schedule) {
        double cost = 0;

        Map<String, Integer> timeslotCount = new HashMap<>();
        for (Match match : schedule) {
            String slotKey = match.getDay() + "_" + match.getTime();
            timeslotCount.compute(slotKey, (k, v) -> (v == null) ? 1 : v + 1);
        }

        int maxGames = Initialisation.getInstance().getMaxPerTime();
        for (int num : timeslotCount.values()) {
            if (num > maxGames) {
                cost += 50 * (num - maxGames);
            }
        }

        return cost;
    }

    /**
     * Date constraint double.
     *
     * @param schedule the schedule
     * @return the double
     */
// Date constraint: stipulate that certain games must be played during certain time periods
    // For example: use a string to record the date preference
    // i.e. "A,B,<=,5;F,J,>=,3;G,I,>=,6"
    public double dateConstraint(List<Match> schedule) {
        double cost = 0;
        String constraintsString = Initialisation.getInstance().getDatePreference();
        String[] constraintsArray = constraintsString.split(";");
        for (String c : constraintsArray) {
            String[] parts = c.split(",");
            String team1 = parts[0].trim();
            String team2 = parts[1].trim();
            String operator = parts[2].trim(); // "<=" or ">="
            int dayLimit = Integer.parseInt(parts[3].trim());

            // If '<=' then isBefore is true, if '>=' then isBefore is false
            boolean isBefore = operator.equals("<=");

            for (Match match : schedule) {
                boolean isThisMatch = (match.getA().getName().equals(team1) && match.getB().getName().equals(team2))
                        || (match.getA().getName().equals(team2) && match.getB().getName().equals(team1));

                if (isThisMatch) {
                    int actualDay = match.getDay();
                    // <= dayLimit
                    if (isBefore && actualDay > dayLimit) {
                        cost += 100;
                    }
                    // >= dayLimit
                    if (!isBefore && actualDay < dayLimit) {
                        cost += 100;
                    }
                }
            }
        }

        return cost;
    }

    /**
     * Separation constraint double.
     *
     * @param schedule the schedule
     * @return the double
     */
// Separation constraint: limit the maximum number of consecutive days a team can play
    // For example: avoid having a team play three consecutive days
    public double separationConstraint(List<Match> schedule) {
        double cost = 0;
        Map<String, List<Integer>> teamDaysMap = new HashMap<>();
        for (Match match : schedule) {
            String teamA = match.getA().getName();
            String teamB = match.getB().getName();
            int day = match.getDay();
            teamDaysMap.computeIfAbsent(teamA, k -> new ArrayList<>()).add(day);
            teamDaysMap.computeIfAbsent(teamB, k -> new ArrayList<>()).add(day);
        }

        for (String team : teamDaysMap.keySet()) {
            List<Integer> days = teamDaysMap.get(team);
            Collections.sort(days);

            int consecutive = 1;
            for (int i = 1; i < days.size(); i++) {
                if (days.get(i) == days.get(i - 1) + 1) {
                    consecutive++;
                } else {
                    consecutive = 1;
                }
                if (consecutive > Initialisation.getInstance().getSeperationDay()) {
                    // Penalty for every extra day
                    cost += 50;
                }
            }
        }
        return cost;
    }

    /**
     * Rest difference constraint double.
     *
     * @param schedule the schedule
     * @return the double
     */
// Rest difference constraint: to achieve fairness, the schedule should minimize the difference in rest time between the two sides
    public double restDifferenceConstraint(List<Match> schedule) {
        double cost = 0;
        // Sort the schedule in order from the smallest day to the largest day
        schedule.sort(Comparator.comparingInt(Match::getDay));

        // Keep track of the last day each team played
        Map<String, Integer> lastDayMap = new HashMap<>();

        for (Match match : schedule) {
            int day = match.getDay();
            String teamA = match.getA().getName();
            String teamB = match.getB().getName();

            // Calculate the rest days for team A and B
            int restA = 0; // 0 for the first match
            int restB = 0;

            if (lastDayMap.containsKey(teamA)) {
                restA = day - lastDayMap.get(teamA);
            }
            if (lastDayMap.containsKey(teamB)) {
                restB = day - lastDayMap.get(teamB);
            }

            int diff = Math.abs(restA - restB);
            cost += 5 * diff;

            // Update lastDay map
            lastDayMap.put(teamA, day);
            lastDayMap.put(teamB, day);
        }
        return cost;
    }

    /**
     * Daily matches constraint double.
     *
     * @param schedule the schedule
     * @return the double
     */
// Daily constraint: limit the number of matches played by each team per day
    // For example: each team should only play at most one match in a day
    public double dailyMatchesConstraint(List<Match> schedule) {
        double cost = 0;
        Map<String, Integer> teamDailyMatch = new HashMap<>();

        for (Match match : schedule) {
            // Calculate the number of matches of each team per day
            String keyA = match.getA().getName() + "_" + match.getDay();
            teamDailyMatch.compute(keyA, (k, v) -> (v == null) ? 1 : v + 1);

            String keyB = match.getB().getName() + "_" + match.getDay();
            teamDailyMatch.compute(keyB, (k, v) -> (v == null) ? 1 : v + 1);
        }

        int maxDaily = Initialisation.getInstance().getMaxDaily();
        for (int num : teamDailyMatch.values()) {
            if (num > maxDaily) {
                cost += 100 * (num - maxDaily);
            }
        }

        return cost;
    }

    /**
     * Preference constraint double.
     *
     * @param schedule the schedule
     * @return the double
     */
// Preference constraint: meet each team's preferred venues and time
    public double preferenceConstraint(List<Match> schedule) {
        double cost = 0;

        // Check if the preference of each team is satisfied
        for (Match match : schedule) {
            if (!match.getA().getPreferredTime().isEmpty() && !match.getTime().equals(match.getA().getPreferredTime())) {
                cost += 5;  // set punishment
            }
            if (!match.getA().getPreferredVenue().isEmpty() && !match.getV().getName().equals(match.getA().getPreferredVenue())) {
                cost += 5;  // set punishment
            }

            if (!match.getB().getPreferredTime().isEmpty() && !match.getTime().equals(match.getB().getPreferredTime())) {
                cost += 5;
            }
            if (!match.getB().getPreferredVenue().isEmpty() && !match.getV().getName().equals(match.getB().getPreferredVenue())) {
                cost += 5;  // set punishment
            }
        }

        return cost;
    }

    // Avoid having one team play multiple games on the same day at the same time
    private boolean isValidSchedule(List<Match> schedule) {
        List<String> teamDailyTimeMap = new ArrayList<>();

        for (Match match : schedule) {
            String team_1_Schedule = match.getA().getName() + "," + match.getDay() + "," + match.getTime();
            String team_2_Schedule = match.getB().getName() + "," + match.getDay() + "," + match.getTime();

            if (teamDailyTimeMap.contains(team_1_Schedule) || teamDailyTimeMap.contains(team_2_Schedule)) {
                return false;
            }

            // Record the match
            teamDailyTimeMap.add(team_1_Schedule);
            teamDailyTimeMap.add(team_2_Schedule);
        }
        return true;
    }

    /**
     * Copy solution.
     *
     * @param A the a
     * @param B the b
     */
// Copy solution B to A
    public void copySolution(List<Match> A, List<Match> B) {
        A.clear();
        for (Match m : B) {
            A.add(new Match(m.getA(), m.getB(), m.getV(), m.getTime(), m.getDay()));
        }
    }

    /**
     * Log cost string.
     *
     * @return the string
     */
    public String logCost() {
        return cost + "\nbest: " + calculateCost(bestSolution);
    }

    /**
     * Log best cost string.
     *
     * @return the string
     */
    public String logBestCost() {
        return bestCostTrack;
    }

    /**
     * Log probability string.
     *
     * @return the string
     */
    public String logProbability(){
        return probability;
    }

    /**
     * Log temperature track string.
     *
     * @return the string
     */
    public String logTemperatureTrack() {
        return temperatureTrack;
    }

}

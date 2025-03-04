package genetic_algorithms_tsp.genetic_algorithm.genetic_operators;

import genetic_algorithms_tsp.genetic_objects.Chromosome;
import genetic_algorithms_tsp.genetic_objects.City;

import java.util.*;

public class Crossover {
    public static ArrayList<Chromosome> uniformOrder (Chromosome p1, Chromosome p2, Random r) {

        City[] parent1 = p1.getCitiesArray();
        City[] parent2 = p2.getCitiesArray();

        City[] child1 = new City[parent1.length];
        City[] child2 = new City[parent2.length];

        HashSet<City> citiesInChild1 = new HashSet<>();
        HashSet<City> citiesInChild2 = new HashSet<>();

        ArrayList<City> citiesNotInChild1 = new ArrayList<>();
        ArrayList<City> citiesNotInChild2 = new ArrayList<>();

        ArrayList<Chromosome> children = new ArrayList<>();

        int[] bitMask = generateBitMask(parent1.length, r);

        // Inherit the cities of the same parent where the bit-mask is 1.
        for (int i = 0; i < bitMask.length; i++) {
            if (bitMask[i] == 1) {
                child1[i] = parent1[i];
                child2[i] = parent2[i];
                citiesInChild1.add(parent1[i]);
                citiesInChild2.add(parent2[i]);
            }
        }

        // Get the cities of the opposite parent if the child does not already contain them.
        for (int i = 0; i < child1.length; i++) {
            if (child1[i] == null && !citiesInChild1.contains(parent2[i])) {
                child1[i] = parent2[i];
                citiesInChild1.add(parent2[i]);
            } else if (child1[i] != null && !citiesInChild1.contains(parent2[i])) {
                citiesNotInChild1.add(parent2[i]);
            }
            if (child2[i] == null && !citiesInChild2.contains(parent1[i])) {
                child2[i] = parent1[i];
                citiesInChild2.add(parent1[i]);
            } else if (child2[i] != null && !citiesInChild2.contains(parent1[i])) {
                citiesNotInChild2.add(parent1[i]);
            }
        }

        // Fill in the blanks.
        for (int i = 0; i < child1.length; i++) {
            if (child1[i] == null) {
                child1[i] = citiesNotInChild1.remove(0);
            }
            if (child2[i] == null) {
                child2[i] = citiesNotInChild2.remove(0);
            }
        }

        if (!citiesNotInChild1.isEmpty() || !citiesNotInChild2.isEmpty()) {
            throw new IllegalStateException("Lists should be empty.");
        }

        Chromosome childOne = new Chromosome(child1);
        Chromosome childTwo = new Chromosome(child2);
        children.add(childOne);
        children.add(childTwo);

        return children;
    }

    private static int[] generateBitMask (int size, Random random) {

        int[] array = new int[size];

        for (int i = 0; i < array.length; i++) {
            array[i] = (random.nextInt(2) == 0) ? 0 : 1;
        }

        return array;
    }

    public static ArrayList<Chromosome> onePointCrossover (Chromosome p1, Chromosome p2, Random r) {
        City[] parent1 = p1.getCitiesArray();
        City[] parent2 = p2.getCitiesArray();

        City[] child1 = new City[parent1.length];
        City[] child2 = new City[parent2.length];

        HashSet<City> citiesInChild1 = new HashSet<>();
        HashSet<City> citiesInChild2 = new HashSet<>();

        ArrayList<City> citiesNotInChild1 = new ArrayList<>();
        ArrayList<City> citiesNotInChild2 = new ArrayList<>();

        ArrayList<Chromosome> children = new ArrayList<>();
        int totalCities = parent1.length;

        int randomPoint = r.nextInt(totalCities);

        // Inherit the cities up to the point.
        for (int i = 0; i < randomPoint; i++) {
            child1[i] = parent1[i];
            child2[i] = parent2[i];
            citiesInChild1.add(parent1[i]);
            citiesInChild2.add(parent2[i]);
        }

        // Get the cities of the opposite parent if the child does not already contain them.
        for (int i = randomPoint; i < totalCities; i++) {
            if (!citiesInChild1.contains(parent2[i])) {
                citiesInChild1.add(parent2[i]);
                child1[i] = parent2[i];
            }
            if (!citiesInChild2.contains(parent1[i])) {
                citiesInChild2.add(parent1[i]);
                child2[i] = parent1[i];
            }
        }

        // Find all the cities that are still missing from each child.
        for (int i = 0; i < totalCities; i++) {
            if (!citiesInChild1.contains(parent2[i])) {
                citiesNotInChild1.add(parent2[i]);
            }
            if (!citiesInChild2.contains(parent1[i])) {
                citiesNotInChild2.add(parent1[i]);
            }
        }

        // Find which spots are still empty in each child.
        ArrayList<Integer> emptySpotsC1 = new ArrayList<>();
        ArrayList<Integer> emptySpotsC2 = new ArrayList<>();
        for (int i = 0; i < totalCities; i++) {
            if (child1[i] == null) {
                emptySpotsC1.add(i);
            }
            if (child2[i] == null) {
                emptySpotsC2.add(i);
            }
        }

        // Fill in the empty spots.
        for (City city : citiesNotInChild1) {
            child1[emptySpotsC1.remove(0)] = city;
        }
        for (City city : citiesNotInChild2) {
            child2[emptySpotsC2.remove(0)] = city;
        }

        Chromosome childOne = new Chromosome(child1);
        Chromosome childTwo = new Chromosome(child2);
        children.add(childOne);
        children.add(childTwo);

        return children;
    }

    public static ArrayList<Chromosome> twoPointCrossover(Chromosome p1, Chromosome p2, Random r) {
        City[] parent1 = p1.getCitiesArray();
        City[] parent2 = p2.getCitiesArray();

        City[] child1 = new City[parent1.length];
        City[] child2 = new City[parent2.length];

        HashSet<City> citiesInChild1 = new HashSet<>();
        HashSet<City> citiesInChild2 = new HashSet<>();

        ArrayList<City> citiesNotInChild1 = new ArrayList<>();
        ArrayList<City> citiesNotInChild2 = new ArrayList<>();

        ArrayList<Chromosome> children = new ArrayList<>();
        int totalCities = parent1.length;

        int firstPoint = r.nextInt(totalCities);
        int secondPoint = r.nextInt(totalCities - firstPoint) + firstPoint;

        // Inherit the cities before and after the points selected.
        for (int i = 0; i < firstPoint; i++) {
            child1[i] = parent1[i];
            child2[i] = parent2[i];
            citiesInChild1.add(parent1[i]);
            citiesInChild2.add(parent2[i]);
        }
        for (int i = secondPoint; i < totalCities; i++) {
            child1[i] = parent1[i];
            child2[i] = parent2[i];
            citiesInChild1.add(parent1[i]);
            citiesInChild2.add(parent2[i]);
        }

        // Get the cities of the opposite parent if the child does not already contain them.
        for (int i = firstPoint; i < secondPoint; i++) {
            if (!citiesInChild1.contains(parent2[i])) {
                citiesInChild1.add(parent2[i]);
                child1[i] = parent2[i];
            }
            if (!citiesInChild2.contains(parent1[i])) {
                citiesInChild2.add(parent1[i]);
                child2[i] = parent1[i];
            }
        }

        // Find all the cities that are still missing from each child.
        for (int i = 0; i < totalCities; i++) {
            if (!citiesInChild1.contains(parent2[i])) {
                citiesNotInChild1.add(parent2[i]);
            }
            if (!citiesInChild2.contains(parent1[i])) {
                citiesNotInChild2.add(parent1[i]);
            }
        }

        // Find which spots are still empty in each child.
        ArrayList<Integer> emptySpotsC1 = new ArrayList<>();
        ArrayList<Integer> emptySpotsC2 = new ArrayList<>();
        for (int i = 0; i < totalCities; i++) {
            if (child1[i] == null) {
                emptySpotsC1.add(i);
            }
            if (child2[i] == null) {
                emptySpotsC2.add(i);
            }
        }

        // Fill in the empty spots.
        for (City city : citiesNotInChild1) {
            child1[emptySpotsC1.remove(0)] = city;
        }
        for (City city : citiesNotInChild2) {
            child2[emptySpotsC2.remove(0)] = city;
        }

        Chromosome childOne = new Chromosome(child1);
        Chromosome childTwo = new Chromosome(child2);
        children.add(childOne);
        children.add(childTwo);

        return children;
    }
}

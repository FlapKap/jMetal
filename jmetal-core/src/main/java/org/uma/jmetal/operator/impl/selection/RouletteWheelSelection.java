package org.uma.jmetal.operator.impl.selection;

import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.solutionattribute.impl.Fitness;

import java.util.List;

/**
 * Created by khb on 17-03-2017.
 * Note: This only works with positive fitness's. If the fitness's are negative, the absolute value is taken. Since we
 * assume minimization, this works as -1 is just as far away from 0 (the optimal) as 1.
 */
public class RouletteWheelSelection<S extends Solution<?>> implements SelectionOperator<List<S>, S> {

    private final JMetalRandom RNG = JMetalRandom.getInstance();
    private Fitness<S> solutionFitness = new Fitness<>();

    private double maxFitness = Double.NEGATIVE_INFINITY;

    /**
     * Initialises a roulette wheel selection (Fitness-Proportionate selection) operator.
     * @param maxFitness if a maxFitness  of the population is given, the selection will run much faster
     */
    public RouletteWheelSelection(double maxFitness){
        this.maxFitness = maxFitness;
    }

    @Override
    public S execute(List<S> s) {
        double max = Double.NEGATIVE_INFINITY;
        if (maxFitness < 0.0){ //if fitness is less than zero we assume it has not been given
            for (S sol : s) {
                maxFitness = Math.max(max, Math.abs(solutionFitness.getAttribute(sol)));
            }
        }
        return select(s, maxFitness);
    }

    /**
     * Select a single solution
     * @param population the entire population
     * @param highestFitness the highest fitness
     * @return an individual. If the list is empty, null is returned
     */
    private S select(List<S> population, double highestFitness){

        if(population.size() == 1) return population.get(0);
        if(population.size() == 0) return null;

        while (true){
            int index = RNG.nextInt(0, population.size()-1);
            double value = Math.abs(solutionFitness.getAttribute(population.get(index)));
                    if(RNG.nextDouble() < value/highestFitness){
                        return population.get(index);
                    }
        }
    }

    public double getMaxFitness() {
        return maxFitness;
    }

    public void setMaxFitness(double maxFitness) {
        this.maxFitness = maxFitness;
    }
}

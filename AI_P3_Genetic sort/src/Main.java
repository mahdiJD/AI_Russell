import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.DoubleStream;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        int[] initialState = new int[20];// حالت اولیه
        int[] arrayList = new int[20];//ایندکس های حالت اولیه
        ArrayList<Chromosome> generation = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            initialState[i] = random.nextInt(10);// ساخت حالت اولیه
            arrayList[i] = i;//ثبت ایندکس ها

        }
        Chromosome chromosome = new Chromosome(initialState, arrayList); // ساخت کرومزوم با حالت اولیه

        for (int i = 0; i < 20; i++) { // چاپ حالت اولیه
            System.out.print("[ ");
            System.out.print(chromosome.numArray[i]);
            System.out.print(" ]");
        }
        System.out.println();
        ArrayList<Integer> newGeneration = new ArrayList<>();

        if (chromosome.fitness() != 20) {
            for (int i = 0; i < 10; i++) { // ساخت 10 کروموزوم رندم به عنوان نسل اول
                for (int j = 0; j < 20; j++) {
                    newGeneration.add(initialState[j]);
                }

                int[] nextGeneration = new int[20];
                for (int j = 0; j < 20; j++) {
                    int x = random.nextInt(newGeneration.size());
                    nextGeneration[j] = newGeneration.get(x);
                    newGeneration.remove(x);
                }
                generation.add(new Chromosome(initialState, nextGeneration));
            }
        }

        while (chromosome.fitness() != 20) {
            ArrayList<Chromosome> nextGeneration = new ArrayList<>();
            Collections.sort(generation);
            while (nextGeneration.size()<10){
                DoubleStream randomX = random.doubles();
                DoubleStream randomY = random.doubles();
                int crossover = random.nextInt(19);
                int[] x = new int[20];
                int[] y = new int[20];/////////////////////////////////////////
                for (int j = 0; j < crossover; j++) {
                    x[j] = generation.get(randomX).arrayIndex.get(j);
                    y[j] = generation.get(randomY).arrayIndex.get(j);
                }
                for (int j = crossover; j < 20; j++) {
                    y[j] = generation.get(randomX).arrayIndex.get(j);
                    x[j] = generation.get(randomY).arrayIndex.get(j);
                }
                Chromosome newChromosome1 = new Chromosome(initialState, x);
                Chromosome newChromosome2 = new Chromosome(initialState, y);
                nextGeneration.add(newChromosome1);
                nextGeneration.add(newChromosome2);
            }

            for (int i = 0; i < 10; i++) {
                while (true) {
                    int mutation = random.nextInt(19);
                    if (nextGeneration.get(i).fitnessArray[mutation] == 0) {
                        int z=0;
                        for (int j = 0; j < 20; j++) {
                            if (nextGeneration.get(i).arrayIndex.get(j)==mutation) z = j;
                        }
                        int forChange = nextGeneration.get(i).arrayIndex.get(z);
                        nextGeneration.get(i).arrayIndex.remove(z);
                        nextGeneration.get(i).arrayIndex.set(0,z); //////////////////////////////////
                        break;
                    }
                }
            }

        }
        System.out.println("we find the answer!");
        for (int i = 0; i < 20; i++) {
            System.out.print("[ ");
            System.out.print(chromosome.numArray[i]);
            System.out.print(" ]");
        }
    }
}

class Chromosome implements Comparable<Chromosome> {
    int[] fitnessArray = new int[20];// 0 & 1
    int[] nextArrayNumIndex = new int[20];
    ArrayList<Integer> arrayIndex = new ArrayList<>();
    int fitnessVal;

    Chromosome(int[] initialState, int[] thisGeneration) {

        for (int i = 0; i < 20; i++) { // make arrayIndex
            arrayIndex.add(null);
        }
        for (int i = 0; i < 20; i++) {
            arrayIndex.add(thisGeneration[i], initialState[i]);
            if (arrayIndex.size() != 20) {
                for (int j = thisGeneration[i]; j < 20; j++) {
                    if (arrayIndex.removeIf(null)) {
                        break;
                    }
                }
            }
        }

        for (int i = 1; i < 20; i++) { // make fitnessArray
            if (nextArrayNumIndex[i] >= nextArrayNumIndex[i - 1]) fitnessArray[i] = 1;
            else fitnessArray[i] = 0;
        }
        fitnessArray[0] = fitnessArray[1];
        fitnessVal = fitness();

    }

    int fitness() {
        int x = 0;
        for (int i = 0; i < 20; i++) {
            x += fitnessArray[i];
        }
        return x;
    }

    @Override
    public int compareTo(Chromosome o) {
        return Integer.compare(this.fitnessVal, o.fitnessVal);
    }
}
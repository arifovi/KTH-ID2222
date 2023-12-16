package se.kth.jabeja;

import org.apache.log4j.Logger;
import se.kth.jabeja.config.Config;
import se.kth.jabeja.config.NodeSelectionPolicy;
import se.kth.jabeja.io.FileIO;
import se.kth.jabeja.rand.RandNoGenerator;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

public class Jabeja {
  final static Logger logger = Logger.getLogger(Jabeja.class);
  private final Config config;
  private final HashMap<Integer/*id*/, Node/*neighbors*/> entireGraph;
  private final List<Integer> nodeIds;
  private int numberOfSwaps;
  private int round;
  private float T;
  private boolean resultFileCreated = false;
  // For task 2
  private Variation variation = Variation.EXPONENTIAL;
  //private Variation variation = Variation.LINEAR;
  private boolean reset = false;

  //-------------------------------------------------------------------
  public Jabeja(HashMap<Integer, Node> graph, Config config) {

	this.config = config;
	//For task 2 additional settings
    config.setNodeSelectionPolicy(NodeSelectionPolicy.HYBRID);
	//config.setNodeSelectionPolicy(NodeSelectionPolicy.LOCAL);
	//config.setNodeSelectionPolicy(NodeSelectionPolicy.RANDOM);
    initializeTemperatureAndDelta();
    //-------

    this.entireGraph = graph;
    this.nodeIds = new ArrayList(entireGraph.keySet());
    this.round = 0;
    this.numberOfSwaps = 0;    
    this.T = config.getTemperature();
  }

  //New method for task 2 for initialize temparature and delta
  private void initializeTemperatureAndDelta() {
	  switch (variation) {
      case LINEAR:
        break;
      case EXPONENTIAL:
        config.setTemperature(1.0f);
        config.setDelta(0.9f);
        break;
    }
  }


  //-------------------------------------------------------------------
  public void startJabeja() throws IOException {
    for (round = 0; round < config.getRounds(); round++) {
      for (int id : entireGraph.keySet()) {
        sampleAndSwap(id);
      }

      //one cycle for all nodes have completed.
      //reduce the temperature
      //For task 2
      if (variation == Variation.LINEAR) {
          saCoolDown();
      } else if (variation == Variation.EXPONENTIAL) {
          exponentialCoolDown();
      }
      //--------
      report();
    }
  }

  /**
   * Simulated analealing cooling function
   */
  private void saCoolDown(){
    // TODO for second task
    if (T > 1)
        T -= config.getDelta();
    else
        resetTemperature();
  }
  //Exponent Temp handling method for task 2
  private void exponentialCoolDown() {
        T *= config.getDelta();
        if (T < 0.001 && reset)
            resetTemperature();
  }
  //Reset Temp for task 2
  private void resetTemperature() {
        if (reset) {
            T = config.getTemperature();
        } else {
            T = 1;
        }
  }

  /**
   * Sample and swap algorith at node p
   * @param nodeId
   */
  private void sampleAndSwap(int nodeId) {
    Node partner = null;
    Node nodep = entireGraph.get(nodeId);

    if (config.getNodeSelectionPolicy() == NodeSelectionPolicy.HYBRID
            || config.getNodeSelectionPolicy() == NodeSelectionPolicy.LOCAL) {
      // swap with random neighbors
      // TODO
      partner = findPartner(nodeId, getNeighbors(nodep));
    }

    if (config.getNodeSelectionPolicy() == NodeSelectionPolicy.HYBRID
            || config.getNodeSelectionPolicy() == NodeSelectionPolicy.RANDOM) {
      // if local policy fails then randomly sample the entire graph
      // TODO
        partner = findPartner(nodeId, getSample(nodeId));
    }

    // swap the colors
    // TODO
    if (partner != null) {
      int color = partner.getColor();
      partner.setColor(nodep.getColor());
      nodep.setColor(color);
      numberOfSwaps++;
    }
  }

  public Node findPartner(int nodeId, Integer[] nodes){

    Node nodep = entireGraph.get(nodeId);

    Node bestPartner = null;
    double highestBenefit = 0;

    // TODO
    for (Integer node : nodes) {
        Node nodeq = entireGraph.get(node);
        int degpp = this.getDegree(nodep, nodep.getColor());
        int degqq = this.getDegree(nodeq, nodeq.getColor());

        double oldD = pow(degpp, config.getAlpha()) + pow(degqq, config.getAlpha());

        int degpq = this.getDegree(nodep, nodeq.getColor());
        int degqp = this.getDegree(nodeq, nodep.getColor());

        double newD = pow(degpq, config.getAlpha()) + pow(degqp, config.getAlpha());
        /* task 1 solution
        if ((newD * T > oldD) && (newD > highestBenefit)) {
          bestPartner = nodeq;
          highestBenefit = newD;
        }*/
        // For Task 2 using new acceptance probability method
        if (acceptSolution(oldD, newD, highestBenefit)) {
        	//if(variation == Variation.EXPONENTIAL && newD!=oldD)//test for edge cut
            bestPartner = nodeq;
            highestBenefit = newD;
        }
        if (variation == Variation.EXPONENTIAL)
            break; // only try one partner
        
    }

    return bestPartner;
  }
  //For task 2 acceptance probability method
  private boolean acceptSolution(double oldD, double newD, double highestBenefit) {
    switch (variation) {
      case LINEAR:
        return newD > highestBenefit && newD * T > oldD;
      case EXPONENTIAL:
        Random r = new Random();
        return r.nextDouble() < exp((1 / oldD - 1 / newD) / T);
      default:
        return false;
    }
  }

  /**
   * The the degreee on the node based on color
   * @param node
   * @param colorId
   * @return how many neighbors of the node have color == colorId
   */
  private int getDegree(Node node, int colorId){
    int degree = 0;
    for(int neighborId : node.getNeighbours()){
      Node neighbor = entireGraph.get(neighborId);
      if(neighbor.getColor() == colorId){
        degree++;
      }
    }
    return degree;
  }

  /**
   * Returns a uniformly random sample of the graph
   * @param currentNodeId
   * @return Returns a uniformly random sample of the graph
   */
  private Integer[] getSample(int currentNodeId) {
    int count = config.getUniformRandomSampleSize();
    int rndId;
    int size = entireGraph.size();
    ArrayList<Integer> rndIds = new ArrayList<Integer>();

    while (true) {
      rndId = nodeIds.get(RandNoGenerator.nextInt(size));
      if (rndId != currentNodeId && !rndIds.contains(rndId)) {
        rndIds.add(rndId);
        count--;
      }

      if (count == 0)
        break;
    }

    Integer[] ids = new Integer[rndIds.size()];
    return rndIds.toArray(ids);
  }

  /**
   * Get random neighbors. The number of random neighbors is controlled using
   * -closeByNeighbors command line argument which can be obtained from the config
   * using {@link Config#getRandomNeighborSampleSize()}
   * @param node
   * @return
   */
  private Integer[] getNeighbors(Node node) {
    ArrayList<Integer> list = node.getNeighbours();
    int count = config.getRandomNeighborSampleSize();
    int rndId;
    int index;
    int size = list.size();
    ArrayList<Integer> rndIds = new ArrayList<Integer>();

    if (size <= count)
      rndIds.addAll(list);
    else {
      while (true) {
        index = RandNoGenerator.nextInt(size);
        rndId = list.get(index);
        if (!rndIds.contains(rndId)) {
          rndIds.add(rndId);
          count--;
        }

        if (count == 0)
          break;
      }
    }

    Integer[] arr = new Integer[rndIds.size()];
    return rndIds.toArray(arr);
  }


  /**
   * Generate a report which is stored in a file in the output dir.
   *
   * @throws IOException
   */
  private void report() throws IOException {
    int grayLinks = 0;
    int migrations = 0; // number of nodes that have changed the initial color
    int size = entireGraph.size();

    for (int i : entireGraph.keySet()) {
      Node node = entireGraph.get(i);
      int nodeColor = node.getColor();
      ArrayList<Integer> nodeNeighbours = node.getNeighbours();

      if (nodeColor != node.getInitColor()) {
        migrations++;
      }

      if (nodeNeighbours != null) {
        for (int n : nodeNeighbours) {
          Node p = entireGraph.get(n);
          int pColor = p.getColor();

          if (nodeColor != pColor)
            grayLinks++;
        }
      }
    }

    int edgeCut = grayLinks / 2;

    logger.info("round: " + round +
            ", edge cut:" + edgeCut +
            ", swaps: " + numberOfSwaps +
            ", migrations: " + migrations);

    saveToFile(edgeCut, migrations);
  }

  private void saveToFile(int edgeCuts, int migrations) throws IOException {
    String delimiter = "\t\t";
    String outputFilePath;

    //output file name
    File inputFile = new File(config.getGraphFilePath());
    outputFilePath = config.getOutputDir() +
            File.separator +
            inputFile.getName() + "_" +
            "NS" + "_" + config.getNodeSelectionPolicy() + "_" +
            "GICP" + "_" + config.getGraphInitialColorPolicy() + "_" +
            "T" + "_" + config.getTemperature() + "_" +
            "D" + "_" + config.getDelta() + "_" +
            "RNSS" + "_" + config.getRandomNeighborSampleSize() + "_" +
            "URSS" + "_" + config.getUniformRandomSampleSize() + "_" +
            "A" + "_" + config.getAlpha() + "_" +
            "R" + "_" + config.getRounds() + ".txt";

    if (!resultFileCreated) {
      File outputDir = new File(config.getOutputDir());
      if (!outputDir.exists()) {
        if (!outputDir.mkdir()) {
          throw new IOException("Unable to create the output directory");
        }
      }
      // create folder and result file with header
      String header = "# Migration is number of nodes that have changed color.";
      header += "\n\nRound" + delimiter + "Edge-Cut" + delimiter + "Swaps" + delimiter + "Migrations" + delimiter + "Skipped" + "\n";
      FileIO.write(header, outputFilePath);
      resultFileCreated = true;
    }

    FileIO.append(round + delimiter + (edgeCuts) + delimiter + numberOfSwaps + delimiter + migrations + "\n", outputFilePath);
  }
  private enum Variation {LINEAR, EXPONENTIAL}//create a enum for task 2
}

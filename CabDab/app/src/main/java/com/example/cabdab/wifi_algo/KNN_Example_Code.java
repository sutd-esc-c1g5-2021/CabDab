package com.example.selflib.wifi_algo;

import java.util.HashMap;
/*
* Refer to the steps in main to use KNN Algorithm
* This is simulated in a 10x10 coordinate grid
* */
public class KNN_Example_Code {
    public static void main(String[] args) {
        DataSet tested;
        //Simulation Parameters
        //Mapping Mode Simulation Parameters
        int xStart = 0;
        int yStart = 0;

        int xEnd = 10;
        int yEnd = 5;

        int timeTaken = 6;  //Assuming we take a RSSI reading every second.

        //Testing Mode Simulation Parameters
        int xPos = 1;
        int yPos = 1;

        // Sequence for Getting Current Position:
        // 1) Create new dataset holder in the activity
        tested = new DataSet();

        // 2) Start a new run when user wants to start mapping
        // Pass xStart and yStart
        tested.startRun(xStart,yStart);

        // 3) After a certain time, take a reading (Example here is 1 taken every second)
        // Required to pass a HashMap of <MAC_Address , RSSI_Value> in <String,Double> format
        for (int i = 0; i<timeTaken; i++){
            int currentX = (int) Math.round(xStart + i*(xEnd - xStart)/timeTaken);
            int currentY = (int) Math.round(yStart + i*(yEnd - yStart)/timeTaken);

            tested.insert( getAP(currentX,currentY) );
        }

        // 4) After user has reached the end, hit end run button
        // Pass xEnd and yEnd
        tested.endRun(xEnd,yEnd);

        // 5) Repeat as many runs as needed
        xStart = 8;
        xEnd = 1;
        yStart = 2;
        yEnd = 10;

        tested.startRun(xStart,yStart);
        for (int i = 0; i<timeTaken; i++){
            int currentX = (int) Math.round(xStart + i*(xEnd - xStart)/timeTaken);
            int currentY = (int) Math.round(yStart + i*(yEnd - yStart)/timeTaken);

            tested.insert( getAP(currentX,currentY) );
        }
        tested.endRun(xEnd,yEnd);

        // 6) After finishing all the runs, Normalize data before saving (Implement saving soon)
        tested.normalizeData();

        // Saving Instructions Here:
        // S1) Ensure floorplan filename is set. Can be used to load floorplan file later on.
        tested.setFloorplan("building2lv3");

        // S2) Get CSV formatted String using .toCSV() method
        String testedString = tested.toCSV();

        // S3) Save to file using Static Method
        // Note that filename does not have .csv format inside. It is provided in the method.
        SaveLoadCSV.saveCSV("test",testedString);

        // Loading Instructions Here:
        // L1) Call static CSV loading method before starting Testing Mode
        String receivedString =  SaveLoadCSV.loadCSV("test");

        System.out.println("Loading Check: "+receivedString.equals(testedString));

        // L2) Regenerate DataSet using CSV string. DataSet has built-in String Constructor
        DataSet loaded = new DataSet(receivedString);
        String loadedString = loaded.toCSV();
        System.out.println("Regen Check:\n"+loadedString+"\nBool: "+loadedString.equals(testedString));

        //Testing Mode Continues Here
        // T1) Initialize KNNTool (Can be done earlier if you want)
        KNNTool knn = new KNNTool();

        // T2) Pass Normalized data into KNN testset when entering Testing Mode
        // Ideally this would be done after loading up the DataSet file from Local/Online storage
        // A new DataSet would also be created before this to load the data, but since we're simulating, it is skipped
        knn.trainKNN(tested.getNormalMACAddr(),tested.getNormalData());

        // T3) Get current WIFI RSSI again, then feed into the test set
        knn.testKNN(getAP(xPos,yPos));

        // T4) Call getKNN to get coordinates back.
        // Returns as int[]
        int[] coord = knn.getKNN();

        if(coord!=null){
            System.out.println("\nFinal position is: "+coord[0]+","+coord[1]);
        }

        //Additional Debugging tools
        System.out.println("Final Test:\n" + testedString);
        DataSet reverse = new DataSet(testedString);
        String reverseString = reverse.toCSV();
        System.out.println("Final Test2:\n"+reverseString);
        System.out.println("Final Compare:" + reverse.toCSV().equals(testedString));
    }
    public static double wifi1(int x, int y){
        double result = 10-Math.sqrt(Math.pow(x-3,2)+Math.pow(y-7,2));
        System.out.println("\nResult 1:"+result);
        if (result<1){result=0;}
        return result;
    }
    public static double wifi2(int x, int y){
        double result = 10-Math.sqrt(Math.pow(x-8,2)+Math.pow(y-2,2));
        System.out.println("Result 2:"+result);
        if (result<1){result=0;}
        return result;
    }
    public static double wifi3(int x, int y){
        double result = 10-Math.sqrt(Math.pow(x-0,2)+Math.pow(y-0,2));
        System.out.println("Result 3:"+result);
        if (result<1){result=0;}
        return result;
    }
    public static HashMap<String,Double> getAP(int x, int y){
        HashMap<String,Double> result = new HashMap<>();
        result.put("10-10-10",wifi1(x,y));
        result.put("20-20-20",wifi2(x,y));
        result.put("30-30-30",wifi3(x,y));
        return result;
    }
}

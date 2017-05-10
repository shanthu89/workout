import java.util.ArrayList;
import java.util.List;

public class CoffeeMachine {
    // Class to define the input locations for wall and coffee.
    static class Dimension{
        private int x;
        private int y;
        Dimension(int x,int y){
            this.x = x;
            this.y = y;
        }
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
    }
    public static void main(String[] args){
        //Input List containing the Coffee Machine locations. Considered index starting from 0.
        List<Dimension> coffeeLocations = new ArrayList<>();
        Dimension c1 = new Dimension(0,2);
        Dimension c2 = new Dimension(2,1);
        coffeeLocations.add(c1);
        coffeeLocations.add(c2);
        //Input List containing the wall locations.
        List<Dimension> walls = new ArrayList<>();
        Dimension w1 = new Dimension(1,1);
        Dimension w2 = new Dimension(1,2);
        Dimension w3 = new Dimension(2,0);
        walls.add(w1);
        walls.add(w2);
        walls.add(w3);
        // Start Desk Location from where we have to measure distance to reach coffee machine.
        Dimension start = new Dimension(1,0);
        int steps = DistanceToCoffee(3,4,start,coffeeLocations,walls);
        System.out.println("Steps to nearest coffee machine: " + steps);
    }
    //OfficeGrid class to create 2D map from the given input.
    static class OfficeGrid{
        Location[][] officeMap;
        int numRows;
        int numCols;
        //Create 2D array of location containing details of each cell,its coordinates,type of cell.
        OfficeGrid(int nrows,int ncols,List<Dimension> coffeeLoc, List<Dimension> walls){
            this.numRows = nrows;
            this.numCols = ncols;
            officeMap = new Location[nrows][ncols];
            //Initialize all the Locations to Desk.
            for(int i=0;i<numRows;i++){
                for(int j=0;j<numCols;j++){
                    officeMap[i][j] = new Location();
                    officeMap[i][j].x = i;
                    officeMap[i][j].y = j;
                    officeMap[i][j].LocationType = "Desk";
                }
            }
            //Initialize Coffee Locations.
            for(Dimension d:coffeeLoc){
                officeMap[d.getX()][d.getY()].LocationType = "Coffee";
                officeMap[d.getX()][d.getY()].stepsToCoffee = 0;
            }
            //Initialize Wall Locations.
            for(Dimension d:walls){
                officeMap[d.getX()][d.getY()].LocationType = "Wall";
            }
        }
        //Function to view office grid.
        public void displayMap(){
            System.out.println("Office Map Created");
            for(int i=0;i<numRows;i++){
                for(int j=0;j<numCols;j++){
                    System.out.println(i+", " + j + "\t" +officeMap[i][j].LocationType );
                }
            }
        }
        //Function to get the valid neighbors for given cell eliminating the boundary cases.
        public List<Location> getNeighborLocations(Location current){
            List<Location> neighbors = new ArrayList<>();
            int x = current.getX();
            int y = current.getY();
            if(x-1>=0)
                neighbors.add(officeMap[x-1][y]);
            if(y-1>=0)
                neighbors.add(officeMap[x][y-1]);
            if(x+1<numRows)
                neighbors.add(officeMap[x+1][y]);
            if(y+1<numCols)
                neighbors.add(officeMap[x][y+1]);
            return neighbors;
        }
    }
    //Location class contains details about each cell in the grid. Information stored: X,Y co-ordinates, cell evaluation status, type of cell.
    static class Location{
        int x;
        int y;
        String LocationType;
        boolean statusInProgress=false;
        int stepsToCoffee = Integer.MAX_VALUE;
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getLocationType() {
            return LocationType;
        }
    }
    // Function to compute the distance to the nearest coffee machine.
    public static int findDistance(Location start,OfficeGrid map){
        if(start.stepsToCoffee<Integer.MAX_VALUE || start.LocationType == "Coffee")
            return start.stepsToCoffee;
        int steps = Integer.MAX_VALUE;
        List<Location> neighborList= map.getNeighborLocations(start);
        start.statusInProgress = true;
        for(Location l:neighborList){
            if(!l.statusInProgress && l.LocationType!="Wall"){
                steps = Math.min(findDistance(l,map),steps);
            }
        }
        if(steps==Integer.MAX_VALUE){//No route to coffee machine from this start. Return max value.
            return steps;
        }
        start.stepsToCoffee = steps+1;
        start.statusInProgress = false;
        return steps+1;
    }
    //Given method to compute the distance to coffee machine.
    public static int DistanceToCoffee(int numRows, int numColumns, Dimension DeskLocation, List<Dimension> coffeeLocations, List<Dimension> walls)
    {
        OfficeGrid grid = new OfficeGrid(numRows,numColumns,coffeeLocations,walls);
        //Call to display the officeGrid created.
        //grid.displayMap();
        Location start = grid.officeMap[DeskLocation.getX()][DeskLocation.getY()];
        System.out.println("Start Desk Location: (" + DeskLocation.getX() + "," + DeskLocation.getY() + ")" );
        //Return -1 if the given start Desk location is in case Wall.
        if(start.LocationType=="Wall")
            return -1;
        return findDistance(start,grid);
    }
}


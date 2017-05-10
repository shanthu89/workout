Code to find distance to coffee machine from desk.

Office layout is laid out in a grid - every cell is either a wall, which is impassible, or a desk (employees can walk through other employees desks to get to a coffee machine). Here is a sample office:
1) X : Wall 2) - : desk 3) C : Coffee

This sample office has 3 rows and 4 columns. The distance from the desk at (2,1) is 3, since it can reach the coffee machine in row 1 in three steps.
--C-
-XX-
XC--

-Code implemented using Java 1.8.
-Input parameter is passed for the DesktopLocation from the main function. 
-I have used index for rows and columns starting from 0. 
-Sample parameter for the distance from the desk at (1,0) is passed in the main function.

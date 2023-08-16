package SchiffeVersenken;

import java.util.HashMap;

public class Testing {
    public static void main(String[] args) {
    	DataModel datamodel = new DataModel();
        Controller controller = new Controller(datamodel);

        /*** Test 1 ***/
        //test the robot
        controller.setMatrixRobot();
        controller.printRobotMap();

        /*** Test 2***/
        //test the shooting
		/*HashMap<Integer, char[][]> response = client.writeServerData(spieler.buildPayload(menuWindow.getCurrentPosition()));
		for(Entry<Integer, char[][]> element : response.entrySet()) {
			System.out.println(element.getKey().toString());
        }	*/
    }
}

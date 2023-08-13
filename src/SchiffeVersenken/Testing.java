package SchiffeVersenken;

public class Testing {
    public static void main(String[] args) {
        Controller controller = new Controller();

        //test the robot
        controller.setShipsRobot();
        controller.printRobotMap();
    }
}

import java.sql.*;
import java.util.Scanner;

public class jdbc_example {

    // The instance variables for the class
    private Connection connection;
    private Statement statement;
    static int userInput;

    // The constructor for the class
    public jdbc_example() {
        connection = null;
        statement = null;
    }

    // The main program", that tests the methods
    public static void main(String[] args) throws SQLException {
        chooseCases();
    }
    public static void chooseCases() throws SQLException {
        Scanner scan1 = new Scanner(System.in);
        System.out.println("Enter the option to be selected from 1 to 6: " + "\n" +
                "********************************************");
        System.out.println("1 - Find Professors\n" +
                "2 - Find Sections\n" +
                "3 - Add Section\n" +
                "4 - Update Section\n" +
                "5 - Report Enrollments\n" +
                "6 - Quit\n");
        userInput = scan1.nextInt();

        if (userInput >= 1 && userInput <= 6) {
            switchCases(userInput);
        } else {
            System.out.println("Please enter the value between 1 and 6");
            chooseCases();
        }
    }

    public static int switchCases(int userInput) throws SQLException {
        Scanner sc = new Scanner(System.in);
        String Username = "USERNAME"; // Change to your own username
        String mysqlPassword = "PASSWORD"; // Change to your own mysql Password

        // Create a jdbc_example instance called test
        jdbc_example test = new jdbc_example();

        // Connect and set up the database for use
        test.initDatabase(Username, mysqlPassword);

        switch (userInput){
            case 1:
                test.query("SELECT DEPT_CODE FROM DEPT");
                System.out.println("Please enter the department name from the above list: ");
                String DEPT_CODE_FIND = sc.nextLine().toUpperCase();
                String deptNameInTable = test.queryString("SELECT DEPT_CODE FROM DEPT WHERE DEPT_CODE LIKE '%"+ DEPT_CODE_FIND +"%'");
                DEPT_CODE_FIND = deptNameInTable;
                while (DEPT_CODE_FIND == null){
                    System.out.println("Invalid Department. Please enter a valid department name from the list.");
                    DEPT_CODE_FIND = sc.nextLine().toUpperCase();
                }
                test.query("SELECT P.PROF_NAME FROM PROFESSOR P, DEPT D WHERE P.DEPT_CODE = D.DEPT_CODE AND P.DEPT_CODE LIKE '%" + DEPT_CODE_FIND + "%'");
                chooseCases();
            case 2:
                System.out.println("Please enter whether you are looking for open classes ('OC') or all classes ('AC'): ");
                String classes = sc.nextLine().toUpperCase();
                if(classes.equals("OC")){
                    System.out.println("Please enter if you want to select by Department or Level: ");
                    String FindSection = sc.nextLine().toUpperCase();
                    if(FindSection.equals("DEPARTMENT")){
                        test.query("SELECT DEPT_CODE FROM DEPT");
                        System.out.println("Please enter the Department name from the above list: ");
                        String DEPT_CODE_OC = sc.nextLine().toUpperCase();
                        test.query("SELECT DEPT_CODE, COURSE_NUM, ROOM_NUM, BUILDING, DAYS, START_TIME, END_TIME, START_DAY, END_DAY,(MAX_ENROLLMENT - CURRENT_ENROLLMENT) As SEATS_AVAILABLE FROM SECTION WHERE DEPT_CODE = '"+ DEPT_CODE_OC +"' AND MAX_ENROLLMENT > CURRENT_ENROLLMENT;");
                        chooseCases();
                    }else if(FindSection.equals("LEVEL")){
                        System.out.println("Please enter the level (2,3,4 or 5): ");
                        String DEPT_LEVEL_OC = sc.nextLine().toUpperCase();
                        test.query("SELECT DEPT_CODE, COURSE_NUM, ROOM_NUM, BUILDING, DAYS, START_TIME, END_TIME, START_DAY, END_DAY, (MAX_ENROLLMENT - CURRENT_ENROLLMENT) As SEATS_AVAILABLE FROM SECTION WHERE COURSE_NUM LIKE '" + DEPT_LEVEL_OC + "%' AND CURRENT_ENROLLMENT < MAX_ENROLLMENT;");
                        chooseCases();
                    }else {
                        System.out.println("Please enter Department or Level.");
                        chooseCases();
                    }
                }else if(classes.equals("AC")){
                    System.out.println("Please enter if you want to select by Department or Level: ");
                    String FindSection = sc.nextLine().toUpperCase();
                    if(FindSection.equals("DEPARTMENT")){
                        test.query("SELECT DEPT_CODE FROM DEPT");
                        System.out.println("Please enter the Department name from the above list: ");
                        String DEPT_CODE_C = sc.nextLine().toUpperCase();
                        test.query("SELECT DEPT_CODE, COURSE_NUM, ROOM_NUM, BUILDING, DAYS, START_TIME, END_TIME, START_DAY, END_DAY,(MAX_ENROLLMENT - CURRENT_ENROLLMENT) As SEATS_AVAILABLE FROM SECTION WHERE DEPT_CODE LIKE '%" + DEPT_CODE_C + "%'");
                        chooseCases();
                    }else if(FindSection.equals("LEVEL")){
                        System.out.println(FindSection);
                        System.out.println("Please enter the level (2,3,4 or 5): ");
                        String DEPT_LEVEL_C = sc.nextLine().toUpperCase();
                        test.query("SELECT DEPT_CODE, COURSE_NUM, ROOM_NUM, BUILDING, DAYS, START_TIME, END_TIME, START_DAY, END_DAY, (MAX_ENROLLMENT - CURRENT_ENROLLMENT) As SEATS_AVAILABLE FROM SECTION WHERE COURSE_NUM LIKE '" + DEPT_LEVEL_C + "%'");
                        chooseCases();
                    }else {
                        System.out.println("Please enter Department or Level.");
                        chooseCases();
                    }
                }else {
                    System.out.println("Please type 'OC' to view open classes OR 'AC' to view all classes'");
                    chooseCases();
                }
            case 3:
                System.out.println("List of available courses: ");
                test.query("SELECT COURSE_NAME FROM COURSE");
                System.out.println("Please enter the course name from the above list: ");
                String courseName = sc.nextLine();
                String courseNameFromCourse = test.queryString("SELECT COURSE_NAME FROM COURSE WHERE COURSE_NAME = '"+ courseName +"';");
                String deptCodeFromCourse = test.queryString("SELECT DEPT_CODE FROM COURSE WHERE COURSE_NAME = '"+ courseName +"';");
                String courseNumFromCourse = test.queryString("SELECT COURSE_NUM FROM COURSE WHERE COURSE_NAME = '"+ courseName +"';");

                if(courseName.equals(courseNameFromCourse)){
                    System.out.println("Please enter department code: ");
                    String departCode = sc.nextLine().toUpperCase();
                    while (departCode.isEmpty() || !(departCode.equals(deptCodeFromCourse))){
                        System.out.println("Department code cannot be empty. Please enter a valid department ("+ deptCodeFromCourse +") ");
                        departCode = sc.nextLine().toUpperCase();
                    }
                    System.out.println("Please enter course Number: ");
                    String courseNum = sc.nextLine().toUpperCase();
                    while (courseNum.isEmpty() || !(courseNum.equals(courseNumFromCourse))){
                        System.out.println("Course Number cannot be empty. Please a valid Number ("+ courseNumFromCourse +").");
                        courseNum = sc.nextLine().toUpperCase();
                    }
                    System.out.println("Please enter the Professor Name to add the PROF_ID: ");
                    String profName = sc.nextLine();
                    String profNamesInTable = test.queryString("SELECT PROF_NAME FROM PROFESSOR WHERE PROF_NAME = '"+ profName+"'");
                    while (profName.isEmpty() || (profNamesInTable==null)){
                        System.out.println("Professor Name cannot be empty. Please enter a valid Name: ");
                        profName = sc.nextLine();
                        profNamesInTable = test.queryString("SELECT PROF_NAME FROM PROFESSOR WHERE PROF_NAME = '"+ profName+"'");
                    }
                    int profID = Integer.parseInt(test.queryString("SELECT PROF_ID FROM PROFESSOR WHERE PROF_NAME LIKE '%" + profName + "%'"));

                    System.out.println("Please enter the ROOM_NUM: ");
                    Integer roomNum = sc.nextInt();
                    String checkroomNum = (test.queryString("SELECT ROOM_NUM FROM ROOM WHERE ROOM_NUM = "+ roomNum +""));
                    while(checkroomNum == null){
                        System.out.println("The Room number field cannot be empty. Please provide a valid room number.");
                        roomNum = sc.nextInt();
                        checkroomNum = (test.queryString("SELECT ROOM_NUM FROM ROOM WHERE ROOM_NUM = "+ roomNum +""));
                    }
                    sc.nextLine();
                    String addRoomBuilding = test.queryString("SELECT BUILDING FROM ROOM WHERE ROOM_NUM = "+ roomNum +" ");
                    System.out.println("Please enter the BUILDING: ");
                    String buildingNames = sc.nextLine().toUpperCase();
                    String checkBuilding = test.queryString("SELECT BUILDING FROM DEPT WHERE BUILDING LIKE '%"+ buildingNames +"%'");
                    buildingNames = checkBuilding;
                    while (checkBuilding == null || !addRoomBuilding.equals(buildingNames)){
                        System.out.println("Building Name cannot be empty OR Building Name is not found OR Building Name does not match with room number\n" +
                                            "Please enter valid Building Name: ");
                        buildingNames = sc.nextLine().toUpperCase();
                    }
                    System.out.println("Please enter the days like ('MWF'): ");
                    String days = sc.nextLine().toUpperCase();
                    System.out.println("Please enter the start time in the format '00:00': ");
                    String startTime = sc.nextLine().toUpperCase();
                    System.out.println("Please enter the end time in the format '00:00': ");
                    String endTime = sc.nextLine().toUpperCase();
                    System.out.println("Please enter the start date in the format ('YYYY-MM-DD OR 2023-08-01')");
                    String startDate = sc.nextLine();
                    while(startDate.isEmpty()){
                        System.out.println("Start date cannot be Null. Please enter the start date: ");
                        startDate = sc.nextLine();
                    }
                    System.out.println("Please enter the end date in the format ('YYYY-MM-DD OR 2023-07-12')");
                    String endDate = sc.nextLine();
                    while(endDate.isEmpty()){
                        System.out.println("End date cannot be Null. Please enter the start date: ");
                        endDate = sc.nextLine();
                    }
                    System.out.println("maximum enrollment: ");
                    int enrollCount = sc.nextInt();
                    System.out.println("Current enrollment: ");
                    int currEnroll = sc.nextInt();
                    int SID = Integer.parseInt(test.queryString("SELECT MAX(SID) FROM SECTION"));

                    //Insert into Table
                    test.insert("SECTION", ""+ (SID + 1) +" ,'"+ departCode +"', '"+courseNum+"', "+profID+", '"+ roomNum +"', " +
                            "'"+ buildingNames +"','"+days+"', '"+ startTime +"', '"+ endTime +"' ,'"+startDate +"', '"+ endDate +"',"+enrollCount+","+currEnroll+"");
                }else {
                    System.out.println("Please enter a valid course name.");
                    chooseCases();
                }
                chooseCases();
            case 4:
                System.out.println("Please enter the Department");
                String deptName = sc.nextLine().toUpperCase();
                System.out.println("Please enter the Course Number");
                String courseNumber = sc.nextLine().toUpperCase();
                test.query("SELECT* FROM SECTION WHERE DEPT_CODE LIKE '%" + deptName + "%' AND COURSE_NUM LIKE '%" + courseNumber + "%'");
                System.out.println("Please enter the valid SectionID: ");
                String sectionId = sc.nextLine().toUpperCase();
                while (sectionId.isEmpty()){
                    System.out.println("Section Id cannot be empty!.");
                    sectionId = sc.nextLine().toUpperCase();
                }
                System.out.println("Please enter the valid attribute name to update: ");
                String attributeName = sc.nextLine().toUpperCase();
                if(attributeName.equals("SID")  ){
                    System.out.println("Primary key cannot be updated. Please update other attributes.");
                    chooseCases();
                }else if(attributeName.equals("DEPT_CODE")){
                    System.out.println("Please enter the valid department name");
                    String updateDeptCode = sc.nextLine().toUpperCase();
                    String checkDept = test.queryString("SELECT DEPT_CODE FROM COURSE WHERE DEPT_CODE = '"+updateDeptCode+"'");
                    while (updateDeptCode.isEmpty() || checkDept == null){
                        System.out.println("Department cannot be empty OR Please enter a  valid department: ");
                        updateDeptCode = sc.nextLine().toUpperCase();
                        checkDept = test.queryString("SELECT DEPT_CODE FROM COURSE WHERE DEPT_CODE = '"+updateDeptCode+"'");
                    }
                    test.statement.executeUpdate("Update SECTION set DEPT_CODE = '"+ updateDeptCode +"' WHERE SID = '"+ sectionId +"'");
                    test.query("SELECT * FROM SECTION WHERE SID = '"+ sectionId +"'");
                }else if(attributeName.equals("COURSE_NUM")){
                    System.out.println("Please enter the valid Course number");
                    String updateCourseNum = sc.nextLine().toUpperCase();
                    String checkCourse = test.queryString("SELECT DEPT_CODE FROM DEPT WHERE DEPT_CODE = '"+updateCourseNum+"'");
                    while (updateCourseNum.isEmpty() || checkCourse == null){
                        System.out.println("Course number cannot be empty OR Please enter a  valid course number: ");
                        updateCourseNum = sc.nextLine().toUpperCase();
                        checkCourse = test.queryString("SELECT COURSE_NUM FROM COURSE WHERE COURSE_NUM = '"+updateCourseNum+"'");
                    }
                    test.statement.executeUpdate("Update SECTION set COURSE_NUM = '"+ updateCourseNum +"' WHERE SID = '"+ sectionId +"'");
                    test.query("SELECT * FROM SECTION WHERE SID = '"+ sectionId +"'");
                }else if(attributeName.equals("PROF_ID")){
                    System.out.println("Please enter the valid Professor Name: ");
                    String updateProfName = sc.nextLine();
                    String profNames = test.queryString("SELECT PROF_NAME FROM PROFESSOR WHERE PROF_NAME = '"+ updateProfName+"'");
                    while (updateProfName.isEmpty() || profNames == null){
                        System.out.println("Professor Name cannot be empty or Please enter a valid Name: ");
                        updateProfName = sc.nextLine();
                        profNames = test.queryString("SELECT PROF_NAME FROM PROFESSOR WHERE PROF_NAME = '"+ updateProfName+"'");
                    }
                    int updateProfId = Integer.parseInt(test.queryString("SELECT PROF_ID FROM PROFESSOR WHERE PROF_NAME LIKE '%" + updateProfName + "%'"));
                    test.statement.executeUpdate("Update SECTION set PROF_ID = '"+ updateProfId +"' WHERE SID = '"+ sectionId +"'");
                    test.query("SELECT * FROM SECTION WHERE SID = '"+ sectionId +"'");
                }else if(attributeName.equals("ROOM_NUM")){
                    System.out.println("Please enter the valid Room number: ");
                    String updateROOM_NUM = sc.nextLine().toUpperCase();
                    String checkRoom = test.queryString("SELECT ROOM_NUM FROM ROOM WHERE ROOM_NUM = '"+ updateROOM_NUM+"'");
                    String getBuilding = test.queryString("SELECT BUILDING FROM ROOM WHERE ROOM_NUM = '"+ updateROOM_NUM +"'");
                    String checkRoomBuilding = test.queryString("SELECT BUILDING FROM SECTION WHERE SID = '"+ sectionId +"'");
                    while (updateROOM_NUM.isEmpty() || checkRoom == null || !getBuilding.equals(checkRoomBuilding)){
                        System.out.println("Room number cannot empty OR Room number does not match the building. \nPlease give valid Room number: ");
                        updateROOM_NUM = sc.nextLine().toUpperCase();
                        checkRoom = test.queryString("SELECT ROOM_NUM FROM ROOM WHERE ROOM_NUM = '"+ updateROOM_NUM+"'");
                        getBuilding = test.queryString("SELECT BUILDING FROM ROOM WHERE ROOM_NUM = '"+ updateROOM_NUM +"'");
                    }
                    test.statement.executeUpdate("Update SECTION set ROOM_NUM = '"+ updateROOM_NUM +"' WHERE SID = '"+ sectionId +"'");
                    test.query("SELECT * FROM SECTION WHERE SID = '"+ sectionId +"'");
                }
                else if(attributeName.equals("BUILDING")){
                    System.out.println("Please enter the valid Building Name: ");
                    String updateBUILDING = sc.nextLine().toUpperCase();
                    String checkBuilding = test.queryString("SELECT BUILDING FROM ROOM WHERE BUILDING = '"+ updateBUILDING+"'");
                    String checkBuildingRoom = test.queryString("SELECT ROOM_NUM FROM SECTION WHERE SID = '"+ sectionId +"'");
                    String getRoom = test.queryString("SELECT BUILDING FROM ROOM WHERE ROOM_NUM = '"+ checkBuildingRoom +"'");
                    System.out.println("getRoom: " + getRoom + " checkRoom: " + checkBuildingRoom);
                    while (updateBUILDING.isEmpty() || checkBuilding == null || !getRoom.equals(updateBUILDING)){
                        System.out.println("Building cannot be empty OR Please enter a valid Building Name: ");
                        updateBUILDING = sc.nextLine().toUpperCase();
                        checkBuilding = test.queryString("SELECT BUILDING FROM ROOM WHERE BUILDING = '"+ updateBUILDING+"'");
                        getRoom = test.queryString("SELECT BUILDING FROM ROOM WHERE ROOM_NUM = '"+ checkBuildingRoom +"'");
                        System.out.println("getRoom: " + getRoom + " checkRoom: " + checkBuildingRoom);
                    }
                    test.statement.executeUpdate("Update SECTION set BUILDING = '"+ updateBUILDING +"' WHERE SID = '"+ sectionId +"'");
                    test.query("SELECT * FROM SECTION WHERE SID = '"+ sectionId +"'");
                }else if(attributeName.equals("DAYS")){
                    System.out.println("Please enter a valid Day ('MWF'): ");
                    String updateDay = sc.nextLine().toUpperCase();
                    test.statement.executeUpdate("Update SECTION set DAYS = '"+ updateDay +"' WHERE SID = '"+ sectionId +"'");
                    test.query("SELECT * FROM SECTION WHERE SID = '"+ sectionId +"'");
                }else if(attributeName.equals("START_TIME")){
                    System.out.println("Please enter a valid Start time ('8:35'): ");
                    String updateSTART_TIME = sc.nextLine().toUpperCase();
                    test.statement.executeUpdate("Update SECTION set START_TIME = '"+ updateSTART_TIME +"' WHERE SID = '"+ sectionId +"'");
                    test.query("SELECT * FROM SECTION WHERE SID = '"+ sectionId +"'");
                }else if(attributeName.equals("END_TIME")){
                    System.out.println("Please enter a valid End time ('9:35'): ");
                    String updateEND_TIME = sc.nextLine().toUpperCase();
                    test.statement.executeUpdate("Update SECTION set END_TIME = '"+ updateEND_TIME +"' WHERE SID = '"+ sectionId +"'");
                    test.query("SELECT * FROM SECTION WHERE SID = '"+ sectionId +"'");
                }else if(attributeName.equals("START_DAY")){
                    System.out.println("Please enter a valid Start date in the format ('YYYY-MM-DD OR 2023-08-01'): ");
                    String updateSTART_DAY = sc.nextLine();
                    while (updateSTART_DAY.isEmpty()){
                        System.out.println("Start Date cannot be empty.Please enter a valid Start date in the format ('YYYY-MM-DD OR 2023-08-01'): ");
                        updateSTART_DAY = sc.nextLine();
                    }
                    test.statement.executeUpdate("Update SECTION set START_DAY = '"+ updateSTART_DAY +"' WHERE SID = '"+ sectionId +"'");
                    test.query("SELECT * FROM SECTION WHERE SID = '"+ sectionId +"'");
                }else if(attributeName.equals("END_DAY")){
                    System.out.println("Please enter the end date in the format ('YYYY-MM-DD OR 2023-07-12'): ");
                    String updateEND_DAY = sc.nextLine().toUpperCase();
                    while (updateEND_DAY.isEmpty()){
                        System.out.println("End date cannot be empty OR Please enter the end date in the format ('YYYY-MM-DD OR 2023-07-12'): ");
                        updateEND_DAY = sc.nextLine().toUpperCase();
                    }
                    test.statement.executeUpdate("Update SECTION set END_DAY = '"+ updateEND_DAY +"' WHERE SID = '"+ sectionId +"'");
                    test.query("SELECT * FROM SECTION WHERE SID = '"+ sectionId +"'");
                }else if(attributeName.equals("MAX_ENROLLMENT")){
                    System.out.println("Please enter the Maximum enrollment: ");
                    String updateMaxEnroll = sc.nextLine().toUpperCase();
                    test.statement.executeUpdate("Update SECTION set MAX_ENROLLMENT = '"+ updateMaxEnroll +"' WHERE SID = '"+ sectionId +"'");
                    test.query("SELECT * FROM SECTION WHERE SID = '"+ sectionId +"'");
                }
                else if(attributeName.equals("CURRENT_ENROLLMENT")){
                    System.out.println("Please enter the valid Number");
                    String updateCurrentEnroll = sc.nextLine().toUpperCase();
                    test.statement.executeUpdate("Update SECTION set CURRENT_ENROLLMENT = '"+ updateCurrentEnroll +"' WHERE SID = '"+ sectionId +"'");
                    test.query("SELECT * FROM SECTION WHERE SID = '"+ sectionId +"'");
                }
                chooseCases();

            case 5:
                test.query("SELECT DEPT_CODE, SUM(CURRENT_ENROLLMENT) FROM SECTION GROUP BY DEPT_CODE");
                chooseCases();
            case 6:
                test.disConnect();
        }
        return userInput;
    }
    // Connect to the database
    public void connect(String Username, String mysqlPassword) throws SQLException {
        try {
            String url = "jdbc:mysql://localhost/" + Username + "?useSSL=false";
            System.out.println(url);
            connection = DriverManager.getConnection(url, Username, mysqlPassword);
        } catch (Exception e) {
            throw e;
        }
    }

    // Disconnect from the database
    public void disConnect() throws SQLException {
        connection.close();
        statement.close();
        System.exit(0);
    }

    // Execute an SQL query passed in as a String parameter
    // and print the resulting relation
    public void query(String q) {
        try {
            ResultSet resultSet = statement.executeQuery(q);
            System.out.println("Query: \n" + q + "\n\nResult: ");
            print(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Execute an SQL query passed in as a String parameter
    //and return the result
    public String queryString(String q) {
        String value = null;
        try {
            ResultSet resultSet = statement.executeQuery(q);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numColumns = metaData.getColumnCount();
            value = printRecordsReturn(resultSet, numColumns);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    // Print the results of a query with attribute names on the first line
    // Followed by the tuples, one per line
    public void print(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int numColumns = metaData.getColumnCount();

        printHeader(metaData, numColumns);
        printRecords(resultSet, numColumns);

    }

    // Print the attribute names
    public void printHeader(ResultSetMetaData metaData, int numColumns) throws SQLException {
        // 15 is the line space given between columns
        int topLine  = numColumns * 14;
        for (int i = 0; i < topLine; i++) {
            System.out.print("-");
        }
        System.out.println();
        for (int i = 1; i <= numColumns; i++) {
                System.out.print(String.format("%-14s",metaData.getColumnName(i)));
        }
        System.out.println();
        for (int i = 0; i < topLine; i++) {
            System.out.print("-");
        }
        System.out.println("");
    }

    // Print the attribute values for all tuples in the result
    public void printRecords(ResultSet resultSet, int numColumns) throws SQLException {
        String columnValue;
        int bottomLine  = numColumns * 14;
        while (resultSet.next()) {
            for(int i=1;i<=numColumns;i++) {
                System.out.printf("%-14s",resultSet.getString(i));
            }
            System.out.println();
            System.out.println("");
        }
        for (int i = 0; i < bottomLine; i++) {
            System.out.print("-");
        }
        System.out.println("");
    }
    public String printRecordsReturn(ResultSet resultSet, int numColumns) throws SQLException {
        String columnValues = null;
        while (resultSet.next()) {
            for (int i = 1; i <= numColumns; i++) {
                if (i > 1)
                    System.out.print(",  ");
                columnValues = resultSet.getString(i);
                //System.out.print(columnValues);
            }
            System.out.println("");
        }
        return columnValues;
    }

    // Insert into any table, any values from data passed in as String parameters
    public void insert(String table, String values) {
        String q = "INSERT into " + table + " values (" + values + ")";
        try {
            statement.executeUpdate(q);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // init and testing
    // Assumes that the tables are already created
    public void initDatabase(String Username, String Password) throws SQLException {
        System.out.println("in initDatabase");
        connect(Username, Password);
        // create a statement to hold mysql queries
        statement = connection.createStatement();
    }
}

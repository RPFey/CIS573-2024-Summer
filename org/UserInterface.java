import java.util.List;
import java.util.Scanner;

public class UserInterface {
    
    private DataManager dataManager;
    private Organization org;
    private Scanner in = new Scanner(System.in);

    public UserInterface(DataManager dataManager, Organization org) {
        this.dataManager = dataManager;
        this.org = org;
    }

    public void start() {

        boolean logout = false;
        while (!logout) {
            System.out.println("\n\n");
            if (org.getFunds().size() > 0) {
                System.out.println("There are " + org.getFunds().size() + " funds in this organization:");

                int count = 1;
                for (Fund f : org.getFunds()) {
                    System.out.println(count + ": " + f.getName());
                    count++;
                }
                System.out.println("Enter the fund number to see more information.");
            }
            System.out.println("Enter 0 to create a new fund");
            System.out.println("Enter m to change the password");
            System.out.println("Enter e to view or edit the organization name and description");
            System.out.println("Enter '-1' to logout");
            System.out.println("Enter 'quit' or 'q' to quit");

            boolean validInput = false;

            while (!validInput) {
                String option = in.nextLine();

                if (option.equals("quit") || option.equals("q")) {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }

                if(option.equalsIgnoreCase("m")){
                    String current_passwd_enter = getUserInput("Enter your current password: ");

                    String newPassword = getUserInput("Enter your new password: ");
                    String confirmNewPassword = getUserInput("Confirm your new password: ");

                    // check the current passwd
                    if (!current_passwd_enter.equals(org.getPasswd())) {
                        System.out.println("Invalid current password.");
                        break;
                    }

                    // match passwd
                    if (!newPassword.equals(confirmNewPassword)) {
                        System.out.println("Passwords do not match.");
                        break;
                    }

                    // update passwd
                    try {
                        boolean success = dataManager.updatePassword(org.getId(), org.getLogin(), newPassword, org.getName(), org.getDescription());
                        if (success) {
                            System.out.println("Password changed successfully.");
                        } else {
                            System.out.println("Failed to change password.");
                        }
                    } catch (IllegalStateException e) {
                        System.out.println("Error changing password: " + e.getMessage());
                    }
                    break;
                }
                else if (option.equalsIgnoreCase("e")){
                    editOrg();
                    break;
                }
                else {
                    try {
                        int opt = Integer.parseInt(option);
                        if (opt == -1) {
                            validInput = true;
                            logout = true;
                            break;
                        }
                        if (opt >= 0 && opt <= org.getFunds().size()) {
                            validInput = true;
                            if (opt == 0) {
                                createFund();
                            } else {
                                displayFund(opt);
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number between 0 and " + org.getFunds().size());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number or 'q' to quit.");
                    }
                }
            }
        }

    }

    public void editOrg() {

        System.out.println("Please enter current password");
        String current_passwd_enter = in.nextLine();
        if (!current_passwd_enter.equals(org.getPasswd())) {
            System.out.println("Invalid current password.");
            return;
        }
        // display current org name and description
        System.out.println("Current organization name: " + org.getName());
        System.out.println("Current organization description: " + org.getDescription());

        String newName = org.getName();
        String newDescription = org.getDescription();
        boolean editContent = false;

        System.out.println("\n Edit organization name? [Y/n]");
        String editName;
        while (true) {
            editName = in.nextLine();
            if (editName.equals("Y")) {
                System.out.println("Enter new organization name: ");
                boolean validname = false;
                while (!validname) {
                    newName = in.nextLine();
                    if (newName.trim().isEmpty()) {
                        System.out.println("Organization name cannot be blank. Please enter a valid name.");
                    } else {
                        validname = true;
                    }
                }
                editContent = true;
                break;
            }
            else if (editName.equals("n")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter Y or n.");
            }
        }

        System.out.println("\n Edit organization description? [Y/n]");
        String editDescription;
        while (true) {
            editDescription = in.nextLine();
            if (editDescription.equals("Y")) {
                System.out.println("Enter new organization description: ");
                boolean validDescription = false;
                while (!validDescription) {
                    newDescription = in.nextLine();
                    if (newDescription.trim().isEmpty()) {
                        System.out.println("Organization description cannot be blank. Please enter a valid description.");
                    } else {
                        validDescription = true;
                    }
                }
                editContent = true;
                break;
            }
            else if (editDescription.equals("n")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter Y or n.");
            }
        }
        if(editContent)
        {
            try {
                boolean success = dataManager.updatePassword(org.getId(), org.getLogin(), org.getPasswd(), newName, newDescription);
                if (success) {
                    org.setName(newName);
                    org.setDescription(newDescription);
                    System.out.println("Organization updated successfully.");
                } else {
                    System.out.println("Failed to update organization.");
                }
            } catch (IllegalStateException e) {
                System.out.println("Error updating organization: " + e.getMessage());
            }
        }
    }

    public void createFund() {

        // Modify this method so that it rejects any blank fund name or description,
        // or a negative value for the fund target, and gracefully handles any non-numeric input for the fund target.
        // In all cases, it should re-prompt the user to enter the value until they enter something valid.
        // It should not be possible for the user to crash the program by entering invalid inputs at the prompts.


        System.out.print("Enter the fund name: ");
        // String name = in.nextLine().trim();
        String name = "";
        while (name.trim().equals("")) {
            name = in.nextLine().trim();
            if (name.trim().equals("")) {
                System.out.println("Fund name cannot be blank.");
            }
        }

        System.out.print("Enter the fund description: ");
        String description = "";
        while (description.trim().equals("")) {
            description = in.nextLine().trim();
            if (description.trim().equals("")) {
                System.out.println("Fund description cannot be blank.");
            }
        }

        System.out.print("Enter the fund target: ");
        // long target = in.nextInt();
        // in.nextLine();
        long target = -1;
        while (target < 0) {
            try {
                target = Long.parseLong(in.nextLine());
                if (target < 0) {
                    System.out.println("Fund target cannot be negative.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                target = -1;
            }
        }
        try {
            Fund fund = dataManager.createFund(org.getId(), name, description, target);
            org.getFunds().add(fund);
            System.out.println("Fund created successfully.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Error creating fund: " + e.getMessage());
        }

    }


    public void displayFund(int fundNumber) {
        // Bug No.1 out of index
        // The bug is that the fundNumber is not checked to be within the bounds of the funds list.
        long totalDonations = 0;
        Fund fund = null;
        if (fundNumber <= org.getFunds().size() && fundNumber > 0) {
            fund = org.getFunds().get(fundNumber - 1);

            System.out.println("\n\n");
            System.out.println("Here is information about this fund:");
            System.out.println("Name: " + fund.getName());
            System.out.println("Description: " + fund.getDescription());
            System.out.println("Target: $" + fund.getTarget());

            List<Donation> donations = fund.getDonations();
            System.out.println("Number of donations: " + donations.size());

            for (Donation donation : donations) {
                System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on " + donation.getDate());
                totalDonations += donation.getAmount();
            }

            System.out.println("Show Aggregate Donations? (Y/n)");
            String input = in.nextLine();
            if (!input.equals("n")) {
                System.out.println("Aggregate Donations:");
                List<AggregateDonation> sorted = fund.getSortedAggregateDonations();
                for (AggregateDonation ad : sorted) {
                    System.out.println("* " + ad.getContributorName() + ": $" + ad.getTotal());
                }
            }
            // make donation
            System.out.println("Would you like to make a donation? (Y/n)");
            input = in.nextLine();
            if (!input.equals("n")) {
                makeDonation();
            }

        } else {
            System.out.println("Invalid fund number");
        }

        double donationPercentage = totalDonations * 1.0 / fund.getTarget();
        String percentage = String.format("%.2f", donationPercentage * 100);
        // set 2 decimal points

        System.out.println("Total donation amount: $" + totalDonations + "(" + percentage + "% of " +
                "the target)");


		while(true) {
			System.out.println("Press the Enter key to go back to the listing of funds");
			System.out.println(("Enter d to delete this fund"));
			String input = in.nextLine();
			if (input.isEmpty()) {
				break;
			}
			else if (input.equals("d")) {
				System.out.println("Are you sure you want to delete this fund? (y/n)");
				String confirm = in.nextLine();
				if (confirm.equals("y")) {
					boolean success = dataManager.deleteFund(fund.getId());
					if (!success) {
						System.out.println("Failed to delete fund.");
					}
					else {
						org.getFunds().remove(fundNumber - 1);
						System.out.println("Fund deleted.");
						break;
					}
				}
				else if (confirm.equals("n")) {
					continue;
				}
				else {
					System.out.println("Invalid input. Please re-enter.");
				}

			}
			else
			{
				System.out.println("Invalid input. Please re-enter.");
			}
		}


    }

    //make donation
    private void makeDonation() {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Making a donation...");
            System.out.print("Enter the fund number: ");
            int fundNumber = 0;

            try {
                fundNumber = Integer.parseInt(in.nextLine());
                if (fundNumber < 1 || fundNumber > org.getFunds().size()) {
                    System.out.println("[Invalid Input] Invalid fund number. Try Again.");
                    continue;
                }

                Fund fund = org.getFunds().get(fundNumber - 1);

                while (true) {
                    System.out.print("Enter the contributor Id: ");
                    String contributorId = in.nextLine().trim();

                    System.out.print("Enter the donation amount: ");
                    String amountStr = in.nextLine().trim();

                    try {
                        List<Donation> donations = dataManager.makeDonation(contributorId, fund.getId(), amountStr);
                        fund.setDonations(donations);
                        printIndividualDonation(donations);
                        return;
                    } catch (IllegalStateException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Try Again.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("[Invalid Input] Please enter a valid fund number.");
                continue;
            }
        }
    }

    private static void printIndividualDonation(List<Donation> donations) {
        System.out.println("Number of donations: " + donations.size());
        for (Donation donation : donations) {
            System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on " + donation.getDate());
        }
    }


    private static String getUserInput(String prompt) {
        Scanner loginScanner = new Scanner(System.in);
        System.out.print(prompt);
        String login = loginScanner.nextLine();
        if (login.equals("")) {
            return null;
        }
        return login;
    }

    private static String userLoginID() {
        return getUserInput("Enter your login ID: ");
    }

    private static String userLoginPassword() {
        return getUserInput("Enter your password: ");
    }

    private static String OrgNameInput() {
        return getUserInput("Enter your Org Name: ");
    }

    private static String OrgDescriptionInput() {
        return getUserInput("Enter your Org Description: ");
    }

    private static String getLoginCommand() {
        return getUserInput("Type L for [L]ogin or R for [R]egister: ");
    }

    public static void main(String[] args) {

        DataManager ds = new DataManager(new WebClient("localhost", 3001));
        String login = null, password = null;

        while(true){
            String command = getLoginCommand();
            if(command.equalsIgnoreCase("l")){
                // break the loop to login
                break;
            } else if (command.equalsIgnoreCase("r")){
                login = userLoginID();
                password = userLoginPassword();
                String name = OrgNameInput();
                String description = OrgDescriptionInput();
                if (name == null || description == null  || name.equals("") || description.equals("") ||
                        name == null || password == null || name.equals("") || description.equals("")){
                    System.out.println("login, password, Name, description cannot be blank.");
                    continue;
                }

                try {
                    Organization org = ds.registerOrganization(login, password, name, description);
                    if (org == null) {
                        System.out.println("Registration failed.");
                    }
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                    login = null;
                    password = null;
                    break;
                } catch(IllegalArgumentException e){
                    // print input error and continue
                    System.out.println(e.getMessage());
                    login = null;
                    password = null;
                    continue;
                }

                // break the loop to login
                break;
            } else {
                System.out.println("Invalid command.");
            }
        }

        boolean run = true;
        while (run) {
            while (true) {
                // get user input for login and password
                if (login == null) { login = userLoginID(); }
                if (password == null) {  password = userLoginPassword(); }

                if (login == null || password == null) {
                    System.out.println("Login and password cannot be blank.");
                    continue;
                }
                try {
                    Organization org = ds.attemptLogin(login, password, "public_key.pem");
                    if (org != null) {
                        UserInterface ui = new UserInterface(ds, org);
                        ui.start();
                        break;
                    } else {
                        System.out.println("Login failed.");
                    }
                } catch (IllegalStateException e) {
                    System.out.println("Error in communicating with server.");
                    System.out.println(e.getMessage());

                    login = null;
                    password = null;
                    break;
                }

                //login again
				System.out.println("Exit[Y]  Login[Enter]:");
				String in = new Scanner(System.in).nextLine();
				if (in.equalsIgnoreCase("Y")) {
					System.out.println("Goodbye!");
					run = false;

				}
            }
        }
    }

}


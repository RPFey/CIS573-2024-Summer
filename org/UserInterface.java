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
            System.out.println("Enter '-1' to logout");
            System.out.println("Enter 'quit' or 'q' to quit");

            boolean validInput = false;

            while (!validInput) {
                String option = in.nextLine();
                try {
                    if (option.equals("quit") || option.equals("q")) {
                        System.out.println("Goodbye!");
                        System.exit(0);
                    }
                    int opt = Integer.parseInt(option);
					if (opt == -1){
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

    private static String userLoginID() {
        Scanner loginScanner = new Scanner(System.in);
        System.out.print("Enter your login ID: ");
        String login = loginScanner.nextLine();
        if (login.equals("")) {
            return null;
        }
        return login;
    }

    private static String userLoginPassword() {
        Scanner loginScanner = new Scanner(System.in);
        System.out.print("Enter your password: ");
        String password = loginScanner.nextLine();
        if (password.equals("")) {
            return null;
        }
        return password;
    }

    public static void main(String[] args) {

        DataManager ds = new DataManager(new WebClient("localhost", 3001));

        boolean run = true;
        while (run) {
            while (true) {
                String login = userLoginID();
                String password = userLoginPassword();
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


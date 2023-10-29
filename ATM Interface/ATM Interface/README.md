## ATM Interface

The ATM class extends Application and is the main class for the JavaFX application.

It defines the primary stage for the application.

The createATMLayout method sets up the initial ATM interface with a card number field, PIN field, and number buttons for 0-9, "Clear," and "Enter."

The handleLogin method is called when the "Enter" button is clicked, and it validates the card number and PIN from the database.

The validateCardNumberAndPIN method checks if the card number and PIN combination is valid by querying the database.

When the login is successful, it calls the openDashboard method, which displays the dashboard based on the account type (Current or Savings).

The fetchNameFromDatabase method retrieves the account holder's name from the database based on the card number.

The handleCurrent,open_Current_Acc_Balance,open_Current_Acc_Deposit,and open_Current_Acc_Withdraw methods handle actions related to the Current Account dashboard. These methods set up the interface and handle balance display, deposit, and withdrawal for the current account.

The fetchCurrentAccountBalance method retrieves the current account balance from the database.

The handleDeposit method handles the deposit action for the current account, updating the balance in the database.

The open_SavingAccount,open_Savings_Acc_Balance, open_Savings_Acc_Deposit, and open_Savings_Acc_Withdraw methods handle actions related to the Savings Account dashboard. These methods set up the interface and handle balance display, deposit, and withdrawal for the savings account.

The fetchSavingsAccountBalance method retrieves the savings account balance from the database.

The handleSavingsDeposit method handles the deposit action for the savings account, updating the balance in the database.

The showAlert method displays information alerts using JavaFX's Alert class.

This code creates a basic ATM application with login functionality, a dashboard for both current and savings accounts, and allows users to check balances, deposit, and withdraw funds. It communicates with a MySQL database for account validation and balance management.

To use this application, you would need to set up a MySQL database with appropriate tables, including ATM_Pins,current_account,savings_account,and accholder. Additionally, you should create corresponding CSS styles for the UI elements based on the provided style class names.
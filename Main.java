import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }

    
    public String toString() {
        return carId + " - " + brand + " " + model;
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class CarRentalSystemGUI extends JFrame {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;
    
    private JTextField customerNameField, rentalDaysField;
    private JTextArea displayArea;
    private JComboBox<Car> availableCarsComboBox;

    public CarRentalSystemGUI() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();

        cars.add(new Car("C001", "Audi", "Avus Hatchback", 60.0));
        cars.add(new Car("C002", "Ford", "Bronco", 70.0));
        cars.add(new Car("C003", "Mahindra", "Thar", 150.0));

        setTitle("Car Rental System");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        
        inputPanel.add(new JLabel("Customer Name:"));
        customerNameField = new JTextField();
        inputPanel.add(customerNameField);

        inputPanel.add(new JLabel("Available Cars:"));
        availableCarsComboBox = new JComboBox<>();
        inputPanel.add(availableCarsComboBox);

        inputPanel.add(new JLabel("Rental Days:"));
        rentalDaysField = new JTextField();
        inputPanel.add(rentalDaysField);

        add(inputPanel, BorderLayout.NORTH);

        JButton rentButton = new JButton("Rent Car");
        rentButton.addActionListener(e -> rentCar());

        JButton returnButton = new JButton("Return Car");
        returnButton.addActionListener(e -> returnCar());

        JButton addCarButton = new JButton("Add Car");
        addCarButton.addActionListener(e -> adminLogin());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(rentButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(addCarButton);
        
        add(buttonPanel, BorderLayout.CENTER);

        displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.SOUTH);

        updateAvailableCarsList();
    }

    public void updateAvailableCarsList() {
        availableCarsComboBox.removeAllItems(); 
        for (Car car : cars) {
            if (car.isAvailable()) {
                availableCarsComboBox.addItem(car);
            }
        }
    }

    public void rentCar() {
        String customerName = customerNameField.getText();
        int rentalDays = Integer.parseInt(rentalDaysField.getText());

        Customer customer = new Customer("CUS" + (customers.size() + 1), customerName);
        customers.add(customer);

        Car selectedCar = (Car) availableCarsComboBox.getSelectedItem(); 

        if (selectedCar != null && selectedCar.isAvailable()) {
            selectedCar.rent();
            rentals.add(new Rental(selectedCar, customer, rentalDays));
            double totalPrice = selectedCar.calculatePrice(rentalDays);
            displayArea.append("Car rented: " + selectedCar.getBrand() + " " + selectedCar.getModel() +
                    " by " + customerName + ". Total Price: $" + totalPrice + "\n");

            updateAvailableCarsList(); 
        } else {
            displayArea.append("Car not available for rent.\n");
        }
    }

    public void returnCar() {
        String carIdToReturn = JOptionPane.showInputDialog(this, "Enter the Car ID to return:");
        Car selectedCar = null;

        for (Rental rental : rentals) {
            if (rental.getCar().getCarId().equals(carIdToReturn)) {
                selectedCar = rental.getCar();
                rental.getCar().returnCar();
                rentals.remove(rental);
                displayArea.append("Car returned: " + rental.getCar().getBrand() + " " + rental.getCar().getModel() + "\n");
                updateAvailableCarsList();
                return;
            }
        }

        if (selectedCar == null) {
            displayArea.append("Car ID not found or not rented.\n");
        }
    }

    
    public void adminLogin() {
        JPanel loginPanel = new JPanel(new GridLayout(2, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);

        int option = JOptionPane.showConfirmDialog(this, loginPanel, "Admin Login", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("admin") && password.equals("12345")) {
                showAddCarDialog();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void showAddCarDialog() {
        JDialog addCarDialog = new JDialog(this, "Add New Car", true);
        addCarDialog.setSize(300, 300);
        addCarDialog.setLayout(new GridLayout(5, 2));

        JTextField carIdField = new JTextField();
        JTextField brandField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField basePriceField = new JTextField();

        addCarDialog.add(new JLabel("Car ID:"));
        addCarDialog.add(carIdField);

        addCarDialog.add(new JLabel("Brand:"));
        addCarDialog.add(brandField);

        addCarDialog.add(new JLabel("Model:"));
        addCarDialog.add(modelField);

        addCarDialog.add(new JLabel("Base Price Per Day:"));
        addCarDialog.add(basePriceField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String carId = carIdField.getText();
            String brand = brandField.getText();
            String model = modelField.getText();
            double basePrice = Double.parseDouble(basePriceField.getText());

            Car newCar = new Car(carId, brand, model, basePrice);
            cars.add(newCar);
            updateAvailableCarsList(); 

            displayArea.append("New car added: " + brand + " " + model + "\n");
            addCarDialog.dispose();
        });

        addCarDialog.add(saveButton);
        addCarDialog.setVisible(true);
    }

    public static void main(String[] args) {
        CarRentalSystemGUI gui = new CarRentalSystemGUI();
        gui.setVisible(true);
    }
}



import java.util.*;
import java.io.*;

@SuppressWarnings("unused")
class Product {
    int id;
    String name;
    double price;
    int stock;

    Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    void display() {
        System.out.println(id + ". " + name
                + " - Rs." + price + " (Stock: " + stock + ")");
    }
}

class CartItem {
    Product product;
    int quantity;

    CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    double getAmount() {
        return product.price * quantity;
    }
}

public class OnlineShopping {
    static Scanner sc = new Scanner(System.in);

    static ArrayList<Product> products = new ArrayList<>();
    static ArrayList<CartItem> cart = new ArrayList<>();

    static void displayProducts() {
        System.out.println("\n--- AVAILABLE PRODUCTS ---");
        products.forEach(Product::display);
    }

    static Product searchProduct(String name) {
        for (Product p : products) {
            if (p.name.equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    static void addToCart() {
        System.out.print("Enter product name: ");
        String name = sc.nextLine().trim();

        Product p = searchProduct(name);

        if (p == null) {
            System.out.println("Product not found!");
            return;
        }

        System.out.print("Enter quantity: ");

        if (!sc.hasNextInt()) {
            System.out.println("Please enter a valid quantity.");
            sc.nextLine();
            return;
        }

        int quantity = sc.nextInt();
        sc.nextLine();

        int cartQuantity = 0;

        for (CartItem c : cart) {
            if (c.product.id == p.id) {
                cartQuantity = c.quantity;
                break;
            }
        }

        if (quantity <= 0 || quantity > p.stock) {
            System.out.println("Invalid quantity or insufficient stock!");
            return;
        }

        CartItem item = null;

        for (CartItem c : cart) {
            if (c.product.id == p.id) {
                item = c;
                break;
            }
        }

        if (item == null) {
            cart.add(new CartItem(p, quantity));
        } else {
            item.quantity += quantity;
        }

        p.stock -= quantity;

        System.out.println("Product added to cart successfully!");
    }

    static double calculateTotal() {
        return cart.stream()
                .mapToDouble(CartItem::getAmount)
                .sum();
    }

    static void viewCart() {
        System.out.println("\n--- YOUR SHOPPING CART ---");

        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        for (CartItem item : cart) {
            System.out.println(item.product.name
                    + " x " + item.quantity
                    + " = Rs." + item.getAmount());
        }

        System.out.println("Total Bill: Rs." + calculateTotal());
    }

    static void saveOrder() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Add products first.");
            return;
        }

        try (PrintWriter writer =
                     new PrintWriter(new FileWriter("orders.txt", true))) {

            writer.println("--- NEW ORDER ---");

            for (CartItem item : cart) {
                writer.println(item.product.name + " x "
                        + item.quantity + " = Rs."
                        + item.getAmount());
            }

            writer.println("Total: Rs." + calculateTotal());
            writer.println();

            System.out.println("Order saved successfully!");
            System.out.println("Final Bill: Rs." + calculateTotal());

            cart.clear();

        } catch (IOException e) {
            System.out.println("Unable to save order.");
        }
    }

    public static void main(String[] args) {
        products.add(new Product(1, "Laptop", 45000, 5));
        products.add(new Product(2, "Headphones", 1500, 10));
        products.add(new Product(3, "Mouse", 500, 20));
        products.add(new Product(4, "Keyboard", 1200, 15));

        int choice = 0;

        do {
            System.out.println("\n===== ONLINE SHOPPING SYSTEM =====");
            System.out.println("1. View Products");
            System.out.println("2. Search and Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Place Order");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                sc.nextLine();
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> displayProducts();

                case 2 -> addToCart();

                case 3 -> viewCart();

                case 4 -> saveOrder();

                case 5 -> System.out.println("Thank you for shopping!");

                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 5);

        sc.close();
    }
}


package com.kha333n.cs506;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Main {

	static Cart myCart = new Cart();
	static ArrayList<Product> products = new ArrayList<Product>();

	public static void main(String[] args) {
		try {
			FileReader inventory = new FileReader("inventoryDB.txt");
			BufferedReader inventoryBuffer = new BufferedReader(inventory);

			String tokens[] = null;
			String id, name, price, quantity, inCart;
			String line = inventoryBuffer.readLine();
			while (line != null) {
				tokens = line.split(",");
				id = tokens[0];
				name = tokens[1];
				price = tokens[2];
				quantity = tokens[3];
				inCart = tokens[4];
				Product product = new Product(Integer.parseInt(id), name,
						Float.parseFloat(price), Integer.parseInt(quantity));
				if (Integer.parseInt(inCart) > 0) {
					myCart.addItem(product, Integer.parseInt(inCart));
				}
				products.add(product);
				line = inventoryBuffer.readLine();
			}


			inventoryBuffer.close();
			;
			inventory.close();
		} catch (IOException e) {
		}
		showGUI();

	}

	private static void showGUI() {
		// Main Menu
		int menuSelection = 0;
		do {
			try {
				menuSelection = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								"Please enter\n" +
										"1 for 'Add item(s) to cart'\n" +
										"2 for 'Remove item from cart'\n" +
										"3 for 'Goto checkout'\n" +
										"4 for 'Empty the cart'\n" +
										"5 for 'Managing Products'\n" +
										"6 for 'Exiting the program'\n",
								"BookShop Cart\n",
								3)
				);
			} catch (NumberFormatException exception) {
				int input;
				do {
					try {
						input = Integer.parseInt(
								JOptionPane.showInputDialog(null,
										"Input must be a positive number\n" +
												"Please Enter again:",
										"Invalid Input", 2)
						);
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Input is not a Number",
								"Invalid Input", 2);
						input = 0;
					}
				} while (!(input > 0));
			}

			switch (menuSelection) {
				case 1:
					addItem();
					break;
				case 2:
					removeItem();
					break;
				case 3:
					checkOutCart();
					break;
				case 4:
					removeAll();
					break;
				case 5:
					manageProducts();
					break;
				case 6:
					developerInfo();
					System.exit(0);
					break;
				default:
					JOptionPane.showMessageDialog(null,
							"Please enter values from given options.",
							"Invalid Input", 2);
			}

		} while (true);
	}

	private static void addItem() {
		String productsList = "";
		int index = 0, input = 0;
		try {
			for (; index < products.size(); index++) {
				productsList = productsList + index + " to add \"" + products.get(index).getName() + "\" (RS: " +
						products.get(index).getPrice() + ") Available items: " + products.get(index).getQuantity() + "\n";
			}

			try {
				input = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								productsList
										+ index + " to go back.",
								"Add to cart", 3)
				);
				if (input > products.size() + 1) {
					JOptionPane.showMessageDialog(null,
							"Invalid item selected.",
							"Try Again", 2);
				} else {
					if (input == index) {
					} else addProductQuantity(input, products.get(input).getQuantity());
				}

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"Invalid Input.");
			}
		} catch (IndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null,
					"No Item in the inventory",
					"Inventory", 2);
		}
	}

	private static void addProductQuantity(int input, int productQuantity) {
		int quantity = 0;
		do {
			try {
				quantity = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								"Enter Number of items to add:\n" +
										"Must be less than or equal to " + productQuantity + " items." +
										"\nEnter " + (productQuantity + 1) + " to go back.",
								"Quantity", 3)
				);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"Input must be a number.",
						"Invalid Input", 2);
			}
		} while (quantity < 1 || quantity > (productQuantity + 1));
		if (quantity == (productQuantity + 1)) {
		} else {
			myCart.addItem(products.get(input), quantity);
			products.get(input).setQuantity(productQuantity - quantity);
		}
	}

	private static void removeItem() {
		ArrayList<Product> cartItems = new ArrayList<>();
		cartItems = myCart.getItemsList();
		if (cartItems.size() == 0) {
			JOptionPane.showMessageDialog(null,
					"No item(s) in the cart",
					"Cart is empty", 2);
			return;
		}
		String itemsList = "";
		int index = 0, selection = 0;
		for (; index < cartItems.size(); index++) {
			itemsList = itemsList + index + " to remove " + cartItems.get(index).getName() + "\n";
		}
		do {
			try {
				selection = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								"Please Enter \n" + itemsList
										+ index + " to go back\n",
								"Remove an item", 3)
				);
				if (selection <= index && selection >= 0) {
					if (selection == index) {
					} else {
						int temp = myCart.getItemsList().get(selection).getInCart();
						if (temp > 0) {
							myCart.getItemsList().get(selection).setQuantity(
									myCart.getItemsList().get(selection).getQuantity() + temp
							);
						}
						myCart.removeItem(selection);
					}
				} else JOptionPane.showMessageDialog(null,
						"Enter from given option.",
						"Invalid Input", 2);

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"Input is not a number.",
						"Invalid Input", 2);
			}
		} while (selection > index);
	}

	private static void checkOutCart() {
		ArrayList<Product> cartItems = new ArrayList<>();
		cartItems = myCart.getItemsList();
		if (cartItems.size() == 0) {
			JOptionPane.showMessageDialog(null,
					"No item(s) in the cart",
					"Cart is empty", 2);
			return;
		}
		String itemsList = "";
		int index = 0, totalItems = 0;
		float subTotal = 0;
		for (; index < cartItems.size(); index++) {
			float tempPrice = cartItems.get(index).getPrice();
			int tempQuantity = cartItems.get(index).getInCart();
			itemsList = itemsList + index + "  " + cartItems.get(index).getName() + " RS. " +
					tempPrice + " x " + tempQuantity + " = " + (tempPrice * tempQuantity) + "\n";
			totalItems = totalItems + tempQuantity;
			subTotal = subTotal + (tempPrice * tempQuantity);
		}
		int selection = -1;
		do {
			try {
				selection = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								itemsList + "\n\nNo. of items: " +
										+totalItems + " - Total Bill: Rs " + subTotal +
										"\n\nEnter 1 to complete checkout and clear cart\n" +
										"Enter 0 to go back.",
								"Checkout", 3)
				);
				switch (selection) {
					case 0:
						return;
					case 1:
						for (Product p:cartItems
						     ) {
							p.setInCart(0);
						}
						myCart.emptyCart();
						return;
					default:
						JOptionPane.showMessageDialog(null,
								"Input must be 0 or 1",
								"Invalid Input", 2);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"Input must be 0 or 1",
						"Invalid Input", 2);
			}

		} while (selection != 0 || selection != 1);
	}

	private static void removeAll() {

		if (myCart.getItemsList().size() == 0) {
			JOptionPane.showMessageDialog(null,
					"No item(s) in the cart",
					"Cart is empty", 2);
			return;
		}
		int selection = -1;
		do {
			try {
				selection = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								"Enter 1 to clear cart\n" +
										"Enter 0 to go back.",
								"Clear Cart", 3)
				);
				switch (selection) {
					case 0:
						return;
					case 1:
						myCart.emptyCart();
						for (Product p : products
						) {
							if (p.getInCart() > 0) {
								p.setQuantity(p.getQuantity() + p.getInCart());
								p.setInCart(0);
							}
						}
						JOptionPane.showMessageDialog(null,
								"Cart has been cleared",
								"Cleared", 2);
						return;
					default:
						JOptionPane.showMessageDialog(null,
								"Input must be 0 or 1",
								"Invalid Input", 2);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"Input must be 0 or 1",
						"Invalid Input", 2);
			}

		} while (selection != 0 || selection != 1);
	}

	private static void developerInfo() {
		try {
			FileWriter inventoryWriter = new FileWriter("inventoryDB.txt");
			PrintWriter inventoryPrintWriter = new PrintWriter(inventoryWriter);

			String productsList = "";
			try {
				for (int i = 0; i < products.size(); i++) {
					productsList = products.get(i).getId() + "," + products.get(i).getName() + "," +
							products.get(i).getPrice() + "," + products.get(i).getQuantity()
							+ "," + products.get(i).getInCart();
					inventoryPrintWriter.println(productsList);
				}
				inventoryPrintWriter.flush();
				inventoryPrintWriter.close();
				inventoryWriter.close();
			} catch (IndexOutOfBoundsException e) {
			}

		} catch (IOException ioException) {
			JOptionPane.showMessageDialog(null,
					"Failed to save progress...",
					"WARNING", 2);
		}
		JOptionPane.showMessageDialog(null,
				"Developed By: Usman Khan (BC190201004)",
				"Developer Info", 1);
	}

	private static void manageProducts() {
		//Products Menu
		int menuSelection = 0;
		do {
			try {
				menuSelection = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								"Please enter\n" +
										"1 for 'Add Product'\n" +
										"2 for 'Remove Product'\n" +
										"3 for 'List Products'\n" +
										"4 for 'Goto Main Menu'\n",
								"Manage Products\n",
								3)
				);
			} catch (NumberFormatException exception) {
				int input;
				do {
					try {
						input = Integer.parseInt(
								JOptionPane.showInputDialog(null,
										"Input must be a positive number\n" +
												"Please Enter again:",
										"Invalid Input", 2)
						);
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Input is not a Number",
								"Invalid Input", 2);
						input = 0;
					}
				} while (!(input > 0));
			}

			switch (menuSelection) {
				case 1:
					addProduct();
					break;
				case 2:
					removeProduct();
					break;
				case 3:
					listProducts();
					break;
				case 4:
					break;
				default:
					JOptionPane.showMessageDialog(null,
							"Please enter values from given options.",
							"Invalid Input", 2);
			}

		} while (menuSelection != 4);
	}

	private static void listProducts() {
		String productsList = "";
		int index = 0;
		try {
			for (; index < products.size(); index++) {
				productsList = productsList + index + " >> " + products.get(index).getId() + "    " + products.get(index).getName() + "    " +
						products.get(index).getPrice() + "    " + products.get(index).getQuantity() + "\n";
			}


			try {
				index = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								productsList + "Enter any number to Go Back.",
								"Product to remove", 3)
				);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"Invalid Input.");
			}
		} catch (IndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null,
					"No Item in the inventory",
					"Inventory", 2);
		}
	}

	private static void removeProduct() {
		String productsList = "";
		int input, i;
		try {
			for (i = 0; i < products.size(); i++) {
				productsList = productsList + i + " " + products.get(i).getId() + ", " + products.get(i).getName() + ", " +
						products.get(i).getPrice() + ", " + products.get(i).getQuantity() + "\n";
			}


			try {
				input = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								"Enter Product number to remove:" +
										productsList
										+ i + " to go back",
								"Product to remove", 2)
				);
				if (input == i) return;
				if (input > products.size()) {
					JOptionPane.showMessageDialog(null,
							"Item is not in list",
							"No item found", 2);
				} else {
					products.remove(input);
				}

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"Invalid Product Number entered.");
			}
		} catch (IndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null,
					"No Item in the inventory",
					"Inventory", 2);
		}

	}

	private static void addProduct() {
		int id = -1, quantity;
		String name;
		float price;
		try {
			id = Integer.parseInt(
					JOptionPane.showInputDialog(null,
							"Enter Product ID\n" +
									"Enter 0 to cancel adding product.",
							"Adding Product", 3)
			);
			if (id == 0) return;
		} catch (NumberFormatException e) {
			do {
				try {
					id = Integer.parseInt(
							JOptionPane.showInputDialog(null,
									"ID of product must be positive number\n" +
											"Please Enter ID again:\n" +
											"Enter 0 to cancel adding product.",
									"Invalid Product ID", 2)
					);
				} catch (NumberFormatException exception) {
					JOptionPane.showMessageDialog(null, "Input is not a Number",
							"Invalid Input", 2);
					id = -1;
				}
				if (id == 0) return;
			} while (!(id > 0));
		}
		name = JOptionPane.showInputDialog(null,
				"Enter Product Name\n" +
						"Enter 0 to cancel adding product.",
				"Adding Product", 3);
		if (name.equals("0")) return;
		try {
			price = Float.parseFloat(
					JOptionPane.showInputDialog(null,
							"Enter Product Price\n" +
									"Enter 0 to cancel adding product.",
							"Adding Product", 3)
			);
			if (price == 0) return;
		} catch (NumberFormatException e) {
			do {
				try {
					price = Float.parseFloat(
							JOptionPane.showInputDialog(null,
									"Price of product must be positive number\n" +
											"Please Enter Price again:\n" +
											"Enter 0 to cancel adding product.",
									"Invalid Product Price", 2)
					);
				} catch (NumberFormatException exception) {
					JOptionPane.showMessageDialog(null, "Input is not a Number",
							"Invalid Input", 2);
					price = -1;
				}
				if (price == 0) return;
			} while (!(price > 0));
		}

		try {
			quantity = Integer.parseInt(
					JOptionPane.showInputDialog(null,
							"Enter Product Quantity\n" +
									"Enter 0 to cancel adding product.",
							"Adding Product", 3)
			);
			if (quantity == 0) return;
		} catch (NumberFormatException e) {
			do {
				try {
					quantity = Integer.parseInt(
							JOptionPane.showInputDialog(null,
									"Quantity of product must be positive number\n" +
											"Please Enter Quantity again:\n" +
											"Enter 0 to cancel adding product.",
									"Invalid Product Quantity", 2)
					);
				} catch (NumberFormatException exception) {
					JOptionPane.showMessageDialog(null, "Input is not a Number",
							"Invalid Input", 2);
					quantity = -1;
				}
				if (quantity == 0) return;
			} while (!(quantity > 0));
		}

		Product p = new Product(id, name, price, quantity);
		products.add(p);
		JOptionPane.showMessageDialog(null,
				"Product Added Successful.",
				"Congrats", 1);
	}

}

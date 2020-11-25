/****************************************************************
* <<<<<<<<<<<<<<<<<CS506 Assignment 1>>>>>>>>>>>>>>>>>>>>>>>>>>>>
* <<<<<<<<<<<<<<<<<BC190201004>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
****************************************************************/

/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * For handling NumberFormatException
 *  try{
 *      //Normal code
 *  }catch(){
 *     //loop to handle concurrent incorrect inputs.
 *      do{
 *          try{
 *              //Show ERROR Message and ask again for input.
 *          }catch(){
 *              //ERROR Message if again invalid input.
 *          }
 *      }while(Input is not valid)
 *  }
 *
 * This method is used everywhere...
 **************************************************************/


package com.kha333n.cs506;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Main {

	static Cart myCart = new Cart();
	static ArrayList<Product> products = new ArrayList<>();

	public static void main(String[] args) {

//>>>>>>>>>>    Saving progress in "inventoryDB.txt" file.

		try {
			FileReader inventory = new FileReader("inventoryDB.txt");
			BufferedReader inventoryBuffer = new BufferedReader(inventory);

			String[] tokens;
			String id, name, price, quantity, inCart;
			String line = inventoryBuffer.readLine();
			while (line != null) {
				tokens = line.split(",");
				id = tokens[0];
				name = tokens[1];
				price = tokens[2];
				quantity = tokens[3];
				inCart = tokens[4];
//>>>>>>>>>> Initializing Products...
				Product product = new Product(Integer.parseInt(id), name,
						Float.parseFloat(price), Integer.parseInt(quantity));
//>>>>>>>>>> Initializing Cart status...
				if (Integer.parseInt(inCart) > 0) {
					myCart.addItem(product, Integer.parseInt(inCart));
				}
				products.add(product);
				line = inventoryBuffer.readLine();
			}


			inventoryBuffer.close();
			inventory.close();
		} catch (IOException e) {

//>>>>>>> Nothing to do if file failed to load...
		}

//>>>>>> Show the main GUI...
		showGUI();

	}

	private static void showGUI() {
//>>>>>> Main Menu
		int menuSelection = 0;
//>>>>> Loop to keep showing menu until user dose not explicitly exits...
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
								JOptionPane.QUESTION_MESSAGE)
				);
/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/
			} catch (NumberFormatException exception) {
				int input;
				do {
					try {
						input = Integer.parseInt(
								JOptionPane.showInputDialog(null,
										"Input must be a positive number\n" +
												"Please Enter again:",
										"Invalid Input", JOptionPane.WARNING_MESSAGE)
						);
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Input is not a Number",
								"Invalid Input", JOptionPane.WARNING_MESSAGE);
						input = 0;
					}
				} while (!(input > 0));
			}
//>>>>>>>>> End of "NumberFormatException" handling...
			switch (menuSelection) {
				case 1 -> addItem();
				case 2 -> removeItem();
				case 3 -> checkOutCart();
				case 4 -> removeAll();
				case 5 -> manageProducts();
				case 6 -> {
					developerInfo();
					System.exit(0);
				}
				default -> JOptionPane.showMessageDialog(null,
						"Please enter values from given options.",
						"Invalid Input", JOptionPane.WARNING_MESSAGE);
			}

		} while (true);
//>>>>>>>>> End of Menu Loop...
	}

	private static void addItem() {
		StringBuilder productsList = new StringBuilder();
		int index = 0, input;
		try {
//>>>>>> "IndexOutOfBoundException" handling...

//>>>>>> Looping through inventory and creating product list to display...
			for (; index < products.size(); index++) {
				productsList.append(index).append(" to add \"").append(products.get(index).getName()).append("\" (RS: ")
						.append(products.get(index).getPrice()).append(") Available items: ")
						.append(products.get(index).getQuantity()).append("\n");
			}

			try {
/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/

//>>>>>>>>>>>>>> Show products list and asking user to select product to add to cart...
				input = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								productsList.toString()
										+ index + " to go back.",
								"Add to cart", JOptionPane.QUESTION_MESSAGE)
				);
//>>>>>>>>>>>>>> Checking if user input is withing the listed items...
				if (input > products.size() + 1) {
					JOptionPane.showMessageDialog(null,
							"Invalid item selected.",
							"Try Again", JOptionPane.WARNING_MESSAGE);
				} else {
//>>>>>>>>>>>>>> If valid, and user doesn't want to go back then,
//>>>>>>>>>>>>>> Calling "addProductQuantity" with product and Quantity to add to cart. (just for not making this method much lengthy.)
					//noinspection StatementWithEmptyBody
					if (input == index) {
					} else addProductQuantity(input, products.get(input).getQuantity());
				}

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"Invalid Input.");
			}
//>>>>>>>>> End of "NumberFormatException" handling...
		} catch (IndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null,
					"No Item in the inventory",
					"Inventory", JOptionPane.WARNING_MESSAGE);
		}
	}

	private static void addProductQuantity(int input, int productQuantity) {
		int quantity = 0;
		do {
			try {
/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/
				quantity = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								"Enter Number of items to add:\n" +
										"Must be less than or equal to " + productQuantity + " items." +
										"\nEnter " + (productQuantity + 1) + " to go back.",
								"Quantity", JOptionPane.QUESTION_MESSAGE)
				);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"Input must be a number.",
						"Invalid Input", JOptionPane.WARNING_MESSAGE);
			}
//>>>>>>>>> End of "NumberFormatException" handling...
		} while (quantity < 1 || quantity > (productQuantity + 1));
		//noinspection StatementWithEmptyBody
		if (quantity == (productQuantity + 1)) {
		} else {
//>>>>> If user doesn't want to cancel, Add to cart...
			myCart.addItem(products.get(input), quantity);
			products.get(input).setQuantity(productQuantity - quantity);
		}
	}

	private static void removeItem() {
		ArrayList<Product> cartItems;
		cartItems = myCart.getItemsList();
//>>>>> If nothing in cart... show message and return...
		if (cartItems.size() == 0) {
			JOptionPane.showMessageDialog(null,
					"No item(s) in the cart",
					"Cart is empty", JOptionPane.WARNING_MESSAGE);
			return;
		}
		StringBuilder itemsList = new StringBuilder();
		int index = 0, selection = 0;
//>>>>> Created list...
		for (; index < cartItems.size(); index++) {
			itemsList.append(index).append(" to remove ").append(cartItems.get(index).getName()).append("\n");
		}
/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/
		do {
			try {
				selection = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								"Please Enter \n" + itemsList
										+ index + " to go back\n",
								"Remove an item", JOptionPane.QUESTION_MESSAGE)
				);
				if (selection <= index && selection >= 0) {
					//noinspection StatementWithEmptyBody
					if (selection == index) {
					} else {
//>>>>>>>>>>>>>> If user provide valid input to remove item, add products back to inventory and remove from cart...
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
						"Invalid Input", JOptionPane.WARNING_MESSAGE);

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"Input is not a number.",
						"Invalid Input", JOptionPane.WARNING_MESSAGE);
			}
		} while (selection > index);
//>>>>>>>>> End of "NumberFormatException" handling...
	}

	private static void checkOutCart() {
		ArrayList<Product> cartItems;
		cartItems = myCart.getItemsList();
//>>>>> If nothing in cart... show message and exit from method...

		if (cartItems.size() == 0) {
			JOptionPane.showMessageDialog(null,
					"No item(s) in the cart",
					"Cart is empty", JOptionPane.WARNING_MESSAGE);
			return;
		}
		StringBuilder itemsList = new StringBuilder();
		int index = 0, totalItems = 0;
		float subTotal = 0;

//>>>>> Creating a list of cart items to display through for loop...
		for (; index < cartItems.size(); index++) {
			float tempPrice = cartItems.get(index).getPrice();
			int tempQuantity = cartItems.get(index).getInCart();

			itemsList.append(index).append("  ").append(cartItems.get(index).getName()).append(" RS. ")
					.append(tempPrice).append(" x ").append(tempQuantity).append(" = ")
					.append(tempPrice * tempQuantity).append("\n");

			totalItems = totalItems + tempQuantity;
			subTotal = subTotal + (tempPrice * tempQuantity);
		}

		int selection = -1;

/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/
		do {
			try {
				selection = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								itemsList + "\n\nNo. of items: " +
										+totalItems + " - Total Bill: Rs " + subTotal +
										"\n\nEnter 1 to complete checkout and clear cart\n" +
										"Enter 0 to go back.",
								"Checkout", JOptionPane.QUESTION_MESSAGE)
				);
				switch (selection) {
					case 0:
//>>>>>>>>>> User want to go back...
						return;
					case 1:
//>>>>>>>>>> Complete Checkout and Remove items from the cart...
						for (Product p:cartItems
						     ) {
							p.setInCart(0);
						}
						myCart.emptyCart();
						return;
					default:
						JOptionPane.showMessageDialog(null,
								"Input must be 0 or 1",
								"Invalid Input", JOptionPane.WARNING_MESSAGE);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"Input must be 0 or 1",
						"Invalid Input", JOptionPane.WARNING_MESSAGE);
			}
//>>>>>>>>> End of "NumberFormatException" handling...
		} while (selection != 0 || (selection != 1));

	}

	private static void removeAll() {
//>>>>>> nothing to remove...
		if (myCart.getItemsList().size() == 0) {
			JOptionPane.showMessageDialog(null,
					"No item(s) in the cart",
					"Cart is empty", JOptionPane.WARNING_MESSAGE);
			return;
		}
		int selection = -1;

/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/
		do {
			try {
				selection = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								"Enter 1 to clear cart\n" +
										"Enter 0 to go back.",
								"Clear Cart", JOptionPane.QUESTION_MESSAGE)
				);
				switch (selection) {
					case 0:
						return;
					case 1:
//>>>>>>>>>>>>>> Remove items from cart and add back to inventory...
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
								"Cleared", JOptionPane.WARNING_MESSAGE);
						return;
					default:
						JOptionPane.showMessageDialog(null,
								"Input must be 0 or 1",
								"Invalid Input", JOptionPane.WARNING_MESSAGE);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"Input must be 0 or 1",
						"Invalid Input", JOptionPane.WARNING_MESSAGE);
			}
//>>>>>>>>> End of "NumberFormatException" handling...
		} while (selection != 0 || selection != 1);
	}

	private static void developerInfo() {
//>>>>> Saving progress of program to "inventoryDB.txt" file...
		try {
			FileWriter inventoryWriter = new FileWriter("inventoryDB.txt");
			PrintWriter inventoryPrintWriter = new PrintWriter(inventoryWriter);

			String productsList;
			try {
				for (Product product : products) {
					productsList = new StringBuilder().append(product.getId()).append(",").append(product.getName())
							.append(",").append(product.getPrice()).append(",").append(product.getQuantity())
							.append(",").append(product.getInCart()).toString();

					inventoryPrintWriter.println(productsList);
				}
				inventoryPrintWriter.flush();
				inventoryPrintWriter.close();
				inventoryWriter.close();
			} catch (IndexOutOfBoundsException ignored) {
			}

		} catch (IOException ioException) {
			JOptionPane.showMessageDialog(null,
					"Failed to save progress...",
					"WARNING", JOptionPane.WARNING_MESSAGE);
		}
//>>>>>> Show developer options...
		JOptionPane.showMessageDialog(null,
				"Developed By: Usman Khan (BC190201004)",
				"Developer Info", JOptionPane.INFORMATION_MESSAGE);
	}

	private static void manageProducts() {
		//Products Menu
		int menuSelection = 0;

/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/
		do {
			try {
//>>>>>>>>>> Show a menu for managing inventory...
				menuSelection = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								"Please enter\n" +
										"1 for 'Add Product'\n" +
										"2 for 'Remove Product'\n" +
										"3 for 'List Products'\n" +
										"4 for 'Goto Main Menu'\n",
								"Manage Products\n",
								JOptionPane.QUESTION_MESSAGE)
				);
			} catch (NumberFormatException exception) {
				int input;
				do {
					try {
						input = Integer.parseInt(
								JOptionPane.showInputDialog(null,
										"Input must be a positive number\n" +
												"Please Enter again:",
										"Invalid Input", JOptionPane.WARNING_MESSAGE)
						);
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Input is not a Number",
								"Invalid Input", JOptionPane.WARNING_MESSAGE);
						input = 0;
					}
				} while (!(input > 0));
			}
//>>>>>>>>> End of "NumberFormatException" handling...

//>>>>>>>> Calling method for selected option...
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
							"Invalid Input", JOptionPane.WARNING_MESSAGE);
			}

		} while (menuSelection != 4);
	}

	private static void listProducts() {
		StringBuilder productsList = new StringBuilder();
		int index = 0;
		try {
//>>>>>>>>> Create a list of products to show...
			for (; index < products.size(); index++) {
				productsList.append(index).append(" >> ").append(products.get(index).getId())
						.append("    ").append(products.get(index).getName()).append("    ")
						.append(products.get(index).getPrice()).append("    ").append(products.get(index)
						.getQuantity()).append("\n");
			}

/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/
			JOptionPane.showMessageDialog(null,
					productsList,
					"Product List",JOptionPane.INFORMATION_MESSAGE);
		} catch (IndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null,
					"No Item in the inventory",
					"Inventory", JOptionPane.WARNING_MESSAGE);
		}
//>>>>>>>>> End of "NumberFormatException" handling...
	}

	private static void removeProduct() {
		StringBuilder productsList = new StringBuilder();
		int input, i;
		try {
//>>>>>>>>>>> Listing products....
			for (i = 0; i < products.size(); i++) {
				productsList.append(i).append(" ").append(products.get(i).getId())
						.append(", ").append(products.get(i).getName()).append(", ")
						.append(products.get(i).getPrice()).append(", ")
						.append(products.get(i).getQuantity()).append("\n");
			}

/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/
			try {
				input = Integer.parseInt(
						JOptionPane.showInputDialog(null,
								"Enter Product number to remove:" +
										productsList
										+ i + " to go back",
								"Product to remove", JOptionPane.INFORMATION_MESSAGE)
				);
				if (input == i) return;
				if (input > products.size()) {
					JOptionPane.showMessageDialog(null,
							"Item is not in list",
							"No item found", JOptionPane.WARNING_MESSAGE);
				} else {
//Remove product from the inventory...
					products.remove(input);
				}

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"Invalid Product Number entered.");
			}
//>>>>>>>>> End of "NumberFormatException" handling...
		} catch (IndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null,
					"No Item in the inventory",
					"Inventory", JOptionPane.WARNING_MESSAGE);
		}

	}

	private static void addProduct() {
		int id, quantity;
		String name;
		float price;
//>>>>> Getting values from user...

/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/
		try {
//>>>>>>   Value for ID...	OR return if user selects cancel...
			id = Integer.parseInt(
					JOptionPane.showInputDialog(null,
							"Enter Product ID\n" +
									"Enter 0 to cancel adding product.",
							"Adding Product", JOptionPane.QUESTION_MESSAGE)
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
									"Invalid Product ID", JOptionPane.WARNING_MESSAGE)
					);
				} catch (NumberFormatException exception) {
					JOptionPane.showMessageDialog(null, "Input is not a Number",
							"Invalid Input", JOptionPane.WARNING_MESSAGE);
					id = -1;
				}
				if (id == 0) return;
			} while (!(id > 0));
		}
//>>>>>>>>> End of "NumberFormatException" handling...

//>>>>> Value for name... OR return on cancel...
		name = JOptionPane.showInputDialog(null,
				"Enter Product Name\n" +
						"Enter 0 to cancel adding product.",
				"Adding Product", JOptionPane.QUESTION_MESSAGE);
		if (name.equals("0")) return;

/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/

//>>>>>>> Value for price... OR return on cancel...
		try {
			price = Float.parseFloat(
					JOptionPane.showInputDialog(null,
							"Enter Product Price\n" +
									"Enter 0 to cancel adding product.",
							"Adding Product", JOptionPane.QUESTION_MESSAGE)
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
									"Invalid Product Price", JOptionPane.WARNING_MESSAGE)
					);
				} catch (NumberFormatException exception) {
					JOptionPane.showMessageDialog(null, "Input is not a Number",
							"Invalid Input", JOptionPane.WARNING_MESSAGE);
					price = -1;
				}
				if (price == 0) return;
			} while (!(price > 0));
		}
//>>>>>>>>> End of "NumberFormatException" handling...

/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/

//>>>>>> Value for quantity... OR return on cancel...
		try {
			quantity = Integer.parseInt(
					JOptionPane.showInputDialog(null,
							"Enter Product Quantity\n" +
									"Enter 0 to cancel adding product.",
							"Adding Product", JOptionPane.QUESTION_MESSAGE)
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
									"Invalid Product Quantity", JOptionPane.WARNING_MESSAGE)
					);
				} catch (NumberFormatException exception) {
					JOptionPane.showMessageDialog(null, "Input is not a Number",
							"Invalid Input", JOptionPane.WARNING_MESSAGE);
					quantity = -1;
				}
				if (quantity == 0) return;
			} while (!(quantity > 0));
		}
//>>>>>>>>> End of "NumberFormatException" handling...

//>>>>>>>>> Adding product....
		Product p = new Product(id, name, price, quantity);
		products.add(p);
		JOptionPane.showMessageDialog(null,
				"Product Added Successful.",
				"Congrats", JOptionPane.INFORMATION_MESSAGE);
	}

}

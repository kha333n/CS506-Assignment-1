package com.kha333n.cs506;
import javax.swing.*;

@SuppressWarnings("unused")
public class Product {
	private int id;
	private String name;
	private float price;
	private int quantity;

//>>> For storing product in the cart.
	private int inCart;

	//Default constructor
	Product(){
		id = 0;
		name = "null";
		price = 0;
		quantity = 0;
		inCart = 0;
	}

	//Parametrized constructor
	Product(int ID, String NAME, float PRICE, int QUANTITY){
		id = verifyId(ID);
		name = NAME;
		price = verifyPrice(PRICE);
		quantity = verifyQuantity(QUANTITY);
		inCart = 0;
	}

	Product(Product product){
		//Verifying that product is not null
		try {
			id = product.id;
			name = product.name;
			price = product.price;
			quantity = product.quantity;
			inCart = product.inCart;
		}
		catch (NullPointerException npe){
			JOptionPane.showMessageDialog(null,
					"Copying product failed.\n" +
							"Initializing with default values.",
					"Copy Constructor Exception", JOptionPane.WARNING_MESSAGE);
			id = 0;
			name = "null";
			price = 0;
			quantity = 0;
		}
	}

	public void setInCart(int INCART){
		inCart = INCART;
	}

	public void setId(int ID){
		id = verifyId(ID);
	}

	public void setName(String NAME){
		name = NAME;
	}

	public void setPrice(float PRICE){
		price = verifyPrice(PRICE);
	}

	public void setQuantity(int QUANTITY){
		quantity = verifyQuantity(QUANTITY);
	}

	public int getInCart(){
		return inCart;
	}

	public int getId(){
		return id;
	}

	public String getName(){
		return name;
	}

	public float getPrice(){
		return price;
	}

	public int getQuantity(){
		return quantity;
	}

	private int verifyId(int ID){
		//Verifying ID
		if (ID > 0) {
			return ID;
		}
		else {
			int input;
			do {
//>>>>>>>>>> Asking user for valid input...

/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/
				try {
					input = Integer.parseInt(
							JOptionPane.showInputDialog(null,
									"ID of product must be positive number\n" +
											"Please Enter ID again:",
									"Invalid Product ID", JOptionPane.WARNING_MESSAGE)
					);
				}
				catch (NumberFormatException e){
					JOptionPane.showMessageDialog(null,"Input is not a Number",
							"Invalid Input", JOptionPane.WARNING_MESSAGE);
					input = 0;
				}
			}while (!(input >0));
//>>>>>>>>> End of "NumberFormatException" handling...
			return input;
		}
	}

	private float verifyPrice(float PRICE){
		//verifying price
		if (PRICE > 0){
			return PRICE;
		}
		else {
			float input;
			do {
/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/

//>>>>>>>>>>>>> Asking user for valid price...
				try {
					input = Float.parseFloat(
							JOptionPane.showInputDialog(null,
									"Price of product must be positive number\n" +
											"Please Enter Price again:",
									"Invalid Product Price", JOptionPane.WARNING_MESSAGE)
					);
				}
				catch (NumberFormatException e){
					JOptionPane.showMessageDialog(null,"Input is not a Number",
							"Invalid Input", JOptionPane.WARNING_MESSAGE);
					input = 0;
				}
			}while (!(input >0));
//>>>>>>>>> End of "NumberFormatException" handling...
			return input;
		}
	}

	private int verifyQuantity(int QUANTITY){
		//verifying price
		if (QUANTITY >= 0){
			return QUANTITY;
		}
		else {
			int input;
			do {
/**************************************************************
 * ~~~~~~~~~~~~~~~NumberFormatException~~~~~~~~~~~~~~~~~~~~~~~~
 * ************************************************************/

//>>>>>>>>>>>>> Asking user for valid quantity...
				try {
					input = Integer.parseInt(
							JOptionPane.showInputDialog(null,
									"Quantity of product must be positive number\n" +
											"Please Enter Quantity again:",
									"Invalid Product Quantity", JOptionPane.WARNING_MESSAGE)
					);
				}
				catch (NumberFormatException e){
					JOptionPane.showMessageDialog(null,"Input is not a Number",
							"Invalid Input", JOptionPane.WARNING_MESSAGE);
					input = 0;
				}
			}while (!(input >0));
//>>>>>>>>> End of "NumberFormatException" handling...
			return input;
		}
	}

}

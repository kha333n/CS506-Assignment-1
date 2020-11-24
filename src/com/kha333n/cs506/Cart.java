package com.kha333n.cs506;

import java.util.ArrayList;

public class Cart {
	private ArrayList<Product> cartItems = new ArrayList<Product>();
	Cart(){
	}

	Cart(Product product,int quantity){
		cartItems.add(product);
	}

	Cart(Cart cart){
		cartItems.addAll(cart.cartItems);
	}

	public void addItem(Product product,int quantity){
		try {
			cartItems.add(product);
			product.setInCart(quantity);
		}
		catch (NullPointerException e){
			throw e;
		}
	}

	public void removeItem(int index){
		try {
			cartItems.remove(index);
		}
		catch (IndexOutOfBoundsException e){
			throw e;
		}
	}

	public int getProductIndex(Product product){
		try {
			return cartItems.indexOf(product);
		}
		catch (IndexOutOfBoundsException e){
			throw e;
		}
	}

	public void emptyCart(){
		cartItems.clear();
	}

	public ArrayList<Product> getItemsList(){
		 return cartItems;
	}

}

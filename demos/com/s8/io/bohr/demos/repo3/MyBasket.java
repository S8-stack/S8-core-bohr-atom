package com.s8.io.bohr.demos.repo3;

import java.util.ArrayList;
import java.util.List;

import com.s8.io.bohr.atom.S8Field;
import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.atom.S8ObjectType;

@S8ObjectType(name = "Basket")
public class MyBasket extends S8Object {

	
	public @S8Field(name = "picks") List<MyPick> picks;
	

	public MyBasket() {
		super();
		this.picks = new ArrayList<MyPick>();
	}
	
	
	
}

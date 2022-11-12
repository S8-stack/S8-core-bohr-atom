package com.s8.io.bohr.demos.neodymium.repo3;


import com.s8.io.bohr.atom.S8Ref;
import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8ObjectType;
import com.s8.io.bohr.lithium.object.LiObject2;


@S8ObjectType(name = "MyPick")
public class MyPick extends LiObject2 {

	@S8Field(name = "item-quantity")
	public int quantity;

	@S8Field(name = "item-ref")
	public S8Ref<MyProduct> product;

	public MyPick() {
		super();
	}

	/**
	
	public void order(S8AsyncFlow flow) { 
		//resolve(this, "item-ref", Behaviour.UNTIL_FLOW_END).
		//resolve(product, "stock", Behaviour.UNTIL_FLOW_END);

	
		launch(transaction -> transaction.then(new Step(vertex.shell().getAddress(), null)) {
			));
		});
	}
	 */

}

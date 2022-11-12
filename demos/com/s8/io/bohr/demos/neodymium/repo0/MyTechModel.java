package com.s8.io.bohr.demos.neodymium.repo0;

import java.util.List;

import com.s8.io.bohr.atom.S8Exception;
import com.s8.io.bohr.atom.S8Ref;
import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8ObjectType;
import com.s8.io.bohr.lithium.object.LiObject2;


/**
 * 
 * @author pc
 * 
 * 
 * 
 */
@S8ObjectType(name = "Dclib/MyTechModel")
public class MyTechModel extends LiObject2 {


	/**
	 * 
	 */
	private @S8Field(name = "myPurchaser") S8Ref<MyTechModel> myPurchaser;
	
	
	private @S8Field(name = "projects") List<MyProject> projects;

	/**
	 * 
	 */
	private @S8Field(name="param#2") int jdgjg;

	
	/**
	 * 
	 * @param vx
	 */
	public MyTechModel() {
		super();
	}

	
	/**
	 * 
	 * @param value
	 * @throws S8Exception
	 */
	public void expand2(double value) throws S8Exception {
		projects.add(new MyProject());
	}
	
	
	/**
	 * 
	 * @throws S8Exception
	 */
	public void expand() throws S8Exception {
		projects.add(new MyProject());
	}

	/**
	 * 
	 */
	public void getPhotos(String val){
		
	}
	

	
	/**
	 * 
	 */
	
	/*
	public void getPhotos2(S8AsyncFlow async, S8Vars scope) { async.
		_do(() -> { 
			//S8Request request = scope.getRequest();
			//f.setL2Var("a", f.getArg(0)); // 0 = arg0
			//scope.set("b", request.getFloat(1)); // 1 = arg1
			//scope.set("${target}", request.<MyTechModel>getReference(2));
			
			async.immediately().forRefDo(myPurchaser, target -> {
				target.getPhotos(scope.get("myParamName"));
			});
		});
	};
	*/
	
}

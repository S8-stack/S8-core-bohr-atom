package com.s8.io.bohr.demos.repo0;

import com.s8.io.bohr.atom.S8Ref;
import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8ObjectType;
import com.s8.io.bytes.alpha.Bool64;


@S8ObjectType(name = "car")
public class CarPayload extends MyProjectPayload {

	
	public final static long COUNTER_UPDATED = Bool64.BIT02;


	/**
	 * 
	 */

	public @S8Field(name="reactivity") double reactivity = 18;



	/**
	 * 
	 */
	public @S8Field(name="magnet-type") String magnetType;


	public @S8Field(name="ref") S8Ref<MyTechModel> ref;


	public CarPayload() {
		super();
	}



	/**
	 * read through
	 */
	
	/*
	public void readThrough(S8AsyncFlow async, S8Vars scope, Object[] args) { 
		async._do(() -> {
			System.out.println("Message 1:"+magnetType);
			scope.set("mag", magnetType);
			//flow.forSlot(ref, p->{});
			async.immediately().
			_do(() -> System.out.println("Message 2")).
			forRefDo(ref, techModel -> {
				// techModel.
			});
		}).
		_do(() -> {
			System.out.println("Message 3:"+reactivity+":="+scope.get("mag"));
			//s.view.asyncControl(null, );
		});
	}
	*/






	/**
	 * 
	public class MyView extends QtzS8View<CarPayload> {

		@Override
		public String declare() {
			return "SimpleBox";
		}

		public MyView() {
			super();

			// mount view
			mount();
		}

		@Override
		public CarPayload _this() {
			return CarPayload.this;
		}


		@Override
		public void render(Bool64 event) throws IOException {
			outflow.pushControl("handle", f -> f.then(
					then(_this -> {

					});
		});


			outflow.pushSyncControl("handle", s -> {
				double requestValue = (double) s.arg(0);
				_this().counter+=requestValue;
				_this().signal(COUNTER_UPDATED);
				s.shell().getRevision();
			});
	}

	 */

}

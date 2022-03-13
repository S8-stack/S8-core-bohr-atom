package com.s8.io.bohr.neodymium.objects;

import java.io.IOException;
import java.util.List;

import com.s8.io.bohr.atom.BOHR_Keywords;
import com.s8.io.bohr.atom.S8Index;
import com.s8.io.bohr.neodymium.branches.NdBranch;
import com.s8.io.bohr.neodymium.branches.NdOutbound;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.NdFieldDelta;
import com.s8.io.bohr.neodymium.type.BuildScope;
import com.s8.io.bytes.alpha.ByteOutflow;
import com.s8.io.bytes.alpha.MemoryFootprint;

public class ExposeNdObjectDelta extends NdObjectDelta {
	
	public List<NdFieldDelta> deltas;
	

	protected boolean isExposeUnpublished;
	
	protected int slot = -1;


	public ExposeNdObjectDelta(S8Index index, int slot) {
		super(index);
		this.slot = slot;
	}

	@Override
	public void serialize(NdOutbound outbound, ByteOutflow outflow) throws IOException {

		outflow.putUInt8(BOHR_Keywords.EXPOSE_NODE);

		/* define index */
		S8Index.write(index, outflow);

		/* define slot */
		outflow.putUInt8(slot);
	}



	@Override
	public void consume(NdBranch branch, BuildScope scope) throws NdIOException {
		
		/* retrieve vertex */
		NdVertex vertex = branch.vertices.get(index);
		
		if(isExposeUnpublished) {
			vertex.port = slot;
			branch.expose(slot, vertex);
		}
	}
	

	@Override
	public void computeFootprint(MemoryFootprint weight) {

		weight.reportInstance();

		// fields
		if(deltas!=null) {
			for(NdFieldDelta delta : deltas) {
				delta.computeFootprint(weight);
			}
		}
	}

}


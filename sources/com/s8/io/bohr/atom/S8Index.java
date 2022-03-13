package com.s8.io.bohr.atom;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;

/**
 * 
 * @author pierreconvert
 *
 */
public final class S8Index {
	
	
	/**
	 * 
	 */
	public final static S8Index NULL = new S8Index("(NULL)".getBytes(StandardCharsets.UTF_8), -1);

	
	/**
	 * the branch this object originates from
	 */
	public final byte[] branchId;
	
	
	/**
	 * object id
	 */
	public final long objectId;

	
	private final int hash;
	
	/**
	 * 
	 * @param branchId
	 * @param objectId
	 */
	public S8Index(byte[] branchId, long objectId) {
		super();
		this.branchId = branchId;
		this.objectId = objectId;
		
		// compute hascode
		hash = (branchId[1] & 0xff) + 31 * ((branchId[0] & 0xff) + 31 * ((int) objectId));
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getBranchId() {
		return new String(branchId, StandardCharsets.UTF_8);
	}
	
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		return hash;
	}
	
	
	/**
	 * 
	 */
	@Override
	public boolean equals(Object right) {
		if(right instanceof S8Index) {
			S8Index index = (S8Index) right;
			if(this.objectId == index.objectId) {
				byte[] rightBranchId = index.branchId;
				int lLength = branchId.length, rLength = rightBranchId.length;
				if(lLength == rLength) {
					for(int i=0; i<lLength; i++) {
						if(branchId[i] != rightBranchId[i]) { return false; }
					}
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
			
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "S8Idx(b="+(new String(branchId, StandardCharsets.UTF_8))+":o=0x"+Long.toHexString(objectId)+")";
	}
	
	
	public static S8Index read(ByteInflow inflow) throws IOException {
		long branchIdLength = inflow.getUInt7x();
		if(branchIdLength >= 0) {
			byte[] branchId = inflow.getByteArray((int) branchIdLength);
			long objectId = inflow.getUInt7x();
			return new S8Index(branchId, objectId);
		}
		else {
			return null;
		}
	}
	




	
	public static void write(S8Index index, ByteOutflow outflow) throws IOException {
		if(index != null) {
			
			// branch Id
			outflow.putUInt7x(index.branchId.length);
			outflow.putByteArray(index.branchId);
			
			outflow.putUInt7x(index.objectId);	
		}
		else {
			outflow.putUInt7x(-1);
		}
	}


	
}

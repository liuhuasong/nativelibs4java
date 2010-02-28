package com.bridj;

public class StructObject extends NativeObject {
	StructIO io;
	Object[] refreshableFields;

    public StructObject() {
		super();
	}
    public StructObject(int constructorId, Object[] args) {
    	super(constructorId, args);
    }
    public StructObject(Pointer p, BridJRuntime runtime) {
    	super(p, runtime);
    }
}

package com.nativelibs4java.ffmpeg.avfilter;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
/**
 * <i>native declaration : libavfilter/avfilter.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */
@Library("avfilter") 
public class AVFilterFormats extends StructObject {
	public AVFilterFormats() {
		super();
	}
	public AVFilterFormats(Pointer pointer) {
		super(pointer);
	}
	/// < number of formats
	@Field(0) 
	public int format_count() {
		return this.io.getIntField(this, 0);
	}
	/// < number of formats
	@Field(0) 
	public AVFilterFormats format_count(int format_count) {
		this.io.setIntField(this, 0, format_count);
		return this;
	}
	public final int format_count_$eq(int format_count) {
		format_count(format_count);
		return format_count;
	}
	/**
	 * < list of media formats<br>
	 * C type : int*
	 */
	@Field(1) 
	public Pointer<Integer > formats() {
		return this.io.getPointerField(this, 1);
	}
	/**
	 * < list of media formats<br>
	 * C type : int*
	 */
	@Field(1) 
	public AVFilterFormats formats(Pointer<Integer > formats) {
		this.io.setPointerField(this, 1, formats);
		return this;
	}
	/// C type : int*
	public final Pointer<Integer > formats_$eq(Pointer<Integer > formats) {
		formats(formats);
		return formats;
	}
	/// < number of references to this list
	@Field(2) 
	public int refcount() {
		return this.io.getIntField(this, 2);
	}
	/// < number of references to this list
	@Field(2) 
	public AVFilterFormats refcount(int refcount) {
		this.io.setIntField(this, 2, refcount);
		return this;
	}
	public final int refcount_$eq(int refcount) {
		refcount(refcount);
		return refcount;
	}
	/**
	 * < references to this list<br>
	 * C type : AVFilterFormats***
	 */
	@Field(3) 
	public Pointer<Pointer<Pointer<AVFilterFormats > > > refs() {
		return this.io.getPointerField(this, 3);
	}
	/**
	 * < references to this list<br>
	 * C type : AVFilterFormats***
	 */
	@Field(3) 
	public AVFilterFormats refs(Pointer<Pointer<Pointer<AVFilterFormats > > > refs) {
		this.io.setPointerField(this, 3, refs);
		return this;
	}
	/// C type : AVFilterFormats***
	public final Pointer<Pointer<Pointer<AVFilterFormats > > > refs_$eq(Pointer<Pointer<Pointer<AVFilterFormats > > > refs) {
		refs(refs);
		return refs;
	}
}

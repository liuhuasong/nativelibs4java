package com.nativelibs4java.ffmpeg.avfilter;
import com.nativelibs4java.ffmpeg.avutil.AVRational;
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
public class AVFilterBufferRefVideoProps extends StructObject {
	public AVFilterBufferRefVideoProps() {
		super();
	}
	public AVFilterBufferRefVideoProps(Pointer pointer) {
		super(pointer);
	}
	/// < image width
	@Field(0) 
	public int w() {
		return this.io.getIntField(this, 0);
	}
	/// < image width
	@Field(0) 
	public AVFilterBufferRefVideoProps w(int w) {
		this.io.setIntField(this, 0, w);
		return this;
	}
	public final int w_$eq(int w) {
		w(w);
		return w;
	}
	/// < image height
	@Field(1) 
	public int h() {
		return this.io.getIntField(this, 1);
	}
	/// < image height
	@Field(1) 
	public AVFilterBufferRefVideoProps h(int h) {
		this.io.setIntField(this, 1, h);
		return this;
	}
	public final int h_$eq(int h) {
		h(h);
		return h;
	}
	/**
	 * < pixel aspect ratio<br>
	 * C type : AVRational
	 */
	@Field(2) 
	public AVRational pixel_aspect() {
		return this.io.getNativeObjectField(this, 2);
	}
	/// < is frame interlaced
	@Field(3) 
	public int interlaced() {
		return this.io.getIntField(this, 3);
	}
	/// < is frame interlaced
	@Field(3) 
	public AVFilterBufferRefVideoProps interlaced(int interlaced) {
		this.io.setIntField(this, 3, interlaced);
		return this;
	}
	public final int interlaced_$eq(int interlaced) {
		interlaced(interlaced);
		return interlaced;
	}
	/// < field order
	@Field(4) 
	public int top_field_first() {
		return this.io.getIntField(this, 4);
	}
	/// < field order
	@Field(4) 
	public AVFilterBufferRefVideoProps top_field_first(int top_field_first) {
		this.io.setIntField(this, 4, top_field_first);
		return this;
	}
	public final int top_field_first_$eq(int top_field_first) {
		top_field_first(top_field_first);
		return top_field_first;
	}
}
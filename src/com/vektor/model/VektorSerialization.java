package com.vektor.model;

import java.util.ArrayList;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannedString;

import com.google.gson.annotations.SerializedName;

public class VektorSerialization {

	public static class sourceCode {
		@SerializedName("status")
		private String status;
		@SerializedName("file")
		private String file;
		@SerializedName("from")
		private int from;
		@SerializedName("to")
		private int to;
		@SerializedName("code")
		private ArrayList<codeLine> code = new ArrayList<codeLine>();

		public String getStatus() {
			return status;
		}

		public String getFile() {
			return file;
		}

		public int getFrom() {
			return from;
		}

		public int getTo() {
			return to;
		}

		public ArrayList<codeLine> getCode() {
			return code;
		}
	}

	public static class codeLine {
		@SerializedName("depth")
		private int depth;
		@SerializedName("code")
		private String code;

		public int getDepth() {
			return depth;
		}

		public String getCode() {
			return code;
		}
	}

	public static class classDocument {
		@SerializedName("status")
		private String status;
		@SerializedName("lines")
		private int lines;
		@SerializedName("name")
		private String name;
		@SerializedName("types")
		private ArrayList<classStructure> classes = new ArrayList<classStructure>();
		@SerializedName("nestlevel")
		private int nestlevel;
		
		public String getStatus() {
			return status;
		}
		public int getLines() {
			return lines;
		}
		public String getName() {
			return name;
		}
		public ArrayList<classStructure> getClasses() {
			return classes;
		}
		public int getNestlevel(){
			return nestlevel;
		}
	}

	public static class classStructure {
		@SerializedName("name")
		private String name;
		@SerializedName("interface")
		private boolean isInterface;
		@SerializedName("access")
		private String access;
		@SerializedName("static")
		private boolean isStatic;
		@SerializedName("final")
		private boolean isFinal;
		@SerializedName("abstract")
		private boolean isAbstract;
		@SerializedName("types")
		private ArrayList<classStructure> types = new ArrayList<classStructure>();
		@SerializedName("fields")
		private ArrayList<classField> fields = new ArrayList<classField>();
		@SerializedName("methods")
		private ArrayList<classMethod> methods = new ArrayList<classMethod>();
		public String getName() {
			return name;
		}
		public boolean isInterface() {
			return isInterface;
		}
		public String getAccess() {
			return access;
		}
		public boolean isStatic() {
			return isStatic;
		}
		public boolean isFinal() {
			return isFinal;
		}
		public boolean isAbstract() {
			return isAbstract;
		}
		public ArrayList<classStructure> getTypes() {
			return types;
		}
		public ArrayList<classField> getFields() {
			return fields;
		}
		public ArrayList<classMethod> getMethods() {
			return methods;
		}
	}

	public static class classField {
		@SerializedName("name")
		private String name;
		@SerializedName("access")
		private String access;
		@SerializedName("final")
		private boolean isFinal;
		@SerializedName("static")
		private boolean isStatic;
		@SerializedName("transient")
		private boolean isTransient;
		@SerializedName("volatile")
		private boolean isVolatile;
		@SerializedName("lineStart")
		private int lineStart;
		@SerializedName("lineEnd")
		private int lineEnd;
		public String getName() {
			return name;
		}
		public String getAccess() {
			return access;
		}
		public boolean isFinal() {
			return isFinal;
		}
		public boolean isStatic() {
			return isStatic;
		}
		public boolean isTransient() {
			return isTransient;
		}
		public boolean isVolatile() {
			return isVolatile;
		}
		public int getLineStart() {
			return lineStart;
		}
		public int getLineEnd() {
			return lineEnd;
		}

	}

	public static class classMethod {
		@SerializedName("name")
		private String name;
		@SerializedName("access")
		private String access;
		@SerializedName("abstract")
		private boolean isAbstract;
		@SerializedName("final")
		private boolean isFinal;
		@SerializedName("native")
		private boolean isNative;
		@SerializedName("static")
		private boolean isStatic;
		@SerializedName("synchronized")
		private boolean isSynchronized;
		@SerializedName("lineStart")
		private int lineStart;
		@SerializedName("lineEnd")
		private int lineEnd;
		public String getName() {
			return name;
		}
		public String getAccess() {
			return access;
		}
		public boolean isAbstract() {
			return isAbstract;
		}
		public boolean isFinal() {
			return isFinal;
		}
		public boolean isNative() {
			return isNative;
		}
		public boolean isStatic() {
			return isStatic;
		}
		public boolean isSynchronized() {
			return isSynchronized;
		}
		public int getLineStart() {
			return lineStart;
		}
		public int getLineEnd() {
			return lineEnd;
		}

	}

	public static class parsedLine {
		private int depth;
		private CharSequence code;

		public parsedLine(int depth, CharSequence code) {
			this.depth = depth;
			this.code = code;
		}

		public int getDepth() {
			return depth;
		}

		public CharSequence getCode() {
			return code;
		}
	}
}

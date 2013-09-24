package com.vektor.sourfer.ui;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.vektor.model.VektorSerialization.codeLine;
import com.vektor.model.VektorSerialization.parsedLine;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;

public class CodeRenderer {

	public static ArrayList<parsedLine> parse(ArrayList<codeLine> code) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp;
		try {
			sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			XMLHandler handler = new XMLHandler();
			xr.setContentHandler((ContentHandler) handler);
			ArrayList<parsedLine> output = new ArrayList<parsedLine>();
			for (codeLine line : code) {
				xr.parse(new InputSource(new StringReader("<line>"
						+ line.getCode() + "</line>")));
				output.addAll(handler.getParsedData());
			}
			return output;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ArrayList<parsedLine>();
	}

	static class XMLHandler extends DefaultHandler {
		private boolean in_line = false;
		private boolean in_com = false;
		private boolean in_kwd = false;
		private boolean in_lit = false;
		private boolean in_typ = false;
		private boolean in_pln = false;
		private boolean in_pun = false;
		private boolean in_str = false;
		// private ArrayList<parsedLine> output = new ArrayList<parsedLine>();
		private ArrayList<SpannableString> theline = new ArrayList<SpannableString>();
		private ArrayList<parsedLine> output = new ArrayList<parsedLine>();

		@Override
		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {
			// atts.getValue("name");
			// Log.i("Tag IN",localName);
			if (localName.equalsIgnoreCase("LINE")) {
				in_line = true;
			} else if (localName.equalsIgnoreCase("COM")) {
				in_com = true;
			} else if (localName.equalsIgnoreCase("KWD")) {
				in_kwd = true;
			} else if (localName.equalsIgnoreCase("LIT")) {
				in_lit = true;
			} else if (localName.equalsIgnoreCase("TYP")) {
				in_typ = true;
			} else if (localName.equalsIgnoreCase("PLN")) {
				in_pln = true;
			} else if (localName.equalsIgnoreCase("PUN")) {
				in_pun = true;
			} else if (localName.equalsIgnoreCase("STR")) {
				in_str = true;
			}

		}

		@Override
		public void endElement(String namespaceURI, String localName,
				String qName) throws SAXException {
			// Log.i("Tag OUT",localName);
			if (localName.equalsIgnoreCase("LINE")) {
				in_line = false;
				// CharSequence[] cs = theline.toArray(new CharSequence[theline
				// .size()]);
				// output = new parsedLine(0, TextUtils.concat(cs));
				// theline.clear();
			} else if (localName.equalsIgnoreCase("COM")) {
				in_com = false;
			} else if (localName.equalsIgnoreCase("KWD")) {
				in_kwd = false;
			} else if (localName.equalsIgnoreCase("LIT")) {
				in_lit = false;
			} else if (localName.equalsIgnoreCase("TYP")) {
				in_typ = false;
			} else if (localName.equalsIgnoreCase("PLN")) {
				in_pln = false;
			} else if (localName.equalsIgnoreCase("PUN")) {
				in_pun = false;
			} else if (localName.equalsIgnoreCase("STR")) {
				in_str = false;
			}
		}

		@Override
		public void characters(char ch[], int start, int length) {
			String textBetween = new String(ch, start, length);

			SpannableString text = new SpannableString(textBetween);
			if (in_com) {
				text.setSpan(
						new ForegroundColorSpan(Color
								.parseColor(Theme.comColor)), start, length,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				text.setSpan(new StyleSpan(Typeface.ITALIC), start, length,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				if (textBetween.contains(System.getProperty("line.separator"))) {
					CharSequence[] cs = theline
							.toArray(new CharSequence[theline.size()]);
					output.add(new parsedLine(0, TextUtils.concat(cs)));

					theline.clear();
				} else
					theline.add(text);
			} else if (in_kwd) {
				text.setSpan(
						new ForegroundColorSpan(Color
								.parseColor(Theme.kwdColor)), start, length,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				text.setSpan(new StyleSpan(Typeface.BOLD), start, length,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				if (textBetween.contains(System.getProperty("line.separator"))) {
					CharSequence[] cs = theline
							.toArray(new CharSequence[theline.size()]);
					output.add(new parsedLine(0, TextUtils.concat(cs)));

					theline.clear();
				} else
					theline.add(text);
			} else if (in_lit) {
				text.setSpan(
						new ForegroundColorSpan(Color
								.parseColor(Theme.litColor)), start, length,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				if (textBetween.contains(System.getProperty("line.separator"))) {
					CharSequence[] cs = theline
							.toArray(new CharSequence[theline.size()]);
					output.add(new parsedLine(0, TextUtils.concat(cs)));

					theline.clear();
				} else
					theline.add(text);
			} else if (in_typ) {
				text.setSpan(
						new ForegroundColorSpan(Color
								.parseColor(Theme.typColor)), start, length,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				text.setSpan(new StyleSpan(Typeface.BOLD), start, length,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				if (textBetween.contains(System.getProperty("line.separator"))) {
					CharSequence[] cs = theline
							.toArray(new CharSequence[theline.size()]);
					output.add(new parsedLine(0, TextUtils.concat(cs)));

					theline.clear();
				} else
					theline.add(text);
			} else if (in_str) {
				text.setSpan(
						new ForegroundColorSpan(Color
								.parseColor(Theme.strColor)), start, length,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				if (textBetween.contains(System.getProperty("line.separator"))) {
					CharSequence[] cs = theline
							.toArray(new CharSequence[theline.size()]);
					output.add(new parsedLine(0, TextUtils.concat(cs)));

					theline.clear();
				} else
					theline.add(text);
			} else if (in_pun) {
				text.setSpan(
						new ForegroundColorSpan(Color
								.parseColor(Theme.punColor)), start, length,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				if (textBetween.contains(System.getProperty("line.separator"))) {
					CharSequence[] cs = theline
							.toArray(new CharSequence[theline.size()]);
					output.add(new parsedLine(0, TextUtils.concat(cs)));

					theline.clear();
				} else
					theline.add(text);
			} else if (in_pln) {
				text.setSpan(
						new ForegroundColorSpan(Color
								.parseColor(Theme.plnColor)), start, length,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				if (textBetween.contains(System.getProperty("line.separator"))) {
					CharSequence[] cs = theline
							.toArray(new CharSequence[theline.size()]);
					output.add(new parsedLine(0, TextUtils.concat(cs)));

					theline.clear();
				} else
					theline.add(text);
			}
		}

		@Override
		public void startDocument() throws SAXException {
			// Do some startup if needed

		}

		public ArrayList<parsedLine> getParsedData() {
			return this.output;
		}
	}

}

package com.millenium.speaker;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

public class Mapper {

	private Mapper() {  }

	public static String listToString(Collection<String> list) {
		return StringUtils.replaceEach(list.toString(), new String[] { "[", "]", "," }, new String[] { "", "", "" });
	}

	public static String listToString(Collection<String> list, int first, int last) {
		if(list.size() >= last) {
			return listToString(((ArrayList<String>)list).subList(first, last));
		}
		return listToString(list);
	}

}

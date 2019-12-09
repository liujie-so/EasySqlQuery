package com.iuie.basic.util;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import com.iuie.basic.util.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.iuie.basic.util.DateUtils;
/**
 * JSON工具类
 * @author liu.jie
 * @since 2019年4月14日
 */
public class JsonUtils {
	
	private final static Gson gson;
	static {
		gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

			@Override
			public Date deserialize(JsonElement arg0, Type arg1,
					JsonDeserializationContext arg2) throws JsonParseException {
				String value = arg0.getAsJsonPrimitive().getAsString();
				if(StringUtils.isEmpty(value)) {
					return null;
				}
				try {
					value = value.replace("T", " ");
					return DateUtils.parseFullDate(value, new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd"});
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return null;
			}
			
		}).registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
			@Override
			public JsonElement serialize(Date src, Type typeOfSrc,
					JsonSerializationContext context) {
				if(src == null) {
					return new JsonPrimitive(StringUtils.EMPTY);
				}
				try {
					return new JsonPrimitive(DateUtil.formatDateToStringForNeed(src, "yyyy-MM-dd HH:mm:ss"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return new JsonPrimitive(src.toString());
			}
		}).serializeNulls().create();
	}

	public static void returnJson(Object object) {
		returnJson(JSON.toJSONString(object));
	}
	
	public static void returnJson(String json) {
		HttpServletResponse response = ServletActionContext.getResponse();
		PrintWriter writer = null;
		try {
			response.setCharacterEncoding("UTF-8");
			writer = response.getWriter();
			writer.write(json);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}
	
	public static Gson getGson() {
		return gson;
	}
}

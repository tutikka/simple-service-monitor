package com.tt.ssm.service;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tt.ssm.service.impl.ICMPService;
import com.tt.ssm.service.impl.JDBCService;
import com.tt.ssm.service.impl.TCPService;
import com.tt.ssm.service.impl.URLService;

public class ServiceDeserializer implements JsonDeserializer<Service> {
	
    @Override
    public Service deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String t = jsonObject.get("type").getAsString();
        if (Service.TYPE_URL.equals(t)) {
	        Service service = jsonDeserializationContext.deserialize(jsonElement, URLService.class);
	        return (service);
        }
        if (Service.TYPE_TCP.equals(t)) {
	        Service service = jsonDeserializationContext.deserialize(jsonElement, TCPService.class);
	        return (service);
        }
        if (Service.TYPE_JDBC.equals(t)) {
	        Service service = jsonDeserializationContext.deserialize(jsonElement, JDBCService.class);
	        return (service);
        }
        if (Service.TYPE_ICMP.equals(t)) {
	        Service service = jsonDeserializationContext.deserialize(jsonElement, ICMPService.class);
	        return (service);
        }
        return (null);
    }
	
}
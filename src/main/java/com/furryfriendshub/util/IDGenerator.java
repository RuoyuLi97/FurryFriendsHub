package com.furryfriendshub.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class IDGenerator {

    public enum EntityType {
        USER("US"), 
        NOTIFICATION("NO"), 
        ADOPTION_LISTING("AL"), 
        FORUM_POST("FP");

        private final String prefix;

        EntityType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    public static String generateId(EntityType entityType) {
        String prefix = entityType.getPrefix(); // Logical grouping by type
        
        // Get current date in "yyyyMMdd" format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());

        // Generate a UUID and extract the first 8 characters
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);

        // Return the concatenated ID
        return prefix + "_" + currentDate + "_" + uuid;
    }
}
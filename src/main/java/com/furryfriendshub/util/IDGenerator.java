package com.furryfriendshub.util;

import org.bson.types.ObjectId;

public class IDGenerator {

    public enum EntityType {
        ADMIN("AD"), 
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
        String objectId = new ObjectId().toHexString(); // Compact and MongoDB-friendly
        return prefix + objectId;
    }
}
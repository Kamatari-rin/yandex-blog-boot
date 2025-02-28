package org.example.enums;


public enum LikeTargetType {
    POST(1),
    COMMENT(2);

    private final int id;

    LikeTargetType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static LikeTargetType fromId(int id) {
        for (LikeTargetType type : LikeTargetType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown LikeTargetType id: " + id);
    }
}

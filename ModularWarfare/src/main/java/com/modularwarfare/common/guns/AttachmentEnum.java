package com.modularwarfare.common.guns;

public enum AttachmentEnum {
    Sight("sight"),
    Slide("slide"),
    Grip("grip"),
    Flashlight("flashlight"),
    Charm("charm"),
    Skin("skin"),
    Barrel("barrel");

    public String typeName;

    AttachmentEnum(String typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return this.typeName;
    }

    public static AttachmentEnum getAttachment(String typeName) {
        for (AttachmentEnum attachment : values()) {
            if (attachment.typeName.equalsIgnoreCase(typeName)) {
                return attachment;
            }
        }
        return Sight;
    }
}
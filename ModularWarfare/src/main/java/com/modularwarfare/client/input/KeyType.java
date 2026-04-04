package com.modularwarfare.client.input;

public enum KeyType {
    GunReload("Reload Gun", 82),
    ClientReload("Reload Client", 67),
    DebugMode("Debug Mode", 68),
    FireMode("Fire Mode", 47),
    GunUnload("Unload Key", 22),
    AddAttachment("Attachment Mode", 50),
    Flashlight("Flashlight", 35),
    Backpack("Backpack Inventory", 48),
    Left("Left (Attach mode)", 263),
    Right("Right (Attach mode)", 262),
    Up("Up (Attach mode)", 265),
    Down("Down (Attach mode)", 264);

    public String displayName;
    public int keyCode;

    KeyType(String displayName, int keyCode) {
        this.displayName = displayName;
        this.keyCode = keyCode;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
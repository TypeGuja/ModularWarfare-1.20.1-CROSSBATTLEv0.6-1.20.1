package com.modularwarfare.client.model.objects;

import com.modularwarfare.loader.api.ObjModelRenderer;
import org.joml.Vector3f;

public class BreakActionData {
    public ObjModelRenderer modelGroup;
    public Vector3f breakPoint;
    public float angle;
    public boolean scopePart;

    public BreakActionData(ObjModelRenderer modelGroup, Vector3f breakPoint, float angle, boolean scopePart) {
        this.modelGroup = modelGroup;
        this.breakPoint = breakPoint;
        this.angle = angle;
        this.scopePart = scopePart;
    }
}
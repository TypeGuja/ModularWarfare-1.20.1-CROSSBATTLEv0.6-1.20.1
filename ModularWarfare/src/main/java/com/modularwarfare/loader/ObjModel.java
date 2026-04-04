package com.modularwarfare.loader;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.loader.api.AbstractObjModel;
import com.modularwarfare.loader.api.ObjModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.ConcurrentModificationException;

public class ObjModel extends AbstractObjModel {
    private List<ObjModelRenderer> partsList;
    private List<ObjModelRenderer> duplications = new ArrayList<>();

    public ObjModel(List<ObjModelRenderer> parts) {
        this.partsList = parts;
        for (ObjModelRenderer part : parts) {
            super.addPart(part.getName(), part);
        }
    }

    public ObjModel() {
        this.partsList = new ArrayList<>();
    }

    public List<ObjModelRenderer> getPartsList() {
        return this.partsList;
    }

    public void setParts(List<ObjModelRenderer> renderers) {
        this.partsList = renderers;
        for (ObjModelRenderer part : renderers) {
            super.addPart(part.getName(), part);
        }
    }

    public void addPart(ObjModelRenderer part) {
        this.partsList.add(part);
        super.addPart(part.getName(), part);
    }

    @Override
    public ObjModelRenderer getPart(String name) {
        for (ObjModelRenderer part : partsList) {
            if (name.equalsIgnoreCase(part.getName())) {
                return part;
            }
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public void renderAll(float scale) {
        checkForNoDuplications();
        for (ObjModelRenderer part : partsList) {
            part.render(null, null, scale);
        }
    }

    public void clearDuplications() throws ConcurrentModificationException {
        try {
            for (ObjModelRenderer renderer : duplications) {
                partsList.remove(renderer);
                super.parts.remove(renderer.getName());
            }
        } catch (ConcurrentModificationException e) {
            throw new ConcurrentModificationException("You must clear duplications ONLY AFTER passing ObjModelRaw#parts!!!\n" + e.getMessage());
        }
        duplications.clear();
    }

    public boolean hasDuplications() {
        return !duplications.isEmpty();
    }

    private String[] formDuplicationList() {
        String[] list = new String[duplications.size()];
        for (int i = 0; i < duplications.size(); i++) {
            list[i] = duplications.get(i).getName();
        }
        return list;
    }

    @OnlyIn(Dist.CLIENT)
    public void renderOnly(float scale, String... groupNames) {
        checkForNoDuplications();
        for (ObjModelRenderer part : partsList) {
            for (String groupName : groupNames) {
                if (groupName.equalsIgnoreCase(part.getName())) {
                    part.render(null, null, scale);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void renderOnly(float scale, ObjModelRenderer... partsIn) {
        checkForNoDuplications();
        for (ObjModelRenderer part : partsList) {
            for (ObjModelRenderer partIn : partsIn) {
                if (part.equals(partIn)) {
                    part.render(null, null, scale);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void renderPart(float scale, String partName) {
        checkForNoDuplications();
        for (ObjModelRenderer part : partsList) {
            if (partName.equalsIgnoreCase(part.getName())) {
                part.render(null, null, scale);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void renderPart(float scale, ObjModelRenderer partIn) {
        checkForNoDuplications();
        for (ObjModelRenderer part : partsList) {
            if (part.equals(partIn)) {
                part.render(null, null, scale);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void renderAllExcept(float scale, ObjModelRenderer... excludedPartsIn) {
        checkForNoDuplications();
        for (ObjModelRenderer part : partsList) {
            boolean skipPart = isExcepted(part, excludedPartsIn);
            if (!skipPart) {
                part.render(null, null, scale);
            }
        }
    }

    private boolean isExcepted(ObjModelRenderer part, ObjModelRenderer[] excludedList) {
        for (ObjModelRenderer excludedPart : excludedList) {
            if (part.equals(excludedPart)) {
                return true;
            }
        }
        return false;
    }

    protected void addDuplication(ObjModelRenderer renderer) {
        duplications.add(renderer);
    }

    private void checkForNoDuplications() {
        if (hasDuplications()) {
            ModularWarfare.LOGGER.error("=============================================================");
            ModularWarfare.LOGGER.error("Duplications were found! You must call method ObjModelRaw#clearDuplications() after adding children to renders.");
            ModularWarfare.LOGGER.error("Duplications:");
            for (String str : formDuplicationList()) {
                ModularWarfare.LOGGER.error(str);
            }
            ModularWarfare.LOGGER.error("=============================================================");
        }
    }
}
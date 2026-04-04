package com.modularwarfare.loader;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.CommonProxy;
import com.modularwarfare.common.type.BaseType;
import com.modularwarfare.loader.api.ObjModelRenderer;
import com.modularwarfare.loader.part.Face;
import com.modularwarfare.loader.part.ModelObject;
import com.modularwarfare.loader.part.TextureCoordinate;
import com.modularwarfare.loader.part.Vertex;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.regex.Pattern;

public class ObjModelBuilder {
    private static final Pattern VERTEX_PATTERN = Pattern.compile("v( (-?\\d+(\\.\\d+)?){3,4})");
    private static final Pattern VERTEX_NORMAL_PATTERN = Pattern.compile("vn( (-?\\d+(\\.\\d+)?){3,4})");
    private static final Pattern TEXTURE_COORDINATE_PATTERN = Pattern.compile("vt( (-?\\d+(\\.\\d+)?){2,3})");
    private static final Pattern FACE_V_VT_VN_PATTERN = Pattern.compile("f( \\d+/\\d+/\\d+){3,4}");
    private static final Pattern FACE_V_VT_PATTERN = Pattern.compile("f( \\d+/\\d+){3,4}");
    private static final Pattern FACE_V_VN_PATTERN = Pattern.compile("f( \\d+//\\d+){3,4}");
    private static final Pattern FACE_V_PATTERN = Pattern.compile("f( \\d+){3,4}");
    private static final Pattern GROUP_OBJECT_PATTERN = Pattern.compile("([go]( \\w+))");

    private ArrayList<Vertex> vertices = new ArrayList<>();
    private ArrayList<Vertex> vertexNormals = new ArrayList<>();
    private ArrayList<TextureCoordinate> textureCoordinates = new ArrayList<>();
    private ModelObject currentModelObject;
    private String fileLocation;
    private ResourceLocation resourceLocation;
    private ArrayList<ObjModelRenderer> renderers = new ArrayList<>();

    public ObjModelBuilder(String rl) {
        this.fileLocation = rl;
    }

    public ObjModelBuilder(ResourceLocation rl) {
        this.resourceLocation = rl;
    }

    private static boolean isValidVertexLine(String line) {
        return VERTEX_PATTERN.matcher(line).matches();
    }

    private static boolean isValidVertexNormalLine(String line) {
        return VERTEX_NORMAL_PATTERN.matcher(line).matches();
    }

    private static boolean isValidTextureCoordinateLine(String line) {
        return TEXTURE_COORDINATE_PATTERN.matcher(line).matches();
    }

    private static boolean isValidFace_V_VT_VN_Line(String line) {
        return FACE_V_VT_VN_PATTERN.matcher(line).matches();
    }

    private static boolean isValidFace_V_VT_Line(String line) {
        return FACE_V_VT_PATTERN.matcher(line).matches();
    }

    private static boolean isValidFace_V_VN_Line(String line) {
        return FACE_V_VN_PATTERN.matcher(line).matches();
    }

    private static boolean isValidFace_V_Line(String line) {
        return FACE_V_PATTERN.matcher(line).matches();
    }

    private static boolean isValidFaceLine(String line) {
        return isValidFace_V_VT_VN_Line(line) || isValidFace_V_VT_Line(line) ||
                isValidFace_V_VN_Line(line) || isValidFace_V_Line(line);
    }

    private static boolean isValidGroupObjectLine(String line) {
        return GROUP_OBJECT_PATTERN.matcher(line).matches();
    }

    public ObjModel loadModelFromZIP(BaseType baseType) throws ModelFormatException {
        int lineCount = 0;
        ObjModel model = new ObjModel();
        int found = 0;

        File[] files = ModularWarfare.MOD_DIR.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.getName().contains("cache")) continue;

                if (CommonProxy.zipJar.matcher(file.getName()).matches() &&
                        file.getName().equalsIgnoreCase(baseType.contentPack + ".zip")) {
                    try (ZipFile zipFile = new ZipFile(file)) {
                        ZipEntry entry = zipFile.getEntry(this.fileLocation);
                        if (entry != null) {
                            found = 1;
                            try (InputStream stream = zipFile.getInputStream(entry);
                                 BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {

                                String currentLine;
                                while ((currentLine = reader.readLine()) != null) {
                                    currentLine = currentLine.replaceAll("\\s+", " ").trim();
                                    lineCount++;

                                    if (currentLine.startsWith("#") || currentLine.isEmpty()) continue;

                                    if (currentLine.startsWith("v ")) {
                                        Vertex vertex = parseVertex(currentLine, lineCount);
                                        if (vertex != null) vertices.add(vertex);
                                    } else if (currentLine.startsWith("vn ")) {
                                        Vertex vertex = parseVertexNormal(currentLine, lineCount);
                                        if (vertex != null) vertexNormals.add(vertex);
                                    } else if (currentLine.startsWith("vt ")) {
                                        TextureCoordinate tc = parseTextureCoordinate(currentLine, lineCount);
                                        if (tc != null) textureCoordinates.add(tc);
                                    } else if (currentLine.startsWith("f ")) {
                                        if (currentModelObject == null) {
                                            currentModelObject = new ModelObject("Default");
                                        }
                                        Face face = parseFace(currentLine, lineCount);
                                        if (face != null) currentModelObject.faces.add(face);
                                    } else if (currentLine.startsWith("g ") || currentLine.startsWith("o ")) {
                                        ModelObject group = parseGroupObject(currentLine, lineCount);
                                        if (group != null && currentModelObject != null) {
                                            renderers.add(new ObjModelRenderer(model, currentModelObject));
                                        }
                                        currentModelObject = group;
                                    }
                                }
                                if (currentModelObject != null) {
                                    renderers.add(new ObjModelRenderer(model, currentModelObject));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (found == 0) {
            ModularWarfare.LOGGER.warn("The model file in " + baseType.contentPack + " at: " + this.fileLocation + " has not been found");
        }

        model.setParts(renderers);
        return model;
    }

    public ObjModel loadModelFromRL() throws ModelFormatException {
        int lineCount = 0;
        ObjModel model = new ObjModel();

        try {
            var resource = Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
            if (resource.isPresent()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.get().open()))) {
                    String currentLine;
                    while ((currentLine = reader.readLine()) != null) {
                        currentLine = currentLine.replaceAll("\\s+", " ").trim();
                        lineCount++;

                        if (currentLine.startsWith("#") || currentLine.isEmpty()) continue;

                        if (currentLine.startsWith("v ")) {
                            Vertex vertex = parseVertex(currentLine, lineCount);
                            if (vertex != null) vertices.add(vertex);
                        } else if (currentLine.startsWith("vn ")) {
                            Vertex vertex = parseVertexNormal(currentLine, lineCount);
                            if (vertex != null) vertexNormals.add(vertex);
                        } else if (currentLine.startsWith("vt ")) {
                            TextureCoordinate tc = parseTextureCoordinate(currentLine, lineCount);
                            if (tc != null) textureCoordinates.add(tc);
                        } else if (currentLine.startsWith("f ")) {
                            if (currentModelObject == null) {
                                currentModelObject = new ModelObject("Default");
                            }
                            Face face = parseFace(currentLine, lineCount);
                            if (face != null) currentModelObject.faces.add(face);
                        } else if (currentLine.startsWith("g ") || currentLine.startsWith("o ")) {
                            ModelObject group = parseGroupObject(currentLine, lineCount);
                            if (group != null && currentModelObject != null) {
                                renderers.add(new ObjModelRenderer(model, currentModelObject));
                            }
                            currentModelObject = group;
                        }
                    }
                    if (currentModelObject != null) {
                        renderers.add(new ObjModelRenderer(model, currentModelObject));
                    }
                }
            }
        } catch (IOException e) {
            throw new ModelFormatException("IO Exception reading model format", e);
        }

        model.setParts(renderers);
        return model;
    }

    public ObjModel loadModel() throws ModelFormatException {
        int lineCount = 0;
        ObjModel model = new ObjModel();

        String absPath = FMLPaths.GAMEDIR.get().toString() + "/ModularWarfare/";
        File modelFile = checkValidPath(absPath + fileLocation);

        if (modelFile == null || !modelFile.exists()) {
            ModularWarfare.LOGGER.info("The model with the name " + fileLocation + " does not exist.");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(modelFile))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                currentLine = currentLine.replaceAll("\\s+", " ").trim();
                lineCount++;

                if (currentLine.startsWith("#") || currentLine.isEmpty()) continue;

                if (currentLine.startsWith("v ")) {
                    Vertex vertex = parseVertex(currentLine, lineCount);
                    if (vertex != null) vertices.add(vertex);
                } else if (currentLine.startsWith("vn ")) {
                    Vertex vertex = parseVertexNormal(currentLine, lineCount);
                    if (vertex != null) vertexNormals.add(vertex);
                } else if (currentLine.startsWith("vt ")) {
                    TextureCoordinate tc = parseTextureCoordinate(currentLine, lineCount);
                    if (tc != null) textureCoordinates.add(tc);
                } else if (currentLine.startsWith("f ")) {
                    if (currentModelObject == null) {
                        currentModelObject = new ModelObject("Default");
                    }
                    Face face = parseFace(currentLine, lineCount);
                    if (face != null) currentModelObject.faces.add(face);
                } else if (currentLine.startsWith("g ") || currentLine.startsWith("o ")) {
                    ModelObject group = parseGroupObject(currentLine, lineCount);
                    if (group != null && currentModelObject != null) {
                        renderers.add(new ObjModelRenderer(model, currentModelObject));
                    }
                    currentModelObject = group;
                }
            }
            if (currentModelObject != null) {
                renderers.add(new ObjModelRenderer(model, currentModelObject));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.setParts(renderers);
        return model;
    }

    private Vertex parseVertex(String line, int lineCount) throws ModelFormatException {
        if (!isValidVertexLine(line)) {
            throw new ModelFormatException("Error parsing vertex at line " + lineCount);
        }

        line = line.substring(line.indexOf(" ") + 1);
        String[] tokens = line.split(" ");

        try {
            if (tokens.length == 2) {
                return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
            } else if (tokens.length == 3) {
                return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
            }
        } catch (NumberFormatException e) {
            throw new ModelFormatException("Number formatting error at line " + lineCount, e);
        }
        return null;
    }

    private Vertex parseVertexNormal(String line, int lineCount) throws ModelFormatException {
        if (!isValidVertexNormalLine(line)) {
            throw new ModelFormatException("Error parsing vertex normal at line " + lineCount);
        }

        line = line.substring(line.indexOf(" ") + 1);
        String[] tokens = line.split(" ");

        try {
            if (tokens.length == 3) {
                return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
            }
        } catch (NumberFormatException e) {
            throw new ModelFormatException("Number formatting error at line " + lineCount, e);
        }
        return null;
    }

    private TextureCoordinate parseTextureCoordinate(String line, int lineCount) throws ModelFormatException {
        if (!isValidTextureCoordinateLine(line)) {
            throw new ModelFormatException("Error parsing texture coordinate at line " + lineCount);
        }

        line = line.substring(line.indexOf(" ") + 1);
        String[] tokens = line.split(" ");

        try {
            if (tokens.length == 2) {
                return new TextureCoordinate(Float.parseFloat(tokens[0]), 1.0f - Float.parseFloat(tokens[1]));
            } else if (tokens.length == 3) {
                return new TextureCoordinate(Float.parseFloat(tokens[0]), 1.0f - Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
            }
        } catch (NumberFormatException e) {
            throw new ModelFormatException("Number formatting error at line " + lineCount, e);
        }
        return null;
    }

    private Face parseFace(String line, int lineCount) throws ModelFormatException {
        if (!isValidFaceLine(line)) {
            throw new ModelFormatException("Error parsing face at line " + lineCount);
        }

        Face face = new Face();
        String trimmedLine = line.substring(line.indexOf(" ") + 1);
        String[] tokens = trimmedLine.split(" ");

        if (tokens.length == 3 && currentModelObject.glDrawingMode == -1) {
            currentModelObject.glDrawingMode = 4;
        } else if (tokens.length == 4 && currentModelObject.glDrawingMode == -1) {
            currentModelObject.glDrawingMode = 7;
        }

        if (isValidFace_V_VT_VN_Line(line)) {
            face.vertices = new Vertex[tokens.length];
            face.textureCoordinates = new TextureCoordinate[tokens.length];
            face.vertexNormals = new Vertex[tokens.length];

            for (int i = 0; i < tokens.length; i++) {
                String[] subTokens = tokens[i].split("/");
                face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                face.textureCoordinates[i] = textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                face.vertexNormals[i] = vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
            }
            face.faceNormal = face.calculateFaceNormal();
        } else if (isValidFace_V_VT_Line(line)) {
            face.vertices = new Vertex[tokens.length];
            face.textureCoordinates = new TextureCoordinate[tokens.length];

            for (int i = 0; i < tokens.length; i++) {
                String[] subTokens = tokens[i].split("/");
                face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                face.textureCoordinates[i] = textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
            }
            face.faceNormal = face.calculateFaceNormal();
        } else if (isValidFace_V_VN_Line(line)) {
            face.vertices = new Vertex[tokens.length];
            face.vertexNormals = new Vertex[tokens.length];

            for (int i = 0; i < tokens.length; i++) {
                String[] subTokens = tokens[i].split("//");
                face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                face.vertexNormals[i] = vertexNormals.get(Integer.parseInt(subTokens[1]) - 1);
            }
            face.faceNormal = face.calculateFaceNormal();
        } else if (isValidFace_V_Line(line)) {
            face.vertices = new Vertex[tokens.length];

            for (int i = 0; i < tokens.length; i++) {
                face.vertices[i] = vertices.get(Integer.parseInt(tokens[i]) - 1);
            }
            face.faceNormal = face.calculateFaceNormal();
        }

        return face;
    }

    private ModelObject parseGroupObject(String line, int lineCount) throws ModelFormatException {
        if (!isValidGroupObjectLine(line)) {
            throw new ModelFormatException("Error parsing group/object at line " + lineCount);
        }

        String trimmedLine = line.substring(line.indexOf(" ") + 1);
        if (trimmedLine.length() > 0) {
            return new ModelObject(trimmedLine);
        }
        return null;
    }

    public File checkValidPath(String path) {
        File file = null;
        String absPath = path;
        if (!path.endsWith(".obj")) {
            absPath = absPath + ".obj";
        }
        file = new File(absPath);
        if (file == null || !file.exists()) {
            return null;
        }
        return file;
    }
}